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

    public static final int WORK = 0;
    public static final int BREAK = 1;
    public static final int LARGE_BREAK = 2;

    private CountDownTimer countDownTimer;
    private CircleView circleView;

    private GregorianCalendar workTime;
    private GregorianCalendar breakTime;
    private GregorianCalendar largeBreakTime;
    private GregorianCalendar startDate;
    private int numberOfPomodoros;

    private int currentPomodoro;
    private int currentPhase;
    private boolean isRunning;

    private OnPomodoroFinished onPomodoroFinished;

    public interface OnPomodoroFinished {
        /**
         * Listener for when a working phase has ended
         */
        public void phaseEnded(int nextPhase);

        /**
         * Listener for when the whole session has ended
         */
        public void sessionEnded();
    }

    public Pomodoro(CircleView circleView) {
        this.circleView = circleView;
    }

    /**
     * @param workTime          Duration of a working phase.
     * @param breakTime         Duration of a break phase.
     * @param largeBreakTime    Duration of a large break phase.
     * @param numberOfPomodoros Number of working phases.
     * @param startDate         Date when the session was scheduled to start. By now, when offline, it will be null and a start button will trigger the countdown.
     */
    public void setSession(GregorianCalendar workTime, GregorianCalendar breakTime, GregorianCalendar largeBreakTime, int numberOfPomodoros, GregorianCalendar startDate) {
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

    public void startSession() {
        // TODO handle startDate
        currentPomodoro = 1;
        currentPhase = Pomodoro.WORK;
        startPhase();
    }

    private void betweenPhase() {
        if (currentPomodoro == numberOfPomodoros) {
//            onPomodoroFinished.sessionEnded();
        } else {
            if (currentPhase == Pomodoro.BREAK || currentPhase == Pomodoro.LARGE_BREAK) {
                currentPhase = Pomodoro.WORK;
            } else if (currentPhase == Pomodoro.WORK) {
                if (currentPomodoro == 4) {
                    currentPhase = Pomodoro.LARGE_BREAK;
                } else {
                    currentPhase = Pomodoro.BREAK;
                }
                ++currentPomodoro;
            }
//            onPomodoroFinished.phaseEnded(currentPhase);
        }
    }

    public void startPhase() {
        int time;
        switch (currentPhase) {
            case Pomodoro.WORK:
                time = workTime.get(Calendar.MINUTE);
                break;
            case Pomodoro.BREAK:
                time = breakTime.get(Calendar.MINUTE);
                break;
            case Pomodoro.LARGE_BREAK:
                time = largeBreakTime.get(Calendar.MINUTE);
                break;
            default:
                time = -1;
                break;
        }
        final int miliTime = time * 60 * 1000;
        countDownTimer = new CountDownTimer(miliTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("onTick", Long.toString(millisUntilFinished));
                circleView.setTitleText(formatTime(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Log.v("onFinish", "Finished");
                betweenPhase();
            }
        }.start();
    }

    private String formatTime(long millisUntilFinished) {
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
