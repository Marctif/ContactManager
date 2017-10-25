package com.cs.utd.contactmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class EditContact extends AppCompatActivity {

    private static Context context;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            Intent returnIntent = new Intent();
            String result = "MY RESULT";
            returnIntent.putExtra("result",result);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
