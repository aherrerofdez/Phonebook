package com.example.phonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //definition of class variables
    ListView listView;
    SearchView searchView;
    String[] listItem;
    String[] astrArr;
    Button searchBtn;
    Button addBtn;
    Button totalBtn;
    EditText name;
    EditText number;
    ArrayList contacts;
    ArrayAdapter<String> adapter;
    TextView result;


    //START of auxiliary methods
    private void filterContacts(String filter) {
        //method that filters the contacts based on filter String
        List result = new ArrayList();//we create an empty list where the filtered contacts will be stored

        if (filter.length() == 0) {//if the user has not written anything set the list to the original contacts list without modification
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {//if the user has written something

            for (int i = 0; i < contacts.size(); i++) {//go through the contacts array
                if (contacts.get(i).toString().toLowerCase().contains(filter.toLowerCase())) {//if the current contact contains the filter (without paying attention to
                    //lower case or upper case )
                    result.add(contacts.get(i));//add it to the results list
                }
            }
            //we set the adapter to the new contacts array
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, (String[]) result.toArray(new String[result.size()]));
            listView.setAdapter(adapter);//we set the listView
            adapter.notifyDataSetChanged(); //we notify a change so that the interface is updated

        }
    }

    //Method to check whether a name exists already in contacts arrayList
    public boolean nameExists(String name, ArrayList contacts) {
        for (int i = 0; i < contacts.size(); i++) {

            if (contacts.get(i).toString().contains(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //we get the elements from the interface and put them in JAVA objects
        listView = (ListView) findViewById(R.id._dynamic);
        searchView = (SearchView) findViewById(R.id.searchView);
        listItem = getResources().getStringArray(R.array.array_technology);
        totalBtn = findViewById(R.id.button3);
        searchBtn = findViewById(R.id.button2);
        addBtn = findViewById(R.id.button);
        name = findViewById(R.id.editTextTextPersonName);
        number = findViewById(R.id.editTextTextPersonName2);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
        listView.setAdapter(adapter);
        astrArr = getResources().getStringArray(R.array.array_technology);
        contacts = new ArrayList<String>(Arrays.asList(astrArr));
        result = findViewById((R.id.textView2));

        //we set a listener on the add button
        addBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //we get the name and phone number the user inputted
                String contactName = name.getText().toString();
                String contactNumber = number.getText().toString();

                if (nameExists(contactName, contacts)) {
                    //if the name is already in the contacts list we show a toast notification
                    Toast.makeText(getApplicationContext(), "Contact already in phonebook", Toast.LENGTH_SHORT).show();

                } else {

                    //now we add them to the contacts string ;
                    String[] newContactArr = new String[astrArr.length + 1];
                    for (int i = 0; i < astrArr.length; i++) {
                        newContactArr[i] = astrArr[i];
                    }
                    newContactArr[newContactArr.length - 1] = contactName + " " + contactNumber;
                    //we modify our contact list to incorporate the new contact
                    contacts.add(newContactArr[newContactArr.length - 1]);

                    //we modify the adapter
                    adapter = new ArrayAdapter<String>(searchView.getContext(),
                            android.R.layout.simple_list_item_1, android.R.id.text1, contacts);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                // we clear both text fields but we could set a placeholder text as well
                name.getText().clear();
                number.getText().clear();

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

              @Override
              public boolean onQueryTextSubmit(String query) {
                  return false;
              }

              @Override
              public boolean onQueryTextChange(String newText) {
                  //tracks changes made to the text field
                  if (searchView.getQuery().toString().length() == 0) {//this detects the closing of the searchView
                      String[] newContacts = new String[contacts.size()];
                      newContacts = (String[]) contacts.toArray(new String[contacts.size()]);
                      adapter = new ArrayAdapter<String>(searchView.getContext(),
                              android.R.layout.simple_list_item_1, android.R.id.text1, newContacts);
                      listView.setAdapter(adapter);
                      adapter.notifyDataSetChanged();
                  }
                  return false;
              }
          }

);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//search button listener
                String contactSearched = searchView.getQuery().toString();
                filterContacts(contactSearched);
            }
        });

        totalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//total button listener
                String numContacts = Integer.toString(contacts.size());
                result.setText("Contacts: " + numContacts);
            }
        });
    }
}