package com.example.pfamonument;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.pfamonument.Model.Question;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements TabHost.OnTabChangeListener {

    private TabHost tabHost;
    private ViewPager mSlideViewPager;
    private LinearLayout mDoteLayout;

    //tab quiz
    private TextView questionTxt, questionNum;
    private Button b1, b2, b3;
    int correct = 0;
    int erreur = 0;
    int total = 1;
    int computerCount = 1;
    Question quest;

    //firebase
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("", getResources().getDrawable(R.drawable.user)).setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("", getResources().getDrawable(R.drawable.quiz)).setContent(R.id.tab2));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("", getResources().getDrawable(R.drawable.home)).setContent(R.id.tab3));
        tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("", getResources().getDrawable(R.drawable.fav)).setContent(R.id.tab4));
        tabHost.addTab(tabHost.newTabSpec("tab5").setIndicator("", getResources().getDrawable(R.drawable.map)).setContent(R.id.tab5));
        tabHost.setOnTabChangedListener(this);

        //quiz
        b1 = (Button) findViewById(R.id.option1);
        b2 = (Button) findViewById(R.id.option2);
        b3 = (Button) findViewById(R.id.option3);
        questionTxt = (TextView) findViewById(R.id.question);
        questionNum = (TextView) findViewById(R.id.numero);
    }

    @Override
    public void onTabChanged(String s) {
        if (s.equals("tab1") ) {

        }else if (s.equals("tab2") ) {
            total = 1;
            correct = 0;
            updateQuestion();
        } else if (s.equals("tab3") ) {

        } else if (s.equals("tab4") ) {

        } else if (s.equals("tab5") ) {
            //Intent intent= new Intent(MainActivity.this, Map.class);
            //startActivity(intent);
        }
    }

    private void updateQuestion() {
        final String num = Integer.toString(total);
        final String score = Integer.toString(correct);

        if(total > 10)
        {
            //ouvrir Resultat Activity
            Intent intent= new Intent(MainActivity.this, Resultat.class);
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

                    questionNum.setText(num +"/10");
                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(b1.getText().toString().equals(question.getReponse())) {
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
                                },1000);
                            } else {
                                //reponse incorrect
                                erreur++;
                                b1.setBackgroundResource(R.drawable.button2);
                                if(b2.getText().toString().equals(question.getReponse()))
                                {
                                    b2.setBackgroundResource(R.drawable.button3);
                                } else if (b3.getText().toString().equals(question.getReponse()))
                                {
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
                                },1000);
                            }
                        }
                    });

                    b2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(b2.getText().toString().equals(question.getReponse())) {
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
                                },1000);
                            } else {
                                //reponse incorrect
                                erreur++;
                                b2.setBackgroundResource(R.drawable.button2);
                                if(b1.getText().toString().equals(question.getReponse()))
                                {
                                    b1.setBackgroundResource(R.drawable.button3);
                                } else if (b3.getText().toString().equals(question.getReponse()))
                                {
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
                                },1000);
                            }
                        }
                    });

                    b3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(b3.getText().toString().equals(question.getReponse())) {
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
                                },1000);
                            } else {
                                //reponse incorrect
                                erreur++;
                                b3.setBackgroundResource(R.drawable.button2);
                                if(b1.getText().toString().equals(question.getReponse()))
                                {
                                    b1.setBackgroundResource(R.drawable.button3);
                                } else if (b2.getText().toString().equals(question.getReponse()))
                                {
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
                                },1000);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Déconnexion");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(MainActivity.this, Login.class));
        return super.onOptionsItemSelected(item);
    }
}
