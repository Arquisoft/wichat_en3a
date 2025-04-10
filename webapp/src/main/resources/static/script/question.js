let timer;
let current;
let interval;
let timeout;
let decrement;

function checkAnswer(answerId){
    $.ajax({
        url:'/game/getAnswer',
        type: 'GET',
        data: {answerId: answerId},
        success: function (response){
            if(typeof interval !== "undefined" && interval !== null){
                clearInterval(interval);
            }

            let buttons = document.querySelectorAll(".answer-button");

            buttons.forEach(button=>{
                button.disabled = true; // Disable buttons but do not change transparency
            });

            let correctId = response.correctId;
            let points = response.points;
            let prevPoints = response.prevPoints;

            let selectedButton = document.getElementById(answerId);
            let correctButton = document.getElementById(correctId);

            correctButton.classList.add("correct");

            if(typeof selectedButton !== "undefined" && selectedButton !== null){
                if(correctId !== answerId){
                    selectedButton.classList.add("wrong");
                }
            }

            animatePoints(prevPoints, points, 1500);

            setTimeout(nextQuestion, 1700);
        }
    });
}

function animatePoints(prevPoints, finalPoints, duration){
    let currentPoints = prevPoints;
    let pointsDisplay = document.getElementById("points");

    //let duration = 2000;
    let incrementTime = 50; // Interval to increment points (in milliseconds)
    let totalSteps = duration / incrementTime; // Total steps for the animation
    let incrementValue = (finalPoints-prevPoints) / totalSteps; // Amount to increment on each step


    let tolerance = 0.001;
    // Function to update the points display gradually
    let interval = setInterval(function() {
        currentPoints += incrementValue;
        pointsDisplay.innerText = "Points: "+Math.round(currentPoints).toString(); // Update the points display

        if (Math.abs(finalPoints-currentPoints) <= tolerance) {
            clearInterval(interval); // Stop the animation when we reach the final points
            pointsDisplay.innerText = "Points: "+finalPoints; // Ensure the final value is displayed
        }
    }, incrementTime);
}

function nextQuestion(){
    window.location.replace("/game/next");
}

function pauseTimer(){
    clearInterval(interval);
    clearTimeout(timeout);
}

function restoreTimer(){

    setUpInterval();

    setUpTimeout(current);
}

function setUpInterval(){
    interval = setInterval(()=>{
        current = current -decrement;
        tickTimer(current, timer);
    }, decrement*1000);
}

function setUpTimeout(t){
    timeout = setTimeout(()=>{
        clearInterval(interval);
        checkAnswer("wrong");
    }, t*1000);
}

function setUpTimer(t){

    decrement = 0.1;
    timer = t;
    current = timer;

    setUpInterval();
    setUpTimeout(timer);

}

function tickTimer(current,timer){
    const canvas = document.getElementById('timer');
    const ctx = canvas.getContext('2d');

    // Canvas center and radius
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const radius = 50;

    // Clear the canvas before redrawing
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // Draw the background circle (the full circle)
    ctx.beginPath();
    ctx.arc(centerX, centerY, radius, 0, 2 * Math.PI); // Full circle
    ctx.lineWidth = 10; // Thickness of the circle's stroke
    ctx.strokeStyle = '#e6e6e6'; // Light gray background color
    ctx.stroke();

    // Calculate the fraction of the circle to draw based on currentTime and totalTime
    const fraction = current / timer;
    const endAngle = fraction * 2 * Math.PI; // Convert fraction to an angle in radians

    // Draw the progress arc (colored part)
    ctx.beginPath();
    ctx.arc(centerX, centerY, radius, -Math.PI / 2, -Math.PI / 2 + endAngle); // Start from top (-Math.PI/2)
    ctx.lineWidth = 10; // Thickness of the arc
    ctx.strokeStyle = '#4caf50'; // Green color for the progress
    ctx.stroke();
}

function typeQuestion(text, elementId, delay = 50) {
    const element = document.getElementById(elementId);
    if (!element) return;

    let index = 0;
    element.textContent = ""; // Clear existing content

    const intervalId = setInterval(() => {
        if (index < text.length) {
            element.textContent += text[index];
            index++;
        } else {
            clearInterval(intervalId);
        }
    }, delay);
}

// Call this function when the page loads
document.addEventListener("DOMContentLoaded", () => {
    const questionText = document.getElementById("questionText").dataset.content;
    typeQuestion(questionText, "questionText");
});