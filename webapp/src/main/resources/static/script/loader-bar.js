let loaderBar = null;
let totalBlocks = 0;
let intervalBar = 0;
let currentBlock = 8;
let intervalBarId = null;

function setUpLoaderBar(timer) {
    loaderBar = document.querySelector('.loader-bar');
    if (!loaderBar) return;

    totalBlocks = loaderBar.children.length;
    intervalBar = (timer / (totalBlocks - 8)) * 1000; // milliseconds
    currentBlock = 8;
    startLoader();
}

function startLoader() {
    if (!loaderBar) return;

    clearInterval(intervalBarId);
    currentBlock = 8;

    intervalBarId = setInterval(() => {
        if (currentBlock < totalBlocks) {
            loaderBar.children[currentBlock].classList.remove('hidden');
            currentBlock++;
        } else {
            clearInterval(intervalBarId);
        }
    }, intervalBar);
}

function stopLoader() {
    clearInterval(intervalBarId);
}

function resumeLoader() {
    if (!loaderBar) return;

    clearInterval(intervalBarId);

    intervalBarId = setInterval(() => {
        if (currentBlock < totalBlocks) {
            loaderBar.children[currentBlock].classList.remove('hidden');
            currentBlock++;
        } else {
            clearInterval(intervalBarId);
        }
    }, intervalBar);
}
