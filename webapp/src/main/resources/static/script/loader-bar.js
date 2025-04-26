let loaderBarIntervalId; // Global variable to store the interval ID
let storedTimer; // New variable to store the initial timer value

function setUpLoaderBar(timer) {
    storedTimer = timer; // Store the timer value
    const loaderBar = document.querySelector('.loader-bar');
    if (!loaderBar) return;

    const totalBlocks = loaderBar.children.length;
    const interval = timer / (totalBlocks-8) * 1000; // Calculate interval in milliseconds

    let currentBlock = 8;

    loaderBarIntervalId = setInterval(() => { // Store interval ID globally
        if (currentBlock < totalBlocks) {
            loaderBar.children[currentBlock].classList.remove('hidden');
            currentBlock++;
        } else {
            clearInterval(loaderBarIntervalId);
        }
    }, interval);
}

// New function to stop the loader bar
function stopLoaderBar() {
    if (loaderBarIntervalId) {
        clearInterval(loaderBarIntervalId);
        loaderBarIntervalId = null;
    }
}

// Modified function to resume the loader bar using stored timer value
function resumeLoaderBar() {
    if (storedTimer) {
        setUpLoaderBar(storedTimer);
    }
}