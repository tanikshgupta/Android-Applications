package com.parse.starter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    ArrayList<String> users = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.tweet_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.tweet) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Send a Tweet");

            final EditText tweetEditext = new EditText(this);

            builder.setView(tweetEditext);

            builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.i("info",tweetEditext.getText().toString());
                    ParseObject tweet = new ParseObject("Tweet");
                    tweet.put("tweet",tweetEditext.getText().toString());
                    tweet.put("username",ParseUser.getCurrentUser().getUsername());

                    tweet.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e==null) {
                                Toast.makeText(UsersActivity.this, "Tweet sent!", Toast.LENGTH_SHORT).show();
                            } else {
                                e.printStackTrace();
                                Toast.makeText(UsersActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.i("info","i dont waanna tweet !");
                    dialogInterface.cancel();
                }
            });

            builder.show();
        } else if (item.getItemId() == R.id.logout) {
            ParseUser.logOut();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(intent);
        } else if (item.getItemId() == R.id.viewFeed) {
            Intent intent = new Intent(getApplicationContext(), FeedActivity.class);

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        setTitle("Users");

        final ListView userListView = findViewById(R.id.usersListView);
        userListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);



        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_checked,users);
        userListView.setAdapter(arrayAdapter);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView checkedTextView =  (CheckedTextView) view;
                if (checkedTextView.isChecked()) {
                    Log.i("info","chked");
                    ParseUser.getCurrentUser().add("isFollowing",users.get(i));
                } else {
                    Log.i("info","not cheked");
                    ParseUser.getCurrentUser().getList("isFollowing").remove(users.get(i));
                    List tempUsers = ParseUser.getCurrentUser().getList("isFollowing");
                    ParseUser.getCurrentUser().remove("isFollowing");
                    ParseUser.getCurrentUser().put("isFollowing",tempUsers);
                }
                ParseUser.getCurrentUser().saveInBackground();
            }
        });

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e==null && objects.size()>0) {
                    for (ParseUser user : objects) {
                        users.add(user.getUsername());
                    }
                    arrayAdapter.notifyDataSetChanged();

                    for (String username : users) {
                        if (ParseUser.getCurrentUser().getList("isFollowing").contains(username)) {
                            userListView.setItemChecked(users.indexOf(username),true);
                        }
                    }
                }
            }
        });

    }
}
