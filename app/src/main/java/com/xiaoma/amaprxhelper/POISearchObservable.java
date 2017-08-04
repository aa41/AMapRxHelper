package com.xiaoma.amaprxhelper;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.xiaoma.amaprxhelper.entry.POIEntry;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by X1 CARBON on 2017/8/4.
 */

public class POISearchObservable extends Observable<POIEntry> {
    private final String ID;
    private final PoiSearch poiSearch;
    private boolean isAsyn;

    public POISearchObservable(String ID, PoiSearch poiSearch, boolean isAsyn) {
        this.ID = ID;
        this.poiSearch = poiSearch;
        this.isAsyn = isAsyn;
    }

    @Override
    protected void subscribeActual(Observer<? super POIEntry> observer) {
        CallBack callBack = new CallBack(ID, observer);
        poiSearch.setOnPoiSearchListener(callBack);
        if (isNullId()) {
            if (isAsyn) {
                poiSearch.searchPOIAsyn();
            } else {
                try {
                    PoiResult poiResult = poiSearch.searchPOI();
                    observer.onNext(new POIEntry(null, -1, poiResult));
                    observer.onComplete();
                } catch (AMapException e) {
                    try {
                        observer.onError(e);
                    } catch (Throwable inner) {
                        Exceptions.throwIfFatal(inner);
                        RxJavaPlugins.onError(new CompositeException(e, inner));
                    }
                }
            }
        } else {
            if (isAsyn) {
                poiSearch.searchPOIIdAsyn(ID);
            } else {
                try {
                    PoiItem poiItem = poiSearch.searchPOIId(ID);
                    observer.onNext(new POIEntry(poiItem, -1, null));
                    observer.onComplete();
                } catch (AMapException e) {
                    try {
                        observer.onError(e);
                    } catch (Throwable inner) {
                        Exceptions.throwIfFatal(inner);
                        RxJavaPlugins.onError(new CompositeException(e, inner));
                    }
                }

            }
        }
    }

    private boolean isNullId() {
        return ID == null;
    }


    private static final class CallBack implements PoiSearch.OnPoiSearchListener {
        private final String ID;
        private final Observer<? super POIEntry> observer;
        private boolean isDisposed = false;

        public CallBack(String ID, Observer<? super POIEntry> observer) {
            this.ID = ID;
            this.observer = observer;
        }

        private boolean isNullId() {
            return ID == null;
        }

        @Override
        public void onPoiSearched(PoiResult poiResult, int i) {
            if (isNullId()) {
                try {
                    observer.onNext(new POIEntry(null, i, poiResult));
                    observer.onComplete();
                } catch (Throwable t) {
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
        public void onPoiItemSearched(PoiItem poiItem, int i) {
            if (!isNullId()) {
                try {
                    observer.onNext(new POIEntry(poiItem, i, null));
                    observer.onComplete();
                } catch (Throwable t) {
                    try {
                        observer.onError(t);
                    } catch (Throwable inner) {
                        Exceptions.throwIfFatal(inner);
                        RxJavaPlugins.onError(new CompositeException(t, inner));
                    }
                }
            }
        }


    }
}
