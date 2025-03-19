document.addEventListener('DOMContentLoaded', function () {
    // Elementos del DOM
    const startChatBtn = document.getElementById('startChat');
    const chatBox = document.getElementById('chatBox');
    const chatMessages = document.getElementById('chatMessages');
    const userInput = document.getElementById('userInput');
    const sendMessageBtn = document.getElementById('sendMessage');

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

    // Evento al hacer clic en el botón de iniciar chat
    startChatBtn.addEventListener('click', function () {
        // Mostrar el chat
        chatBox.style.display = 'block';
        startChatBtn.style.display = 'none';

        // Reiniciar el índice de conversación
        conversationIndex = 0;
        chatMessages.innerHTML = '';

        // Mostrar los mensajes de la conversación con un retraso para simular escritura
        conversationInterval = setInterval(function () {
            if (conversationIndex < predefinedConversation.length) {
                addMessage(predefinedConversation[conversationIndex]);
                conversationIndex++;
            } else {
                clearInterval(conversationInterval);
            }
        }, 1000);
    });

    // Funcionalidad para enviar mensaje (opcional)
    async function sendUserMessage() {
        const text = userInput.value.trim();
        if (text) {
            addMessage({ type: 'user', text: text });
            userInput.value = '';
            try {
                // Enviar la pregunta al backend
                //const response = await fetch(`/getHint?question=${encodeURIComponent(text)}&answerQuestion=default`, {
                const response = await fetch(`/hint?question=${encodeURIComponent(text)}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });

                if (!response.ok) {
                    throw new Error('Error al obtener la respuesta de la IA');
                }

                const result = await response.text();

                // Mostrar la respuesta de la IA en el chat
                addMessage({ type: 'ai', text: result });
            } catch (error) {
                console.error('Error:', error);
                addMessage({ type: 'ai', text: 'Hubo un error al procesar tu solicitud.' });
            }
        }
    }

    sendMessageBtn.addEventListener('click', sendUserMessage);
    userInput.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            sendUserMessage();
        }
    });
});