package com.cs.utd.contactmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class EditContact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String firstName = intent.getStringExtra(ContactViewer.PASS_FIRST);
        String lastName = intent.getStringExtra(ContactViewer.PASS_LAST);
        String email = intent.getStringExtra(ContactViewer.PASS_EMAIL);
        String phone = intent.getStringExtra(ContactViewer.PASS_PHONE);

        // Capture the layout's TextView and set the string as its text
        EditText textViewFirst = (EditText) findViewById(R.id.editContactFirst);
        textViewFirst.setText(firstName);
        EditText textViewLast = (EditText) findViewById(R.id.editContactLast);
        textViewLast.setText(lastName);
        EditText textViewPhone = (EditText) findViewById(R.id.editContactPhone);
        textViewPhone.setText(phone);
        EditText textViewEmail = (EditText) findViewById(R.id.editContactEmail);
        textViewEmail.setText(email);
    }
}
