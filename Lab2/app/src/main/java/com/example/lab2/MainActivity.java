package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_NAME="output.txt";

    TextView mEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText=findViewById(R.id.textNameSurname);
    }

    double function(double x){
        double K;
        K=Math.sqrt(Math.pow(3+x,6)-Math.log(x))/(Math.pow(Math.E,0)+Math.asin(Math.pow(6*x,2)));
        return K;
    }

    public void clickInfo(View v){
        View mainView=findViewById(R.id.scrollView2);
        mainView.setVisibility(mainView.getVisibility()==View.VISIBLE ? (View.INVISIBLE) : (View.VISIBLE));
    }

    public void save(View v){

        TextView textA, textB, textStep;
        String a="",b="";
        double step=0;
        FileOutputStream fos = null;
        String text="";
        textA = findViewById(R.id.editTextA);
        textB = findViewById(R.id.editTextB);

        try{
            textStep = findViewById(R.id.editTextStep);
            a = textA.getText().toString();
            b = textB.getText().toString();
            step = Double.parseDouble(textStep.getText().toString());
            text = "a= "+ a + ", b= "+b+", step=" + step+'\n';

        }
        catch (Exception e){
            TextView t=findViewById(R.id.textViewOut);
            t.setText("You have not inputted parameters correctly!");
            return;
        }
        if(Double.parseDouble(textA.getText().toString())>Double.parseDouble(textB.getText().toString())){
            TextView t=findViewById(R.id.textViewOut);
            t.setText("You have not inputted parameters correctly!");
            return;
        }


        try{
            fos = openFileOutput(FILE_NAME,MODE_PRIVATE);
            fos.write(text.getBytes());

            for(double aa=Double.parseDouble(textA.getText().toString());aa<Double.parseDouble(textB.getText().toString())+step;aa+=step){
                double value = function(aa);
                String tempo = "x= "+ aa + ", F= "+value+'\n';
                fos.write(tempo.getBytes());

            }
            Toast.makeText(this,"Saved to "+getFilesDir()+"/"+FILE_NAME,Toast.LENGTH_SHORT).show();

        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try{
                if (fos != null)
                    fos.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }


    }

    public void load(View v){
        FileInputStream fis = null;
        try{
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder stringBuilder=new StringBuilder();
            String text;
            while((text = br.readLine())!=null){
                stringBuilder.append(text).append("\n");

            }
            TextView t=findViewById(R.id.textViewOut);
            t.setText(stringBuilder.toString());

        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally{
            try{
                if(fis!=null){
                    fis.close();
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
