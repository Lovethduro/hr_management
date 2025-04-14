function validateForm() {
    // Get form values
    var username = document.getElementById("username").value;
    var email = document.getElementById("email").value;
    var password = document.getElementById("password").value;
    var confirmPassword = document.getElementById("confirmPassword").value;

    // Check if all fields are filled
    if (!username || !email || !password || !confirmPassword) {
        alert("Please fill out all fields!");
        return false; // Prevent form submission
    }

    // Perform password validation
    if (password !== confirmPassword) {
        alert("Passwords do not match!");
        return false; // Prevent form submission
    }


    // Simulate successful sign-up (You can replace this with actual sign-up logic)
    alert("Sign-up successful!");

    // Redirect to the login page
    window.location.href = "index2.html"; // Redirect to the login page

    return false; // Prevent form submission as we handle the redirect

}
function goBack() {
        if (window.history.length > 1) {
            window.history.back();
        } else {
            // Redirect to a specific page if there's no history
            window.location.href = 'index.html'; // Replace with your desired page
        }
    }
