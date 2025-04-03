package com.uniovi.wichatwebapp.controller;

import com.uniovi.wichatwebapp.entities.Answer;
import com.uniovi.wichatwebapp.entities.Question;
import com.uniovi.wichatwebapp.entities.QuestionCategory;
import com.uniovi.wichatwebapp.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Operation(summary = "Retrieves a random question matching the category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question was returned successfully",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Question.class),
            examples = @ExampleObject(value = "{ \"id\": \"q123\", " +
                    "\"content\": \"Which country's flag is this?\", " +
                    "\"imageUrl\": \"https://example.com/france-flag.jpg\", " +
                    "\"category\": \"GEOGRAPHY\", " +
                    "\"answers\": [ " +
                    "    { \"id\": \"a1\", \"text\": \"France\", \"language\": \"en\", \"category\": \"FLAG\" }, " +
                    "    { \"id\": \"a2\", \"text\": \"Germany\", \"language\": \"en\", \"category\": \"FLAG\" }, " +
                    "    { \"id\": \"a3\", \"text\": \"Italy\", \"language\": \"en\", \"category\": \"FLAG\" }, " +
                    "    { \"id\": \"a4\", \"text\": \"Spain\", \"language\": \"en\", \"category\": \"FLAG\" } " +
                    "], " +
                    "\"correctAnswer\": { \"id\": \"a1\", \"text\": \"France\", \"language\": \"en\", \"category\": \"FLAG\" } " +
                    "}"))}),
    })
    @RequestMapping(
            value = {"/game/newQuestion/{category}"},
            method = {RequestMethod.GET}
    )
    public Question getQuestion(
            @Parameter(description = "category of the question")
            @PathVariable QuestionCategory category) {
        return questionService.getRandomQuestion("en",category);
    }

    @Operation(summary = "Gets the correct answers for a question given its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Answer for the question",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Answer.class),
            examples = @ExampleObject(value = "{ \"id\": \"a1\", \"text\": \"France\", \"language\": \"en\", \"category\": \"FLAG\" }"))
            }),
            @ApiResponse(responseCode = "404", description = "Question was not found", content = @Content)
    })
    @RequestMapping(
            value = {"/game/getCorrectAnswer/{id}"},
            method = {RequestMethod.GET}
    )
    public Answer getCorrectAnswer(
            @Parameter(description = "Id of the question whose answer we want to retrieve")
            @PathVariable String id) {
        Question question = questionService.findQuestionById(id);
        if (question == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found");
        }
        return questionService.findAnswerById(question.getCorrectAnswerId());
    }

    @Operation(summary = "Gets the answer that matched the ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the Answer",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Answer.class),
            examples = @ExampleObject(value = "{ \"id\": \"a1\", \"text\": \"France\", \"language\": \"en\", \"category\": \"FLAG\" }"))
            }),
            @ApiResponse(responseCode = "404", description = "Answer was not found", content = @Content)
    })
    @RequestMapping(
            value = {"/game/getAnswer/{id}"},
            method = {RequestMethod.GET}
    )
    public Answer getAnswer(
            @Parameter(description = "Id of the answer")
            @PathVariable String  id) {
        Answer answer = questionService.findAnswerById(id);
        if(answer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found");
        }
        return answer;
    }

    @Operation(summary = "Removes the question matching the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletes the question", content = @Content),
            @ApiResponse(responseCode = "404", description = "Question not found", content = @Content)
    })
    @RequestMapping(
            value = {"/game/removeQuestion/{id}"},
            method = {RequestMethod.GET}
    )
    public void removeQuestion(
            @Parameter(description = "Id of the question to be removed")
            @PathVariable String id){
        Question question = questionService.findQuestionById(id);
        if(question == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found");
        }
        questionService.removeQuestion(question);
    }
}
