package com.korz.ScrollableViewPagerIndicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.korz.ScrollableViewPagerIndicator.Dot.State;

/**
 * View for indicate index of viewPager
 * Every point represent by class {@link Dot}
 * If count pages more than maxDotCount dots will scroll
 */
public class ViewPageIndicator extends FrameLayout {

    RecyclerView recyclerView;
    DotRecyclerViewAdapter adapter;
    LinearLayoutManager manager;

    ViewParams params;

    boolean ignoreOnPageScrolled = true;

    int currentPage;
    int maxDotCount;
    int totalCount;

    public ViewPageIndicator(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ViewPageIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ViewPageIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {

        params = new ViewParams(context, attrs);
        recyclerView = new RecyclerView(context);
        adapter = new DotRecyclerViewAdapter(params);
        maxDotCount = params.getMaxDotCount();
        manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewDisabler());
        int width = (params.getDotMargin() * 2 + params.getActiveDotSize()) * params.getMaxDotCount();
        FrameLayout.LayoutParams layoutParams = new LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        addView(recyclerView, layoutParams);
    }

    public void setViewPager(final ViewPager pager) {
        totalCount = pager.getAdapter().getCount();
        //change size recycler for actual dot count
        if (totalCount < maxDotCount) {
            LayoutParams layoutParams = (LayoutParams) recyclerView.getLayoutParams();
            layoutParams.width = (params.getDotMargin() * 2 + params.getActiveDotSize()) * totalCount;
            recyclerView.setLayoutParams(layoutParams);
        }


        pager.addOnPageChangeListener(new OnPageChangeListener() {
            float lastOffset;

            @Override
            public void onPageScrolled(int position, float offset, int i1) {
                //position - edge left visible page
                //offset - (values from 0 to 1) offset of page defined by position
                //for swipe to left position == currentPage position and offset start from 0 and end 1
                //for swipe to right position == currentPage position -1 and offset start from 1 and end 0
                if (ignoreOnPageScrolled) {
                    return;
                }
                //ignore first offset == 0 for next page
                if (offset == 0 && offset < lastOffset - 0.5f) {
                    lastOffset = offset;
                    return;
                }
                lastOffset = offset;
                int widthItem = recyclerView.getChildAt(0).getWidth();
                int currentActiveDot;
                int willBeActiveDot;
                int lastDot;
                int firstDot;
                if (currentPage == position) {//swipe to left
                    currentActiveDot = position;
                    willBeActiveDot = position + 1;
                    firstDot = currentActiveDot - maxDotCount / 2;//will be gone to left
                    lastDot = willBeActiveDot + maxDotCount / 2;//will be appear from right

                    changeState(State.INACTIVE_STATE, State.ACTIVE_STATE, willBeActiveDot, offset);
                    changeState(State.ACTIVE_STATE, State.INACTIVE_STATE, currentActiveDot, offset);
                    if (needScroll(currentActiveDot)) {
                        manager.scrollToPositionWithOffset(firstDot + 1, (int) (widthItem * (1 - offset)));
                        changeState(State.OUT_SIDE_STATE,
                                lastDot == totalCount - 1
                                        ? State.INACTIVE_STATE
                                        : State.EDGE_STATE,
                                lastDot, offset);
                        changeState(State.EDGE_STATE, State.INACTIVE_STATE, lastDot - 1, offset);
                        changeState(State.EDGE_STATE, State.OUT_SIDE_STATE, firstDot, offset);
                        changeState(State.INACTIVE_STATE, State.EDGE_STATE, firstDot + 1, offset);

                    }
                } else {//swipe to right: reverse offset
                    currentActiveDot = position + 1;
                    willBeActiveDot = position;
                    firstDot = willBeActiveDot - maxDotCount / 2;//will be appear from left
                    lastDot = currentActiveDot + maxDotCount / 2;//will be gone to right

                    changeState(State.ACTIVE_STATE, State.INACTIVE_STATE, currentActiveDot, 1 - offset);
                    changeState(State.INACTIVE_STATE, State.ACTIVE_STATE, willBeActiveDot, 1 - offset);
                    if (needScroll(willBeActiveDot)) {
                        manager.scrollToPositionWithOffset(firstDot, (int) (-widthItem * offset));
                        changeState(State.OUT_SIDE_STATE, firstDot == 0 ? State.INACTIVE_STATE : State.EDGE_STATE, firstDot, 1 - offset);
                        changeState(State.EDGE_STATE, State.INACTIVE_STATE, firstDot + 1, 1 - offset);
                        changeState(State.EDGE_STATE, State.OUT_SIDE_STATE, lastDot, 1 - offset);
                        changeState(State.INACTIVE_STATE, State.EDGE_STATE, lastDot - 1, 1 - offset);
                    }
                }

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int scrollState) {

                switch (scrollState) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        currentPage = pager.getCurrentItem();
                        ignoreOnPageScrolled = true;
                        setStateForVisibleDots(currentPage);
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        ignoreOnPageScrolled = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        ignoreOnPageScrolled = false;
                        break;
                }
            }
        });
        adapter.setCount(totalCount);
        currentPage = pager.getCurrentItem();
        post(new Runnable() {
            @Override
            public void run() {
                setStateForVisibleDots(currentPage);
            }
        });

    }

    private boolean needScroll(int position) {
        return position >= maxDotCount / 2 && position < totalCount - maxDotCount / 2 - 1;
    }

    @Nullable
    private Dot getDot(int position) {
        ViewGroup view = (ViewGroup) manager.findViewByPosition(position);
        return view != null ? ((Dot) view.getChildAt(0)) : null;
    }

    private void changeState(@Nullable @State int originState, @State int targetState, int position, float progress) {

        Dot dot = getDot(position);
        if (dot != null)
            if (originState > 0) {
                dot.changeStateFromTo(originState, targetState, progress);
            } else {
                dot.changeStateTo(targetState, progress);
            }
    }

    private void setState(@State final int state, int position) {
        final Dot dot = getDot(position);
        if (dot != null)
            dot.setState(state);
    }

    private void setStateForVisibleDots(int selectedPosition) {
        int firstVisiblePosition = selectedPosition < totalCount - maxDotCount / 2
                ? selectedPosition - maxDotCount / 2
                : totalCount - maxDotCount;
        // if first visible dot represent first page in list it shown how INACTIVE, else how EDGE.
        //if dot's position in list equals current selected page shown how ACTIVE
        setState(firstVisiblePosition > 0
                ? State.EDGE_STATE
                : selectedPosition == firstVisiblePosition
                ? State.ACTIVE_STATE
                : State.INACTIVE_STATE, firstVisiblePosition);

        int lastVisiblePosition = selectedPosition > maxDotCount / 2
                ? selectedPosition + maxDotCount / 2
                : maxDotCount - 1;
        // if last visible dot represent last page in list it shown how INACTIVE, else how EDGE.
        //if dot's position in list equals current selected page shown how ACTIVE
        setState(lastVisiblePosition < totalCount - 1
                ? State.EDGE_STATE
                : lastVisiblePosition == selectedPosition
                ? State.ACTIVE_STATE
                : State.INACTIVE_STATE, lastVisiblePosition);
        //all dots from firstVisible to lastVisible set how INACTIVE if they position =! position of current page
        for (int i = firstVisiblePosition + 1; i < lastVisiblePosition; i++) {
            setState(i == selectedPosition ? State.ACTIVE_STATE : State.INACTIVE_STATE, i);
        }
    }

    /**
     * class container for store custom attributes for {@link ViewPageIndicator}
     * Container pass to child views (inner {@link #recyclerView } and {@link Dot dot's created in {@link #adapter}})
     * of {@link ViewPageIndicator}
     */
    public static class ViewParams {
        private int activeColor;
        private int inactiveColor;
        private float inactiveFinalScale = 0.6f;
        private float edgeFinalScale = 0.4f;
        private int activeDotSize = 25;
        private int dotMargin = 10;
        private int maxDotCount = 5;

        public ViewParams(Context context, @Nullable AttributeSet attrs) {
            activeColor = context.getResources().getColor(R.color.activeDotDefault);
            inactiveColor = context.getResources().getColor(R.color.inactiveDotDefault);
            if (attrs != null) {
                TypedArray a = context.getTheme().obtainStyledAttributes(
                        attrs,
                        R.styleable.ViewPageIndicator,
                        0, 0);

                try {
                    maxDotCount = a.getInteger(R.styleable.ViewPageIndicator_maxDotCount, 5);
                    activeDotSize = a.getDimensionPixelSize(R.styleable.ViewPageIndicator_activeDotSize, 25);
                    dotMargin = a.getDimensionPixelSize(R.styleable.ViewPageIndicator_dotMargin, 10);
                    edgeFinalScale = a.getFloat(R.styleable.ViewPageIndicator_edgeFinalScale, 0.4f);
                    inactiveFinalScale = a.getFloat(R.styleable.ViewPageIndicator_inactiveFinalScale, 0.6f);
                    inactiveColor = a.getColor(R.styleable.ViewPageIndicator_inactiveColor,
                            context.getResources().getColor(R.color.inactiveDotDefault));
                    activeColor = a.getColor(R.styleable.ViewPageIndicator_activeColor,
                            context.getResources().getColor(R.color.activeDotDefault));
                } finally {
                    a.recycle();
                }
            }

        }

        public int getActiveColor() {
            return activeColor;
        }

        public int getInactiveColor() {
            return inactiveColor;
        }

        public float getInactiveFinalScale() {
            return inactiveFinalScale;
        }

        public float getEdgeFinalScale() {
            return edgeFinalScale;
        }

        public int getActiveDotSize() {
            return activeDotSize;
        }

        public int getDotMargin() {
            return dotMargin;
        }

        public int getMaxDotCount() {
            return maxDotCount;
        }
    }

}
