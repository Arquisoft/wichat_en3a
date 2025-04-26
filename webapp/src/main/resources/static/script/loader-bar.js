let loaderBar = null;
let totalBlocks = 0;
let interval = 0;
let currentBlock = 8;
let intervalId = null;

function setUpLoaderBar(timer) {
    loaderBar = document.querySelector('.loader-bar');
    if (!loaderBar) return;

    totalBlocks = loaderBar.children.length;
    interval = (timer / (totalBlocks - 8)) * 1000; // milliseconds
    currentBlock = 8;
    startLoader();
}

function startLoader() {
    if (!loaderBar) return;

    clearInterval(intervalId);
    currentBlock = 8;

    intervalId = setInterval(() => {
        if (currentBlock < totalBlocks) {
            loaderBar.children[currentBlock].classList.remove('hidden');
            currentBlock++;
        } else {
            clearInterval(intervalId);
        }
    }, interval);
}

function stopLoader() {
    clearInterval(intervalId);
}

function resumeLoader() {
    if (!loaderBar) return;

    clearInterval(intervalId);

    intervalId = setInterval(() => {
        if (currentBlock < totalBlocks) {
            loaderBar.children[currentBlock].classList.remove('hidden');
            currentBlock++;
        } else {
            clearInterval(intervalId);
        }
    }, interval);
}
