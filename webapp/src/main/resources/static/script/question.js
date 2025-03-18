function checkAnswer(answerId){
    $.ajax({
        url:'/game/getAnswer',
        type: 'GET',
        data: {answerId: answerId},
        success: function (response){
            let buttons = document.querySelectorAll(".answer-button");

            buttons.forEach(button=>{
                button.disabled = true;
            })

            let correctId = response.correctId;
            let points = response.points;
            let prevPoints = response.prevPoints;

            let selectedButton = document.getElementById(answerId);
            let correctButton = document.getElementById(correctId);

            if(correctId === answerId){
                selectedButton.classList.add("correct");
            }else{
                selectedButton.classList.add("wrong");
                correctButton.classList.add("correct");
            }

            animatePoints(prevPoints, points, 1500);

            setTimeout(nextQuestion, 1700);



        }
    })
}

function animatePoints(prevPoints, finalPoints, duration){
    let currentPoints = prevPoints;
    let pointsDisplay = document.getElementById("points");

    //let duration = 2000;
    let incrementTime = 50; // Interval to increment points (in milliseconds)
    let totalSteps = duration / incrementTime; // Total steps for the animation
    let incrementValue = (finalPoints-prevPoints) / totalSteps; // Amount to increment on each step

    // Function to update the points display gradually
    let interval = setInterval(function() {
        currentPoints += incrementValue;
        pointsDisplay.innerText = "Points: "+Math.round(currentPoints).toString(); // Update the points display

        if (currentPoints === finalPoints) {
            clearInterval(interval); // Stop the animation when we reach the final points
            pointsDisplay.innerText = "Points: "+finalPoints; // Ensure the final value is displayed
        }
    }, incrementTime);
}

function nextQuestion(){
    window.location.replace("/game/next");
}