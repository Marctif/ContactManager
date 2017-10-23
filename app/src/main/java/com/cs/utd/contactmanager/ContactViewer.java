package com.cs.utd.contactmanager;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ContactViewer extends AppCompatActivity {

    List<Contact> contactList;
    FileManager fileManager;

    private static Context context;

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

        List<String> formattedList = toStringArray(contactList);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, formattedList);

        ListView listView = (ListView) findViewById(R.id.contact_list);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public List<String> toStringArray(List<Contact> list){
        List<String> formattedList = new ArrayList<String>();
        for(Contact c : list){
            formattedList.add(c.toString());
        }
        return formattedList;
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
                    Contact contact = new Contact(split[0],split[1],split[2],split[3]);
                    contacts.add(contact);
                }

                return contacts;
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }
            return null;
        }

        public void saveNewContact(Contact newContact){

            try {
                AssetManager am = context.getAssets();
                BufferedWriter updatedContactFile = new BufferedWriter(new FileWriter("contact.txt"));
                for(Contact c : contactList){
                    updatedContactFile.write(c.firstName + "," + c.lastName + "," + c.phoneNumber + "," + c.email + "\n");
                }
                updatedContactFile.close();
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    public class Contact {
        String firstName;
        String lastName;
        String phoneNumber;
        String email;

        public Contact(String first,String last, String phoneNum, String email){
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
