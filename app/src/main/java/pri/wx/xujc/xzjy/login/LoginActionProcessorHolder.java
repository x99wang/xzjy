package pri.wx.xujc.xzjy.login;

import android.support.annotation.NonNull;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import pri.wx.xujc.xzjy.data.model.User;
import pri.wx.xujc.xzjy.data.source.DataSource;
import pri.wx.xujc.xzjy.util.schedulers.BaseSchedulerProvider;

public class LoginActionProcessorHolder {
    private static final String TAG = "LoginActionPH";

    @NonNull
    private DataSource mRepository;

    @NonNull
    private BaseSchedulerProvider mSchedulerProvider;

    public LoginActionProcessorHolder(@NonNull DataSource mRepository,
                                      @NonNull BaseSchedulerProvider mSchedulerProvider) {
        this.mRepository = mRepository;
        this.mSchedulerProvider = mSchedulerProvider;
    }

    private ObservableTransformer<LoginAction.Login, LoginResult.LoginUser>
            doLogin = actions ->
            actions.doOnNext(log -> Log.i(TAG, "Login Action ..."))
                    .doOnNext(user -> Log.i(TAG,"Start Login... " + user.sno() + " + " + user.pwd()))
                    .flatMap(user -> mRepository.getToken(user.sno(),user.pwd())
                            // 获得token之后获取info
                            .map(token -> {
                                if(null != token) return LoginResult.LoginUser.success("登陆成功",
                                        new User(token.getUsername(),"",user.pwd(),token.getToken()));
                                else return LoginResult.LoginUser.success("学号或密码错误",
                                        new User(user.sno(),"",user.pwd(),""));
                            })
                            .flatMap(loginUser -> {
                                if (!loginUser.user().getToken().isEmpty()) {
                                    return mRepository.getStuInfo()
                                            .doOnSuccess(student -> loginUser.user().setName(student.getXjXm()))
                                            .map(student -> loginUser);
                                } else
                                    return Single.just(loginUser);
                            })
                            .doOnSuccess(log -> Log.i(TAG,log.msg()))
                            .onErrorReturn(LoginResult.LoginUser::failure)
                            .toObservable()
                            .subscribeOn(mSchedulerProvider.io())
                            .startWith(LoginResult.LoginUser.inFlight()
                            ));

    ObservableTransformer<LoginAction, LoginResult> actionProcessor =
            actions -> actions.publish(shared ->
                    shared.ofType(LoginAction.Login.class)
                            .doOnNext(log -> Log.i(TAG, "Start Login Action:sno:"+log.sno()))
                            .compose(doLogin)
                            .doOnNext(log -> Log.i(TAG, "End Login Action -> Return Result:" + log.msg()))
                            .cast(LoginResult.class)
                            .mergeWith(shared.filter(v -> !(v instanceof LoginAction.Login))
                                    .flatMap(w -> Observable.error(
                                            new IllegalArgumentException("Action type" + w))))
            );

}
