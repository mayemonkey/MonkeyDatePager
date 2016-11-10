package com.zc.view.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.maye.view.MonkeyDatePager;
import java.util.Calendar;

public class HomeActivity extends Activity implements MonkeyDatePager.OnMonkeyTimeChangedListener, MonkeyDatePager.OnMonkeyTypeChangeListener {

    private TextView tv_date;
    private MonkeyDatePager mdp_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initComponent();
    }

    private void initComponent() {
        mdp_home = (MonkeyDatePager) findViewById(R.id.mdp_home);
        mdp_home.setOnMonkeyTypeChangeListener(this);
        mdp_home.setOnMonkeyTimeChangedListener(this);
        tv_date = (TextView) findViewById(R.id.tv_date);

    }

    @Override
    public void onTypeChange(int type) {
        String innerTime = mdp_home.getInnerTime();
        tv_date.setText("Date:" + innerTime);
    }

    @Override
    public void onTimeChanged(Calendar time) {
        String innerTime = mdp_home.getInnerTime();
        tv_date.setText("Date:" + innerTime);
    }

}
