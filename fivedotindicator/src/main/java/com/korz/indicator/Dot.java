package com.korz.indicator;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;

import com.korz.indicator.ViewPageIndicator.ViewParams;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.korz.indicator.Dot.State.ACTIVE_STATE;
import static com.korz.indicator.Dot.State.EDGE_STATE;
import static com.korz.indicator.Dot.State.INACTIVE_STATE;
import static com.korz.indicator.Dot.State.OUT_SIDE_STATE;

public class Dot extends AppCompatImageView {

    final SpringAnimation springScaleX = new SpringAnimation(this, DynamicAnimation.SCALE_X, 1);
    final SpringAnimation springScaleY = new SpringAnimation(this, DynamicAnimation.SCALE_Y, 1);

    int activeColor;
    int inactiveColor;
    float inactiveFinalScale = 0.75f;
    float edgeFinalScale = 0.2f;
    @State
    int currentState;

    public Dot(Context context) {
        super(context);
    }

    public Dot(Context context, ViewParams params) {
        super(context);
        init(context, params);
    }


    public Dot(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public Dot(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    protected void init(Context context, ViewParams params) {

        activeColor = params.getActiveColor();
        inactiveColor = params.getInactiveColor();
        inactiveFinalScale = params.getInactiveFinalScale();
        edgeFinalScale = params.getEdgeFinalScale();

        setImageDrawable(context.getResources().getDrawable(R.drawable.circle));
    }

    public void changeStateTo(@State int targetState, float progress) {
        changeStateFromTo(currentState, targetState, progress);
    }

    public void changeStateFromTo(@State int originState, @State int targetState, float progress) {
        int startColor = 0;
        int endColor = 0;
        float scaleRatio = getScaleX();

        switch (originState) {
            case ACTIVE_STATE:
                startColor = activeColor;
                switch (targetState) {
                    case ACTIVE_STATE:
                        endColor = activeColor;
                        scaleRatio = 1f;
                        break;
                    case INACTIVE_STATE:
                        scaleRatio = 1 - progress * (1 - inactiveFinalScale);
                        endColor = inactiveColor;
                        break;
                    case EDGE_STATE:
                        endColor = inactiveColor;
                        scaleRatio = 1 - progress * (1 - edgeFinalScale);
                        break;
                    case OUT_SIDE_STATE:
                        scaleRatio = (1 - progress);
                        endColor = inactiveColor;
                        break;
                }
                break;
            case INACTIVE_STATE:
                startColor = inactiveColor;
                switch (targetState) {
                    case ACTIVE_STATE:
                        endColor = activeColor;
                        scaleRatio = inactiveFinalScale + progress * (1 - inactiveFinalScale);
                        break;
                    case INACTIVE_STATE:
                        endColor = inactiveColor;
                        scaleRatio = inactiveFinalScale;
                        break;
                    case EDGE_STATE:
                        endColor = inactiveColor;
                        scaleRatio = inactiveFinalScale - progress * (inactiveFinalScale - edgeFinalScale);
                        break;
                    case OUT_SIDE_STATE:
                        scaleRatio = inactiveFinalScale * (1 - progress);
                        endColor = inactiveColor;
                        break;
                }
                break;
            case EDGE_STATE:
                startColor = inactiveColor;
                switch (targetState) {
                    case ACTIVE_STATE:
                        endColor = activeColor;
                        scaleRatio = edgeFinalScale + progress * (1 - edgeFinalScale);
                        break;
                    case INACTIVE_STATE:
                        endColor = inactiveColor;
                        scaleRatio = edgeFinalScale + progress * (inactiveFinalScale - edgeFinalScale);
                        break;
                    case EDGE_STATE:
                        endColor = inactiveColor;
                        scaleRatio = edgeFinalScale;
                        break;
                    case OUT_SIDE_STATE:
                        scaleRatio = edgeFinalScale * (1 - progress);
                        endColor = inactiveColor;
                        break;
                }
                break;

            case OUT_SIDE_STATE:
                startColor = inactiveColor;
                switch (targetState) {
                    case ACTIVE_STATE:
                        endColor = activeColor;
                        scaleRatio = progress;
                        break;
                    case INACTIVE_STATE:
                        endColor = inactiveColor;
                        scaleRatio = inactiveFinalScale * progress;
                        break;
                    case EDGE_STATE:
                        endColor = inactiveColor;
                        scaleRatio = edgeFinalScale * progress;
                        break;
                    case OUT_SIDE_STATE:
                        endColor = inactiveColor;
                        scaleRatio = 0f;
                        break;
                }
                break;
        }

        setColorFilter(getTransitionColor(endColor, startColor, progress), Mode.DST_ATOP);
        springScaleX.animateToFinalPosition(scaleRatio);
        springScaleY.animateToFinalPosition(scaleRatio);
    }

    public void setState(@State int state) {
        changeStateFromTo(state, state, 1);
    }

    public int getTransitionColor(int targetColor, int originColor, float progress) {
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        return (Integer) argbEvaluator.evaluate(progress, originColor, targetColor);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ACTIVE_STATE, INACTIVE_STATE, EDGE_STATE, OUT_SIDE_STATE})
    public @interface State {
        int ACTIVE_STATE = 1;
        int INACTIVE_STATE = 2;
        int EDGE_STATE = 3;
        int OUT_SIDE_STATE = 4;
    }

}
