document.addEventListener('DOMContentLoaded', function () {
    // Elementos del DOM
    const startChatBtn = document.getElementById('startChat');
    const chatBox = document.getElementById('chatBox');
    const chatMessages = document.getElementById('chatMessages');
    const userInput = document.getElementById('userInput');
    const sendMessageBtn = document.getElementById('sendMessage');
    const chatWrapper = document.getElementById("chatWrapper");

    const answers = document.getElementById("answers");
    const exitButton = document.getElementById("exitButton");

    // Conversación predefinida
    const predefinedConversation = [
        { type: 'ai', text: 'Welcome little novice! I am the wiser wizard of the universe. What you need?' },
    ];

    let conversationIndex = 0;
    let conversationInterval;

    // Función para mostrar un mensaje en el chat
    function addMessage(message) {
        const messageElement = document.createElement('div');
        messageElement.classList.add(message.type === 'ai' ? 'ai-message' : 'user-message');
        messageElement.textContent = message.text;
        chatMessages.appendChild(messageElement);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    // Función para simular que la IA está escribiendo
    function typeMessage(message, element, delay = 40) {
        let index = 0;
        const typingInterval = setInterval(() => {
            if (index < message.length) {
                element.textContent += message[index];
                index++;
                chatMessages.scrollTop = chatMessages.scrollHeight; // Desplazar al último mensaje
            } else {
                clearInterval(typingInterval);
            }
        }, delay);
    }

    exitButton.addEventListener("click", function (){
        answers.removeAttribute("hidden");
        chatBox.removeAttribute("style");
        startChatBtn.removeAttribute("style");
        chatWrapper.classList.remove("chat-wrapper-chat-visible");
        startChatBtn.classList.add("exhausted");
        startChatBtn.setAttribute("disabled", "true");
        restoreTimer();
    })

    // Evento al hacer clic en el botón de iniciar chat
    startChatBtn.addEventListener('click', function () {
        // Mostrar el chat
        chatWrapper.classList.add("chat-wrapper-chat-visible");
        chatBox.style.pointerEvents = 'all';
        chatBox.style.visibility = 'visible';
        startChatBtn.style.visibility = 'hidden';

        answers.setAttribute("hidden", "true");

        pauseTimer();

        // Reiniciar el índice de conversación
        conversationIndex = 0;
        chatMessages.innerHTML = '';

        // Mostrar los mensajes de la conversación con un retraso para simular escritura
        conversationInterval = setInterval(function () {
            if (conversationIndex < predefinedConversation.length) {
                const message = predefinedConversation[conversationIndex];
                const messageElement = document.createElement('div');
                messageElement.classList.add(message.type === 'ai' ? 'ai-message' : 'user-message');
                chatMessages.appendChild(messageElement);
                typeMessage(message.text, messageElement); // Escribir el mensaje carácter por carácter
                conversationIndex++;
            } else {
                clearInterval(conversationInterval);
            }
        }, 1000);
    });

    // Funcionalidad para enviar mensaje
    async function sendUserMessage() {
        const text = userInput.value.trim();
        if (text) {
            addMessage({ type: 'user', text: text });
            userInput.value = '';
            try {
                // Enviar la pregunta al backend
                const response = await fetch(`/hint?question=${encodeURIComponent(text)}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });

                if (!response.ok) {
                    throw new Error('Error al obtener la respuesta de la IA');
                }

                const result = await response.text(); // Obtener la respuesta del backend

                // Mostrar la respuesta de la IA carácter por carácter
                const aiMessageElement = document.createElement('div');
                aiMessageElement.classList.add('ai-message');
                chatMessages.appendChild(aiMessageElement);
                typeMessage(result, aiMessageElement); // Escribir la respuesta poco a poco
            } catch (error) {
                console.error('Error:', error);

                // Eliminar el mensaje de "pensando" en caso de error
                chatMessages.removeChild(thinkingMessage);

                // Mostrar un mensaje de error
                addMessage({ type: 'ai', text: 'Hubo un error al procesar tu solicitud.' });
            }
        }
    }

    // Evento al hacer clic en el botón de enviar
    sendMessageBtn.addEventListener('click', sendUserMessage);

    // Evento al presionar Enter en el campo de entrada
    userInput.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            sendUserMessage();
        }
    });
});