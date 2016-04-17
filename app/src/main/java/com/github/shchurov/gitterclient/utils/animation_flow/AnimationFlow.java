package com.github.shchurov.gitterclient.utils.animation_flow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Handler;
import android.os.Looper;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Written on Java to be used in other non-Kotlin projects
 */
public class AnimationFlow {

    private Handler handler = new Handler(Looper.getMainLooper());
    private Deque<StepInfo> steps = new LinkedList<>();
    private Deque<Animation> animators = new LinkedList<>();
    private Deque<Runnable> setups = new LinkedList<>();
    private int nextStepDelay;
    private Animator playingAnimator;

    public static AnimationFlow create() {
        return new AnimationFlow();
    }

    private AnimationFlow() {
    }

    public AnimationFlow setup(Runnable runnable) {
        steps.add(new StepInfo(false, nextStepDelay));
        nextStepDelay = 0;
        setups.add(runnable);
        return this;
    }

    public AnimationFlow wait(int milliseconds) {
        nextStepDelay = milliseconds;
        return this;
    }

    public AnimationFlow play(Animation factory) {
        steps.add(new StepInfo(true, nextStepDelay));
        nextStepDelay = 0;
        animators.add(factory);
        return this;
    }

    public AnimationFlow start() {
        performStepRecursively();
        return this;
    }

    private void performStepRecursively() {
        if (steps.isEmpty()) {
            return;
        }
        StepInfo info = steps.pop();
        if (info.animation) {
            performPlay(animators.pop(), info.delay);
        } else {
            performSetup(setups.pop(), info.delay);
        }
    }

    private void performSetup(Runnable runnable, int delay) {
        handler.postDelayed(runnable, delay);
        handler.postDelayed(performNextStepRunnable, delay);
    }

    private Runnable performNextStepRunnable = new Runnable() {
        @Override
        public void run() {
            performStepRecursively();
        }
    };

    private void performPlay(Animation factory, int delay) {
        playingAnimator = factory.createAnimator();
        playingAnimator.setStartDelay(delay);
        playingAnimator.addListener(performNextStepListener);
        playingAnimator.start();
    }

    private Animator.AnimatorListener performNextStepListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            playingAnimator = null;
            performStepRecursively();
        }
    };

    public void cancel() {
        if (playingAnimator != null) {
            playingAnimator.removeAllListeners();
            playingAnimator.cancel();
            playingAnimator = null;
        }
        handler.removeCallbacksAndMessages(null);
    }

    private class StepInfo {
        boolean animation;
        int delay;

        public StepInfo(boolean animation, int delay) {
            this.animation = animation;
            this.delay = delay;
        }
    }

    public interface Animation {

        Animator createAnimator();

        void onCancel();

    }

}
