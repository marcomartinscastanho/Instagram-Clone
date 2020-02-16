package com.martinscastanho.marco.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> usernames;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        listView = findViewById(R.id.listView);
        usernames = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, usernames);
        listView.setAdapter(arrayAdapter);

        populateUserList();
    }

    public void populateUserList(){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e != null){
                    e.printStackTrace();
                    return;
                }
                if(objects.size() == 0){
                    Log.e("ERROR", "objects is empty");
                }
                for(ParseUser user : objects){
                    usernames.add(user.getUsername());
                }
                listView.setAdapter(arrayAdapter);
            }
        });
    }
}
