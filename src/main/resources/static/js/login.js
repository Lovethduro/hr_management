// Toggle password visibility
function togglePassword() {
    let passwordInput = document.getElementById('password');
    let icon = document.querySelector('.bx.bxs-lock-alt'); // Select the icon

    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        icon.classList.remove('bxs-lock-alt'); // Change the icon class
        icon.classList.add('bxs-lock-open'); // Change to open lock icon
    } else {
        passwordInput.type = 'password';
        icon.classList.remove('bxs-lock-open'); // Change back to closed lock icon
        icon.classList.add('bxs-lock-alt'); // Change back to closed lock icon
    }
}

// Go back to the previous page or redirect if no history
function goBack() {
    if (window.history.length > 1) {
        window.history.back();
    } else {
        // Redirect to a specific page if there's no history
        window.location.href = 'index.html'; // Replace with your desired page
    }
}

// JavaScript function to validate login form
async function validateLoginForm(event) {
    event.preventDefault(); // Prevent the default form submission

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('LoginServlet', { // Update with your servlet URL
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });

        const result = await response.json();
        if (result.success) {
            alert('Login successful!');
            window.location.href = 'index.html'; // Redirect to your landing page (index2.html)
        } else {
            alert('Invalid username or password!');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred during login. Please try again.');
    }
}
