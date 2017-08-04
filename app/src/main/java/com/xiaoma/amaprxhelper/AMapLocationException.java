package com.xiaoma.amaprxhelper;

/**
 * Created by X1 CARBON on 2017/8/4.
 */

public class AMapLocationException extends RuntimeException {

    private int code;

    private String msg;


    public AMapLocationException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

}
