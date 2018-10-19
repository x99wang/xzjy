package pri.wx.xujc.xzjy.score;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Spinner;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import pri.wx.xujc.xzjy.R;
import pri.wx.xujc.xzjy.data.model.Score;
import pri.wx.xujc.xzjy.data.model.Term;
import pri.wx.xujc.xzjy.home.schedule.TermSpinnerAdapter;
import pri.wx.xujc.xzjy.mvibase.MviView;
import pri.wx.xujc.xzjy.util.ViewModelFactory;
import pri.wx.xujc.xzjy.util.schedulers.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity
        implements MviView<ScoreIntent,ScoreViewState>,LifecycleRegistryOwner {
    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    private ScoreViewModel mViewModel;

    private CompositeDisposable mDisposables;

    private Spinner termSp;
    private ListView scoreLv;
    private ScoreAdapter mAdapter;
    private TermSpinnerAdapter spinnerAdapter;
    private List<Score> scores = new ArrayList<>();
    private List<Term> terms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        mViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(this))
                .get(ScoreViewModel.class);

        mDisposables = new CompositeDisposable();

        termSp = findViewById(R.id.sp_score_term);
        scoreLv = findViewById(R.id.list_scores);
        mAdapter = new ScoreAdapter(this, scores);
        scoreLv.setAdapter(mAdapter);
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
    public Observable<ScoreIntent> intents() {
        return Observable.merge(initIntent(),spinnerIntent());
    }

    private Observable<ScoreIntent> initIntent(){
        return Observable.just(ScoreIntent.InitIntent.create());
    }

    private Observable<ScoreIntent> spinnerIntent(){
        return RxAdapterView.itemSelections(termSp)
                .throttleFirst(2, TimeUnit.SECONDS)
                .filter(integer -> integer >= 0)
                .map(integer -> ScoreIntent.CheckScoreIntent.create(terms.get(integer).getTmId()));
    }



    @Override
    public void render(ScoreViewState state) {
        if(!state.isLoading() && null == state.error()) {
            if(state.termList().size()>0){
                this.terms.clear();
                this.terms.addAll(state.termList());
                spinnerAdapter.notifyDataSetChanged();
            }
            if(state.scoreList().size()>0){
                scoreLv.setAdapter(null);
                scores.clear();
                scores.addAll(state.scoreList());
                mAdapter.notifyDataSetChanged();
                scoreLv.setAdapter(mAdapter);
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
