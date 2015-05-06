package com.xuwt.boundservice.aidl;

import com.xuwt.boundservice.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by xuweitao on 2015/5/6.
 */
public class RemoteActivity extends Activity {

    boolean mIsBound;
    TextView mCallbackText;

    IRemoteService mService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_messenger_communicate);

        // Watch for button clicks.
        Button button = (Button)findViewById(R.id.bind);
        button.setOnClickListener(mBindListener);
        button = (Button)findViewById(R.id.unbind);
        button.setOnClickListener(mUnbindListener);

        mCallbackText = (TextView)findViewById(R.id.callback);
        mCallbackText.setText("Not attached.");
    }

    private View.OnClickListener mBindListener = new View.OnClickListener() {
        public void onClick(View v) {
            doBindService();
        }
    };

    private View.OnClickListener mUnbindListener = new View.OnClickListener() {
        public void onClick(View v) {
            doUnbindService();
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            mService = IRemoteService.Stub.asInterface(service);
            mCallbackText.setText("Attached.");

            try {
                mService.registerCallback(mCallback);
            } catch (RemoteException e) {

            }
        }

        public void onServiceDisconnected(ComponentName className) {

            mService = null;
            mCallbackText.setText("Disconnected.");

        }
    };
    void doBindService() {

        bindService(new Intent(RemoteActivity.this,
                RemoteService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
        mCallbackText.setText("Binding.");
    }

    void doUnbindService() {
        if (mIsBound) {

            if (mService != null) {
                try {
                    mService.unregisterCallback(mCallback);
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service
                    // has crashed.
                }
            }

            unbindService(mConnection);
            mIsBound = false;
            mCallbackText.setText("Unbinding.");
        }
    }

    private IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub() {

        public void valueChanged(int value) {
            mHandler.sendMessage(mHandler.obtainMessage(BUMP_MSG, value, 0));
        }
    };

    private static final int BUMP_MSG = 1;

    private Handler mHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
                case BUMP_MSG:
                    mCallbackText.setText("Received from service: " + msg.arg1);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

    };
}
