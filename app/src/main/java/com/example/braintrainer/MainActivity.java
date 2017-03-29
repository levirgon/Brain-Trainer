package com.example.braintrainer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //ArrayList<Quiz> quizzes;

    boolean gameOn = false;
    TextView questionText;
    int locationOfCorrectAnswer;
    ArrayList<Integer> answers;
    Random random;
    GridLayout gridLayout;
    int score = 0;
    int numberOfQuestions = 0;
    TextView scoreText;
    TextView timeText;
    TextView response;
    Button goButton;//= (Button) findViewById(R.id.goButton);
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        scoreText = (TextView) findViewById(R.id.scoreTextView);
        questionText = (TextView) findViewById(R.id.questionTextView);
        answers = new ArrayList<>();
        gridLayout = (GridLayout) findViewById(R.id.answersGrid);
        timeText = (TextView) findViewById(R.id.timerTextView);
        response = (TextView) findViewById(R.id.responseTextView);
        goButton = (Button) findViewById(R.id.goButton);
        mediaPlayer = MediaPlayer.create(this, R.raw.airhorn);

        random = new Random();


        generateQuestion();
    }

    private void updateTimer(int i) {

        String time = String.valueOf(i);
        if (i < 10)
            time = "0" + String.valueOf(i);

        timeText.setText(time + "s");
    }

    private void generateQuestion() {

        //answers = new ArrayList<>();
        answers.clear();

        int a = random.nextInt(50 - 5) + 4;
        int b = random.nextInt(50 - 5) + 4;

        int sum = a + b;

        locationOfCorrectAnswer = random.nextInt(4);

        int incorrect;


        for (int i = 0; i < 4; i++) {

            if (i == locationOfCorrectAnswer) {
                answers.add(sum);
            } else {
                incorrect = random.nextInt(50 - 10) + 8;

                while (incorrect == sum || answers.contains(incorrect)) {

                    incorrect = random.nextInt(50 - 10) + 8;
                }

                answers.add(incorrect);
            }
        }

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            Button button = (Button) (gridLayout.getChildAt(i));
            button.setText(String.valueOf(answers.get(i)));
        }

        String question = String.valueOf(a) + "+" + String.valueOf(b);

        questionText.setText(question);
    }


    public void chooseAnswer(View view) {

        if (gameOn) {

            String answer;
            if (view.getTag().equals(String.valueOf(locationOfCorrectAnswer))) {
                answer = "correct";
                score++;
            } else {
                answer = "incorrect";

            }

            generateQuestion();
            //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
            response.setText(answer);
            numberOfQuestions++;
            scoreText.setText(String.valueOf(score) + "/" + String.valueOf(numberOfQuestions));
        }
    }

    public void hideAll() {

        timeText.setVisibility(View.INVISIBLE);
        questionText.setVisibility(View.INVISIBLE);
        scoreText.setVisibility(View.INVISIBLE);
        gridLayout.setVisibility(View.INVISIBLE);
        response.setVisibility(View.INVISIBLE);
    }

    public void begin(View view) {
        final TextView text = (TextView) findViewById(R.id.scoreText);
        final TextView scoreView = (TextView) findViewById(R.id.finalScoreView);

        goButton.setVisibility(View.INVISIBLE);
        text.setVisibility(View.INVISIBLE);
        scoreView.setVisibility(View.INVISIBLE);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameOn = true;
                scoreText.setText("0/0");
                score = 0;
                numberOfQuestions = 0;
                generateQuestion();
                timeText.setVisibility(View.VISIBLE);
                questionText.setVisibility(View.VISIBLE);
                scoreText.setVisibility(View.VISIBLE);
                gridLayout.setVisibility(View.VISIBLE);
                response.setVisibility(View.VISIBLE);
                response.setText("");
            }
        });

        new CountDownTimer(30000 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimer((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                timeText.setText("00s");
                gameOn = false;
                hideAll();
                mediaPlayer.start();
                text.setVisibility(View.VISIBLE);
                scoreView.setText(String.valueOf(score) + "/" + String.valueOf(numberOfQuestions));
                scoreView.setVisibility(View.VISIBLE);

                new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        goButton.setVisibility(View.VISIBLE);
                    }
                }.start();


            }
        }.start();
    }
}
