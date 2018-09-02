package pri.wx.xujc.xzjy.login;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;
import io.reactivex.subjects.PublishSubject;
import pri.wx.xujc.xzjy.mvibase.MviIntent;
import pri.wx.xujc.xzjy.mvibase.MviViewModel;


import static com.google.common.base.Preconditions.checkNotNull;

public class LoginViewModel extends ViewModel implements MviViewModel<LoginIntent,LoginViewState> {

    private static final String TAG = "LoginViewModel";

    @NonNull
    private PublishSubject<LoginIntent> mIntentsSubject;
    @NonNull
    private Observable<LoginViewState> mStatesObservable;

    @NonNull
    private LoginActionProcessorHolder mActionProcessorHolder;

    public LoginViewModel(@NonNull LoginActionProcessorHolder ActionProcessorHolder) {
        this.mActionProcessorHolder = checkNotNull(ActionProcessorHolder, "actionProcessorHolder cannot be null");
        mIntentsSubject = PublishSubject.create();
        mStatesObservable = compose();
    }

    @Override
    public void processIntents(Observable<LoginIntent> intents) {
        intents.subscribe(mIntentsSubject);
    }

    @Override
    public Observable<LoginViewState> states() {
        return mStatesObservable;
    }

    private Observable<LoginViewState> compose() {
        return mIntentsSubject
                .compose(intentFilter)
                .map(this::actionFromIntent)
                .compose(mActionProcessorHolder.actionProcessor)
                .scan(LoginViewState.idle(), reducer)
                .replay(1)
                .autoConnect(0);
    }

    private LoginAction actionFromIntent(MviIntent intent) {
        if (intent instanceof LoginIntent.Login) {
            Log.i(TAG, "Login Intent -> Login Action");
            return LoginAction.Login.create(((LoginIntent.Login) intent).sno(),((LoginIntent.Login) intent).pwd());
        }
        throw new IllegalArgumentException("do not know how to treat this intent " + intent);
    }

    private ObservableTransformer<LoginIntent, LoginIntent> intentFilter =
            intents -> intents.doOnNext(log -> Log.i(TAG, "Start Filter Intent"))
                    .publish(shared ->
                            Observable.merge(shared.ofType(LoginIntent.Login.class),
                                    shared.filter(intent -> !(intent instanceof LoginIntent.Login)))
                    );

    private BiFunction<LoginViewState,LoginResult,LoginViewState> reducer =
            (previousState, result) -> {
                LoginViewState.Builder stateBuilder = previousState.buildWith();
                if (result instanceof LoginResult.LoginUser) {
                    LoginResult.LoginUser loadResult = (LoginResult.LoginUser) result;
                    Log.i(TAG, "Login Result -> State");
                    switch (loadResult.status()) {
                        case SUCCESS:
                            return stateBuilder.isSuccess(true).msg(loadResult.msg()).name(loadResult.user().getName()).build();
                        case FAILURE:
                            return stateBuilder.isSuccess(true).msg("").error(loadResult.error()).build();
                        case IN_FLIGHT:
                            return stateBuilder.isSuccess(false).build();
                    }
                }else {
                    throw new IllegalArgumentException("Don't know this result " + result);
                }
                // Fail for unhandled results
                throw new IllegalStateException("Mishandled result? Should not happen (as always)");
            };
}
