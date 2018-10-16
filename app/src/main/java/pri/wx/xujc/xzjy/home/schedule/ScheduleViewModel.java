package pri.wx.xujc.xzjy.home.schedule;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;
import io.reactivex.subjects.PublishSubject;
import pri.wx.xujc.xzjy.MyApplication;
import pri.wx.xujc.xzjy.data.model.Course;
import pri.wx.xujc.xzjy.data.model.CourseClass;
import pri.wx.xujc.xzjy.mvibase.MviIntent;
import pri.wx.xujc.xzjy.mvibase.MviViewModel;
import pri.wx.xujc.xzjy.util.MyUtils;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ScheduleViewModel extends ViewModel
        implements MviViewModel<ScheduleIntent, ScheduleViewState> {

    @NonNull
    private PublishSubject<ScheduleIntent> mIntentsSubject;
    @NonNull
    private Observable<ScheduleViewState> mStatesObservable;

    @NonNull
    private ScheduleProcessorHolder mProcessorHolder;

    public ScheduleViewModel(@NonNull ScheduleProcessorHolder mProcessorHolder) {
        this.mProcessorHolder = checkNotNull(mProcessorHolder, "ScheduleProcessorHolder cannot be null");
        mIntentsSubject = PublishSubject.create();
        mStatesObservable = compose();
    }

    @Override
    public void processIntents(Observable<ScheduleIntent> intents) {
        intents.subscribe(mIntentsSubject);
    }

    @Override
    public Observable<ScheduleViewState> states() {
        return mStatesObservable;
    }

    private Observable<ScheduleViewState> compose() {
        return mIntentsSubject
                .compose(intentFilter)
                .map(this::actionFromIntent)
                .compose(mProcessorHolder.actionProcessor)
                .scan(ScheduleViewState.idle(),reducer)
                .replay(1)
                .autoConnect(0);
    }


    private ObservableTransformer<ScheduleIntent, ScheduleIntent> intentFilter =
            intents -> intents.publish(shared ->
                    Observable.merge(
                            shared.ofType(ScheduleIntent.InitScheduleIntent.class).take(1),
                            shared.ofType(ScheduleIntent.InitTermIntent.class).take(1),
                            shared.ofType(ScheduleIntent.InitWeekIntent.class).take(1),
                            shared.ofType(ScheduleIntent.ClassesIntent.class))
            );

    private ScheduleAction actionFromIntent(MviIntent intent) {
        if (intent instanceof ScheduleIntent.ClassesIntent) {
            return ScheduleAction.LoadScheduleAction.create(((ScheduleIntent.ClassesIntent) intent).term());
        } else if (intent instanceof ScheduleIntent.InitScheduleIntent) {
            return ScheduleAction.InitScheduleAction.create();
        } else if (intent instanceof ScheduleIntent.InitTermIntent) {
            return ScheduleAction.InitTermAction.create();
        } else if (intent instanceof ScheduleIntent.InitWeekIntent) {
            return ScheduleAction.InitWeekAction.create();
        }
        throw new IllegalArgumentException("do not know how to treat this intent " + intent);
    }

    private static BiFunction<ScheduleViewState, ScheduleResult, ScheduleViewState> reducer =
            (previousState, result) -> {
                ScheduleViewState.Builder stateBuilder = previousState.buildWith();
                if (result instanceof ScheduleResult.LoadSchedule) {
                    ScheduleResult.LoadSchedule loadResult = (ScheduleResult.LoadSchedule) result;
                    switch (loadResult.status()) {
                        case SUCCESS:
                            List<Course> list = new ArrayList<>(42);
                            for(int i=0;i<42;i++){
                                list.add(null);
                            }
                            for(CourseClass c:loadResult.course()){
                                // 单双每周 起止周
                                int w = Integer.valueOf(MyApplication.getInstance().getWeek());
                                int zc;
                                if(c.getSksdZc().contains("每")) zc = -1;
                                else zc = c.getSksdZc().contains("单") ? 1 : 0;
                                if(w >= c.getSksdQzzS() && w <= c.getSksdQzzE() && (zc<0 || w%2 == zc)){
                                    int s = c.getSksdJcS();
                                    int e = c.getSksdJcE();
                                    // 午课
                                    if(s> 50){
                                        s = 5;
                                        e = 6;
                                    } else if(s > 4){
                                        s += 2;
                                        e += 2;
                                    }
                                    int index = (s+1) / 2 * 7 -7  + MyUtils.getXq(c.getSksdXq()) -1;
                                    list.set(index,new Course(c.getKcMc(),c.getCrMc()));
                                    if(e - s > 2){
                                        list.set((s+3) / 2 * 7 -7 + MyUtils.getXq(c.getSksdXq()) -1,
                                                new Course(c.getKcMc(),c.getCrMc()));
                                    }
                                }
                            }
                            return stateBuilder.isLoading(false)
                                    .course(list)
                                    .build();
                        case FAILURE:
                            return stateBuilder.isLoading(false).error(loadResult.error()).build();
                        case IN_FLIGHT:
                            return stateBuilder.isLoading(true).build();
                    }
                } else if (result instanceof ScheduleResult.LoadTerms) {
                    ScheduleResult.LoadTerms loadResult = (ScheduleResult.LoadTerms) result;
                    switch (loadResult.status()) {
                        case SUCCESS:
                            return stateBuilder.isLoading(false)
                                    .terms(loadResult.termList())
                                    .build();
                        case FAILURE:
                            return stateBuilder.isLoading(false).error(loadResult.error()).build();
                        case IN_FLIGHT:
                            return stateBuilder.isLoading(true).build();
                    }
                } else if (result instanceof ScheduleResult.LoadWeek) {
                    ScheduleResult.LoadWeek loadResult = (ScheduleResult.LoadWeek) result;
                    switch (loadResult.status()) {
                        case SUCCESS:
                            return stateBuilder.isLoading(false)
                                    .week(loadResult.week())
                                    .build();
                        case FAILURE:
                            return stateBuilder.isLoading(false).error(loadResult.error()).build();
                        case IN_FLIGHT:
                            return stateBuilder.isLoading(true).build();
                    }
                } else {
                    throw new IllegalArgumentException("Don't know this result " + result);
                }
                // Fail for unhandled results
                throw new IllegalStateException("Mishandled result? Should not happen (as always)");
            };
}
