package pri.wx.xujc.xzjy.course;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Spinner;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import pri.wx.xujc.xzjy.R;
import pri.wx.xujc.xzjy.data.model.CourseEntity;
import pri.wx.xujc.xzjy.data.model.Term;
import pri.wx.xujc.xzjy.home.schedule.TermSpinnerAdapter;
import pri.wx.xujc.xzjy.mvibase.MviView;
import pri.wx.xujc.xzjy.util.ViewModelFactory;
import pri.wx.xujc.xzjy.util.schedulers.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CourseActivity extends AppCompatActivity
        implements MviView<CourseIntent, CourseViewState>,LifecycleRegistryOwner {
    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    private CourseViewModel mViewModel;

    private CompositeDisposable mDisposables;

    private Spinner termSp;
    private ListView courseLv;
    private CourseAdapter mAdapter;
    private TermSpinnerAdapter spinnerAdapter;
    private List<CourseEntity> courses = new ArrayList<>();
    private List<Term> terms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        mViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(this))
                .get(CourseViewModel.class);

        mDisposables = new CompositeDisposable();

        termSp = findViewById(R.id.sp_course_term);
        courseLv = findViewById(R.id.list_courses);
        mAdapter = new CourseAdapter(this, courses);
        courseLv.setAdapter(mAdapter);
        spinnerAdapter = new TermSpinnerAdapter(this,terms);
        termSp.setAdapter(spinnerAdapter);

        bind();
    }

    private void bind() {
        mDisposables.add(mViewModel.states()
                .observeOn(SchedulerProvider.getInstance().ui())
                .subscribe(this::render));
        mViewModel.processIntents(intents());
    }

    @Override
    public Observable<CourseIntent> intents() {
        return Observable.merge(initIntent(),spinnerIntent());
    }

    private Observable<CourseIntent> initIntent(){
        return Observable.just(CourseIntent.InitIntent.create());
    }

    private Observable<CourseIntent> spinnerIntent(){
        return RxAdapterView.itemSelections(termSp)
                .throttleFirst(2, TimeUnit.SECONDS)
                .filter(integer -> integer >= 0)
                .map(integer -> CourseIntent.CheckCourseIntent.create(terms.get(integer).getTmId()));
    }



    @Override
    public void render(CourseViewState state) {
        if(!state.isLoading() && null == state.error()) {
            if(state.termList().size()>0){
                this.terms.clear();
                this.terms.addAll(state.termList());
                spinnerAdapter.notifyDataSetChanged();
            }
            if(state.courseList().size()>0){
                courseLv.setAdapter(null);
                courses.clear();
                courses.addAll(state.courseList());
                mAdapter.notifyDataSetChanged();
                courseLv.setAdapter(mAdapter);
            }
        }
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposables.dispose();
    }
}
