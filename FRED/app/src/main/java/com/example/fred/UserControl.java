package com.example.fred;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import ch.aplu.tcpcom.*;

public class UserControl extends AppCompatActivity implements JoystickView.JoystickListener{

    TCPClient tcpClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_control);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.d("address",ConnectionTask.getAddress());
        Log.d("port",String.valueOf(ConnectionTask.getPort()));
        tcpClient = new TCPClient(ConnectionTask.getAddress(), ConnectionTask.getPort());
        boolean connectionSuccess = tcpClient.connect(5);
        if(connectionSuccess){
            Log.i("connection","connected");
        }else{
            Log.i("connection","failed");
        }
        Button exit_button = findViewById(R.id.exit_button2);
        exit_button.setOnClickListener(view -> {
            tcpClient.disconnect();
            Intent intent = new Intent(UserControl.this,MainActivity.class);
            startActivity(intent);
        });
    }



    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id) {
        TextView wheelBase_Text = (TextView) findViewById(R.id.wheelbase_text);
        TextView turret_Text = (TextView) findViewById(R.id.turret_text);
        switch (id){
            case R.id.left_joystick:
                String wheelBase_String = "Wheelbase;"+ xPercent + ";"+ yPercent;
                wheelBase_Text.setText(wheelBase_String);
                if(tcpClient.isConnected()){
                    tcpClient.sendMessage(wheelBase_String);
                }else{
                    wheelBase_Text.setText("Connection Lost");
                    turret_Text.setText(R.string.empty);
                }

                break;
            case R.id.right_joystick:
                String turret_String = "Turret;" + xPercent + ";"+ yPercent;
                turret_Text.setText(turret_String);
                if(tcpClient.isConnected()){
                    tcpClient.sendMessage(turret_String);
                }else{
                    wheelBase_Text.setText("Connection Lost");
                    turret_Text.setText(R.string.empty);
                }
                break;
        }

    }
}
