package pri.wx.xujc.xzjy.home.service;

import android.support.annotation.NonNull;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import pri.wx.xujc.xzjy.data.source.DataSource;
import pri.wx.xujc.xzjy.util.schedulers.BaseSchedulerProvider;

public class ServiceProcessorHolder {
    private static final String TAG = "ServiceActionPH";

    @NonNull
    private DataSource mRepository;

    @NonNull
    private BaseSchedulerProvider mSchedulerProvider;

    public ServiceProcessorHolder(@NonNull DataSource mRepository,
                                  @NonNull BaseSchedulerProvider mSchedulerProvider) {
        this.mRepository = mRepository;
        this.mSchedulerProvider = mSchedulerProvider;
    }

    private ObservableTransformer<ServiceAction.InitIcon,ServiceResult.ServiceIcons>
            loadIcons = actions ->
            actions.flatMap(user -> mRepository.getServiceIcons()
                    .filter(o -> null != o)
                    .map(ServiceResult.ServiceIcons::success)
                    .onErrorReturn(ServiceResult.ServiceIcons::failure)
                    .toObservable()
                    .subscribeOn(mSchedulerProvider.io())
                    .startWith(ServiceResult.ServiceIcons.inFlight()
                    ));

    ObservableTransformer<ServiceAction, ServiceResult> actionProcessor =
            actions -> actions.publish(shared ->
                    shared.ofType(ServiceAction.InitIcon.class)
                            .compose(loadIcons)
                            .cast(ServiceResult.class)
                            .mergeWith(shared.filter(v -> !(v instanceof ServiceAction.InitIcon))
                                    .flatMap(w -> Observable.error(
                                            new IllegalArgumentException("Action type" + w))))
            );
}
