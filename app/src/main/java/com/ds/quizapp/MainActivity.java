package com.ds.quizapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daasuu.ahp.AnimateHorizontalProgressBar;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.hanks.htextview.evaporate.EvaporateTextView;
import com.hanks.htextview.fade.FadeText;
import com.hanks.htextview.fade.FadeTextView;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "QuizApp";


    // TODO: Declare member variables here:
    Button mTrueButton;
    Button mFalseButton;
    TextView mQuestionTextView;
    TextView mScoreTextView;
    AnimateHorizontalProgressBar mProgressBar;
    int mIndex;
    int mScore;
    int mQuestion;
    boolean mCorrectAnswer;

    // TODO: Declare Animation variable
    private Animation buttonPressAnim, alertDialogAnim;

    // TODO: Uncomment to create question bank
    private TrueFalse[] mQuestionBank = new TrueFalse[]{
            new TrueFalse(R.string.question_1, true),
            new TrueFalse(R.string.question_2, false),
            new TrueFalse(R.string.question_3, false),
            new TrueFalse(R.string.question_4, false),
            new TrueFalse(R.string.question_5, false),
            new TrueFalse(R.string.question_6, false),
            new TrueFalse(R.string.question_7, false),
            new TrueFalse(R.string.question_8, false),
            new TrueFalse(R.string.question_9, true),
            new TrueFalse(R.string.question_10, true),
            new TrueFalse(R.string.question_11, false),
            new TrueFalse(R.string.question_12, true),
            new TrueFalse(R.string.question_13, false),
            new TrueFalse(R.string.question_14, false),
            new TrueFalse(R.string.question_15, false),
            new TrueFalse(R.string.question_16, true),
            new TrueFalse(R.string.question_17, true),
            new TrueFalse(R.string.question_18, true),
            new TrueFalse(R.string.question_19, true),
            new TrueFalse(R.string.question_20, false),
    };

    // TODO: Declare constants here
    final int PROGRESS_BAR_INCREMENT = (int) Math.ceil(100.0 / mQuestionBank.length);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mScore = savedInstanceState.getInt("ScoreKey");
            mIndex = savedInstanceState.getInt("IndexKey");
        } else {
            mScore = 0;
            mIndex = 0;
        }

        // Toolbar and AppBar Configuration
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // TODO: LoadAnimation resource file here
        buttonPressAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_pressed);
        alertDialogAnim = AnimationUtils.loadAnimation(this, R.anim.alert_popup);

        // TODO: Load UI variables id here:
        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        mQuestionTextView = findViewById(R.id.question_text_view);
        mScoreTextView = findViewById(R.id.score);
        mProgressBar = findViewById(R.id.progress_bar);

        mScoreTextView.setText("Score : " + mScore + "/" + mQuestionBank.length);

        // set First Question to the TextView On Startup;
        mQuestion = mQuestionBank[mIndex].getmQuestionID();
        mQuestionTextView.setText(mQuestion);

        // TODO: onClickListener declared here
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "True Button pressed");
                //Toast.makeText(getApplicationContext(), "True pressed", Toast.LENGTH_SHORT).show();
                view.startAnimation(buttonPressAnim);
                checkAnswer(true);
                updateQuestion();
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "False Button pressed");
                //Toast.makeText(getApplicationContext(), "False pressed!", Toast.LENGTH_SHORT).show();
                view.startAnimation(buttonPressAnim);
                checkAnswer(false);
                updateQuestion();
            }
        });

    }


    // TODO: When back Arrow btn tapped
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private void updateQuestion() {
        mIndex = (mIndex + 1) % mQuestionBank.length;

        aletEndUp();

        mQuestion = mQuestionBank[mIndex].getmQuestionID();
        mQuestionTextView.setText(mQuestion);
        mProgressBar.incrementProgressBy(PROGRESS_BAR_INCREMENT);
        mScoreTextView.setText("Score : " + mScore + "/" + mQuestionBank.length);

    }

    private void checkAnswer(boolean userSelection) {
        mCorrectAnswer = mQuestionBank[mIndex].ismAnswer();

        if (userSelection == mCorrectAnswer) {
            //Toast.makeText(getApplicationContext(), R.string.correct_toast, Toast.LENGTH_SHORT).show();
            mScore = mScore + 1;
        } else {
            Toast.makeText(getApplicationContext(), R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
        }
    }

    private void aletEndUp() {

        // When Game Win
        if (mIndex == 0 && mScore == mQuestionBank.length) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("You Win")
                    .setCancelable(false)
                    .setMessage("Your scored " + mScore + " points!")
                    .setIcon(R.drawable.icon)
                    .setPositiveButton("Close App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("Restart", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mScore = 0;
                            mScoreTextView.setText("Score : " + mScore + "/" + mQuestionBank.length);
                            mProgressBar.setProgress(0);
                        }
                    })
                    .show();

        }
        // When Game Over!
        else if(mIndex == 0) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("GameOver")
                    .setCancelable(false)
                    .setMessage("Your scored " + mScore + " points!")
                    .setIcon(R.drawable.icon)
                    .setPositiveButton("Close App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("Restart", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mScore = 0;
                            mScoreTextView.setText("Score : " + mScore + "/" + mQuestionBank.length);
                            mProgressBar.setProgress(0);
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ScoreKey", mScore);
        outState.putInt("IndexKey", mIndex);
    }
}
