// toggle menu button
function toggleMenu(){
    const menu = document.querySelector(".menu");
    const nav = document.querySelector(".nav")
    menu.classList.toggle("active");
    nav.classList.toggle("active");
}
// Function to switch between background videos
let currentVideoIndex = 0;
const videos = document.querySelectorAll('.bg-video');

function changeBackgroundVideo() {
    // Hide all videos
    videos.forEach((video, index) => {
        video.classList.remove('active');
        if (index === currentVideoIndex) {
            video.pause(); // Pause the current video
        }
    });

    // Increment the index for the next video
    currentVideoIndex = (currentVideoIndex + 1) % videos.length;

    // Show the next video
    videos[currentVideoIndex].classList.add('active');
    videos[currentVideoIndex].play(); // Play the next video
}

// Set an interval to change the video every 5 seconds
setInterval(changeBackgroundVideo, 5000); // 5000 milliseconds = 5 seconds
