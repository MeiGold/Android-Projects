package com.example.lab5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ArrayList<Contact> contacts;
    Button showAll;
    Button showT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);

        contacts = new ArrayList<>();
        loadContacts();

        showAll = findViewById(R.id.buttonShowAllContacts);
        showT = findViewById(R.id.buttonTaskContacts);

        setShowAllButton();
        SetShowTaskButton();



    }

    public void setInfoActivity(View view) {
        Intent e = new Intent(MainActivity.this, InfoActivity.class);
        startActivity(e);
    }

    private void setShowAllButton() {
        showAll.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           showAll();
                                       }
                                   }
        );
    }

    private void SetShowTaskButton() {
        showT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTask();
            }
        });
    }

    private void showTask() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("");
        for (Contact contact : contacts) {
            if (!contact.email.contains("@mail.ru")) {
                buffer.append("First name: " + contact.firstName + "\n");
                buffer.append("Second name: " + contact.secondName + "\n");
                buffer.append("Number: " + contact.number + "\n");
                buffer.append("Email: " + contact.email + "\n");
                buffer.append("\n");
            }
        }

        showMessage("Contacts, email of which doesn't end with @mail.ru:", buffer.toString());
    }

    private void showAll() {
        StringBuffer buffer = new StringBuffer();
        for (Contact contact : contacts) {
            buffer.append("First name: " + contact.firstName + "\n");
            buffer.append("Second name: " + contact.secondName + "\n");
            buffer.append("Number: " + contact.number + "\n");
            buffer.append("Email: " + contact.email + "\n");
            buffer.append("\n");
        }

        showMessage("All contacts:", buffer.toString());
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


    private void loadContacts() {
        Cursor all_contacts_cursor = null;
        ContentResolver contentResolver = getContentResolver();

        all_contacts_cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (all_contacts_cursor.getCount() > 0) {
            while (all_contacts_cursor.moveToNext()) {
                Contact curr = new Contact();

                String id = all_contacts_cursor.getString(all_contacts_cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String fullName = all_contacts_cursor.getString(all_contacts_cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (fullName != null) {
                    int ind = fullName.lastIndexOf(' ');
                    if (ind == -1) {
                        curr.firstName = fullName;
                    } else {
                        curr.firstName = fullName.substring(0, ind);
                        curr.secondName = fullName.substring(ind + 1);
                    }
                    curr.ID = Integer.parseInt(id);

                    Cursor numb = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    while (numb.moveToNext()) {
                        curr.number = numb.getString(numb.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }

                    numb.close();

                    numb = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    while (numb.moveToNext()) {
                        curr.email = numb.getString(numb.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                    }

                    numb.close();


                    contacts.add(curr);
                }
            }
        }

        all_contacts_cursor.close();
    }
}
