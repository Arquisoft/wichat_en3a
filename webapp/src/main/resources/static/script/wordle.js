const userId = "user123"; // Esto deberías cambiarlo con un ID real
const API_BASE = "/api/wordle";

const board = document.getElementById("game-board");
const form = document.getElementById("guess-form");
const input = document.getElementById("guess-input");
const startBtn = document.getElementById("start-btn");
const statusText = document.getElementById("status");

let currentGame = null;

function createBoard(attempts, feedback) {
    board.innerHTML = "";
    for (let i = 0; i < attempts.length; i++) {
        const row = document.createElement("div");
        row.className = "row";

        const word = attempts[i];
        const fb = feedback[i];

        for (let j = 0; j < word.length; j++) {
            const tile = document.createElement("div");
            tile.className = "tile " + fb[j].toLowerCase();
            tile.textContent = word[j];
            row.appendChild(tile);
        }

        board.appendChild(row);
    }
}

async function fetchGameStatus() {
    const res = await fetch(`${API_BASE}/status/${userId}`);
    currentGame = await res.json();
    createBoard(currentGame.attempts, currentGame.feedbackHistory);
    updateStatus();
}

function updateStatus() {
    switch (currentGame.status) {
        case "WIN":
            statusText.textContent = "¡Has ganado!";
            break;
        case "LOSE":
            statusText.textContent = "Has perdido. Palabra: " + currentGame.targetWord;
            break;
        default:
            statusText.textContent = "Intentos restantes: " + currentGame.remainingAttempts;
    }
}

form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const guess = input.value;
    input.value = "";

    await fetch(`${API_BASE}/guess/${userId}?guess=${guess}`, { method: "POST" });
    await fetchGameStatus();
});

startBtn.addEventListener("click", async () => {
    await fetch(`${API_BASE}/start/${userId}`, { method: "POST" });
    await fetchGameStatus();
});

// Carga inicial (opcional)
fetchGameStatus();
