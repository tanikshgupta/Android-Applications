package com.example.taniksh.connect3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int activePlayer = 0; //0 for yellow and 1 for red and 2 for empty
    int[] gameState = {2,2,2,2,2,2,2,2,2};
    int[][] winningPositions = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    boolean gameActive = true;

    public void dropIn(View view){

        ImageView counter = (ImageView)view;
        int tappedCounter = Integer.parseInt(counter.getTag().toString());
        if(gameState[tappedCounter]==2 && gameActive ) {
            counter.setTranslationY(-1000);
            gameState[tappedCounter] = activePlayer;
            if (activePlayer == 0) {
                counter.setImageResource(R.drawable.yellow);
                activePlayer = 1;
            } else {
                counter.setImageResource(R.drawable.red);
                activePlayer = 0;
            }
            counter.animate().translationYBy(1000).rotation(3600).setDuration(400);
            for (int[] winningPosition : winningPositions) {
                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2) {
                    String winner = "";
                    if (activePlayer == 1) {
                        winner = "yellow";
                    } else {
                        winner = "red";
                    }
                    //Toast.makeText(this, winner + " has won!", Toast.LENGTH_SHORT).show();
                    winner+=" has won!";
                    gameActive = false;
                    Button playAgainButton = (Button) findViewById(R.id.button);
                    TextView winnerTextView = (TextView) findViewById(R.id.textView);
                    winnerTextView.setText(winner);
                    playAgainButton.setVisibility(View.VISIBLE);
                    winnerTextView.setVisibility(View.VISIBLE);
                }
            }
            boolean someOneHasNotWon = true;
            for(int i=0;i<gameState.length;i++){
                if(gameState[i]==2 ){
                    someOneHasNotWon = false;
                }
            }
            if(someOneHasNotWon && gameActive){
                gameActive = false;
                String winner="Nobody Won :(";
                Button playAgainButton = (Button) findViewById(R.id.button);
                TextView winnerTextView = (TextView) findViewById(R.id.textView);
                winnerTextView.setText(winner);
                playAgainButton.setVisibility(View.VISIBLE);
                winnerTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void playAgain(View view){
        Log.i("info","play again button preesed");
        Button playAgainButton = (Button) findViewById(R.id.button);
        TextView winnerTextView = (TextView) findViewById(R.id.textView);
        android.support.v7.widget.GridLayout gridLayout= (android.support.v7.widget.GridLayout) findViewById(R.id.gridLayout);
        for (int i=0; i<gridLayout.getChildCount();i++){
            ImageView counter = (ImageView) gridLayout.getChildAt(i);
            counter.setImageDrawable(null);
        }
        int activePlayer = 0; //0 for yellow and 1 for red and 2 for empty
        for(int i=0;i<gameState.length;i++){
            gameState[i]=2;
        }
        gameActive = true;
        playAgainButton.setVisibility(View.INVISIBLE);
        winnerTextView.setVisibility(View.INVISIBLE);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
