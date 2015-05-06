package com.xuwt.boundservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by xuweitao on 2015/5/6.
 */
public class MessengerCommunicateActivity extends Activity {

    Messenger mService = null;
    boolean mIsBound;
    TextView mCallbackText;

    /**
     * Handler of incoming messages from service.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerCommunicateService.MSG_SET_VALUE:
                    mCallbackText.setText("Received from service: " + msg.arg1);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }


    final Messenger mMessenger = new Messenger(new IncomingHandler());


    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            mService = new Messenger(service);

            // We want to monitor the service for as long as we are
            // connected to it.
            try {
                Message msg = Message.obtain(null,
                        MessengerCommunicateService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);

                msg = Message.obtain(null,
                        MessengerCommunicateService.MSG_SET_VALUE, this.hashCode(), 0);
                mService.send(msg);
            } catch (RemoteException e) {

            }

        }

        public void onServiceDisconnected(ComponentName className) {

            mService = null;
            mCallbackText.setText("Disconnected.");
        }
    };

    void doBindService() {

        bindService(new Intent(MessengerCommunicateActivity.this,
                MessengerCommunicateService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
        mCallbackText.setText("Binding.");
    }

    void doUnbindService() {
        if (mIsBound) {

            if (mService != null) {
                try {
                    Message msg = Message.obtain(null,
                            MessengerCommunicateService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                }
            }

            unbindService(mConnection);
            mIsBound = false;
            mCallbackText.setText("Unbinding.");
        }
    }

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
}
