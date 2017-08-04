package com.xiaoma.amaprxhelper;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by X1 CARBON on 2017/8/4.
 */

public class LocationObservable extends Observable<AMapLocation> {
    private final  AMapLocationClient aMapLocationClient;


    public LocationObservable(AMapLocationClient aMapLocationClient) {
        this.aMapLocationClient = aMapLocationClient;
    }

    @Override
    protected void subscribeActual(Observer<? super AMapLocation> observer) {
        AMapCallBack callBack=new AMapCallBack(aMapLocationClient,observer);
        aMapLocationClient.setLocationListener(callBack);
        observer.onSubscribe(callBack);
        aMapLocationClient.startLocation();

    }


    private static final class AMapCallBack implements AMapLocationListener, Disposable {
        private boolean isDisposed = false;
        private final AMapLocationClient aMapLocationClient;
        private final Observer<? super AMapLocation> observer;

        public AMapCallBack(AMapLocationClient aMapLocationClient, Observer<? super AMapLocation> observer) {
            this.aMapLocationClient = aMapLocationClient;
            this.observer = observer;
        }

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {

            try {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        observer.onNext(aMapLocation);
                        if(isDisposed){
                            observer.onComplete();
                        }

                    }else {
                        try {
                            observer.onError(new AMapLocationException(aMapLocation.getErrorCode(),aMapLocation.getErrorInfo()));
                        } catch (Throwable inner) {
                            Exceptions.throwIfFatal(inner);
                            RxJavaPlugins.onError(new CompositeException(new AMapLocationException(aMapLocation.getErrorCode(),aMapLocation.getErrorInfo()), inner));
                        }

                    }
                }
            }catch (Throwable t){
                if(isDisposed){
                    RxJavaPlugins.onError(t);
                }else {
                    try {
                        observer.onError(t);
                    } catch (Throwable inner) {
                        Exceptions.throwIfFatal(inner);
                        RxJavaPlugins.onError(new CompositeException(t, inner));
                    }

                }
            }

        }

        @Override
        public void dispose() {
            aMapLocationClient.onDestroy();
            isDisposed = true;
        }

        @Override
        public boolean isDisposed() {
            return isDisposed;
        }
    }
}
