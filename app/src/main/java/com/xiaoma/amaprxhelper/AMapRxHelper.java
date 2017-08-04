package com.xiaoma.amaprxhelper;

import android.app.Application;
import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.services.poisearch.PoiSearch;
import com.xiaoma.amaprxhelper.entry.POIEntry;

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
       app=application;
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


    public static void checkClient(AMapLocationClient client) {
        if (client == null) {
            throw new NullPointerException("AMapLocationClient==null???");
        }
    }


    public interface LocationSettingsListener {
        void locationOptions(AMapLocationClient client);
    }

    private LocationSettingsListener listener;

    public void setLocationSettingsListener(LocationSettingsListener listener) {
        this.listener = listener;
    }


}
