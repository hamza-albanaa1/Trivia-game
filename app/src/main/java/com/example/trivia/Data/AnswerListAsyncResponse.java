package com.example.trivia.Data;


import com.example.trivia.model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinished(ArrayList<Question>QuestionArrayList);
}
