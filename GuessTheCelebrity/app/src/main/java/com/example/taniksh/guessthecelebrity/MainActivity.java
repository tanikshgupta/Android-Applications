package com.example.taniksh.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> celebURLs = new ArrayList<String>();
    ArrayList<String> celebNames = new ArrayList<String>();
    int choosenCeleb=0;
    ImageView imageView;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    String[] ans=new String[4];
    int locationOfCorectAns=0;
    public void newQuestion() {
        try {
            Random random = new Random();
            choosenCeleb = random.nextInt(celebURLs.size());
            ImageDownloader imageDownloader = new ImageDownloader();
            Bitmap celebImage = imageDownloader.execute(celebURLs.get(choosenCeleb)).get();
            imageView.setImageBitmap(celebImage);
            locationOfCorectAns = random.nextInt(4);
            int incorrectAnsLocation;
            for (int i = 0; i < 4; i++) {
                if (i == locationOfCorectAns) {
                    ans[i] = celebNames.get(choosenCeleb);
                } else {
                    incorrectAnsLocation = random.nextInt(celebURLs.size());
                    while (incorrectAnsLocation == choosenCeleb) {
                        incorrectAnsLocation = random.nextInt(celebURLs.size());

                    }
                    ans[i] = celebNames.get(incorrectAnsLocation);
                }
                button0.setText(ans[0]);
                button1.setText(ans[1]);
                button2.setText(ans[2]);
                button3.setText(ans[3]);


            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void checkAns(View view){
        if (view.getTag().toString().equals(Integer.toString(locationOfCorectAns))){
            Toast.makeText(this, "CORRECT!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "WRONG! It was "+celebNames.get(choosenCeleb), Toast.LENGTH_SHORT).show();
        }
        newQuestion();
    }
    public class ImageDownloader extends  AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream=connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection=(HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data!=-1){
                    char current = (char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.imageView);
        button0=findViewById(R.id.button0);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        DownloadTask task=new DownloadTask();
        String result=null;
        try {
            result=task.execute("http://www.posh24.se/kandisar").get();
            //Log.i("contents of url",result);
            String[] splitResult = result.split("<div class=\"listedArticles\">");
            Pattern p = Pattern.compile("img src=\"(.*?)\"");
            Matcher m = p.matcher(splitResult[0]);
            while (m.find()){
                celebURLs.add(m.group(1));
            }
             p = Pattern.compile("alt=\"(.*?)\"");
             m = p.matcher(splitResult[0]);
            while (m.find()){
                //System.out.println(m.group(1));
                celebNames.add(m.group(1));
            }

            newQuestion();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
