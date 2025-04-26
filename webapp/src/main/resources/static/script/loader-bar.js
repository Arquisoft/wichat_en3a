function setUpLoaderBar(timer) {
    const loaderBar = document.querySelector('.loader-bar');
    if (!loaderBar) return;

    const totalBlocks = loaderBar.children.length;
    const interval = timer / (totalBlocks-8) * 1000; // Calculate interval in milliseconds

    let currentBlock = 8;

    const intervalId = setInterval(() => {
        if (currentBlock < totalBlocks) {
            loaderBar.children[currentBlock].classList.remove('hidden');
            currentBlock++;
        } else {
            clearInterval(intervalId);
        }
    }, interval);
}
