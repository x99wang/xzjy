package pri.wx.xujc.xzjy.course;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;
import io.reactivex.subjects.PublishSubject;
import pri.wx.xujc.xzjy.mvibase.MviIntent;
import pri.wx.xujc.xzjy.mvibase.MviViewModel;

public class CourseViewModel extends ViewModel implements MviViewModel<CourseIntent, CourseViewState> {
    private static final String TAG = "WelcomeViewModel";

    @NonNull
    private PublishSubject<CourseIntent> mIntentsSubject;
    @NonNull
    private Observable<CourseViewState> mStatesObservable;
    @NonNull
    private CourseProcessorHolder mActionProcessorHolder;

    public CourseViewModel(@NonNull CourseProcessorHolder mActionProcessorHolder) {
        this.mActionProcessorHolder = mActionProcessorHolder;
        mIntentsSubject = PublishSubject.create();
        mStatesObservable = compose();
    }

    private Observable<CourseViewState> compose() {
        return mIntentsSubject
                .compose(intentFilter)
                .map(this::actionFromIntent)
                .compose(mActionProcessorHolder.actionProcessor)
                .scan(CourseViewState.idle(),reducer);
    }

    private CourseAction actionFromIntent(MviIntent intent) {
        if (intent instanceof CourseIntent.InitIntent) {
            Log.i(TAG, "Init Intent -> Init Term Action");
            return CourseAction.InitTermAction.create();
        }else if (intent instanceof CourseIntent.CheckCourseIntent) {
            Log.i(TAG, "Course Intent -> Select Course Action");
            return CourseAction.LoadCourseAction.create(((CourseIntent.CheckCourseIntent) intent).term());
        }
        throw new IllegalArgumentException("do not know how to treat this intent " + intent);
    }

    private ObservableTransformer<CourseIntent, CourseIntent> intentFilter =
            intents -> intents.doOnNext(log -> Log.i(TAG, "Start Filter Intent"))
                    .publish(shared ->
                            Observable.merge(shared.ofType(CourseIntent.InitIntent.class),
                                    shared.ofType(CourseIntent.CheckCourseIntent.class),
                                    shared.filter(intent -> !(intent instanceof CourseIntent.InitIntent)
                                            && !(intent instanceof CourseIntent.CheckCourseIntent)))
                    );

    @Override
    public void processIntents(Observable<CourseIntent> intents) {
        intents.subscribe(mIntentsSubject);
    }

    @Override
    public Observable<CourseViewState> states() {
        return mStatesObservable;
    }

    private BiFunction<CourseViewState, CourseResult, CourseViewState> reducer =
            (previousState, result) -> {
                CourseViewState.Builder stateBuilder = previousState.buildWith();
                if (result instanceof CourseResult.TermList) {
                    CourseResult.TermList loadResult = (CourseResult.TermList) result;
                    Log.i(TAG, "Term Result -> State");
                    switch (loadResult.status()) {
                        case SUCCESS:
                            return stateBuilder.isLoading(false).termList(loadResult.list()).build();
                        case FAILURE:
                            return stateBuilder.isLoading(false).error(loadResult.error()).build();
                        case IN_FLIGHT:
                            return stateBuilder.isLoading(true).build();
                    }
                } else if (result instanceof CourseResult.CourseList) {
                    CourseResult.CourseList loadResult = (CourseResult.CourseList) result;
                    Log.i(TAG, "Course Result -> State");
                    switch (loadResult.status()) {
                        case SUCCESS:
                            return stateBuilder.isLoading(false).courseList(loadResult.list()).build();
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
