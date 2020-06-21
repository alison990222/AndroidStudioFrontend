
package com.example.tsinghuahelp.utils;

//import android.content.Context;
//import android.content.res.Resources;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.graphics.drawable.ShapeDrawable;
//import android.graphics.drawable.shapes.RoundRectShape;
//import android.util.AttributeSet;
//import android.util.TypedValue;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
//import android.view.ViewParent;
//import android.view.animation.AccelerateInterpolator;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.Animation;
//import android.view.animation.DecelerateInterpolator;
//import android.widget.FrameLayout;
//import android.widget.TabWidget;
//import android.widget.TextView;
//
///**
// * A simple text label view that can be applied as a "badge" to any given {@link android.view.View}.
// * This class is intended to be instantiated at runtime rather than included in XML layouts.
// *
// * @author Jeff Gilfelt
// */
//public class BadgeView extends androidx.appcompat.widget.AppCompatTextView {
//
//    public static final int POSITION_TOP_LEFT = 1;
//    public static final int POSITION_TOP_RIGHT = 2;
//    public static final int POSITION_BOTTOM_LEFT = 3;
//    public static final int POSITION_BOTTOM_RIGHT = 4;
//    public static final int POSITION_CENTER = 5;
//
//    private static final int DEFAULT_MARGIN_DIP = 5;
//    private static final int DEFAULT_LR_PADDING_DIP = 5;
//    private static final int DEFAULT_CORNER_RADIUS_DIP = 8;
//    private static final int DEFAULT_POSITION = POSITION_TOP_RIGHT;
//    private static final int DEFAULT_BADGE_COLOR = Color.parseColor("#CCFF0000"); //Color.RED;
//    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;
//
//    private static Animation fadeIn;
//    private static Animation fadeOut;
//
//    private Context context;
//    private View target;
//
//    private int badgePosition;
//    private int badgeMarginH;
//    private int badgeMarginV;
//    private int badgeColor;
//
//    private boolean isShown;
//
//    private ShapeDrawable badgeBg;
//
//    private int targetTabIndex;
//
//    public BadgeView(Context context) {
//        this(context, (AttributeSet) null, android.R.attr.textViewStyle);
//    }
//
//    public BadgeView(Context context, AttributeSet attrs) {
//        this(context, attrs, android.R.attr.textViewStyle);
//    }
//
//    /**
//     * Constructor -
//     *
//     * create a new BadgeView instance attached to a target {@link android.view.View}.
//     *
//     * @param context context for this view.
//     * @param target the View to attach the badge to.
//     */
//    public BadgeView(Context context, View target) {
//        this(context, null, android.R.attr.textViewStyle, target, 0);
//    }
//
//    /**
//     * Constructor -
//     *
//     * create a new BadgeView instance attached to a target {@link android.widget.TabWidget}
//     * tab at a given index.
//     *
//     * @param context context for this view.
//     * @param target the TabWidget to attach the badge to.
//     * @param index the position of the tab within the target.
//     */
//    public BadgeView(Context context, TabWidget target, int index) {
//        this(context, null, android.R.attr.textViewStyle, target, index);
//    }
//
//    public BadgeView(Context context, AttributeSet attrs, int defStyle) {
//        this(context, attrs, defStyle, null, 0);
//    }
//
//    public BadgeView(Context context, AttributeSet attrs, int defStyle, View target, int tabIndex) {
//        super(context, attrs, defStyle);
//        init(context, target, tabIndex);
//    }
//
//    private void init(Context context, View target, int tabIndex) {
//
//        this.context = context;
//        this.target = target;
//        this.targetTabIndex = tabIndex;
//
//        // apply defaults
//        badgePosition = DEFAULT_POSITION;
//        badgeMarginH = dipToPixels(DEFAULT_MARGIN_DIP);
//        badgeMarginV = badgeMarginH;
//        badgeColor = DEFAULT_BADGE_COLOR;
//
//        setTypeface(Typeface.DEFAULT_BOLD);
//        int paddingPixels = dipToPixels(DEFAULT_LR_PADDING_DIP);
//        setPadding(paddingPixels, 0, paddingPixels, 0);
//        setTextColor(DEFAULT_TEXT_COLOR);
//
//        fadeIn = new AlphaAnimation(0, 1);
//        fadeIn.setInterpolator(new DecelerateInterpolator());
//        fadeIn.setDuration(200);
//
//        fadeOut = new AlphaAnimation(1, 0);
//        fadeOut.setInterpolator(new AccelerateInterpolator());
//        fadeOut.setDuration(200);
//
//        isShown = false;
//
//        if (this.target != null) {
//            applyTo(this.target);
//        } else {
//            show();
//        }
//
//    }
//
//    private void applyTo(View target) {
//
//        LayoutParams lp = target.getLayoutParams();
//        ViewParent parent = target.getParent();
//        FrameLayout container = new FrameLayout(context);
//
//        if (target instanceof TabWidget) {
//
//            // set target to the relevant tab child container
//            target = ((TabWidget) target).getChildTabViewAt(targetTabIndex);
//            this.target = target;
//
//            ((ViewGroup) target).addView(container,
//                    new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//
//            this.setVisibility(View.GONE);
//            container.addView(this);
//
//        } else {
//
//            // TODO verify that parent is indeed a ViewGroup
//            ViewGroup group = (ViewGroup) parent;
//            int index = group.indexOfChild(target);
//
//            group.removeView(target);
//            group.addView(container, index, lp);
//
//            container.addView(target);
//
//            this.setVisibility(View.GONE);
//            container.addView(this);
//
//            group.invalidate();
//
//        }
//
//    }
//
//    /**
//     * Make the badge visible in the UI.
//     *
//     */
//    public void show() {
//        show(false, null);
//    }
//
//    /**
//     * Make the badge visible in the UI.
//     *
//     * @param animate flag to apply the default fade-in animation.
//     */
//    public void show(boolean animate) {
//        show(animate, fadeIn);
//    }
//
//    /**
//     * Make the badge visible in the UI.
//     *
//     * @param anim Animation to apply to the view when made visible.
//     */
//    public void show(Animation anim) {
//        show(true, anim);
//    }
//
//    /**
//     * Make the badge non-visible in the UI.
//     *
//     */
//    public void hide() {
//        hide(false, null);
//    }
//
//    /**
//     * Make the badge non-visible in the UI.
//     *
//     * @param animate flag to apply the default fade-out animation.
//     */
//    public void hide(boolean animate) {
//        hide(animate, fadeOut);
//    }
//
//    /**
//     * Make the badge non-visible in the UI.
//     *
//     * @param anim Animation to apply to the view when made non-visible.
//     */
//    public void hide(Animation anim) {
//        hide(true, anim);
//    }
//
//    /**
//     * Toggle the badge visibility in the UI.
//     *
//     */
//    public void toggle() {
//        toggle(false, null, null);
//    }
//
//    /**
//     * Toggle the badge visibility in the UI.
//     *
//     * @param animate flag to apply the default fade-in/out animation.
//     */
//    public void toggle(boolean animate) {
//        toggle(animate, fadeIn, fadeOut);
//    }
//
//    /**
//     * Toggle the badge visibility in the UI.
//     *
//     * @param animIn Animation to apply to the view when made visible.
//     * @param animOut Animation to apply to the view when made non-visible.
//     */
//    public void toggle(Animation animIn, Animation animOut) {
//        toggle(true, animIn, animOut);
//    }
//
//    private void show(boolean animate, Animation anim) {
//        if (getBackground() == null) {
//            if (badgeBg == null) {
//                badgeBg = getDefaultBackground();
//            }
//            setBackgroundDrawable(badgeBg);
//        }
//        applyLayoutParams();
//
//        if (animate) {
//            this.startAnimation(anim);
//        }
//        this.setVisibility(View.VISIBLE);
//        isShown = true;
//    }
//
//    private void hide(boolean animate, Animation anim) {
//        this.setVisibility(View.GONE);
//        if (animate) {
//            this.startAnimation(anim);
//        }
//        isShown = false;
//    }
//
//    private void toggle(boolean animate, Animation animIn, Animation animOut) {
//        if (isShown) {
//            hide(animate && (animOut != null), animOut);
//        } else {
//            show(animate && (animIn != null), animIn);
//        }
//    }
//
//    /**
//     * Increment the numeric badge label. If the current badge label cannot be converted to
//     * an integer value, its label will be set to "0".
//     *
//     * @param offset the increment offset.
//     */
//    public int increment(int offset) {
//        CharSequence txt = getText();
//        int i;
//        if (txt != null) {
//            try {
//                i = Integer.parseInt(txt.toString());
//            } catch (NumberFormatException e) {
//                i = 0;
//            }
//        } else {
//            i = 0;
//        }
//        i = i + offset;
//        setText(String.valueOf(i));
//        return i;
//    }
//
//    /**
//     * Decrement the numeric badge label. If the current badge label cannot be converted to
//     * an integer value, its label will be set to "0".
//     *
//     * @param offset the decrement offset.
//     */
//    public int decrement(int offset) {
//        return increment(-offset);
//    }
//
//    private ShapeDrawable getDefaultBackground() {
//
//        int r = dipToPixels(DEFAULT_CORNER_RADIUS_DIP);
//        float[] outerR = new float[] {r, r, r, r, r, r, r, r};
//
//        RoundRectShape rr = new RoundRectShape(outerR, null, null);
//        ShapeDrawable drawable = new ShapeDrawable(rr);
//        drawable.getPaint().setColor(badgeColor);
//
//        return drawable;
//
//    }
//
//    private void applyLayoutParams() {
//
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//
//        switch (badgePosition) {
//            case POSITION_TOP_LEFT:
//                lp.gravity = Gravity.LEFT | Gravity.TOP;
//                lp.setMargins(badgeMarginH, badgeMarginV, 0, 0);
//                break;
//            case POSITION_TOP_RIGHT:
//                lp.gravity = Gravity.RIGHT | Gravity.TOP;
//                lp.setMargins(0, badgeMarginV, badgeMarginH, 0);
//                break;
//            case POSITION_BOTTOM_LEFT:
//                lp.gravity = Gravity.LEFT | Gravity.BOTTOM;
//                lp.setMargins(badgeMarginH, 0, 0, badgeMarginV);
//                break;
//            case POSITION_BOTTOM_RIGHT:
//                lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
//                lp.setMargins(0, 0, badgeMarginH, badgeMarginV);
//                break;
//            case POSITION_CENTER:
//                lp.gravity = Gravity.CENTER;
//                lp.setMargins(0, 0, 0, 0);
//                break;
//            default:
//                break;
//        }
//
//        setLayoutParams(lp);
//
//    }
//
//    /**
//     * Returns the target View this badge has been attached to.
//     *
//     */
//    public View getTarget() {
//        return target;
//    }
//
//    /**
//     * Is this badge currently visible in the UI?
//     *
//     */
//    @Override
//    public boolean isShown() {
//        return isShown;
//    }
//
//    /**
//     * Returns the positioning of this badge.
//     *
//     * one of POSITION_TOP_LEFT, POSITION_TOP_RIGHT, POSITION_BOTTOM_LEFT, POSITION_BOTTOM_RIGHT, POSTION_CENTER.
//     *
//     */
//    public int getBadgePosition() {
//        return badgePosition;
//    }
//
//    /**
//     * Set the positioning of this badge.
//     *
//     * @param layoutPosition one of POSITION_TOP_LEFT, POSITION_TOP_RIGHT, POSITION_BOTTOM_LEFT, POSITION_BOTTOM_RIGHT, POSTION_CENTER.
//     *
//     */
//    public void setBadgePosition(int layoutPosition) {
//        this.badgePosition = layoutPosition;
//    }
//
//    /**
//     * Returns the horizontal margin from the target View that is applied to this badge.
//     *
//     */
//    public int getHorizontalBadgeMargin() {
//        return badgeMarginH;
//    }
//
//    /**
//     * Returns the vertical margin from the target View that is applied to this badge.
//     *
//     */
//    public int getVerticalBadgeMargin() {
//        return badgeMarginV;
//    }
//
//    /**
//     * Set the horizontal/vertical margin from the target View that is applied to this badge.
//     *
//     * @param badgeMargin the margin in pixels.
//     */
//    public void setBadgeMargin(int badgeMargin) {
//        this.badgeMarginH = badgeMargin;
//        this.badgeMarginV = badgeMargin;
//    }
//
//    /**
//     * Set the horizontal/vertical margin from the target View that is applied to this badge.
//     *
//     * @param horizontal margin in pixels.
//     * @param vertical margin in pixels.
//     */
//    public void setBadgeMargin(int horizontal, int vertical) {
//        this.badgeMarginH = horizontal;
//        this.badgeMarginV = vertical;
//    }
//
//    /**
//     * Returns the color value of the badge background.
//     *
//     */
//    public int getBadgeBackgroundColor() {
//        return badgeColor;
//    }
//
//    /**
//     * Set the color value of the badge background.
//     *
//     * @param badgeColor the badge background color.
//     */
//    public void setBadgeBackgroundColor(int badgeColor) {
//        this.badgeColor = badgeColor;
//        badgeBg = getDefaultBackground();
//    }
//
//    private int dipToPixels(int dip) {
//        Resources r = getResources();
//        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
//        return (int) px;
//    }
//
//}






