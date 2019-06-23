package com.example.taniksh.higherorlower;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int a;
    public void generateRandomNumber(){
        Random rand = new Random();
        a=rand.nextInt(20)+1;
    }
    public void guessFunction(View view){
        Log.i("info", Integer.toString(a));

        EditText editText = (EditText) findViewById(R.id.editText);
        int b=Integer.parseInt(editText.getText().toString());
        if(a==b){
            Toast.makeText(this, "Voila! You guessed correct number ,i.e. "+a+". Try again!! ", Toast.LENGTH_SHORT).show();
            Random rand = new Random();
            a=rand.nextInt(20)+1;
        }else if(a<b){
            Toast.makeText(this, "Guess Lower", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Guess Higher", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generateRandomNumber();
    }
}
