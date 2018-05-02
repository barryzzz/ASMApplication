package com.example.lishoulin.amsapplication;

import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class DebouncedClick {

    private static final String TAG = "DebouncedClick";

    private static long WAIT_TIME = 300L;


    private static final Map<View, DebouncedView> viewWaitTimeMap = new HashMap<>();


    public static boolean isCanClick(View tagview) {
        DebouncedView debouncedView = viewWaitTimeMap.get(tagview);
        if (debouncedView == null) {
            long starttime = System.currentTimeMillis();
            debouncedView = new DebouncedView(tagview);
            debouncedView.setTime(starttime);
            viewWaitTimeMap.put(tagview, debouncedView);
            Log.e(TAG,"点击通行1");
            return true;
        }
        long currenttime = System.currentTimeMillis();
        if (currenttime - debouncedView.getTime() >= WAIT_TIME) {
            debouncedView.setTime(currenttime);
            Log.e(TAG,"点击通行2");
            return true;
        }
        Log.e(TAG,"点击拦截");
        return false;
    }


    private static class DebouncedView extends WeakReference<View> {

        private long starttime;

        public DebouncedView(View referent) {
            super(referent);
        }

        public long getTime() {
            return starttime;
        }

        public void setTime(long starttime) {
            this.starttime = starttime;
        }
    }
}
