package security.godmantis.com.vpindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * 自定义viewPager指示器
 * Created by Alex on 2017/3/25.
 */

public class ViewPagerIndicator extends LinearLayout {

    private Paint mPaint;//三角形画笔

    private Path mPath;

    private int mTriangleWidth;//tab宽

    private int mTriangleHeight;//tab高

    private  static final float RADIO_TRIANGLE_WIDTH=1/6f;
    //三角形底边最大宽
    private  final int DIMENSION_TRIANGLE_WIDTH_MAX= (int) (getScreenWidth()/3*RADIO_TRIANGLE_WIDTH);

    private int mInitTranslationX;

    private int mTranslationX;

    private int mTabVisibleCount;

    private static final int COUNT_DEFAULT_TAB=4;

    private List<String> mTitles;

    private ViewPager mViewPager;

    private static final int COLOR_TEXT_NORMAL=0x77ffffff;
    private static final int COLOR_TEXT_HIGHLIGHT=0xffffffff;

    public ViewPagerIndicator(Context context) {
        super(context);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mTabVisibleCount = typedArray.getInt(R.styleable.ViewPagerIndicator_visible_tab_count, COUNT_DEFAULT_TAB);
        if(mTabVisibleCount<0){
            mTabVisibleCount=COUNT_DEFAULT_TAB;
        }
        typedArray.recycle();


        init();
    }


    /**
     * 初始化三角形画笔
     */
    private void init() {

        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));//设置三角形角圆度
    }

    /**
     * xml加载完以后调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int cCount = getChildCount();
        if (cCount == 0) {
            return;
        }
        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            layoutParams.weight = 0;
            layoutParams.width = getScreenWidth() / mTabVisibleCount;
            view.setLayoutParams(layoutParams);
        }

        setItemClickEvent();

    }

    /**
     * 画三角形
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {

        canvas.save();
        canvas.translate(mInitTranslationX+mTranslationX,getHeight());
        canvas.drawPath(mPath,mPaint);
        canvas.restore();

        super.dispatchDraw(canvas);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 控件宽高发生变化回调
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth = (int) (w/mTabVisibleCount*RADIO_TRIANGLE_WIDTH);//三角形tab的宽度整个宽的1/6
        mTriangleWidth =Math.min(mTriangleWidth,DIMENSION_TRIANGLE_WIDTH_MAX);//初始化
        mInitTranslationX=w/mTabVisibleCount/2-mTriangleWidth/2;//初始化的位置
        initTrangle();

    }

    /**
     * 初始化三角形
     */
    private void initTrangle() {
        mTriangleHeight= (int) (mTriangleWidth/2.5);
        mPath=new Path();
        mPath.moveTo(0,0);
        mPath.lineTo(mTriangleWidth,0);
        mPath.lineTo(mTriangleWidth/2,-mTriangleHeight);
        mPath.close();
    }

    /**
     * 指示器跟随手指进行滚动
     * @param position
     * @param positionOffset
     */
    public void scroll(int position, float positionOffset) {
        int tabWidth = getWidth() / mTabVisibleCount;
        mTranslationX = (int) (tabWidth * (positionOffset + position));
        //容器移动条件：当tab移动至最后一个显示时，

        if(mTabVisibleCount!=1) {
            if (position >= (mTabVisibleCount - 2) && positionOffset > 0 && getChildCount() > mTabVisibleCount&&position<(mTitles.size()-2)) {
                this.scrollTo(((position - (mTabVisibleCount - 2)) * tabWidth + (int) (tabWidth * positionOffset)), 0);
            }
        }else{
            this.scrollTo(position*tabWidth+(int)(tabWidth*positionOffset),0);
        }

        invalidate();//重绘

    }

    /**
     * 获取屏幕宽度
     * @return
     */
    public int getScreenWidth() {

        WindowManager wm= (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }

    /**
     * 动态设置tab
     * @param titles
     */
    public void setTabItemTitles(List<String> titles){
        if(titles!=null&&titles.size()>0){
            this.removeAllViews();
            mTitles=titles;
            for (String title : mTitles) {
             addView(generateTextView(title));
            }
        }

        setItemClickEvent();
    }

    /**
     * 设置可见的tab数量
     * @param count
     */
    public void setVisibleTabCount(int count){
        mTabVisibleCount=count;
    }

    /**
     * 根据title创建tab
     * @param title
     * @return
     */
    private View generateTextView(String title) {
        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.width=getScreenWidth()/mTabVisibleCount;
        textView.setText(title);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        textView.setTextColor(COLOR_TEXT_NORMAL);
        textView.setLayoutParams(layoutParams);
        return textView;
    }


    /**
     * 暴露一个page监听给用户,由于我们本身把viewpager的接口给用了
     */
    public interface PageOnChangeListener{

        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);

    }

    private PageOnChangeListener mPageOnChangeListener;

    public void setonPageOnChangeListener(PageOnChangeListener listener){
        this.mPageOnChangeListener=listener;
    }

    /**
     * 设置关联的viewPager
     * @param vp
     * @param pos
     */
    public void setViewPager(ViewPager vp,int pos){
        mViewPager=vp;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position,positionOffset);
                if(mPageOnChangeListener!=null){
                    mPageOnChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                highLightTextView(position);
                if (mPageOnChangeListener!=null) {
                    mPageOnChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(mPageOnChangeListener!=null){
                    mPageOnChangeListener.onPageScrollStateChanged(state);
                }
            }
        });

        mViewPager.setCurrentItem(pos);
        highLightTextView(pos);
    }

    /**
     * 设置某一文本高亮
     * @param pos
     */
    private void highLightTextView(int pos){
        resetTextViewColor();
        View view = getChildAt(pos);
        if(view instanceof TextView){
            ((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHT);
        }
    }

    /**
     * 重置文本颜色
     */
    private void resetTextViewColor(){
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if(view instanceof TextView){
                ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }

    /**
     * 设置tab点击事件
     */
    private void setItemClickEvent(){

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {

            final int j=i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }
}
