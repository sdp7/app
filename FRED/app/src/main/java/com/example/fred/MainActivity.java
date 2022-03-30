package com.example.fred;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ch.aplu.tcpcom.*;

public class MainActivity extends AppCompatActivity {
    private Button launchButton;
    private EditText addressInput;
    private TextView hintText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hideAllBars();
        setContentView(R.layout.activity_main);
        addressInput = findViewById(R.id.device_name_input);
        addressInput.setInputType(128);
        hintText = findViewById(R.id.fail_hint_text);
        launchButton =  findViewById(R.id.connect_button);
        launchButton.setOnClickListener(view -> {
            String address = addressInput.getText().toString();
            if(connectionCheck(address)){
                connectionAccept();
            }else{
                connectionFail();
            }
        });


    }

    private void connectionAccept(){
        hintText.setText(R.string.empty);
        Intent intent = new Intent(MainActivity.this,UserControl.class);
        startActivity(intent);
    }

    private void connectionFail(){
        hintText.setText(R.string.incorrect_hint);
        addressInput.getText().clear();
    }

    private boolean connectionCheck(String hostAddress){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        TCPClient tcpClient = new TCPClient(hostAddress, ConnectionTask.getPort());
        boolean connectionSuccess = tcpClient.connect(5);
        if(connectionSuccess){
            Log.i("connection","connected");
            ConnectionTask.setAddress(hostAddress);
            tcpClient.disconnect();
            return true;
        }else{
            Log.i("connection","failed");
            return false;
        }
    }

}