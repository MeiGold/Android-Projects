package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public void clickInfo(View v){
        View mainView=findViewById(R.id.scrollViewInfo);
        mainView.setVisibility(mainView.getVisibility()==View.VISIBLE ? (View.INVISIBLE) : (View.VISIBLE));
    }

    public void calculateExpression(View v){
        TextView result=findViewById(R.id.textResult);
        double x=0,a=0,c=0;

        try{
            TextView inputX=findViewById(R.id.inputParameterX);
            CharSequence X=inputX.getText();
            x=Double.parseDouble(X.toString());
        }
        catch (Exception e){
            result.setText("You didn't input parameter correctly(it's probably blank)!");
            return;
        }

        double K=0;

        try{
            //L= (Math.sqrt( Math.pow(Math.E,x) - Math.pow(Math.cos(x*x*Math.pow(a,5)),4 ) + Math.pow(Math.atan(a-Math.pow(x,4)),4) )) / ( Math.pow (Math.abs(a+x*Math.pow(c,4)) , 12) );
            K=Math.sqrt(Math.pow(3+x,6)-Math.log(x))/(Math.pow(Math.E,0)+Math.asin(Math.pow(6*x,2)));
        }
        catch (Exception e){
            result.setText("Calculation error!");
            return;
        }
        result.setText("K = "+Double.toString(K));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
