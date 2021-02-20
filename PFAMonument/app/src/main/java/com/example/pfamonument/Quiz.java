package com.example.pfamonument;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pfamonument.Model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Quiz extends Fragment {

    //tab quiz
    private TextView questionTxt, questionNum;
    private Button b1, b2, b3;
    int correct = 0;
    int erreur = 0;
    int total = 1;
    MediaPlayer audio;
    Button play;
    boolean test = false;
    //firebase
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.quiz, container, false);

        audio = (MediaPlayer) MediaPlayer.create(getActivity(), R.raw.audio);
        play = (Button) rootView.findViewById(R.id.audio);
        //quiz
        b1 = (Button) rootView.findViewById(R.id.option1);
        b2 = (Button) rootView.findViewById(R.id.option2);
        b3 = (Button) rootView.findViewById(R.id.option3);
        questionTxt = (TextView) rootView.findViewById(R.id.question);
        questionNum = (TextView) rootView.findViewById(R.id.numero);

        total = 1;
        correct = 0;
        updateQuestion();
        play.setOnClickListener(playIT);
        return rootView;
    }

    private View.OnClickListener playIT = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(test == true)
            {
                audio.pause();
                test = false;
            }else {
                audio.start();
                test = true;
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        audio.release();
    }

    private void updateQuestion() {
        final String num = Integer.toString(total);
        final String score = Integer.toString(correct);

        if (total > 10) {
            //ouvrir Resultat Activity
            Intent intent = new Intent(getActivity(), Resultat.class);
            intent.putExtra("score", score);
            startActivity(intent);
        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Questions").child(String.valueOf(total));
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final Question question = dataSnapshot.getValue(Question.class);
                    questionTxt.setText(question.getQuestion());
                    b1.setText(question.getOption1());
                    b2.setText(question.getOption2());
                    b3.setText(question.getOption3());

                    questionNum.setText(num + "/10");
                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (b1.getText().toString().equals(question.getReponse())) {
                                //reponse correct
                                b1.setBackgroundResource(R.drawable.button3);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        correct++;
                                        //b1.setBackgroundColor(Color.parseColor("#FAACD3"));
                                        b1.setBackgroundResource(R.drawable.button);
                                        updateQuestion();
                                    }
                                }, 1000);
                            } else {
                                //reponse incorrect
                                erreur++;
                                b1.setBackgroundResource(R.drawable.button2);
                                if (b2.getText().toString().equals(question.getReponse())) {
                                    b2.setBackgroundResource(R.drawable.button3);
                                } else if (b3.getText().toString().equals(question.getReponse())) {
                                    b3.setBackgroundResource(R.drawable.button3);
                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        b1.setBackgroundResource(R.drawable.button);
                                        b2.setBackgroundResource(R.drawable.button);
                                        b3.setBackgroundResource(R.drawable.button);
                                        updateQuestion();
                                    }
                                }, 1000);
                            }
                        }
                    });

                    b2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (b2.getText().toString().equals(question.getReponse())) {
                                //reponse correct
                                b2.setBackgroundResource(R.drawable.button3);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        correct++;
                                        b2.setBackgroundResource(R.drawable.button);
                                        updateQuestion();
                                    }
                                }, 1000);
                            } else {
                                //reponse incorrect
                                erreur++;
                                b2.setBackgroundResource(R.drawable.button2);
                                if (b1.getText().toString().equals(question.getReponse())) {
                                    b1.setBackgroundResource(R.drawable.button3);
                                } else if (b3.getText().toString().equals(question.getReponse())) {
                                    b3.setBackgroundResource(R.drawable.button3);
                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        b1.setBackgroundResource(R.drawable.button);
                                        b2.setBackgroundResource(R.drawable.button);
                                        b3.setBackgroundResource(R.drawable.button);
                                        updateQuestion();
                                    }
                                }, 1000);
                            }
                        }
                    });

                    b3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (b3.getText().toString().equals(question.getReponse())) {
                                //reponse correct
                                b3.setBackgroundResource(R.drawable.button3);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        correct++;
                                        b3.setBackgroundResource(R.drawable.button);
                                        updateQuestion();
                                    }
                                }, 1000);
                            } else {
                                //reponse incorrect
                                erreur++;
                                b3.setBackgroundResource(R.drawable.button2);
                                if (b1.getText().toString().equals(question.getReponse())) {
                                    b1.setBackgroundResource(R.drawable.button3);
                                } else if (b2.getText().toString().equals(question.getReponse())) {
                                    b2.setBackgroundResource(R.drawable.button3);
                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        b1.setBackgroundResource(R.drawable.button);
                                        b2.setBackgroundResource(R.drawable.button);
                                        b3.setBackgroundResource(R.drawable.button);
                                        updateQuestion();
                                    }
                                }, 1000);
                            }
                        }
                    });
                    total++;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

}
