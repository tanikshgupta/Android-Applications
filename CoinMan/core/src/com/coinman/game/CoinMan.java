package com.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture dizzy;
	Texture[] man;
	int manState = 0;
	int pause = 0;
	float gravity = 0.2f;
	float velocity = 0;
	int manY = 0;
	Rectangle manRectangle;
	int score = 0;
	BitmapFont font;
    int gameState = 0;

	Random random;

	ArrayList<Integer> coinsX = new ArrayList<Integer>();
	ArrayList<Integer> coinsY = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangles = new ArrayList<Rectangle>();
	Texture coin;
	int coinCount;

	ArrayList<Integer> bombsX = new ArrayList<Integer>();
	ArrayList<Integer> bombsY = new ArrayList<Integer>();
	ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>();
	Texture bomb;
	int bombsCount;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0]= new Texture("frame-1.png");
		man[1]= new Texture("frame-2.png");
		man[2]= new Texture("frame-3.png");
		man[3]= new Texture("frame-4.png");
		manY = Gdx.graphics.getHeight()/2;

		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		dizzy =  new Texture("dizzy-1.png");

		random = new Random();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
	}

	public void makeCoin() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinsY.add((int)height);
		coinsX.add(Gdx.graphics.getWidth());
	}

	public void makeBomb() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombsY.add((int)height);
		bombsX.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if (gameState == 1) {
		    //GAME IS ACTIVE
            //Bombs

            if (bombsCount < 300) {
                bombsCount++;
            } else {
                bombsCount = 0;
                makeBomb();
            }

            bombRectangles.clear();
            for (int i = 0; i < bombsX.size(); i++) {
                batch.draw(bomb, bombsX.get(i),bombsY.get(i));
                bombsX.set(i,bombsX.get(i) -4);
                bombRectangles.add(new Rectangle(bombsX.get(i), bombsY.get(i), bomb.getWidth(), bomb.getHeight()));
            }

            for (int i = 0; i < bombsX.size(); i++) {
                batch.draw(bomb, bombsX.get(i),bombsY.get(i));
                bombsX.set(i,bombsX.get(i) - 8);
            }

            //Coins

            if (coinCount < 100) {
                coinCount++;
            } else {
                coinCount = 0;
                makeCoin();
            }


            coinRectangles.clear();
            for (int i = 0; i < coinsX.size(); i++) {
                batch.draw(coin, coinsX.get(i),coinsY.get(i));
                coinsX.set(i,coinsX.get(i) -4);
                coinRectangles.add(new Rectangle(coinsX.get(i), coinsY.get(i), coin.getWidth(), coin.getHeight()));
            }

            if (Gdx.input.justTouched()){
                velocity = -10;
            }

            if (pause < 5) {
                pause++;
            } else {
                pause = 0;

                if (manState < 3) {
                    manState++;
                } else {
                    manState = 0;
                }
            }
            velocity = velocity + gravity;
            manY -= velocity;
            if (manY <= 0) {
                manY = 0;
            }

        } else if (gameState == 0){
		    //WAITING TO START
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else if (gameState == 2) {
		    //GAME OVER
            if (Gdx.input.justTouched()) {
                gameState = 1;
                manY = Gdx.graphics.getHeight()/2;

                score  =0;
                velocity = 0;
                coinsX.clear();;
                coinsY.clear();
                coinRectangles.clear();
                coinCount = 0;
                bombsX.clear();;
                bombsY.clear();
                bombRectangles.clear();

            }
        }

		if (gameState == 2) {
			batch.draw(dizzy,Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2 - 150,manY);
		} else {
			batch.draw(man[manState],Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2 - 150,manY);
		}
		manRectangle = new Rectangle(Gdx.graphics.getWidth()/2 - man[manState].getWidth()/2 - 150, manY, man[manState].getWidth(), man[manState].getHeight());

		for (int i= 0;i<coinRectangles.size();i++) {
			if (Intersector.overlaps(manRectangle,coinRectangles.get(i))) {
				score++;
				coinRectangles.remove(i);
				coinsX.remove(i);
				coinsY.remove(i);
				break;
			}
		}

		for (int i= 0;i<bombRectangles.size();i++) {
			if (Intersector.overlaps(manRectangle,bombRectangles.get(i))) {
				//Gdx.app.log("Bomb!","Collision!");
                gameState = 2;
			}
		}

		font.draw(batch, String.valueOf(score), 100, 200);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
