/**
 *  Android Contact Manager app for CS 6326 assignment 4.
 *
 * A simple android app for adding and managing phone contacts.
 *  Users can view existing contacts, add new ones, delete, and modifty contacts.
 *  This is primary activity which handles the viewing of all exsting contacts
 *
 *  Authors: Marc Tifrea and Deba Imade
 *           mxt130730
 */

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
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Activity responsible for viewing all current contacts
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

    // Adds contacts to a List view by reading from the text file.
    // Called on activity creation
    // Author: Marc Tifrea
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ContactViewer.context = getApplicationContext();
        fileManager = new FileManager();
        contactList = fileManager.readFile();
        if(contactList == null)
            contactList = new ArrayList<Contact>();

        remakeListView();
    }

    // Handles the results from the EditContact Activity
    // Will either delete an existing contact or update an old one
    // Author: Marc Tifrea
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == CONTACT_REQUEST) {
            // Response for adding or modifying
            if (resultCode == RESULT_OK) {
                //Get data from request
                String id = data.getStringExtra(ContactViewer.PASS_ID);
                String first=data.getStringExtra(ContactViewer.PASS_FIRST);
                String last=data.getStringExtra(ContactViewer.PASS_LAST);
                String phone=data.getStringExtra(ContactViewer.PASS_PHONE);
                String email=data.getStringExtra(ContactViewer.PASS_EMAIL);
                boolean existing=data.getBooleanExtra(ContactViewer.PASS_EXISTING,true);

                //Updates or creates a contact
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

                //Display toast saying which action was performed
                String message = "";
                if(existing){
                    message = "Contact Updated";
                }
                else{
                    message = "Contact Create Sucessfully";
                }
                Toast toast = Toast.makeText(getApplicationContext(), message , Toast.LENGTH_SHORT);
                toast.show();

                // Do something with the contact here (bigger example below)
            }
            // Contact should be deleted
            if (resultCode == RESULT_CANCELED) {
                String id = data.getStringExtra(ContactViewer.PASS_ID);
                Contact contact = findByID(id);
                contactList.remove(contact);
                saveNewContact();
                remakeListView();

                Toast toast = Toast.makeText(getApplicationContext(), "Contact Deleted: " + contact.toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    // Method to remake the ListView. Takes the contact list array and
    // sets the ListView to mirror the arrays contents.
    // Author: Marc Tifrea
    public void remakeListView(){
        List<String> formattedList = toStringArray(contactList);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, formattedList);

        ListView listView = (ListView) findViewById(R.id.contact_list);
        listView.setAdapter(adapter);

        //Add a click listener so that EditConact activity can be launched
        // to edit a current contact
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
            }
        });
    }

    // Inflates the actionbar Menu
    // Author: Marc Tifrea
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_viewer, menu);
        return true;
    }

    // Find the next largest ID # of all contacts in the list
    // Author: Deba Imade
    public int findNextID(){
        String max = "0";
        if(contactList == null)
            return 0;

        for(Contact c : contactList){
            if(c.id.compareTo(max) > 0){
                max = c.id;
            }
        }
        return Integer.parseInt(max) + 1;
    }

    // Handles any presses on the menu (add button)
    // Author: Marc Tifrea
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //If add is clicked launch EditContact
        if (id == R.id.action_add) {
            Intent intent = new Intent(context, EditContact.class);
            intent.putExtra(PASS_EXISTING, false);
            startActivityForResult(intent, CONTACT_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Returns a sorted list of strings from a list of contacts
    // Author: Deba Imade
    public List<String> toStringArray(List<Contact> list){
        List<String> formattedList = new ArrayList<String>();
        if(list == null)
            return formattedList;

        for(Contact c : list){
            formattedList.add(c.toString());
        }
        Collections.sort(formattedList);

        return formattedList;
    }

    // Find a contact by its string name
    // Author: Deba Imade
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

    // Find a contact by its ID
    // Author: Deba Imade
    public Contact findByID(String id){
        Contact match = null;
        for(Contact c : contactList){
            if(c.id.equals(id)){
                match = c;
            }
        }
        return match;
    }

    // Class to handle the reading of the file for initial input into the ListView
    // Author: Deba Imade
    public class FileManager {
        String path;

        public FileManager(){
            path = "contacts.txt";
        }

        // Read the file line by line creating a contact for each line.
        // Adds the contact to the list
        // Author: Deba Imade
        public List<Contact> readFile(){
            List<String> fileLines = new ArrayList<String>();
            try{
                FileInputStream fis = context.openFileInput("contact.txt");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader reader=new BufferedReader(isr);
                String line;

                while ((line = reader.readLine()) != null)
                    fileLines.add(line);

                reader.close();

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

    }

    // Method to write the contacts of the list into the textfile for saving
    // Author: Deba Imade
    public void saveNewContact(){

        try {
            BufferedWriter updatedContactFile = new BufferedWriter(new OutputStreamWriter(context.openFileOutput("contact.txt", Context.MODE_PRIVATE)));
            for(Contact c : contactList){
                updatedContactFile.write(c.id + "," + c.firstName + "," + c.lastName + "," + c.phoneNumber + "," + c.email + "\n");
            }
            updatedContactFile.close();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    // Expert Class to manage info about a contact
    // Author: Marc Tifrea
    public class Contact {
        String id;
        String firstName;
        String lastName;
        String phoneNumber;
        String email;

        // Constructor for a contact
        // Author: Marc Tifrea
        public Contact(String id,String first,String last, String phoneNum, String email){
            this.id = id;
            firstName = first;
            lastName = last;
            phoneNumber = phoneNum;
            this.email = email;
        }

        // ToString method for a contact
        // Author: Marc Tifrea
        public String toString(){
            return firstName + " " + lastName + " " + phoneNumber;
        }
    }
}
