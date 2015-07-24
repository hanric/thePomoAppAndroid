package com.example.android.thepomoappandroid;

import android.os.CountDownTimer;
import android.util.Log;

import com.github.pavlospt.CircleView;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Enric on 15/04/2015.
 */
public class Pomodoro {

    private static final String CLASS_TAG = Pomodoro.class.getSimpleName();

    public static final int TO_START = 0;
    public static final int WORK = 1;
    public static final int BREAK = 2;
    public static final int LARGE_BREAK = 3;
    public static final int ENDED = 4;

    protected CountDownTimer countDownTimer;
    protected CircleView circleView;
    protected String key;

    protected GregorianCalendar workTime;
    protected GregorianCalendar breakTime;
    protected GregorianCalendar largeBreakTime;
    protected GregorianCalendar startDate;
    protected int numberOfPomodoros;

    protected int currentPomodoro;
    protected int currentPhase;
    protected boolean isRunning;

    private OnPomodoroFinished onPomodoroFinished;

    public interface OnPomodoroFinished {
        /**
         * Listener for when a working phase has ended
         * @param nextPhase The following phase of the session
         */
        void phaseEnded(String key, int nextPhase);

        /**
         * Listener for when the whole session has ended
         */
        void sessionEnded(String key);
    }

    public Pomodoro(CircleView circleView) {
        this.circleView = circleView;
        this.isRunning = false;
    }

    /**
     * @param workTime          Duration of a working phase.
     * @param breakTime         Duration of a break phase.
     * @param largeBreakTime    Duration of a large break phase.
     * @param numberOfPomodoros Number of working phases.
     * @param startDate         Date when the session was scheduled to start. By now, when offline, it will be null and a start button will trigger the countdown.
     */
    public void setSession(String key, GregorianCalendar workTime, GregorianCalendar breakTime, GregorianCalendar largeBreakTime, int numberOfPomodoros, GregorianCalendar startDate) {
        this.key = key;
        this.workTime = workTime;
        this.breakTime = breakTime;
        this.largeBreakTime = largeBreakTime;
        this.numberOfPomodoros = numberOfPomodoros;
        if (startDate != null) this.startDate = startDate;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public void setOnPomodoroFinished(OnPomodoroFinished onPomodoroFinished) {
        this.onPomodoroFinished = onPomodoroFinished;
    }

    /**
     * setSession
     * startSession -> startPhase -> betweenPhase -> finishSession
     */

    public void startSession() {
        Log.v(CLASS_TAG, "startSession");
        // TODO handle startDate
        isRunning = true;
        currentPomodoro = 1;
        currentPhase = Pomodoro.WORK;
        startPhase();
    }

    protected void startPhase() {
        Log.v(CLASS_TAG, "startPhase");
        int time;
        switch (currentPhase) {
            case WORK:
                time = workTime.get(Calendar.MINUTE);
                circleView.setStrokeColor(circleView.getContext().getResources().getColor(R.color.red));
                break;
            case BREAK:
                time = breakTime.get(Calendar.MINUTE);
                circleView.setStrokeColor(circleView.getContext().getResources().getColor(R.color.green));
                break;
            case LARGE_BREAK:
                time = largeBreakTime.get(Calendar.MINUTE);
                circleView.setStrokeColor(circleView.getContext().getResources().getColor(R.color.darkgreen));
                break;
            default:
                time = -1;
                break;
        }
        final int miliTime = time * 60 * 1000;
        countDownTimer = new CountDownTimer(miliTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                circleView.setTitleText(formatTime(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Log.v("onFinish", "Finished");
                betweenPhase();
            }
        }.start();
    }

    protected void betweenPhase() {
        Log.v(CLASS_TAG, "betweenPhase");
        if (currentPomodoro == numberOfPomodoros && currentPhase == WORK) {
            finishSession();
        } else {
            if (currentPhase == BREAK || currentPhase == LARGE_BREAK) {
                currentPhase = WORK;
            } else if (currentPhase == WORK) {
                if (currentPomodoro == 4) {
                    currentPhase = LARGE_BREAK;
                } else {
                    currentPhase = BREAK;
                }
                ++currentPomodoro;
            }
            onPomodoroFinished.phaseEnded(key, currentPhase);
            startPhase();
        }
    }

    protected void finishSession() {
        Log.v(CLASS_TAG, "finishSession");
        isRunning = false;
        circleView.setStrokeColor(circleView.getContext().getResources().getColor(R.color.colorPrimary));
        circleView.setTitleText(circleView.getContext().getResources().getString(R.string.start));
        onPomodoroFinished.sessionEnded(key);
    }

    protected String formatTime(long millisUntilFinished) {
        long minutes = millisUntilFinished / (60 * 1000);
        long seconds = (millisUntilFinished - (minutes * 60 * 1000))/1000;
        String formatted = Long.toString(minutes) + ":";
        if (seconds < 10) {
            formatted += "0";
        }
        formatted += Long.toString(seconds);
        return formatted;
    }
}
