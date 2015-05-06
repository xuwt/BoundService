package com.xuwt.boundservice;

import java.util.Random;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by xuweitao on 2015/5/6.
 */
public class LocalService extends Service {

    // binder given to clients
    private final IBinder mBinder =new LocalBinder();

    private final Random mGenerator = new Random();
    public class LocalBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * method for clients
     * @return
     */
    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }
}
