package com.xiaoma.amaprxhelper;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


/**
 * Created by X1 CARBON on 2017/8/4.
 */

public class AMapRxHelper {


    public static Observable<AMapLocation> createAMapLocation(Context context, final LocationSettingsListener listener) {
        AMapLocationClient client = new AMapLocationClient(context);
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
