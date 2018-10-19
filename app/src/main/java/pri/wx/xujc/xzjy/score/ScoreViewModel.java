package pri.wx.xujc.xzjy.score;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;
import io.reactivex.subjects.PublishSubject;
import pri.wx.xujc.xzjy.mvibase.MviIntent;
import pri.wx.xujc.xzjy.mvibase.MviViewModel;

public class ScoreViewModel extends ViewModel implements MviViewModel<ScoreIntent,ScoreViewState> {
    private static final String TAG = "WelcomeViewModel";

    @NonNull
    private PublishSubject<ScoreIntent> mIntentsSubject;
    @NonNull
    private Observable<ScoreViewState> mStatesObservable;
    @NonNull
    private ScoreProcessorHolder mActionProcessorHolder;

    public ScoreViewModel(@NonNull ScoreProcessorHolder mActionProcessorHolder) {
        this.mActionProcessorHolder = mActionProcessorHolder;
        mIntentsSubject = PublishSubject.create();
        mStatesObservable = compose();
    }

    private Observable<ScoreViewState> compose() {
        return mIntentsSubject
                .compose(intentFilter)
                .map(this::actionFromIntent)
                .compose(mActionProcessorHolder.actionProcessor)
                .scan(ScoreViewState.idle(),reducer);
    }

    private ScoreAction actionFromIntent(MviIntent intent) {
        if (intent instanceof ScoreIntent.InitIntent) {
            Log.i(TAG, "Init Intent -> Init Term Action");
            return ScoreAction.InitTermAction.create();
        }else if (intent instanceof ScoreIntent.CheckScoreIntent) {
            Log.i(TAG, "Score Intent -> Select Score Action");
            return ScoreAction.LoadScoreAction.create(((ScoreIntent.CheckScoreIntent) intent).term());
        }
        throw new IllegalArgumentException("do not know how to treat this intent " + intent);
    }

    private ObservableTransformer<ScoreIntent, ScoreIntent> intentFilter =
            intents -> intents.doOnNext(log -> Log.i(TAG, "Start Filter Intent"))
                    .publish(shared ->
                            Observable.merge(shared.ofType(ScoreIntent.InitIntent.class),
                                    shared.ofType(ScoreIntent.CheckScoreIntent.class),
                                    shared.filter(intent -> !(intent instanceof ScoreIntent.InitIntent)
                                            && !(intent instanceof ScoreIntent.CheckScoreIntent)))
                    );

    @Override
    public void processIntents(Observable<ScoreIntent> intents) {
        intents.subscribe(mIntentsSubject);
    }

    @Override
    public Observable<ScoreViewState> states() {
        return mStatesObservable;
    }

    private BiFunction<ScoreViewState,ScoreResult,ScoreViewState> reducer =
            (previousState, result) -> {
                ScoreViewState.Builder stateBuilder = previousState.buildWith();
                if (result instanceof ScoreResult.TermList) {
                    ScoreResult.TermList loadResult = (ScoreResult.TermList) result;
                    Log.i(TAG, "Term Result -> State");
                    switch (loadResult.status()) {
                        case SUCCESS:
                            return stateBuilder.isLoading(false).termList(loadResult.list()).build();
                        case FAILURE:
                            return stateBuilder.isLoading(false).error(loadResult.error()).build();
                        case IN_FLIGHT:
                            return stateBuilder.isLoading(true).build();
                    }
                } else if (result instanceof ScoreResult.ScoreList) {
                    ScoreResult.ScoreList loadResult = (ScoreResult.ScoreList) result;
                    Log.i(TAG, "Score Result -> State");
                    switch (loadResult.status()) {
                        case SUCCESS:
                            return stateBuilder.isLoading(false).scoreList(loadResult.list()).build();
                        case FAILURE:
                            return stateBuilder.isLoading(false).error(loadResult.error()).build();
                        case IN_FLIGHT:
                            return stateBuilder.isLoading(true).build();
                    }
                } else{
                    throw new IllegalArgumentException("Don't know this result " + result);
                }
                // Fail for unhandled results
                throw new IllegalStateException("Mishandled result? Should not happen (as always)");
            };
}
