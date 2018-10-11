package pri.wx.xujc.xzjy.home;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.disposables.CompositeDisposable;
import pri.wx.xujc.xzjy.R;
import pri.wx.xujc.xzjy.home.me.PersonFragment;
import pri.wx.xujc.xzjy.home.schedule.ScheduleFragment;
import pri.wx.xujc.xzjy.home.service.ServiceFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout schedule;
    private LinearLayout person;
    private LinearLayout service;

    private CompositeDisposable mDisposables;

    private ViewPager mViewPager;
    private HomeViewAdapter mViewPagerFragmentAdapter;
    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDisposables = new CompositeDisposable();

        initView();

        initFragmentList();

        initViewPager();

        bind();

    }

    private void initView() {
        mViewPager = findViewById(R.id.layout_viewpager);
        schedule = findViewById(R.id.bottom_schedule);
        person = findViewById(R.id.bottom_me);
        service = findViewById(R.id.bottom_service);
    }

    private void initFragmentList() {
        mFragmentList = new ArrayList<>();

        ScheduleFragment scheduleFragment = ScheduleFragment.newInstance();
        mFragmentList.add(scheduleFragment);

        ServiceFragment serviceFragment = ServiceFragment.newInstance();
        mFragmentList.add(serviceFragment);

        PersonFragment personFragment = PersonFragment.newInstance();
        mFragmentList.add(personFragment);
    }

    private void initViewPager() {
        mViewPagerFragmentAdapter = new HomeViewAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mViewPagerFragmentAdapter);
        mViewPager.setCurrentItem(0);
    }

    private void bind() {
        mDisposables.add(RxView.clicks(schedule).subscribe(action ->
                mViewPager.setCurrentItem(0)));
        mDisposables.add(RxView.clicks(service).subscribe(action ->
                mViewPager.setCurrentItem(1)));
        mDisposables.add(RxView.clicks(person).subscribe(action ->
                mViewPager.setCurrentItem(2)));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposables.dispose();
    }

}
