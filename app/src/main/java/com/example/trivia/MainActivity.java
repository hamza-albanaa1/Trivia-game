package com.example.trivia;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.trivia.Data.AnswerListAsyncResponse;
import com.example.trivia.Data.QuestionBank;
import com.example.trivia.model.Question;
import com.example.trivia.model.Score;
import com.example.trivia.util.Prefer;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView quetiontextview;
    private TextView countertextview;
    private TextView scoretext;
    private TextView highscore;
    private ImageButton next_question;
    private ImageButton pre_question;
    private Button True;
    private Button False;

    private Score score;
    private Prefer prefs;
    private int b =0;
    private int c = -1;

    private List<Question> questionList;

    private Button share;







    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        next_question = findViewById(R.id.nextButton);
        pre_question = findViewById(R.id.prevButton);
        True = findViewById(R.id.true_button);
        False = findViewById(R.id.False_button);
        countertextview = findViewById(R.id.cornorText);
        quetiontextview = findViewById(R.id.question_textview);


        scoretext =findViewById(R.id.textscore);
        highscore = findViewById(R.id.highscore);



        share = findViewById(R.id.sharebutton);






        score = new Score();//score object
        prefs = new Prefer(MainActivity.this);//contract
        scoretext.setText(MessageFormat.format("Score is : {0}", String.valueOf(score.getScore())));
        highscore.setText(MessageFormat.format("Highest Score : {0}", String.valueOf(prefs.getHighScore())));
        c=prefs.getStat();





        next_question.setOnClickListener(this);
        pre_question.setOnClickListener(this);
        True.setOnClickListener(this);
        False.setOnClickListener(this);



        share.setOnClickListener(this);




        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {

            @Override
            public void processFinished(ArrayList<Question> QuestionArrayList) {
                updateQuestion();
                ///////to get the first question
                quetiontextview.setText(QuestionArrayList.get(c).getAnswer());
            }
        });


    }













    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prevButton:
                if (c > 0) {
                    c = (c - 1) % questionList.size();
                    updateQuestion();
                }

                break;
            case R.id.nextButton:
                c = (c + 1) % questionList.size();
                updateQuestion();

                break;
            case R.id.true_button:

                updateQuestion();
                chechAnswer(true);
                break;
            case R.id.False_button:
                updateQuestion();
                chechAnswer(false);
                break;
            case R.id.sharebutton:
                //share button logic
                shareScore();
                break;

        }

    }









    private void shareScore(){
        String message = "My current score is "+score.getScore()+"and"
                +"My highest score is "+prefs.getHighScore();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"I am Playing Trivia");
        intent.putExtra(Intent.EXTRA_TEXT,message);
        startActivity(intent);
    }











    private void chechAnswer(boolean correctanswer) {
        boolean answerIstrue = questionList.get(c).Trueanswer();
        int b;
        if (correctanswer == answerIstrue) {
            AddPoint();
            fade();
            b = R.string.correct_answer;
        } else {
            shake();
            subPoint();
            b = R.string.wrong_answer;
        }
        Toast.makeText(MainActivity.this, b, Toast.LENGTH_SHORT).show();        //طلع المسج
    }








    private void AddPoint(){
        b+=100;
        score.setScore(b);
        scoretext.setText(MessageFormat.format("Score is : {0}", String.valueOf(score.getScore())));
    }






    private void subPoint() {
        b -= 100;
        if (b > 0) {
            score.setScore(b);
            scoretext.setText(MessageFormat.format("Score is : {0}", String.valueOf(score.getScore())));
        }
        else{
            b=0;
            score.setScore(b);
            scoretext.setText(MessageFormat.format("Score is : {0}", String.valueOf(score.getScore())));
        }
    }










    private void updateQuestion() {
        String question = questionList.get(c).getAnswer();
        quetiontextview.setText(question);
        countertextview.setText(MessageFormat.format("{0}/{1}", c, questionList.size()));
    }









    private void shake() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_shake);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                if (c > 0) {
                    c = (c - 1) % questionList.size();
                    updateQuestion();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }








    private void fade() {
        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation fade = new AlphaAnimation(1.0f, 0.0f);
        fade.setDuration(250);
        fade.setRepeatCount(1);
        fade.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(fade);
        fade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }




            @Override
            public void onAnimationEnd(Animation animation) {
            cardView.setCardBackgroundColor(Color.WHITE);
            c = (c + 1) % questionList.size();
            updateQuestion();
        }


            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }








    @Override
    protected void onPause() {
        prefs.saveHighScore(score.getScore());
        prefs.setState(c);
        super.onPause();
    }

}



