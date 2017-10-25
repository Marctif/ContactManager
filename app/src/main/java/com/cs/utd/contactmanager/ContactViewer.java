package com.cs.utd.contactmanager;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactViewer extends AppCompatActivity {

    List<Contact> contactList;
    FileManager fileManager;

    private static Context context;
    public static final String PASS_ID = "com.example.myfirstapp.MESSAGE_ID";
    public static final String PASS_FIRST = "com.example.myfirstapp.MESSAGE_FIRST";
    public static final String PASS_LAST = "com.example.myfirstapp.MESSAGE_LAST";
    public static final String PASS_PHONE = "com.example.myfirstapp.MESSAGE_PHONE";
    public static final String PASS_EMAIL = "com.example.myfirstapp.MESSAGE_EMAIL";
    public static final String PASS_EXISTING = "com.example.myfirstapp.MESSAGE_EXISTING";

    public static final int CONTACT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        ContactViewer.context = getApplicationContext();
        fileManager = new FileManager();
        contactList = fileManager.readFile();

        remakeListView();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                String id = data.getStringExtra(ContactViewer.PASS_ID);
                String first=data.getStringExtra(ContactViewer.PASS_FIRST);
                String last=data.getStringExtra(ContactViewer.PASS_LAST);
                String phone=data.getStringExtra(ContactViewer.PASS_PHONE);
                String email=data.getStringExtra(ContactViewer.PASS_EMAIL);
                boolean existing=data.getBooleanExtra(ContactViewer.PASS_EXISTING,true);

                Contact contact = null;
                if(existing){
                    contact = findByID(id);
                    contact.firstName = first;
                    contact.lastName = last;
                    contact.phoneNumber = phone;
                    contact.email = email;
                } else {
                    contact = new Contact("" + findNextID(),first,last,phone,email);
                    contactList.add(contact);
                }
                saveNewContact();
                remakeListView();

                Toast toast = Toast.makeText(getApplicationContext(), contact.toString() , Toast.LENGTH_SHORT);
                toast.show();

                // Do something with the contact here (bigger example below)
            }
            if (resultCode == RESULT_CANCELED) {
                Toast toast = Toast.makeText(getApplicationContext(), "SHOULD DELETE", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void remakeListView(){
        List<String> formattedList = toStringArray(contactList);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, formattedList);

        ListView listView = (ListView) findViewById(R.id.contact_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // selected item
                String name = (String)parent.getAdapter().getItem(position);
                Contact match = findByName(name);

                Intent intent = new Intent(context, EditContact.class);
                intent.putExtra(PASS_ID,match.id);
                intent.putExtra(PASS_FIRST, match.firstName);
                intent.putExtra(PASS_LAST, match.lastName);
                intent.putExtra(PASS_PHONE, match.phoneNumber);
                intent.putExtra(PASS_EMAIL, match.email);
                intent.putExtra(PASS_EXISTING, true);
                startActivityForResult(intent, CONTACT_REQUEST);

                /*Toast toast = Toast.makeText(getApplicationContext(), match.toString() + " " + match.email, Toast.LENGTH_SHORT);
                toast.show();
                */
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_viewer, menu);
        return true;
    }

    public int findNextID(){
        String max = "";
        for(Contact c : contactList){
            if(c.id.compareTo(max) > 0){
                max = c.id;
            }
        }
        return Integer.parseInt(max) + 1;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(context, EditContact.class);
            intent.putExtra(PASS_EXISTING, false);
            startActivityForResult(intent, CONTACT_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public List<String> toStringArray(List<Contact> list){
        List<String> formattedList = new ArrayList<String>();
        for(Contact c : list){
            formattedList.add(c.toString());
        }
        Collections.sort(formattedList);

        return formattedList;
    }

    public Contact findByName(String name){
        String[] split = name.split(" ");
        Contact match = null;
        for(Contact c : contactList){
            if(c.firstName.equals( split[0]) && c.lastName.equals( split[1]) && c.phoneNumber.equals( split[2])){
                match = c;
            }
        }
        return match;
    }

    public Contact findByID(String id){
        Contact match = null;
        for(Contact c : contactList){
            if(c.id.equals(id)){
                match = c;
            }
        }
        return match;
    }

    public class FileManager {
        String path;


        public FileManager(){
            path = "contacts.txt";
        }

        public List<Contact> readFile(){
            List<String> fileLines = new ArrayList<String>();
            try{
                AssetManager am = context.getAssets();
                InputStream is = am.open("contacts.txt");
                BufferedReader reader=new BufferedReader(new InputStreamReader(is));
                String line;

                while ((line = reader.readLine()) != null)
                    fileLines.add(line);

                reader.close(); //Deba - close file reader

                List<Contact> contacts = new ArrayList<Contact>();
                for(String s : fileLines){
                    String[] split = s.split(",");
                    Contact contact = new Contact(split[0],split[1],split[2],split[3],split[4]);
                    contacts.add(contact);
                }

                return contacts;
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }
            return null;
        }

//        public void saveNewContact(Contact newContact){
//
//            try {
//                AssetManager am = context.getAssets();
//                BufferedWriter updatedContactFile = new BufferedWriter(new FileWriter("contact.txt"));
//                for(Contact c : contactList){
//                    updatedContactFile.write(c.id + "," + c.firstName + "," + c.lastName + "," + c.phoneNumber + "," + c.email + "\n");
//                }
//                updatedContactFile.close();
//            }
//            catch (Exception ex){
//                System.out.println(ex.getMessage());
//            }
//        }
    }

    public void saveNewContact(){

        try {
            AssetManager am = context.getAssets();
            BufferedWriter updatedContactFile = new BufferedWriter(new FileWriter("contact.txt"));
            for(Contact c : contactList){
                updatedContactFile.write(c.id + "," + c.firstName + "," + c.lastName + "," + c.phoneNumber + "," + c.email + "\n");
            }
            updatedContactFile.close();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public class Contact {
        String id;
        String firstName;
        String lastName;
        String phoneNumber;
        String email;

        public Contact(String id,String first,String last, String phoneNum, String email){
            this.id = id;
            firstName = first;
            lastName = last;
            phoneNumber = phoneNum;
            this.email = email;
        }

        public String toString(){
            return firstName + " " + lastName + " " + phoneNumber;
        }
    }
}
