package com.xiaoma.amaprxhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.services.poisearch.PoiSearch;
import com.xiaoma.amaprxhelper.entry.POIEntry;

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
        AMapRxHelper.createAMapLocation(new AMapRxHelper.LocationSettingsListener() {
            @Override
            public void locationOptions(AMapLocationClient client) {

            }
        }).subscribe(new Consumer<AMapLocation>() {
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

        PoiSearch.Query query = new PoiSearch.Query("郑州", "", "");
        query.setPageNum(1);
        query.setPageSize(20);
        AMapRxHelper.poiSearch(query)
                .subscribe(new Consumer<POIEntry>() {
                    @Override
                    public void accept(POIEntry poiEntry) throws Exception {
                        Toast.makeText(MainActivity.this,poiEntry.toString(),Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
