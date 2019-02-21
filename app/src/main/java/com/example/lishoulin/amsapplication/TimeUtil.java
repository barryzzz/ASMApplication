package com.example.lishoulin.amsapplication;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * @author barry
 * @version 1.0
 * @date 2019/2/20
 */
public class TimeUtil {

    private Map<Integer, Record> mRecordMap = new HashMap<>();


    private TimeUtil() {

    }

    private static class Builder {
        private static TimeUtil sTimeUtil = new TimeUtil();
    }

    public static TimeUtil getInstance() {
        return Builder.sTimeUtil;
    }

    public void recordEnter(String name) {
        Record record = mRecordMap.get(name.hashCode());
        if (record == null) {
            record = new Record();
            record.setStartTime(System.currentTimeMillis());
            mRecordMap.put(name.hashCode(), record);
        } else {
            record.setStartTime(System.currentTimeMillis());
            record.setEndTime(0);
        }
    }

    public void recordExit(String name) {
        Record record = mRecordMap.get(name.hashCode());
        if (record != null) {
            record.setEndTime(System.currentTimeMillis());
            mRecordMap.put(name.hashCode(), record);
            Log.e("record-->", "name:" + name + " time:" + (record.getEndTime() - record.getStartTime()));
        }

    }


    private class Record {
        private long startTime;
        private long endTime;


        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }
    }
}
