package com.uniovi.wichatwebapp.services;

import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.repositories.HintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HintService {
    private final static String setupMessageChat = "You are part of a web game about questions and answers. Your role is to provide clues to the player. " +
            "You will receive a message in the following format: `<question or sentence>;<answer>;<already given hints by you separated by '_' character>`. " +
            "Based on this, you must respond according to the following rules:\n\n" +

            "1. **If the question or sentence is unrelated to the answer**: " +
            "Answer the question directly and freely, as if you were a general-purpose assistant. This isn't a abnormal behaviour but DON'T give the answer passed to you" +
            "Example:\n" +
            "   - Input: 'What is the weather today?;Paris'\n" +
            "   - Response: 'I cannot provide real-time weather updates, but you can check a weather website or app.'\n\n" +

            "2. **If the question or sentence  is related to the answer**: " +
            "Provide a clue that helps the player guess the answer, but **never reveal the answer directly**. " +
            "The clue should be short, relevant, and not contain the answer and shouldn't have been said before. " +
            "Example:\n" +
            "   - Input: 'What is the capital of France?;Paris'\n" +
            "   - Response: 'This city is famous for the Eiffel Tower.'\n\n" +

            "3. **General guidelines**:\n" +
            "   - Keep your responses concise and to the point.\n" +
            "   - Do not include the answer in your response.\n" +
            "   - Ask the player to ask another question.\n\n" +

            "Remember: Your goal is to assist the player without giving away the answer directly.";

    private Question currentQuestion;
    private List<String> alreadyGivenHints = new ArrayList<>();

    private HintRepository hintRepository;

    public HintService(HintRepository hintRepository) {
        this.hintRepository = hintRepository;
    }

    public String askQuestionToIA(Question question, String questionUser) {
        if(currentQuestion == null || !currentQuestion.getId().equals(question.getId())) {
            this.currentQuestion = question;
            alreadyGivenHints = new ArrayList<>();
        }
        String answer =currentQuestion.getCorrectAnswer().getText();
        String hint = hintRepository.askWithInstructions(setupMessageChat, questionUser, answer, alreadyGivenHints());
        alreadyGivenHints.add(hint);
        return hint;
    }

    public String alreadyGivenHints(){
        String text ="";
        for(String hint : alreadyGivenHints){
            text+= hint+"_";
        }
        return text;
    }

    protected String getSetupMessageChat(){
        return setupMessageChat;
    }
}
