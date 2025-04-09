const texts = ["SPORT", "GEOGRAPHY", "BIOLOGY", "POP CULTURE"];
let currentIndex = 0;

function changeText(direction) {
    currentIndex = (currentIndex + direction + texts.length) % texts.length;
    document.getElementById("frame-text").innerText = texts[currentIndex];
}
