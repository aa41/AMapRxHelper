package com.xiaoma.amaprxhelper;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by X1 CARBON on 2017/8/5.
 */

public class InputTipsObservable extends Observable<List<Tip>> {
    private final Inputtips inputtips;

    private final boolean isAsync;

    public InputTipsObservable(Inputtips inputtips, boolean isAsync) {
        this.inputtips = inputtips;
        this.isAsync = isAsync;
    }

    @Override
    protected void subscribeActual(Observer<? super List<Tip>> observer) {
        CallBack callBack = new CallBack(observer);
        inputtips.setInputtipsListener(callBack);
        observer.onSubscribe(callBack);
        if (isAsync) {
            inputtips.requestInputtipsAsyn();
        } else {
            try {
                List<Tip> tips = inputtips.requestInputtips();
                if (tips != null) {
                    observer.onNext(tips);
                    observer.onComplete();
                }
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


    private static final class CallBack implements Inputtips.InputtipsListener, Disposable {
        private final Observer<? super List<Tip>> observer;
        private boolean isDisposed;

        public CallBack(Observer<? super List<Tip>> observer) {
            this.observer = observer;
        }

        @Override
        public void onGetInputtips(List<Tip> list, int i) {
            if (isDisposed) return;
            if (list != null) {
                try {
                    observer.onNext(list);
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
        public void dispose() {
            isDisposed = true;
        }

        @Override
        public boolean isDisposed() {
            return isDisposed;
        }
    }

}
