document.addEventListener('DOMContentLoaded', function () {
    // Elementos del DOM
    const startChatBtn = document.getElementById('startChat');
    const chatBox = document.getElementById('chatBox');
    const chatMessages = document.getElementById('chatMessages');
    const userInput = document.getElementById('userInput');
    const sendMessageBtn = document.getElementById('sendMessage');

    // Conversación predefinida
    const predefinedConversation = [
        { type: 'ai', text: '¡Hola! Soy tu asistente IA. ¿En qué puedo ayudarte hoy?' },
        { type: 'user', text: '¿Qué puedes hacer?' },
        { type: 'ai', text: 'Puedo responder preguntas, proporcionarte información, o simplemente conversar contigo. ¿Hay algo específico que quieras saber?' },
        { type: 'user', text: 'Cuéntame sobre la inteligencia artificial' },
        { type: 'ai', text: 'La inteligencia artificial (IA) es un campo de la informática que se centra en crear sistemas capaces de realizar tareas que normalmente requieren inteligencia humana. Esto incluye aprendizaje, razonamiento, resolución de problemas, percepción y comprensión del lenguaje. Las IAs modernas utilizan técnicas como el aprendizaje profundo y el procesamiento del lenguaje natural para analizar datos y tomar decisiones.' }
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
    function sendUserMessage() {
        const text = userInput.value.trim();
        if (text) {
            addMessage({ type: 'user', text: text });
            userInput.value = '';

            // Simular respuesta de la IA después de un breve retraso
            setTimeout(function () {
                addMessage({
                    type: 'ai',
                    text: 'Gracias por tu mensaje. En este momento solo puedo mostrar conversaciones predefinidas.'
                });
            }, 1000);
        }
    }

    sendMessageBtn.addEventListener('click', sendUserMessage);
    userInput.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            sendUserMessage();
        }
    });
});