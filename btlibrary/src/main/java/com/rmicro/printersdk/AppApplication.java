package com.rmicro.printersdk;

import android.app.Application;


public class AppApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        BluetoothSdkManager.INSTANCE.init(this);
    }

}
