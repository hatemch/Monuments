package com.example.pfamonument;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Resultat extends AppCompatActivity {

    TextView score, scoreF;
    private String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle infoNum = this.getIntent().getExtras();
        if(infoNum != null) {
            num = infoNum.getString("score");
            if(Integer.parseInt(num) >= 7 )
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.resultat);
                score=(TextView)findViewById(R.id.score);
                score.setText("Bravo Votre score est  "+num+"/10");
            } else {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.perdu);
                scoreF=(TextView)findViewById(R.id.scoreF);
                scoreF.setText("Votre score est  "+num);
            }
        }
    }
}
