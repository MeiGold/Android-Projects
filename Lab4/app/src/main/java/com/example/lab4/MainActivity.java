package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd, btnRead, btnClear, btnUpdate, btnDelete, btnDeficiteGoods, btnMinMax;
    EditText editTextID, editTextName, editTextPrice, editTextCount;
    DBGoods dbGoods;
    TextView dbTextView;
    ArrayList<DBRow> rows;
    ArrayList<String> rowsS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rows = new ArrayList<>();
        rowsS = new ArrayList<>();
        btnAdd = (Button) findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.buttonRead);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.buttonClear);
        btnClear.setOnClickListener(this);

        btnUpdate = (Button) findViewById(R.id.buttonUpdate);
        btnUpdate.setOnClickListener(this);

        btnDelete = (Button) findViewById(R.id.buttonDelete);
        btnDelete.setOnClickListener(this);

        btnDeficiteGoods = (Button) findViewById(R.id.buttonDeficiteGoods);
        btnDeficiteGoods.setOnClickListener(this);

        btnMinMax = (Button) findViewById(R.id.buttonMaxMinPrice);
        btnMinMax.setOnClickListener(this);

        editTextID = findViewById(R.id.editTextID);
        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextCount = findViewById(R.id.editTextCount);

        dbTextView = findViewById(R.id.dbTextView);

        dbGoods = new DBGoods(this);


    }

    public void setInfoActivity(View view) {
        Intent e = new Intent(MainActivity.this, MyInfoActivity.class);
        startActivity(e);
    }

    @Override
    public void onClick(View v) {
        String name = editTextName.getText().toString();
        String price = editTextPrice.getText().toString();
        String count = editTextCount.getText().toString();
        String id = editTextID.getText().toString();

        SQLiteDatabase database = dbGoods.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        switch (v.getId()) {
            case R.id.buttonAdd:
                contentValues.put(DBGoods.KEY_NAME, name);
                contentValues.put(DBGoods.KEY_PRICE, price);
                contentValues.put(DBGoods.KEY_COUNT, count);

                database.insert(DBGoods.TABLE_GOODS, null, contentValues);
                break;
            case R.id.buttonRead:
                Cursor cursor = database.query(DBGoods.TABLE_GOODS, null, null, null, null, null, null);
                StringBuffer result = new StringBuffer();
                if (cursor.moveToFirst()) {
                    do {
                        int idIndex = cursor.getColumnIndex(DBGoods.KEY_ID);
                        int nameIndex = cursor.getColumnIndex(DBGoods.KEY_NAME);
                        int priceIndex = cursor.getColumnIndex(DBGoods.KEY_PRICE);
                        int countIndex = cursor.getColumnIndex(DBGoods.KEY_COUNT);
                        rows.add(new DBRow(cursor.getInt(idIndex), cursor.getString(nameIndex), cursor.getString(priceIndex), cursor.getString(countIndex)));
                        result.append("ID = " + cursor.getInt(idIndex) +
                                ", name = " + cursor.getString(nameIndex) +
                                ", price = " + cursor.getString(priceIndex) +
                                ", count = " + cursor.getString(countIndex) + '\n');

                    } while (cursor.moveToNext());
                } else
                    Log.d("mLog", "0 rows");
                cursor.close();
                dbTextView.setText(result.toString());


                break;
            case R.id.buttonClear:
                database.delete(DBGoods.TABLE_GOODS, null, null);
                dbTextView.setText("");
                break;
            case R.id.buttonUpdate:
                if (id.equalsIgnoreCase("")) {
                    break;
                }
                contentValues.put(DBGoods.KEY_NAME, name);
                contentValues.put(DBGoods.KEY_PRICE, price);
                contentValues.put(DBGoods.KEY_COUNT, count);
                int updCount = database.update(DBGoods.TABLE_GOODS, contentValues, DBGoods.KEY_ID + "= ?", new String[]{id});

                break;
            case R.id.buttonDelete:
                if (id.equalsIgnoreCase("")) {
                    break;
                }
                int delCount = database.delete(DBGoods.TABLE_GOODS, DBGoods.KEY_ID + "=" + id, null);
                Log.d("mLog", "deleted rows count = " + delCount);
                break;
            case R.id.buttonDeficiteGoods:

                Cursor cursor_ = database.query(DBGoods.TABLE_GOODS, null, DBGoods.KEY_COUNT + "< 5", null, null, null, null);
                StringBuilder result_ = new StringBuilder();
                if (cursor_.moveToFirst()) {
                    do {
                        int idIndex = cursor_.getColumnIndex(DBGoods.KEY_ID);
                        int nameIndex = cursor_.getColumnIndex(DBGoods.KEY_NAME);
                        int priceIndex = cursor_.getColumnIndex(DBGoods.KEY_PRICE);
                        int countIndex = cursor_.getColumnIndex(DBGoods.KEY_COUNT);
                        rows.add(new DBRow(cursor_.getInt(idIndex), cursor_.getString(nameIndex), cursor_.getString(priceIndex), cursor_.getString(countIndex)));
                        rowsS.add(String.valueOf(cursor_.getInt(idIndex)));
                        rowsS.add(cursor_.getString(nameIndex));
                        rowsS.add(cursor_.getString(priceIndex));
                        rowsS.add(cursor_.getString(countIndex));
                        int countIs = Integer.parseInt(cursor_.getString(countIndex));
                        /*if (countIs < 5)
                            result_.append("name = ").append(cursor_.getString(nameIndex)).append(", count = ").append(cursor_.getString(countIndex)).append('\n');
                        */
                        result_.append("name = ").append(cursor_.getString(nameIndex)).append(", count = ").append(cursor_.getString(countIndex)).append('\n');
                    } while (cursor_.moveToNext());
                } else
                    Log.d("mLog", "0 rows");
                cursor_.close();
                dbTextView.setText(result_.toString());
                break;
            case R.id.buttonMaxMinPrice:
                Cursor _cursor = database.query(DBGoods.TABLE_GOODS, null, null, null, null, null, null);
                float minPrice = 0, maxPrice = 0;
                if (_cursor.moveToFirst()) {
                    int idIndex = _cursor.getColumnIndex(DBGoods.KEY_ID);
                    int nameIndex = _cursor.getColumnIndex(DBGoods.KEY_NAME);
                    int priceIndex = _cursor.getColumnIndex(DBGoods.KEY_PRICE);
                    int countIndex = _cursor.getColumnIndex(DBGoods.KEY_COUNT);
                    String s = _cursor.getString(priceIndex);
                    minPrice = Float.parseFloat(_cursor.getString(priceIndex));
                    maxPrice = minPrice;
                } else {
                    dbTextView.setText("There is no elements in database");
                }
                if (_cursor.moveToFirst()) {
                    do {
                        int idIndex = _cursor.getColumnIndex(DBGoods.KEY_ID);
                        int nameIndex = _cursor.getColumnIndex(DBGoods.KEY_NAME);
                        int priceIndex = _cursor.getColumnIndex(DBGoods.KEY_PRICE);
                        int countIndex = _cursor.getColumnIndex(DBGoods.KEY_COUNT);
                        rows.add(new DBRow(_cursor.getInt(idIndex), _cursor.getString(nameIndex), _cursor.getString(priceIndex), _cursor.getString(countIndex)));
                        float priceCurr = Float.parseFloat(_cursor.getString(priceIndex));
                        if (priceCurr < minPrice) {
                            minPrice = priceCurr;
                        }
                        if (priceCurr > maxPrice) {
                            maxPrice = priceCurr;
                        }


                    } while (_cursor.moveToNext());
                } else
                    Log.d("mLog", "0 rows");
                _cursor.close();
                dbTextView.setText("Maximum price - " + maxPrice + "\nMinimum price - " + minPrice);
                break;
        }
    }
}
