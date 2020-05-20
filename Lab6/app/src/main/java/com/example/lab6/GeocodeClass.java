package com.example.lab6;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocodeClass extends AppCompatActivity {

    double d_latitude=0;
    double d_longitude=0;
    private Geocoder geocoder;
    List<Address> addressList;

    Button backButton;
    Button checkButton;
    TextView address;

    EditText latET;
    EditText longitET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        geocoder = new Geocoder(this, Locale.getDefault());
        backButton = findViewById(R.id.buttonConfirm);
        checkButton = findViewById(R.id.buttonCheck);
        latET = findViewById(R.id.editTextLat);
        longitET =findViewById(R.id.editTextLong);
        address=findViewById(R.id.textViewAddressCheck);

        ConfigureBackButton();
        ConfigureCheckButton();
    }

    private void ConfigureBackButton()
    {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("lat", d_latitude);
                intent.putExtra("long", d_longitude);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void ConfigureCheckButton()
    {
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!longitET.getText().toString().equals(""))
                    d_longitude = Double.parseDouble(longitET.getText().toString());
                else
                    return;
                if (!latET.getText().toString().equals(""))
                    d_latitude = Double.parseDouble(latET.getText().toString());
                else
                    return;
                if (d_latitude>90 || d_latitude<-90) {
                    latET.setText("0");
                    return;
                }
                if (d_longitude>180 || d_longitude<-180) {
                    longitET.setText("0");
                    return;
                }

                try {
                    addressList = geocoder.getFromLocation(d_latitude, d_longitude, 1);

                    if (addressList.size()>0)
                        address.setText(addressList.get(0).getAddressLine(0));
                    else
                        address.setText("Nothing found here!");

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}
