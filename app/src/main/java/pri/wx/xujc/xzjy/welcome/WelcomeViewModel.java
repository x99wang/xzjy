package pri.wx.xujc.xzjy.welcome;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;
import io.reactivex.subjects.PublishSubject;

import pri.wx.xujc.xzjy.data.model.User;
import pri.wx.xujc.xzjy.mvibase.MviIntent;
import pri.wx.xujc.xzjy.mvibase.MviViewModel;

import static com.google.common.base.Preconditions.checkNotNull;

public class WelcomeViewModel extends ViewModel implements MviViewModel<WelcomeIntent, WelcomeViewState> {

    private static final String TAG = "WelcomeViewModel";

    @NonNull
    private PublishSubject<WelcomeIntent> mIntentsSubject;
    @NonNull
    private Observable<WelcomeViewState> mStatesObservable;
    @NonNull
    private WelcomeActionProcessorHolder mActionProcessorHolder;

    public WelcomeViewModel(@NonNull WelcomeActionProcessorHolder ActionProcessorHolder) {
        this.mActionProcessorHolder = checkNotNull(ActionProcessorHolder, "actionProcessorHolder cannot be null");
        mIntentsSubject = PublishSubject.create();
        mStatesObservable = compose();
    }

    @Override
    public void processIntents(Observable<WelcomeIntent> intents) {
        Log.i(TAG, "Intent订阅");
        intents.subscribe(mIntentsSubject);
    }

    @Override
    public Observable<WelcomeViewState> states() {
        return mStatesObservable;
    }

    private Observable<WelcomeViewState> compose() {
        Log.i(TAG, "Start Compose");
        return mIntentsSubject
                .compose(intentFilter)
                .map(this::actionFromIntent)
                .compose(mActionProcessorHolder.actionProcessor)
                .scan(WelcomeViewState.idle(), reducer)
                .replay(1)
                .autoConnect(0);
    }

    private ObservableTransformer<WelcomeIntent, WelcomeIntent> intentFilter =
            intents -> intents.doOnNext(log -> Log.i(TAG, "Start Filter Intent"))
                    .publish(shared ->
                            Observable.merge(
                                    shared.ofType(WelcomeIntent.ImageIntent.class),
                                    shared.ofType(WelcomeIntent.LocalUserIntent.class),
                                    shared.filter(intent -> !(intent instanceof WelcomeIntent.ImageIntent)
                                            && !(intent instanceof WelcomeIntent.LocalUserIntent))
                            )
                    );

    private WelcomeAction actionFromIntent(MviIntent intent) {
        Log.i(TAG, "Start Intent -> Action");
        if (intent instanceof WelcomeIntent.ImageIntent) {
            Log.i(TAG, "Image Intent -> Image Action");
            return WelcomeAction.LoadingUrl.create();
        }else if (intent instanceof WelcomeIntent.LocalUserIntent) {
            Log.i(TAG, "Local User Intent -> Local User Action");
            return WelcomeAction.LoadingUser.create();
        }
        throw new IllegalArgumentException("do not know how to treat this intent " + intent);
    }

    private static BiFunction<WelcomeViewState, WelcomeResult, WelcomeViewState> reducer =
            (previousState, result) -> {
                WelcomeViewState.Builder stateBuilder = previousState.buildWith();
                if (result instanceof WelcomeResult.LoadWelcomeImage) {
                    WelcomeResult.LoadWelcomeImage loadResult = (WelcomeResult.LoadWelcomeImage) result;
                    stateBuilder.user(null).image("");
                    Log.i(TAG, "Image Result -> State");
                    switch (loadResult.status()) {
                        case SUCCESS:
                            return stateBuilder.isLoading(false).image(loadResult.img()).build();
                        case FAILURE:
                            return stateBuilder.isLoading(false).error(loadResult.error()).build();
                        case IN_FLIGHT:
                            return stateBuilder.isLoading(true).build();
                    }
                }else if(result instanceof WelcomeResult.LoadLocalUser){
                    WelcomeResult.LoadLocalUser loadResult = (WelcomeResult.LoadLocalUser) result;
                    stateBuilder.image(null).user(new User());
                    Log.i(TAG, "User Result -> State");
                    switch (loadResult.status()) {
                        case SUCCESS:
                            return stateBuilder.isLoading(false).user(loadResult.user()).build();
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
