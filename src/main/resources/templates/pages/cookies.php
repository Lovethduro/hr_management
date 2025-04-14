<?php
header('Content-Type: application/json');

// Database configuration
define('DB_HOST', 'localhost');
define('DB_NAME', 'your_database');
define('DB_USER', 'your_username');
define('DB_PASS', 'your_password');
define('DB_CHARSET', 'utf8mb4');

// Error reporting (disable in production)
ini_set('display_errors', 0);
error_reporting(E_ALL);

// Set response headers for security
header("X-Content-Type-Options: nosniff");
header("X-Frame-Options: DENY");
header("X-XSS-Protection: 1; mode=block");

function handleError($message, $code = 400) {
    http_response_code($code);
    echo json_encode(['error' => $message]);
    exit;
}

function getDatabaseConnection() {
    static $pdo = null;

    if ($pdo === null) {
        try {
            $dsn = "mysql:host=" . DB_HOST . ";dbname=" . DB_NAME . ";charset=" . DB_CHARSET;
            $pdo = new PDO($dsn, DB_USER, DB_PASS, [
                PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
                PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
                PDO::ATTR_EMULATE_PREPARES => false,
            ]);
        } catch (PDOException $e) {
            error_log("Database connection failed: " . $e->getMessage());
            handleError('Database connection error', 500);
        }
    }

    return $pdo;
}

// Only handle POST requests
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    handleError('Method not allowed', 405);
}

// Get JSON input
$input = json_decode(file_get_contents('php://input'), true);

if (!$input || !isset($input['consentType'])) {
    handleError('Invalid request data');
}

// Validate consent type
$allowedTypes = ['all', 'necessary', 'analytics', 'none'];
if (!in_array($input['consentType'], $allowedTypes)) {
    handleError('Invalid consent type');
}

try {
    $pdo = getDatabaseConnection();

    // Begin transaction
    $pdo->beginTransaction();

    // Generate consent ID
    $consentId = bin2hex(random_bytes(16));
    $ipAddress = $_SERVER['REMOTE_ADDR'] ?? 'unknown';
    $userAgent = substr($_SERVER['HTTP_USER_AGENT'] ?? '', 0, 500);

    // Insert consent record
    $stmt = $pdo->prepare("
        INSERT INTO cookie_consents 
        (id, consent_type, ip_address, user_agent) 
        VALUES (:id, :type, :ip, :ua)
    ");

    $stmt->execute([
        ':id' => $consentId,
        ':type' => $input['consentType'],
        ':ip' => $ipAddress,
        ':ua' => $userAgent
    ]);

    // Set cookies with secure options
    $expiry = time() + (365 * 24 * 60 * 60);
    $cookieOptions = [
        'expires' => $expiry,
        'path' => '/',
        'domain' => $_SERVER['HTTP_HOST'], // Adjust if using subdomains
        'secure' => true,
        'httponly' => true,
        'samesite' => 'Lax'
    ];

    // Set consent cookie
    setcookie('cookie_consent', $input['consentType'], $cookieOptions);

    // Set category cookies
    $categories = [
        'necessary' => 'true',
        'analytics' => ($input['consentType'] === 'all' || $input['consentType'] === 'analytics') ? 'true' : 'false',
        'marketing' => ($input['consentType'] === 'all') ? 'true' : 'false'
    ];

    foreach ($categories as $name => $value) {
        setcookie("cookie_$name", $value, $cookieOptions);
    }

    // Commit transaction
    $pdo->commit();

    // Return success
    echo json_encode([
        'success' => true,
        'consent_id' => $consentId,
        'timestamp' => time()
    ]);

} catch (PDOException $e) {
    // Roll back on error
    if (isset($pdo) && $pdo->inTransaction()) {
        $pdo->rollBack();
    }

    error_log("Database error: " . $e->getMessage());
    handleError('Database operation failed', 500);

} catch (Exception $e) {
    error_log("System error: " . $e->getMessage());
    handleError('Internal server error', 500);
}
?>