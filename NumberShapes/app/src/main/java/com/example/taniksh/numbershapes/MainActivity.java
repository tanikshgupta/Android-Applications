package com.example.taniksh.numbershapes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

            class Number{
                int no;

                public boolean isSquare() {
                    double squareRoot = Math.sqrt(no);
                    if (squareRoot == Math.floor(squareRoot)) {
                        return true;
                    }else {
                        return false;
                    }
                }

                public boolean isTriangular() {
                    int triangularNumber = 1;

                    for(int i=2;triangularNumber<no;i++) {

                        triangularNumber=triangularNumber+i;
                    }
                    if(triangularNumber==no) {
                        return true;
                    }else {
                        return false;
                    }
                }

            }



    public void testNumber(View view) {

        Log.i("Info", "button pressed!");
        EditText editText = (EditText) findViewById(R.id.editText2);

        String s = "";
        if (editText.getText().toString().isEmpty()) {
            s = "Please enter a number...";
        } else {
            Number myNumber = new Number();
            myNumber.no = Integer.parseInt(editText.getText().toString());
            if (!myNumber.isSquare() && !myNumber.isTriangular()) {
                s = myNumber.no + " is neither Triangular nor Square Number";
            } else if (myNumber.isSquare() && myNumber.isTriangular()) {
                s = myNumber.no + " is both Triangular and Square Number";
            } else if (myNumber.isTriangular()) {
                s = myNumber.no + " is only Triangular Number";
            } else {
                s = myNumber.no + " is only Square Number";
            }
        }
            Toast.makeText(this, s, Toast.LENGTH_LONG).show();
            }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
