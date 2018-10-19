package pri.wx.xujc.xzjy.score;

import android.support.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import pri.wx.xujc.xzjy.data.source.DataSource;
import pri.wx.xujc.xzjy.util.schedulers.BaseSchedulerProvider;

public class ScoreProcessorHolder {
    @NonNull
    private DataSource mRepository;

    @NonNull
    private BaseSchedulerProvider mSchedulerProvider;

    public ScoreProcessorHolder(@NonNull DataSource mRepository,
                                @NonNull BaseSchedulerProvider mSchedulerProvider) {
        this.mRepository = mRepository;
        this.mSchedulerProvider = mSchedulerProvider;
    }

    private ObservableTransformer<ScoreAction.InitTermAction, ScoreResult.TermList>
            initTermProcessor = actions ->
            actions.flatMap(action -> mRepository.getTerm()
                    .map(ScoreResult.TermList::success)
                    .onErrorReturn(ScoreResult.TermList::failure)
                    .toObservable()
                    .subscribeOn(mSchedulerProvider.io())
                    .startWith(ScoreResult.TermList.inFlight()));

    private ObservableTransformer<ScoreAction.LoadScoreAction, ScoreResult.ScoreList>
            loadScoreProcessor = actions ->
            actions.flatMap(action -> mRepository.getScore(action.term())
                    .map(ScoreResult.ScoreList::success)
                    .onErrorReturn(ScoreResult.ScoreList::failure)
                    .toObservable()
                    .subscribeOn(mSchedulerProvider.io())
                    .startWith(ScoreResult.ScoreList.inFlight()));

    ObservableTransformer<ScoreAction,ScoreResult> actionProcessor =
            actions -> actions.publish(shared -> Observable.merge(
                    shared.ofType(ScoreAction.InitTermAction.class)
                    .compose(initTermProcessor)
                    .cast(ScoreResult.class),
                    shared.ofType(ScoreAction.LoadScoreAction.class)
                    .compose(loadScoreProcessor)
                    .cast(ScoreResult.class)
            ));

}
