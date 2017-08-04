package com.xiaoma.amaprxhelper.entry;


import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;

/**
 * Created by X1 CARBON on 2017/8/4.
 */

public class POIEntry {
    private PoiItem poiItem;
    private  int rCode;
    private PoiResult poiResult;

    public POIEntry(PoiItem poiItem, int rCode, PoiResult poiResult) {
        this.poiItem = poiItem;
        this.rCode = rCode;
        this.poiResult = poiResult;
    }

    public PoiResult getPoiResult() {
        return poiResult;
    }

    public void setPoiResult(PoiResult poiResult) {
        this.poiResult = poiResult;
    }

    public PoiItem getPoiItem() {
        return poiItem;
    }

    public void setPoiItem(PoiItem poiItem) {
        this.poiItem = poiItem;
    }

    public int getrCode() {
        return rCode;
    }

    public void setrCode(int rCode) {
        this.rCode = rCode;
    }


}
