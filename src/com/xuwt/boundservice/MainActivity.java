package com.xuwt.boundservice;

import com.xuwt.boundservice.aidl.RemoteActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener{
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.local:
                startActivity(new Intent(this , LocalActivity.class));
                break;
            case R.id.messenger:
                startActivity(new Intent(this , MessengerActivity.class));
                break;
            case R.id.messenger_communicate:
                startActivity(new Intent(this , MessengerCommunicateActivity.class));
                break;
            case R.id.aidl:
                startActivity(new Intent(this , RemoteActivity.class));
                break;
        }

    }
}
