package com.example.jenb.testserver;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText textLogin = (EditText) findViewById(R.id.editText);
        final EditText textPwd = (EditText) findViewById(R.id.editText2);

        ConnectivityManager connectMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = connectMan.getActiveNetworkInfo();
        Button bt = (Button) findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(netInfo == null || !netInfo.isConnected())
                {
                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                }
                else
                {
                    String login = textLogin.getText().toString();
                    String pwd = textPwd.getText().toString();
                    String data = "";
//                    try {
//                        data = "login="+ URLEncoder.encode(login,"UTF-8");
//                        data += "&password="+URLEncoder.encode(pwd,"UTF-8");
//                    } catch (Exception ex) { }

                    new MyAsyncTask().execute("10.20.21.12/test_rest.php",data);
                }

            }
        });


    }

    class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                URL url = new URL(params[0]);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("POST");
                httpConn.setDoInput(true);
                httpConn.setDoOutput(true);
                httpConn.connect();

                DataOutputStream outputStream = new DataOutputStream(httpConn.getOutputStream());
                //outputStream.writeBytes(params[1]); // params[1] = data
                outputStream.flush();
                outputStream.close();

                InputStream inputStream = httpConn.getInputStream();
                Scanner scanner = new Scanner(inputStream, "UTF-8");
                response = scanner.useDelimiter("\\A").next();
            } catch (Exception ex) {}

            Log.e("Check res",response);

            return response;
        }

        @Override
        public void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), result,
                    Toast.LENGTH_LONG).show();

        }
    }
}
