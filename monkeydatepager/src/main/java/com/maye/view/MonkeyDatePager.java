package com.maye.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.maye.view.monkeytabpager.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonkeyDatePager extends FrameLayout implements View.OnClickListener, ViewPager.OnPageChangeListener, NavigationTabStrip
        .OnTabStripSelectedIndexListener {

    public static final int YEAR = 0;
    public static final int MONTH = 1;
    public static final int DAY = 2;

    private boolean allowChange = true;

    private Calendar time = Calendar.getInstance();
    private String time_inner;

    public interface OnMonkeyTimeChangedListener {
        void onTimeChanged(Calendar time);
    }

    private OnMonkeyTimeChangedListener onMonkeyTimeChangedListener;

    public void setOnMonkeyTimeChangedListener(OnMonkeyTimeChangedListener listener) {
        this.onMonkeyTimeChangedListener = listener;
    }

    public interface OnMonkeyTypeChangeListener {
        void onTypeChange(int type);
    }

    private OnMonkeyTypeChangeListener onMonkeyTypeChangeListener;

    public void setOnMonkeyTypeChangeListener(OnMonkeyTypeChangeListener listener) {
        this.onMonkeyTypeChangeListener = listener;
    }

    private List<String> list = new ArrayList<>();
    private int type = DAY;
    private ViewPager vp_time;
    private TimeAdapter adapter;

    public MonkeyDatePager(Context context) {
        super(context);
        init(context);
    }

    public MonkeyDatePager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MonkeyDatePager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        View view = View.inflate(context, R.layout.view_tabpager, this);

        //顶部Tab初始化
        NavigationTabStrip nts_tab = (NavigationTabStrip) view.findViewById(R.id.nts_tab);
        nts_tab.setTabIndex(2);
        nts_tab.setOnTabStripSelectedIndexListener(this);

        initTime(context, view);

        ImageView iv_preview = (ImageView) view.findViewById(R.id.iv_preview);
        iv_preview.setOnClickListener(this);

        ImageView iv_next = (ImageView) view.findViewById(R.id.iv_next);
        iv_next.setOnClickListener(this);
    }

    private void initTime(Context context, View view) {
        //初始化控件中央适配，设定初始类型及时间
        vp_time = (ViewPager) view.findViewById(R.id.vp_time);
        vp_time.addOnPageChangeListener(this);

        List<String> list_init = new ArrayList<>();
        for (int i = 0; i < Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH); i++){
            list_init.add(String.valueOf(i + 1));
        }

        Calendar time_init = Calendar.getInstance();
        time_inner = time_init.get(Calendar.YEAR) + "-" + (time_init.get(Calendar.MONTH) + 1) + "-" + time_init.get(Calendar.DAY_OF_MONTH);

        adapter = new TimeAdapter(context, list);
        vp_time.setAdapter(adapter);

        setTypeAndData(DAY, list_init);
    }

    /**
     * 根据类型及List设置显示
     *
     * @param type      类型
     * @param list_data 显示内容
     */
    public void setTypeAndData(int type, List<String> list_data) {
        this.type = type;

        list.clear();
        list.addAll(list_data);
        allowChange = false;
        adapter.notifyDataSetChanged();
        allowChange = true;

        if (type == YEAR) {
            vp_time.setCurrentItem(list.indexOf(String.valueOf(time.get(Calendar.YEAR))));
        }
        if (type == MONTH) {
            vp_time.setCurrentItem(time.get(Calendar.MONTH));
        }
        if (type == DAY) {
            vp_time.setCurrentItem(time.get(Calendar.DAY_OF_MONTH) - 1);
        }

        if (onMonkeyTypeChangeListener != null) {
            onMonkeyTypeChangeListener.onTypeChange(type);
        }
    }

    public int getType() {
        return type;
    }

    public Calendar getTime() {
        return time;
    }

    public String getInnerTime() {
        return time_inner;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_preview) {
            changeCurrentItem(PREVIEW);
        } else if (view.getId() == R.id.iv_next) {
            changeCurrentItem(NEXT);
        }
    }

    private static final int PREVIEW = 0;
    private static final int NEXT = 1;

    private void changeCurrentItem(int changeType) {
        long currentItem = vp_time.getCurrentItem();

        switch (changeType) {
            case PREVIEW:
                if (currentItem == 0) {
                    return;
                }
                vp_time.setCurrentItem((int) (currentItem - 1));
                break;

            case NEXT:
                if (currentItem == list.size() - 1) {
                    return;
                }
                vp_time.setCurrentItem((int) (currentItem + 1));
                break;
        }
    }


    /*--ViewPager滑动监听Implements部分--*/

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (allowChange) {
            String data = list.get(position);
            setTimeTo(data);
            if (onMonkeyTimeChangedListener != null)
                onMonkeyTimeChangedListener.onTimeChanged(time);
        }
    }
    /*----------------------------------*/

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 根据选定的序号与类型设置当前的时间值
     *
     * @param data
     */
    private void setTimeTo(String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }

        int index = Integer.parseInt(data);

        switch (type) {
            case YEAR:
                time.set(Calendar.YEAR, index);
                break;

            case MONTH:
                time.set(Calendar.MONTH, index - 1);
                break;

            case DAY:
                time.set(Calendar.DAY_OF_MONTH, index);
                break;
        }

        setTimeInner(type, time);
    }

    /*-----顶部Tab选择监听Implements部分-----*/

    @Override
    public void onStartTabSelected(String title, int index) {

    }

    @Override
    public void onEndTabSelected(String title, int index) {
        List<String> list;
        switch (index) {
            case 0:
                list = setList(YEAR, time);
                setTypeAndData(YEAR, list);
                break;

            case 1:
                list = setList(MONTH, time);
                setTypeAndData(MONTH, list);
                break;

            case 2:
                list = setList(DAY, time);
                setTypeAndData(DAY, list);
                break;
        }
    }

    /*--------------------------------------*/

    /**
     * 根据时间类型设置List中整体数据
     *
     * @param type
     * @param time
     * @return
     */
    private List<String> setList(int type, Calendar time) {
        List<String> list = new ArrayList<>();
        switch (type) {
            case YEAR:
                for (int i = 0; i < 200; i++) {
                    list.add(String.valueOf(i + 1969));
                }
                break;

            case MONTH:
                for (int i = 0; i < 12; i++) {
                    list.add(String.valueOf(i + 1));
                }
                break;

            case DAY:
                for (int i = 0; i < time.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    list.add(String.valueOf(i + 1));
                }
                break;
        }
        return list;
    }

    /**
     * 根据当前的时间类型与时间数据设定控件当前代表的时间段
     * @param type 类型
     * @param time 时间值
     */
    private void setTimeInner(int type, Calendar time) {
        int year = time.get(Calendar.YEAR);
        int month = time.get(Calendar.MONTH) + 1;
        int day = time.get(Calendar.DAY_OF_MONTH);

        switch (type) {
            case MonkeyDatePager.YEAR:
                time_inner = String.valueOf(year);
                break;

            case MonkeyDatePager.MONTH:
                time_inner = year + "-" + month;
                break;

            case MonkeyDatePager.DAY:
                time_inner = year + "-" + month + "-" + day;
                break;
        }
    }

}
