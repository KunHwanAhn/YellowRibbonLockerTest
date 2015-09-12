package com.ak.yellow.ribbon.locker;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.ak.yellow.ribbon.locker.MainActivityTest \
 * com.ak.yellow.ribbon.locker.tests/android.test.InstrumentationTestRunner
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final int ERROR_RANGE = 20;

    private Instrumentation mInstrumentation = null;
    private Activity mActivity = null;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(true);
        mInstrumentation = getInstrumentation();
        mActivity = getActivity();

        assertNotNull("Activity is NULL", mActivity);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        mInstrumentation = null;
        mActivity = null;
    }

    @SmallTest
    public void testInitialScreen() {
        ImageView unlockCircle = (ImageView) mActivity
                .findViewById(R.id.screen_locker_circle_view);
        assertEquals(View.INVISIBLE, unlockCircle.getVisibility());
    }

    @MediumTest
    public void testClickedScreen() {
        ImageView unlockCircle = (ImageView) mActivity
                .findViewById(R.id.screen_locker_circle_view);
        assertEquals(View.INVISIBLE, unlockCircle.getVisibility());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        int centerX = displayMetrics.widthPixels / 2;
        int centerY = displayMetrics.heightPixels / 2;

        long downTime = SystemClock.uptimeMillis();
        MotionEvent event = MotionEvent.obtain(downTime, SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN, centerX, centerY, 0);
        mInstrumentation.sendPointerSync(event);
        assertEquals(View.VISIBLE, unlockCircle.getVisibility());
        event.recycle();

        event = MotionEvent.obtain(downTime, SystemClock.uptimeMillis(),
                MotionEvent.ACTION_UP, centerX, centerY, 0);
        mInstrumentation.sendPointerSync(event);
        assertEquals(View.INVISIBLE, unlockCircle.getVisibility());
        event.recycle();
    }

    @MediumTest
    public void testDragScreen_unlockState() {
        ImageView unlockCircle = (ImageView) mActivity
                .findViewById(R.id.screen_locker_circle_view);
        Integer resId = unlockCircle.getTag() == null ? 0
                : (Integer) unlockCircle.getTag();
        assertEquals(View.INVISIBLE, unlockCircle.getVisibility());
        assertEquals(R.drawable.screen_locker_circle_lock, (int) resId);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        int centerX = displayMetrics.widthPixels / 2;
        int centerY = displayMetrics.heightPixels / 2;
        int dragSize = unlockCircle.getMeasuredWidth() / 2 + ERROR_RANGE;

        TouchUtils.drag(this, centerX, centerX + dragSize, centerY, centerY, 10);
        resId = unlockCircle.getTag() == null ? 0 : (Integer) unlockCircle.getTag();
        assertEquals(View.INVISIBLE, unlockCircle.getVisibility());
        assertEquals(R.drawable.screen_locker_circle_unlock, (int) resId);
    }

    @MediumTest
    public void testDragScreen_lockState() {
        ImageView unlockCircle = (ImageView) mActivity
                .findViewById(R.id.screen_locker_circle_view);
        Integer resId = unlockCircle.getTag() == null ? 0
                : (Integer) unlockCircle.getTag();
        assertEquals(View.INVISIBLE, unlockCircle.getVisibility());
        assertEquals(R.drawable.screen_locker_circle_lock, (int) resId);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        int centerX = displayMetrics.widthPixels / 2;
        int centerY = displayMetrics.heightPixels / 2;
        int dragSize = (unlockCircle.getMeasuredWidth() / 2) - ERROR_RANGE;

        TouchUtils.drag(this, centerX, centerX + dragSize, centerY, centerY, 10);
        resId = unlockCircle.getTag() == null ? 0 : (Integer) unlockCircle.getTag();
        assertEquals(View.INVISIBLE, unlockCircle.getVisibility());
        assertEquals(R.drawable.screen_locker_circle_lock, (int) resId);
    }

    @MediumTest
    public void testDragScreen_goToUnlockPositionAndBackToLockPosition() {
        ImageView unlockCircle = (ImageView) mActivity
                .findViewById(R.id.screen_locker_circle_view);
        Integer resId = unlockCircle.getTag() == null ? 0
                : (Integer) unlockCircle.getTag();
        assertEquals(View.INVISIBLE, unlockCircle.getVisibility());
        assertEquals(R.drawable.screen_locker_circle_lock, (int) resId);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        int centerX = displayMetrics.widthPixels / 2;
        int centerY = displayMetrics.heightPixels / 2;
        int dragSize = (unlockCircle.getMeasuredWidth() / 2) + ERROR_RANGE;

        long downTime = SystemClock.uptimeMillis();
        sendMotionEvent(unlockCircle, downTime, SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN, centerX, centerY);
        resId = unlockCircle.getTag() == null ? 0 : (Integer) unlockCircle.getTag();
        assertEquals(View.VISIBLE, unlockCircle.getVisibility());
        assertEquals(R.drawable.screen_locker_circle_lock, (int) resId);

        sendMotionEvent(unlockCircle, downTime, SystemClock.uptimeMillis(),
                MotionEvent.ACTION_MOVE, centerX + dragSize, centerY);
        resId = unlockCircle.getTag() == null ? 0 : (Integer) unlockCircle.getTag();
        assertEquals(View.VISIBLE, unlockCircle.getVisibility());
        assertEquals(R.drawable.screen_locker_circle_unlock, (int) resId);

        sendMotionEvent(unlockCircle, downTime, SystemClock.uptimeMillis(),
                MotionEvent.ACTION_MOVE, centerX, centerY);
        resId = unlockCircle.getTag() == null ? 0 : (Integer) unlockCircle.getTag();
        assertEquals(View.VISIBLE, unlockCircle.getVisibility());
        assertEquals(R.drawable.screen_locker_circle_lock, (int) resId);

        sendMotionEvent(unlockCircle, downTime, SystemClock.uptimeMillis(),
                MotionEvent.ACTION_UP, centerX, centerY);
        resId = unlockCircle.getTag() == null ? 0 : (Integer) unlockCircle.getTag();
        assertEquals(View.INVISIBLE, unlockCircle.getVisibility());
        assertEquals(R.drawable.screen_locker_circle_lock, (int) resId);
    }

    private void sendMotionEvent(View v, long downTime, long eventTime, int action, int x, int y) {
        MotionEvent event = MotionEvent.obtain(downTime, eventTime, action, x,
                y, 0);
        mInstrumentation.sendPointerSync(event);
        event.recycle();
    }
}
