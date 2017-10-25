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
    private static boolean existingContact;
    private static String Contactid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        Contactid = intent.getStringExtra(ContactViewer.PASS_ID);
        String firstName = intent.getStringExtra(ContactViewer.PASS_FIRST);
        String lastName = intent.getStringExtra(ContactViewer.PASS_LAST);
        String email = intent.getStringExtra(ContactViewer.PASS_EMAIL);
        String phone = intent.getStringExtra(ContactViewer.PASS_PHONE);
        existingContact = intent.getBooleanExtra(ContactViewer.PASS_EXISTING,false);

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
        if(!existingContact)
            menu.getItem(0).setVisible(false);
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

            // Capture the layout's TextView and get the string values
            EditText textViewFirst = (EditText) findViewById(R.id.editContactFirst);
            String firstName = textViewFirst.getText().toString();
            EditText textViewLast = (EditText) findViewById(R.id.editContactLast);
            String lastName = textViewLast.getText().toString();
            EditText textViewPhone = (EditText) findViewById(R.id.editContactPhone);
            String phone = textViewPhone.getText().toString();
            EditText textViewEmail = (EditText) findViewById(R.id.editContactEmail);
            String email = textViewEmail.getText().toString();

            //Set the intent values
            returnIntent.putExtra(ContactViewer.PASS_ID,Contactid);
            returnIntent.putExtra(ContactViewer.PASS_FIRST, firstName);
            returnIntent.putExtra(ContactViewer.PASS_LAST, lastName);
            returnIntent.putExtra(ContactViewer.PASS_PHONE, phone);
            returnIntent.putExtra(ContactViewer.PASS_EMAIL, email);
            returnIntent.putExtra(ContactViewer.PASS_EXISTING, true);

            //Return the intent
            setResult(Activity.RESULT_OK,returnIntent);

            finish();
        }
        if (id == R.id.action_delete) {
            Intent returnIntent = new Intent();

            EditText textViewFirst = (EditText) findViewById(R.id.editContactFirst);
            String firstName = textViewFirst.getText().toString();
            EditText textViewLast = (EditText) findViewById(R.id.editContactLast);
            String lastName = textViewLast.getText().toString();
            EditText textViewPhone = (EditText) findViewById(R.id.editContactPhone);
            String phone = textViewPhone.getText().toString();
            EditText textViewEmail = (EditText) findViewById(R.id.editContactEmail);
            String email = textViewEmail.getText().toString();

            //Set the intent values
            returnIntent.putExtra(ContactViewer.PASS_ID,Contactid);
            returnIntent.putExtra(ContactViewer.PASS_FIRST, firstName);
            returnIntent.putExtra(ContactViewer.PASS_LAST, lastName);
            returnIntent.putExtra(ContactViewer.PASS_PHONE, phone);
            returnIntent.putExtra(ContactViewer.PASS_EMAIL, email);
            returnIntent.putExtra(ContactViewer.PASS_EXISTING, true);

            //Return the intent
            setResult(Activity.RESULT_CANCELED,returnIntent);

            finish();
        }


        return super.onOptionsItemSelected(item);
    }
}
