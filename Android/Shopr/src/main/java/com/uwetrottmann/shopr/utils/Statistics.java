
package com.uwetrottmann.shopr.utils;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.uwetrottmann.shopr.provider.ShoprContract.Stats;

/**
 * Stores data about the current task.
 */
public class Statistics {

    private static Statistics _instance;

    private long mStartTime;
    private int mCycleCount;
    private String mUserName;
    private boolean mIsDiversity;

    private boolean mIsStarted;

    private Statistics() {

    }

    public synchronized static Statistics get() {
        if (_instance == null) {
            _instance = new Statistics();
        }
        return _instance;
    }

    public synchronized void startTask(String username, boolean isDiversity) {
        mIsStarted = true;
        mUserName = username;
        mIsDiversity = isDiversity;
        mStartTime = System.currentTimeMillis();
        mCycleCount = 0;
    }

    public void incrementCycleCount() {
        mCycleCount++;
    }

    /**
     * Stops the task and writes all data to the database.
     * 
     * @return The {@link Uri} pointing to the new data set.
     */
    public synchronized Uri finishTask(Context context) {
        if (!mIsStarted) {
            return null;
        }

        mIsStarted = false;
        long duration = System.currentTimeMillis() - mStartTime;

        // Write to database
        ContentValues statValues = new ContentValues();
        statValues.put(Stats.USERNAME, mUserName);
        statValues.put(Stats.TASK_TYPE, mIsDiversity ? "div" : "sim");
        statValues.put(Stats.CYCLE_COUNT, mCycleCount);
        statValues.put(Stats.DURATION, duration);
        final Uri inserted = context.getContentResolver().insert(Stats.CONTENT_URI, statValues);

        return inserted;
    }

}