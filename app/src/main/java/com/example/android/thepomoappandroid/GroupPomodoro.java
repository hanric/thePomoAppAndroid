package com.example.android.thepomoappandroid;

import android.os.CountDownTimer;
import android.util.Log;

import com.github.pavlospt.CircleView;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by hanric on 13/7/15.
 */
public class GroupPomodoro extends Pomodoro {

    private static final String CLASS_TAG = GroupPomodoro.class.getSimpleName();

    private class CalculateResults {
        int currentPhase;
        int currentPomodoro;
        long differenceInSeconds;
        int phaseNumber;
    }

    private CalculateResults calculateResults;

    public GroupPomodoro(CircleView circleView) {
        super(circleView);
    }

    /**
     * setSession -> getSessionState -> restartPhaseFromState -> betweenPhase -> finishSession
     */

    @Override
    public void setSession(String key, GregorianCalendar workTime, GregorianCalendar breakTime, GregorianCalendar largeBreakTime, int numberOfPomodoros, GregorianCalendar startDate) {
        super.setSession(key, workTime, breakTime, largeBreakTime, numberOfPomodoros, startDate);
        getSessionState();
    }

    private void getSessionState() {
        Log.v(CLASS_TAG, "getSessionState");
        GregorianCalendar now = new GregorianCalendar();

        calculateResults = new CalculateResults();
        calculateResults.currentPhase = 0;
        calculateResults.currentPomodoro = 0;
        calculateResults.phaseNumber = 1;
        // differenceInSeconds is the time that already passed
        calculateResults.differenceInSeconds = (now.getTimeInMillis() - startDate.getTimeInMillis()) / 1000;

        long sec = countPhase();
//        long sec = countPhase(differenceInSeconds, 1, currentPhase, currentPomodoro);
        this.currentPhase = calculateResults.currentPhase;
        this.currentPomodoro = calculateResults.currentPomodoro;

        restartPhaseFromState(sec);
    }

    private long countPhase() {
        long leftSeconds = 0;
        int auxPhase = calculateResults.currentPhase;

        Log.v(CLASS_TAG, "countPhase, " + "entering call: " + "currentPhase: " + calculateResults.currentPhase +
                " currentPomodoro: " + calculateResults.currentPomodoro +
                " phaseNumber: " + calculateResults.phaseNumber +
                " differenceInSeconds " + calculateResults.differenceInSeconds);


        if (calculateResults.phaseNumber % 2 != 0) { // WORK
            int workSeconds = workTime.get(Calendar.MINUTE) * 60;
            leftSeconds = calculateResults.differenceInSeconds - workSeconds;
            auxPhase = WORK;
            ++calculateResults.currentPomodoro;
        } else if (calculateResults.phaseNumber % 8 != 0) { // BREAK
            int breakSeconds = breakTime.get(Calendar.MINUTE) * 60;
            leftSeconds = calculateResults.differenceInSeconds - breakSeconds;
            auxPhase = BREAK;
        } else { // LARGE BREAK
            int largeBreakSeconds = largeBreakTime.get(Calendar.MINUTE) * 60;
            leftSeconds = calculateResults.differenceInSeconds - largeBreakSeconds;
            auxPhase = LARGE_BREAK;
        }
        calculateResults.currentPhase = auxPhase;
        if (leftSeconds <= 0) {
            Log.v(CLASS_TAG, "countPhase, " + "leftSeconds<=0: " + "currentPhase: " + calculateResults.currentPhase +
                    " currentPomodoro: " + calculateResults.currentPomodoro +
                    " phaseNumber: " + calculateResults.phaseNumber +
                    " differenceInSeconds " + calculateResults.differenceInSeconds);
            
            return -leftSeconds;
        }
        calculateResults.differenceInSeconds = leftSeconds;
        ++calculateResults.phaseNumber;
        countPhase();
        return 0;
    }

//    /**
//     * @param differenceInSeconds
//     * @param phaseNumber
//     * @param currentPhase        the calculated currentPhase, nothing until the end
//     * @param currentPomodoro the calculated currentPomodoro, nothing until the end
//     * @return
//     */
//    private long countPhase(long differenceInSeconds, int phaseNumber, int currentPhase, int currentPomodoro) {
//        long leftSeconds = 0;
//        int auxPhase = currentPhase;
//
//        if (phaseNumber % 2 != 0) { // WORK
//            int workSeconds = workTime.get(Calendar.MINUTE) * 60;
//            leftSeconds = differenceInSeconds - workSeconds;
//            auxPhase = WORK;
//            ++currentPomodoro;
//        } else if (phaseNumber % 8 != 0) { // BREAK
//            int breakSeconds = breakTime.get(Calendar.MINUTE) * 60;
//            leftSeconds = differenceInSeconds - breakSeconds;
//            auxPhase = BREAK;
//        } else { // LARGE BREAK
//            int largeBreakSeconds = largeBreakTime.get(Calendar.MINUTE) * 60;
//            leftSeconds = differenceInSeconds - largeBreakSeconds;
//            auxPhase = LARGE_BREAK;
//        }
//        if (leftSeconds <= 0) {
//            return differenceInSeconds;
//        }
//        countPhase(leftSeconds, ++phaseNumber, auxPhase, currentPomodoro);
//        return 0;
//    }

    private void restartPhaseFromState(long sec) {
        Log.v(CLASS_TAG, "startPhase");
        switch (currentPhase) {
            case WORK:
                circleView.setStrokeColor(circleView.getContext().getResources().getColor(R.color.red));
                break;
            case BREAK:
                circleView.setStrokeColor(circleView.getContext().getResources().getColor(R.color.green));
                break;
            case LARGE_BREAK:
                circleView.setStrokeColor(circleView.getContext().getResources().getColor(R.color.darkgreen));
                break;
            default:
                break;
        }
        final int miliTime = (int) sec * 1000;
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
}
