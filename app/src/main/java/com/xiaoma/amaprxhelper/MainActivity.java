package com.xiaoma.amaprxhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AMapRxHelper.createAMapLocation(getApplicationContext(), new AMapRxHelper.LocationSettingsListener() {
            @Override
            public void locationOptions(AMapLocationClient client) {

            }
        })
                .subscribe(new Consumer<AMapLocation>() {
                    @Override
                    public void accept(AMapLocation aMapLocation) throws Exception {
                        Toast.makeText(MainActivity.this,aMapLocation.toStr(),Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
