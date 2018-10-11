package pri.wx.xujc.xzjy.home.schedule;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import pri.wx.xujc.xzjy.R;
import pri.wx.xujc.xzjy.data.model.Course;
import pri.wx.xujc.xzjy.data.model.Term;
import pri.wx.xujc.xzjy.mvibase.MviView;
import pri.wx.xujc.xzjy.util.MyUtils;
import pri.wx.xujc.xzjy.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment
        implements MviView<ScheduleIntent,ScheduleViewState>, LifecycleRegistryOwner {

    public static final String TAG = "ScheduleFragment";

    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    private ScheduleViewModel mViewModel;

    private CompositeDisposable mDisposables;

    private View mView;
    private TextView week;
    private GridView detailCourse;
    private Spinner detailTerm;

    private ScheduleAdapter scheduleAdapter;
    private MySpinnerAdapter spinnerAdapter;
    private List<Course> courses = new ArrayList<>(42);
    private List<Term> terms = new ArrayList<>();


    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_schedule, container, false);
        }
        // UI init
        TextView date = mView.findViewById(R.id.tv_date);
        date.setText(MyUtils.getNowDate());// 当前日期
        week = mView.findViewById(R.id.tv_week);// 周数
        detailCourse = mView.findViewById(R.id.grid_detail);// 课表
        detailTerm = mView.findViewById(R.id.sp_term);// 学期列表
        return mView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 适配课表视图
        scheduleAdapter = new ScheduleAdapter(getContext(),courses);
        detailCourse.setAdapter(scheduleAdapter);
        //适配学期列表视图
        spinnerAdapter = new MySpinnerAdapter(getContext(),terms);
        detailTerm.setAdapter(spinnerAdapter);

        mViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(getContext()))
                .get(ScheduleViewModel.class);
        mDisposables = new CompositeDisposable();
        bind();
    }

    private void bind() {
        mDisposables.add(mViewModel.states().subscribe(this::render));
        mViewModel.processIntents(intents());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposables.dispose();
    }

    @Override
    public Observable<ScheduleIntent> intents() {
        return initIntents();
    }

    /**
     * 界面初始化
     * @return 初始化事件
     */
    private Observable<ScheduleIntent> initIntents() {
        return Observable.merge(
                Observable.just(ScheduleIntent.InitScheduleIntent.create()),
                Observable.just(ScheduleIntent.InitTermIntent.create()),
                Observable.just(ScheduleIntent.InitWeekIntent.create()),
                spinnerIntents());
    }

    /**
     * Spinner监听
     * @return 学期列表点击事件
     */
    private Observable<ScheduleIntent> spinnerIntents() {
        return RxAdapterView.itemSelections(detailTerm)
                .throttleFirst(2, TimeUnit.SECONDS)
                .filter(integer -> integer >= 0)

                .map(integer -> ScheduleIntent.ClassesIntent.create(terms.get(integer).getTmId()));
    }

    @Override
    public void render(ScheduleViewState state) {
        if(!state.isLoading() && null == state.error()){
            if(!state.week().equals("0"))
                week.setText(state.week());
            if(state.terms().size()>0){
                this.terms.clear();
                this.terms.addAll(state.terms());
                spinnerAdapter.notifyDataSetChanged();
            }
            if(state.course().size()>0){
                detailCourse.setAdapter(null);
                courses.clear();
                courses.addAll(state.course());
                scheduleAdapter.notifyDataSetChanged();
                detailCourse.setAdapter(scheduleAdapter);
            }
        }
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }

}
