package com.example.wordcounter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static int idValue=0;
    public static int value;
    public EditText word;
    public RelativeLayout relativeLayout;
    public FloatingActionButton add;
    public ArrayList<String> arrayText;
    public LinearLayout linearLayout;
    public static String inputContent;
    public ArrayList<String> inputList =new ArrayList<String >();
    public Button submit;
    HashMap<String,Integer> in;
    ProgressDialog progressDialog;
    EditText a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addView(idValue);
        submit= (Button) findViewById(R.id.search);
        int layoutCount=linearLayout.getChildCount();
        for(int h=0;h<layoutCount;h++) {
            a=(EditText)((RelativeLayout) linearLayout.getChildAt(h)).getChildAt(0);
            arrayText.add(a.getText().toString());
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View view) {
                new Calculate().execute(arrayText);
            }
        });
    }
    class Calculate extends AsyncTask<ArrayList<String>,String,String> {
        @Override
        protected String doInBackground(ArrayList<String>... arrayLists) {
            try {
                ArrayList<String> a=arrayLists[0];
                in=new HashMap<String, Integer>();
                int count=0;
                int whileCount=0;
                InputStream inputStream = getAssets().open("textfile.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                while ((inputContent = br.readLine()) != null) {
                    inputContent.replace(","," ").replace("?"," ").replace("."," ").replace("!"," ").replace(":"," ");
                    String[] sentenceArray=inputContent.split(" ");
                    for(int i=0;i<a.size();i++){
                        count=0;
                        for(int j=0;j<sentenceArray.length;j++){
                            if(a.get(i).trim().equals(sentenceArray[j].trim())){
                                count++;
                            }
                        }
                        if(whileCount==0){
                            in.put(a.get(i),count);
                        }else{
                            in.put(a.get(i),in.get(i)+count);
                        }
                    }
                    whileCount++;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("loading");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Intent intent=new Intent(MainActivity.this, Words.class);
            intent.putExtra("Result",in);
            Log.d("Keyset",in.keySet().toString());
            startActivity(intent);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
    public void addView(int id){
        linearLayout= (LinearLayout) findViewById(R.id.linearLayout);
        relativeLayout=new RelativeLayout(this);
        word=new EditText(this);
        add=new FloatingActionButton(this);

        word.setId(id+100);
        add.setId(id);
        relativeLayout.setId(id+1000);
        RelativeLayout.LayoutParams actionButtonLayout=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        actionButtonLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        add.setLayoutParams(actionButtonLayout);
        relativeLayout.addView(word);
        relativeLayout.addView(add);
        linearLayout.addView(relativeLayout);
        add.setImageResource(R.drawable.add);
        add.setBackgroundColor(Color.WHITE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idval=view.getId();

               if(idval==idValue && idValue<19)
                {
                    add.setImageResource(R.drawable.subt);
                    idValue++;
                    addView(idValue);
                }
                else if(idval<idValue)
                {
                    linearLayout.removeView(findViewById(view.getId()+1000));
                    idValue--;
                }
                else if(idValue==19){
                    Toast.makeText(MainActivity.this,"Maximum words you can search is 20",Toast.LENGTH_LONG).show();
                }


            }
        });

    }

}
