package pri.wx.xujc.xzjy.course;

import android.support.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import pri.wx.xujc.xzjy.data.source.DataSource;
import pri.wx.xujc.xzjy.util.schedulers.BaseSchedulerProvider;

public class CourseProcessorHolder {
    @NonNull
    private DataSource mRepository;

    @NonNull
    private BaseSchedulerProvider mSchedulerProvider;

    public CourseProcessorHolder(@NonNull DataSource mRepository,
                                @NonNull BaseSchedulerProvider mSchedulerProvider) {
        this.mRepository = mRepository;
        this.mSchedulerProvider = mSchedulerProvider;
    }

    private ObservableTransformer<CourseAction.InitTermAction, CourseResult.TermList>
            initTermProcessor = actions ->
            actions.flatMap(action -> mRepository.getTerm()
                    .map(CourseResult.TermList::success)
                    .onErrorReturn(CourseResult.TermList::failure)
                    .toObservable()
                    .subscribeOn(mSchedulerProvider.io())
                    .startWith(CourseResult.TermList.inFlight()));

    private ObservableTransformer<CourseAction.LoadCourseAction, CourseResult.CourseList>
            loadCourseProcessor = actions ->
            actions.flatMap(action -> mRepository.getCourse(action.term())
                    .map(CourseResult.CourseList::success)
                    .onErrorReturn(CourseResult.CourseList::failure)
                    .toObservable()
                    .subscribeOn(mSchedulerProvider.io())
                    .startWith(CourseResult.CourseList.inFlight()));

    ObservableTransformer<CourseAction, CourseResult> actionProcessor =
            actions -> actions.publish(shared -> Observable.merge(
                    shared.ofType(CourseAction.InitTermAction.class)
                    .compose(initTermProcessor)
                    .cast(CourseResult.class),
                    shared.ofType(CourseAction.LoadCourseAction.class)
                    .compose(loadCourseProcessor)
                    .cast(CourseResult.class)
            ));

}
