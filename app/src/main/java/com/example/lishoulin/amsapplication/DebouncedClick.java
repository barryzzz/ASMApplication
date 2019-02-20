package com.example.lishoulin.amsapplication;

import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class DebouncedClick {

    private static final String TAG = "DebouncedClick";

    private static final long WAIT_TIME = 300L;


    private Map<Integer, DebouncedView> viewWaitTimeMap = new HashMap<>();


    private DebouncedClick() {

    }

    private static class Builder {
        private static DebouncedClick sDebouncedClick = new DebouncedClick();
    }

    public static DebouncedClick getInstance() {
        return Builder.sDebouncedClick;
    }


    public boolean isCanClick(View tagview) {
        DebouncedView debouncedView = viewWaitTimeMap.get(tagview.getId());
        if (debouncedView == null) {
            long starttime = System.currentTimeMillis();
            debouncedView = new DebouncedView();
            debouncedView.setTime(starttime);
            viewWaitTimeMap.put(tagview.getId(), debouncedView);
            Log.e(TAG, "点击通行1");
            return true;
        }
        long currenttime = System.currentTimeMillis();
        if (currenttime - debouncedView.getTime() >= WAIT_TIME) {
            debouncedView.setTime(currenttime);
            Log.e(TAG, "点击通行2");
            return true;
        }
        Log.e(TAG, "点击拦截");
        return false;
    }


    private class DebouncedView {

        private long starttime;


        public long getTime() {
            return starttime;
        }

        public void setTime(long starttime) {
            this.starttime = starttime;
        }
    }
}
