package com.xiaoma.amaprxhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiSearch;
import com.xiaoma.amaprxhelper.entry.POIEntry;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity {
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et= (EditText) findViewById(R.id.et);
        AMapRxHelper.createAMapLocation(new AMapRxHelper.LocationSettingsListener() {
            @Override
            public void locationOptions(AMapLocationClient client) {
                AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                mLocationOption.setOnceLocation(true);
                mLocationOption.setOnceLocationLatest(true);
                client.setLocationOption(mLocationOption);
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

        AMapRxHelper.inputTipsWithEditText(et,"上海",true)
                .subscribe(new Consumer<List<Tip>>() {
                    @Override
                    public void accept(List<Tip> tips) throws Exception {
                        for (Tip tip:tips){
                           Log.e("tips",tip.getName()+tip.getAdcode());
                            Toast.makeText(MainActivity.this,tip.getAddress(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


      /*  PoiSearch.Query query = new PoiSearch.Query("郑州", "", "");
        query.setPageNum(1);
        query.setPageSize(20);
        AMapRxHelper.poiSearch(query)
                .subscribe(new Consumer<POIEntry>() {
                    @Override
                    public void accept(POIEntry poiEntry) throws Exception {
                        Toast.makeText(MainActivity.this,poiEntry.getPoiResult().getPois().size()+"",Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


        AMapRxHelper.inputTipsSearch(new InputtipsQuery("中环V领地",""))
                .subscribe(new Consumer<List<Tip>>() {
                    @Override
                    public void accept(List<Tip> tips) throws Exception {
                        for (Tip tip:tips){
                            Log.e("tip:",tip.getName());
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });*/
    }
}
