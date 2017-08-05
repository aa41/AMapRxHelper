package com.xiaoma.amaprxhelper;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
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

public class InputTipsWithEditTextObservable extends Observable<List<Tip>> {
    private final String args;
    private final Context context;
    private final boolean isTextOrCity;
    private final TextView textView;

    public InputTipsWithEditTextObservable(String args, Context context, boolean isTextOrCity, TextView textView) {
        this.args = args;
        this.context = context;
        this.isTextOrCity = isTextOrCity;
        this.textView = textView;
    }

    @Override
    protected void subscribeActual(Observer<? super List<Tip>> observer) {
        CallBack callBack=new CallBack(context,args,observer,isTextOrCity);
        textView.addTextChangedListener(callBack);
        observer.onSubscribe(callBack);

    }

    private static final class CallBack implements TextWatcher, Inputtips.InputtipsListener, Disposable {
        private final String args;
        private final Observer<? super List<Tip>> observer;
        private final Context context;
        private final boolean isTextOrCity;
        private Inputtips inputtips;
        private boolean isDisposed;

        public CallBack(Context context, String args, Observer<? super List<Tip>> observer, boolean isTextOrCity) {
            this.context = context;
            this.args = args;
            this.observer = observer;
            this.isTextOrCity = isTextOrCity;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s == null || s.length() == 0) return;
            InputtipsQuery query;
            if (isTextOrCity) {
                query = new InputtipsQuery(s.toString(), args);
            } else {
                query = new InputtipsQuery(args, s.toString());
            }

            inputtips = new Inputtips(context, query);
            inputtips.setInputtipsListener(this);
            inputtips.requestInputtipsAsyn();
        }

        @Override
        public void onGetInputtips(List<Tip> list, int i) {
            if (isDisposed) return;
            if (list != null) {
                try {
                    observer.onNext(list);
                    if(isDisposed){
                        observer.onComplete();
                    }

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
