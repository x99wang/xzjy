package pri.wx.xujc.xzjy.home.service;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;
import io.reactivex.subjects.PublishSubject;
import pri.wx.xujc.xzjy.mvibase.MviIntent;
import pri.wx.xujc.xzjy.mvibase.MviViewModel;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class ServiceViewModel extends ViewModel implements MviViewModel<ServiceIntent,ServiceViewState> {
    public static final String TAG = "ServiceViewModel";

    @NonNull
    private PublishSubject<ServiceIntent> mIntentsSubject;
    @NonNull
    private Observable<ServiceViewState> mStatesObservable;

    @NonNull
    private ServiceProcessorHolder mProcessorHolder;

    public ServiceViewModel(@NonNull ServiceProcessorHolder mProcessorHolder) {
        this.mProcessorHolder = checkNotNull(mProcessorHolder, "ServiceProcessorHolder cannot be null");

        mIntentsSubject = PublishSubject.create();
        mStatesObservable = compose();
    }

    @Override
    public void processIntents(Observable<ServiceIntent> intents) {
        intents.subscribe(mIntentsSubject);
    }

    @Override
    public Observable<ServiceViewState> states() {
        return mStatesObservable;
    }
    
    private Observable<ServiceViewState> compose(){

        return mIntentsSubject
                .compose(intentFilter)
                .map(this::actionFromIntent)
                .compose(mProcessorHolder.actionProcessor)
                .scan(ServiceViewState.idle(),reducer)
                .replay(1)
                .autoConnect(0);
    }

    private ServiceAction actionFromIntent(MviIntent intent) {
        if (intent instanceof ServiceIntent.InitIcon) {
            Log.i(TAG, "Service Intent -> Service Action");
            return ServiceAction.InitIcon.create();
        }
        throw new IllegalArgumentException("do not know how to treat this intent " + intent);
    }

    private ObservableTransformer<ServiceIntent, ServiceIntent> intentFilter =
            intents -> intents.doOnNext(log -> Log.i(TAG, "Start Filter Intent"))
                    .publish(shared ->
                            Observable.merge(shared.ofType(ServiceIntent.InitIcon.class),
                                    shared.filter(intent -> !(intent instanceof ServiceIntent.InitIcon)))
                    );
    
    private BiFunction<ServiceViewState,ServiceResult,ServiceViewState> reducer =
            (previousState, result) -> {
                ServiceViewState.Builder stateBuilder = previousState.buildWith();
                if (result instanceof ServiceResult.ServiceIcons) {
                    ServiceResult.ServiceIcons loadResult = (ServiceResult.ServiceIcons) result;
                    Log.i(TAG, "ServiceIcons Result -> State");
                    switch (loadResult.status()) {
                        case SUCCESS:
                            return stateBuilder.isLoading(false).isSuccess(true).icons(loadResult.icons()).build();
                        case FAILURE:
                            return stateBuilder.isLoading(false).isSuccess(false).icons(new ArrayList<>()).error(loadResult.error()).build();
                        case IN_FLIGHT:
                            return stateBuilder.isLoading(true).isSuccess(false).build();
                    }
                }else {
                    throw new IllegalArgumentException("Don't know this result " + result);
                }
                // Fail for unhandled results
                throw new IllegalStateException("Mishandled result? Should not happen (as always)");
            };
}