import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.IntDef;

import com.google.android.material.tabs.TabLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

/**
 * 作者:东芝(2018/8/23).
 * 支持 重叠目标模式 和 放目标右上角但不重叠的模式 两种模式 通过setOverlap 设置,  默认为false=不重叠
 */

public class BadgeView extends View {
    private static final String TAG = "BadgeHelper";
    private float density;
    private Paint mTextPaint;
    private Paint mBackgroundPaint;
    private String text = "0";
    private int number;

    @Type
    private int type = Type.TYPE_POINT;
    private boolean isOverlap;
    private final RectF rect = new RectF();
    private int badgeColor = 0xFFD3321B; //默认的小红点颜色
    private int textColor = 0xFFFFFFff;
    private float textSize;
    private int w;
    private int h;
    private boolean isSetup;
    private boolean mIgnoreTargetPadding;
    private boolean isCenterVertical;
    private int leftMargin;
    private int topMargin;
    private int rightMargin;
    private int bottomMargin;

    @IntDef({Type.TYPE_POINT, Type.TYPE_TEXT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
        int TYPE_POINT = 0;
        int TYPE_TEXT = 1;
    }


    public BadgeView(Context context) {
        super(context);

    }


    private void init(@Type int type, boolean isOverlap) {
        this.type = type;
        this.isOverlap = isOverlap;
        density = getResources().getDisplayMetrics().density;

        switch (type) {
            case Type.TYPE_POINT:
                mBackgroundPaint = new Paint();
                mBackgroundPaint.setStyle(Paint.Style.FILL);
                mBackgroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
                mBackgroundPaint.setColor(badgeColor);
                //计算小红点无文本情况下的小红点大小,  按屏幕像素计算, 如果你有你自己认为更好的算法, 改这里即可
                w = h = Math.round(density * 7f);
                break;
            case Type.TYPE_TEXT:
                mBackgroundPaint = new Paint();
                mBackgroundPaint.setStyle(Paint.Style.FILL);
                mBackgroundPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
                mBackgroundPaint.setColor(badgeColor);

                mTextPaint = new Paint();
                mTextPaint.setStyle(Paint.Style.FILL);
                mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
                mTextPaint.setColor(textColor);//文本颜色
                if (textSize == 0) {
                    mTextPaint.setTextSize(density * 10);//文本大小按屏幕像素 计算, 没写死是为了适配各种屏幕,  但如果你有你认为更合理的计算方式 你可以改这里
                } else {
                    mTextPaint.setTextSize(textSize);//使用自定义大小
                }

                //计算小红点有文本情况下的小红点大小,  按文本宽高计算, 如果你有你自己认为更好的算法, 改这里即可
                float textWidth = getTextWidth("99", mTextPaint);
                w = h = Math.round(textWidth * 1.4f);//让背景比文本大一点
                break;
        }
    }

    /**
     * 设置Margin 可用于做偏移
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return
     */
    public BadgeView setBadgeMargins(int left, int top, int right, int bottom) {
        leftMargin = left;
        topMargin = top;
        rightMargin = right;
        bottomMargin = bottom;
        return this;
    }
    /**
     * 设置Gravity居中

     * @return
     */
    public BadgeView setBadgeCenterVertical(  ) {
        isCenterVertical = true;
        return this;
    }

    /**
     * 设置小红点类型
     *
     * @param type
     * @return
     */
    public BadgeView setBadgeType(@Type int type) {
        this.type = type;
        return this;
    }

    /**
     * 设置小红点大小, 默认自动适配
     *
     * @param textSize
     * @return
     */
    public BadgeView setBadgeTextSize(int textSize) {
        Context c = getContext();
        Resources r;
        if (c == null) {
            r = Resources.getSystem();
        } else {
            r = c.getResources();
        }
        this.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, r.getDisplayMetrics());
        return this;
    }


