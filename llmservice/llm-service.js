const axios = require('axios');
const express = require('express');

const app = express();
const port = 8003;

app.use(express.json());

const setupMessage = "You are part of a web game about questions and answers. You will be in charge of giving clues " +
    "to the player. You will receive a question and an answer from now on in the following format <question>;<answer> " +
    "when you receive that message you will reply with a clue taking in to account both the question and the answer." +
    "Your replies must have the following characteristics: They must be as short as posible, they will consist of just a" +
    "clue with no additional information, the clues must not contain the answer in them. Example of expected response\n" +
    "you receive: 'What is the capital of France;Paris' \n" +
    "posible answer: 'In the capital of france you can visit the Eiffel Tower' ";

// Track if setup message has been sent per model
const modelSetupTracker = {};

// Define configurations for different LLM APIs
const llmConfigs = {
  gemini: {
    url: (apiKey) => `https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${apiKey}`,
    transformRequest: (question, answer, model) => {
      const formattedInput = `${question}';'${answer}`;
      const messages = [];

      if (!modelSetupTracker[model]) {
        messages.push({ parts: [{ text: setupMessage }] });
        modelSetupTracker[model] = true;
      }
      messages.push({ parts: [{ text: formattedInput }] });

      return { contents: messages };
    },
    transformResponse: (response) => {
      if (!response.data || !response.data.candidates || response.data.candidates.length === 0) {
        return "There was an unexpected problem: No response from LLM model.";
      }
      return response.data.candidates[0].content.parts[0].text;
    }
  },
  empathy: {
    url: () => 'https://empathyai.prod.empathy.co/v1/chat/completions',
    transformRequest: (question, answer, model) => {
      const formattedInput = `${question}';'${answer}`;
      const messages = [];

      if (!modelSetupTracker[model]) {
        messages.push({ role: "system", content: setupMessage });
        modelSetupTracker[model] = true;
      }
      messages.push({ role: "user", content: formattedInput });

      return {
        model: "qwen/Qwen2.5-Coder-7B-Instruct",
        messages
      };
    },
    transformResponse: (response) => response.data.choices[0]?.message?.content,
    headers: (apiKey) => ({
      Authorization: `Bearer ${apiKey}`,
      'Content-Type': 'application/json'
    })
  }
};

// Function to get a clue from the LLM
async function getClueFromLLM(question, answer, apiKey, model = 'gemini') {
  try {
    const config = llmConfigs[model];
    if (!config) {
      //throw new Error(`Model "${model}" is not supported.`);
      return;
    }

    const url = config.url(apiKey);
    const requestData = config.transformRequest(question, answer, model);

    const headers = {
      'Content-Type': 'application/json',
      ...(config.headers ? config.headers(apiKey) : {})
    };

    const response = await axios.post(url, requestData, { headers });

    if (!(!response || !response.data)) {
      return config.transformResponse(response);
    } else {
      //throw new Error(`Empty response from ${model} API.`);

    }

  } catch (error) {
    console.error(`Error getting clue from ${model}:`, error.response?.data || error.message);

    return error.response?.data?.error?.message || "Failed to fetch response from model.";
  }
}

let server;
try {
  server = app.listen(port, () => {
    console.log(`LLM Service listening at http://localhost:${port}`);
  });
} catch (error) {
  console.error("Failed to start the server:", error.message);
}

if (server != null) {
  module.exports = server;
}

