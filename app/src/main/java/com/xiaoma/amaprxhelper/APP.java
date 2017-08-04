package com.xiaoma.amaprxhelper;

import android.app.Application;

/**
 * Created by X1 CARBON on 2017/8/4.
 */

public class APP extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AMapRxHelper.init(this);
    }
}