    /**
     * 设置小红点文字颜色, 默认白
     *
     * @param textColor
     * @return
     */
    public BadgeView setBadgeTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    /**
     * 设置重叠模式, 默认是false(不重叠)
     *
     * @param isOverlap 是否把小红点重叠到目标View之上
     * @return
     */
    public BadgeView setBadgeOverlap(boolean isOverlap) {
        return setBadgeOverlap(isOverlap, false);
    }

    /**
     * 设置重叠模式, 默认是false(不重叠)
     *
     * @param isOverlap             是否把小红点重叠到目标View之上
     * @param isIgnoreTargetPadding 是否忽略目标View的padding
     * @return
     */
    public BadgeView setBadgeOverlap(boolean isOverlap, boolean isIgnoreTargetPadding) {
        this.isOverlap = isOverlap;
        this.mIgnoreTargetPadding = isIgnoreTargetPadding;
        if (!isOverlap && isIgnoreTargetPadding) {
            Log.w(TAG, "警告:只有重叠模式isOverlap=true 设置mIgnoreTargetPadding才有意义");
        }
        return this;
    }

    /**
     * 设置小红点颜色
     *
     * @param mBadgeColor
     * @return
     */
    public BadgeView setBadgeColor(int mBadgeColor) {
        this.badgeColor = mBadgeColor;
        return this;
    }

