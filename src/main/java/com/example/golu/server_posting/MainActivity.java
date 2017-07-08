package com.example.golu.server_posting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
EditText et1,et2,et3;
    Button b1;
    TextView textView;
    MyTask myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1=(EditText)findViewById(R.id.tv1);
        et2=(EditText)findViewById(R.id.tv2);
        et3=(EditText)findViewById(R.id.tv3);
        textView=(TextView)findViewById(R.id.textView);
        b1=(Button) findViewById(R.id.b1);
        myTask=new MyTask();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* String name= et1.getText().toString();
                String city= et1.getText().toString();
                String twitter= et1.getText().toString();*/
               // myTask.execute("http://hmkcode.appspot.com/jsonservlet",name,city,twitter);
                myTask.execute("http://hmkcode.appspot.com/jsonservlet");
                b1.setVisibility(View.GONE);
            }
        });
    }

    private class MyTask extends AsyncTask<String ,Void,Integer> {
        URL url;
        HttpURLConnection connection;
        OutputStream outputStream;
        OutputStreamWriter outputStreamWriter;
        //NO NEED OG BUFFERED WRITER - AS DATA IS SMALL AMOUNT OF DATA WE ARE POSTING TO SERVER
        String name,city,twitter;
        @Override
        protected void onPreExecute() {
            Toast.makeText(MainActivity.this,"ABOUT TO POST",Toast.LENGTH_SHORT).show();
             name= et1.getText().toString();//I WANT TO SEND NAME
             city= et2.getText().toString();//I WANT TO SEND CITY
             twitter= et3.getText().toString();//I WANT TO SEND TWITTER DETAAILS - B36@
        }

        @Override
        protected Integer doInBackground(String... strings) {
            try {
                url=new URL(strings[0]);
                connection= (HttpURLConnection) url.openConnection();//open only for reading purpose
                //extra steps for posting data to server
                //BELOW LINE TELL TO SERVER THAT I WANT CONNECTION FOR POST REQST
                connection.setDoOutput(true);
                //BELOW LINE TELL TO SERVER THAT I am sending jSON DATA TO YOU
                connection.setRequestProperty("Content-type","application/json");
                //what data want to send to server?
                //IT IS ALREADY AVALABLE  IN ONPRE EXECUTE
                //NOW LET US CONVERT NAME,CITY,AND TWITTER -TO JSON DATA
                JSONObject j=new JSONObject();
                j.accumulate("name",name);
                j.accumulate("country",city);
                j.accumulate("twitter",twitter);
                //extra steps done
                outputStream=connection.getOutputStream();
                outputStreamWriter=new OutputStreamWriter(outputStream);
                //now post json-data to server using above output stream writter
                outputStreamWriter.write(j.toString());//json object to string
                //tell to server that you are done with writing ,so that server can read
                outputStreamWriter.flush();//--this is where server starts reading your data
                //now ask server for the response
                int result=connection.getResponseCode();//if everything is fine , server gives 200

                //now return this server response to onpost execute
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return -2;//WE are assuming that 2 means wrong URL
            } catch (IOException e) {
                e.printStackTrace();
                return -1;////WE are assuming that 1 means IO Exception
            } catch (JSONException e) {
                e.printStackTrace();
                return 0;//we assume that 0 means json exception
            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            switch (integer){
                case -2:
                    textView.setText("WRONG URL");
                    break;
                case -1:
                    textView.setText("CHECK INTERNET -THERE IS AN ERROR");
                    break;
                case 0:
                    textView.setText("JSON EXCEPTION - GIVE VALUES PROPERLY TO SERVER");
                    break;
                case HttpURLConnection.HTTP_OK:
                    textView.setText("djbejhkvje");
            }
        }
    }
}
