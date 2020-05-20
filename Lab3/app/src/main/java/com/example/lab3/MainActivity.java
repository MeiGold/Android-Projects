package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME="output.txt";
    ArrayList<Double> x = new ArrayList<>();
    ArrayList<Double> y = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    double function(double x){
        double K;
        K=Math.sqrt(Math.pow(3+x,6)-Math.log(x))/(Math.pow(Math.E,0)+Math.asin(Math.pow(6*x,2)));
        return K;
    }

    public void setInfoActivity(View view) {
        Intent e = new Intent(MainActivity.this,MyInfoActivity.class);
        startActivity(e);
    }

    public void save(View v){

        x.clear();y.clear();
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
            text = "a= "+ a + " , b= "+b+", step=" + step+'\n';

        }
        catch (Exception e){
            TextView t=findViewById(R.id.textViewXY);
            t.setText("You have not inputted parameters correctly!");
            return;
        }
        if(Double.parseDouble(textA.getText().toString())>Double.parseDouble(textB.getText().toString())){
            TextView t=findViewById(R.id.textViewXY);
            t.setText("You have not inputted parameters correctly!");
            return;
        }


        try{
            fos = openFileOutput(FILE_NAME,MODE_PRIVATE);
            fos.write(text.getBytes());

            for(double aa=Double.parseDouble(textA.getText().toString());aa<Double.parseDouble(textB.getText().toString())+step;aa+=step){
                double value = function(aa);
                x.add(aa);
                y.add(value);
                String tempo = "x= "+ aa + " , F= "+value+'\n';
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
            TextView t=findViewById(R.id.textViewXY);
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

    private void parseFileToPlot(){
        x.clear();
        y.clear();
        FileInputStream fis = null;
        StringBuilder stringBuilder = new StringBuilder();
        try{
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            stringBuilder=new StringBuilder();
            String text;
            while((text = br.readLine())!=null){
                stringBuilder.append(text).append("\n");

            }
            TextView t=findViewById(R.id.textViewXY);
            t.setText(stringBuilder.toString());

        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        String s = stringBuilder.toString();

            Scanner scanner = new Scanner(s);
            String ss = scanner.nextLine();
            while(scanner.hasNext()){
                String s1 = scanner.next();
                x.add(scanner.nextDouble());
                String s2 = scanner.next();
                String s3 =scanner.next();
                y.add(scanner.nextDouble());
            }



    }

    public void draw(View v){
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.removeAllSeries();
        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        parseFileToPlot();
        for(int i=0;i<x.size();++i){
            dataPoints.add(new DataPoint(x.get(i),y.get(i)));
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints.toArray(new DataPoint[dataPoints.size()]));
        graph.addSeries(series);
        graph.setTitleColor(Color.RED);
    }
}