    /**
     * 设置小红点大小
     *
     * @param w
     * @param h
     * @return
     */
    public BadgeView setBadgeSize(int w, int h) {
        this.w = w;
        this.h = h;
        return this;
    }

    /**
     * 是否显示
     * @param enable
     */
    public void setBadgeEnable(boolean enable) {
        setVisibility(enable?VISIBLE:INVISIBLE);
    }


    /**
     * 设置小红点的文字
     *
     * @param number
     */
    public void setBadgeNumber(int number) {
        this.number = number;
        this.text = String.valueOf(number);
        if (isSetup) {
            if(number==0){
                setVisibility(INVISIBLE);
            }else{
                setVisibility(VISIBLE);
            }
            invalidate();
        }
    }

    public void bindToTargetView(TabLayout target, int tabIndex) {
        TabLayout.Tab tab = target.getTabAt(tabIndex);
        View targetView = null;
        View tabView = null;
        try {
            Field viewField = TabLayout.Tab.class.getDeclaredField("mView");
            viewField.setAccessible(true);
            targetView = tabView = (View) viewField.get(tab);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (tabView != null) {
                Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");//"mIconView"
                mTextViewField.setAccessible(true);
                targetView = (View) mTextViewField.get(tabView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (targetView != null) {
            bindToTargetView(targetView);
        }
    }


    /**
     * 绑定小红点到目标View的右上角
     *
     * @param target
     */
    public void bindToTargetView(View target) {
        init(type, isOverlap);
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        if (target == null) {
            return;
        }
        if (target.getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) target.getParent();

            int groupIndex = parent.indexOfChild(target);
            parent.removeView(target);

            if (isOverlap) {//[小红点与目标View重叠]模式
                FrameLayout badgeContainer = new FrameLayout(getContext());
                ViewGroup.LayoutParams targetLayoutParams = target.getLayoutParams();
                badgeContainer.setLayoutParams(targetLayoutParams);

                target.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                parent.addView(badgeContainer, groupIndex, targetLayoutParams);
                badgeContainer.addView(target);
                badgeContainer.addView(this);

                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
                if(isCenterVertical) {
                    layoutParams.gravity =  Gravity.CENTER_VERTICAL ;
                }else{
                    layoutParams.gravity = Gravity.END | Gravity.TOP;
                }
                if (mIgnoreTargetPadding) {
                    layoutParams.rightMargin = target.getPaddingRight() - w;
                    layoutParams.topMargin = target.getPaddingTop() - h / 2;

                }

                setLayoutParams(layoutParams);


            } else {//[小红点放右侧]模式
                LinearLayout badgeContainer = new LinearLayout(getContext());
                badgeContainer.setOrientation(LinearLayout.HORIZONTAL);
                ViewGroup.LayoutParams targetLayoutParams = target.getLayoutParams();
                badgeContainer.setLayoutParams(targetLayoutParams);


                target.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                parent.addView(badgeContainer, groupIndex, targetLayoutParams);
                badgeContainer.addView(target);
                badgeContainer.addView(this);
                if(isCenterVertical) {
                    badgeContainer.setGravity(Gravity.CENTER_VERTICAL);
                }
            }
            boolean hasSetMargin = leftMargin>0||topMargin>0||rightMargin>0||bottomMargin>0;
            if (hasSetMargin&&getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) getLayoutParams();
                p.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                setLayoutParams(p);
            }
            isSetup = true;
        } else if (target.getParent() == null) {
            throw new IllegalStateException("目标View不能没有父布局!");
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (w > 0 && h > 0) {
            setMeasuredDimension(w, h);
        } else {
            throw new IllegalStateException("如果你自定义了小红点的宽高,就不能设置其宽高小于0 ,否则请不要设置!");
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //这里不用解释了 很简单 就是画一个圆形和文字
        rect.left = 0;
        rect.top = 0;
        rect.right = getWidth();
        rect.bottom = getHeight();
        canvas.drawRoundRect(rect, getWidth() / 2, getWidth() / 2, mBackgroundPaint);

        if (type == Type.TYPE_TEXT) {
            float textWidth = getTextWidth(text, mTextPaint);
            float textHeight = getTextHeight(text, mTextPaint);
            canvas.drawText(text, getWidth() / 2 - textWidth / 2, getHeight() / 2 + textHeight / 2, mTextPaint);
        }
    }

    private float getTextWidth(String text, Paint p) {
        return p.measureText(text, 0, text.length());
    }

    private float getTextHeight(String text, Paint p) {
        Rect rect = new Rect();
        p.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }


}