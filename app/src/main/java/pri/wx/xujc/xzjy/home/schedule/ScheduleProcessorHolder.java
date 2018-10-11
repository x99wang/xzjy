package pri.wx.xujc.xzjy.home.schedule;

import android.support.annotation.NonNull;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import pri.wx.xujc.xzjy.MyApplication;
import pri.wx.xujc.xzjy.data.source.DataSource;
import pri.wx.xujc.xzjy.util.schedulers.BaseSchedulerProvider;


public class ScheduleProcessorHolder {
    private static final String TAG = "ScheduleActionPH";

    @NonNull
    private DataSource mRepository;
    @NonNull
    private BaseSchedulerProvider mSchedulerProvider;

    public ScheduleProcessorHolder(@NonNull DataSource mRepository, @NonNull BaseSchedulerProvider mSchedulerProvider) {
        this.mRepository = mRepository;
        this.mSchedulerProvider = mSchedulerProvider;
    }

    private ObservableTransformer<ScheduleAction.LoadScheduleAction, ScheduleResult.LoadSchedule>
            loadScheduleProcessor = actions ->
            actions.flatMap(action -> mRepository.getSchedule(action.term())
                    .map(ScheduleResult.LoadSchedule::success)
                    .onErrorReturn(ScheduleResult.LoadSchedule::failure)
                    .toObservable()
                    .subscribeOn(mSchedulerProvider.io())
                    .startWith(ScheduleResult.LoadSchedule.inFlight()));

    private ObservableTransformer<ScheduleAction.InitScheduleAction, ScheduleResult.LoadSchedule>
            initScheduleProcessor = actions ->
            actions.flatMap(action -> mRepository.getSchedule(MyApplication.getTerm())
                    .map(ScheduleResult.LoadSchedule::success)
                    .onErrorReturn(ScheduleResult.LoadSchedule::failure)
                    .toObservable()
                    .subscribeOn(mSchedulerProvider.io())
                    .startWith(ScheduleResult.LoadSchedule.inFlight()));

    private ObservableTransformer<ScheduleAction.InitTermAction, ScheduleResult.LoadTerms>
            initTermProcessor = actions ->
            actions.flatMap(action -> mRepository.getTerm()
                    .map(ScheduleResult.LoadTerms::success)
                    .onErrorReturn(ScheduleResult.LoadTerms::failure)
                    .toObservable()
                    .subscribeOn(mSchedulerProvider.io())
                    .startWith(ScheduleResult.LoadTerms.inFlight()));

    private ObservableTransformer<ScheduleAction.InitWeekAction, ScheduleResult.LoadWeek>
            initWeekProcessor = actions ->
            actions.flatMap(action -> mRepository.getWeek()
                    .map(ScheduleResult.LoadWeek::success)
                    .onErrorReturn(ScheduleResult.LoadWeek::failure)
                    .toObservable()
                    .subscribeOn(mSchedulerProvider.io())
                    .startWith(ScheduleResult.LoadWeek.inFlight()));

    ObservableTransformer<ScheduleAction, ScheduleResult> actionProcessor =
            actions -> actions.publish(shared -> Observable.merge(
                    // 初始课表
                    shared.ofType(ScheduleAction.InitScheduleAction.class)
                            .doOnNext(log -> Log.i(TAG, "Start InitSchedule Action"))
                            .compose(initScheduleProcessor)
                            .doOnNext(log -> Log.i(TAG, "End InitSchedule Action -> Class Size:" + log.course().size()))
                            .cast(ScheduleResult.class),
                    // 学期列表
                    shared.ofType(ScheduleAction.InitTermAction.class)
                            .doOnNext(log -> Log.i(TAG, "Start InitTerm Action"))
                            .compose(initTermProcessor)
                            .doOnNext(log -> Log.i(TAG, "End InitTerm Action -> Class Size:" + log.termList().size()))
                            .cast(ScheduleResult.class),
                    // 最新周数
                    shared.ofType(ScheduleAction.InitWeekAction.class)
                            .doOnNext(log -> Log.i(TAG, "Start InitWeek Action"))
                            .compose(initWeekProcessor)
                            .doOnNext(log -> Log.i(TAG, "End InitWeek Action -> Class Size:" + log.week()))
                            .cast(ScheduleResult.class),
                    // 选择学期更新课表
                    shared.ofType(ScheduleAction.LoadScheduleAction.class)
                            .doOnNext(log -> Log.i(TAG,"Start LoadSchedule" + log.term()))
                            .compose(loadScheduleProcessor)
                            .cast(ScheduleResult.class)))
            .observeOn(mSchedulerProvider.ui());
}
