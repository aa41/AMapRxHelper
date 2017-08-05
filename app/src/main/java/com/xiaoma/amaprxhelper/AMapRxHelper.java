package com.xiaoma.amaprxhelper;

import android.app.Application;
import android.content.Context;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiSearch;
import com.xiaoma.amaprxhelper.entry.POIEntry;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


/**
 * Created by X1 CARBON on 2017/8/4.
 */

public class AMapRxHelper {
    private static Application app;

    public static void init(Application application) {
        app = application;
    }


    public static Observable<AMapLocation> createAMapLocation(final LocationSettingsListener listener) {
        AMapLocationClient client = new AMapLocationClient(app);
        return Observable.just(client)
                .flatMap(new Function<AMapLocationClient, ObservableSource<AMapLocation>>() {
                    @Override
                    public ObservableSource<AMapLocation> apply(@NonNull AMapLocationClient client) throws Exception {
                        if (listener != null) {
                            listener.locationOptions(client);
                        }
                        return new LocationObservable(client);
                    }
                });

    }


    public static Observable<POIEntry> poiSearch(PoiSearch.Query query, String id, boolean isAsync) {
        return new POISearchObservable(id, new PoiSearch(app, query), isAsync);
    }

    public static Observable<POIEntry> poiSearch(PoiSearch.Query query, String id) {
        return poiSearch(query, id, true);
    }

    public static Observable<POIEntry> poiSearch(PoiSearch.Query query) {
        return poiSearch(query, null, true);
    }


    public static Observable<List<Tip>> inputTipsSearch(InputtipsQuery query, boolean isAsync) {
        Inputtips inputtips = new Inputtips(app, query);
        return new InputTipsObservable(inputtips, isAsync);
    }

    public static Observable<List<Tip>> inputTipsSearch(InputtipsQuery query) {
        return inputTipsSearch(query, true);
    }

    public static Observable<List<Tip>> inputTipsWithEditText(TextView textView, String args, boolean isTextOrCity) {
        return new InputTipsWithEditTextObservable(args, app, isTextOrCity, textView);
    }

    public static Observable<List<Tip>> inputTipsWithEditText(TextView textView, String args) {
        return new InputTipsWithEditTextObservable(args, app, true, textView);
    }




    public interface LocationSettingsListener {
        void locationOptions(AMapLocationClient client);
    }
    




}
