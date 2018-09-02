package pri.wx.xujc.xzjy.welcome;

import android.support.annotation.NonNull;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import pri.wx.xujc.xzjy.MyApplication;
import pri.wx.xujc.xzjy.data.model.User;
import pri.wx.xujc.xzjy.data.source.DataSource;
import pri.wx.xujc.xzjy.util.schedulers.BaseSchedulerProvider;

public class WelcomeActionProcessorHolder {
    private static final String TAG = "WelcomeActionPH";

    @NonNull
    private DataSource mRepository;

    @NonNull
    private BaseSchedulerProvider mSchedulerProvider;

    public WelcomeActionProcessorHolder(@NonNull DataSource mRepository,
                                        @NonNull BaseSchedulerProvider mSchedulerProvider) {
        this.mRepository = mRepository;
        this.mSchedulerProvider = mSchedulerProvider;
    }

    private ObservableTransformer<WelcomeAction.LoadingUrl, WelcomeResult.LoadWelcomeImage>
            loadingBackground = actions ->
            actions.doOnNext(log -> Log.i(TAG, "Loading Background Action ...")).flatMap(action ->
                    mRepository.getWelcomeImage()
                            .map(WelcomeResult.LoadWelcomeImage::success)
                            .doOnSuccess(url -> Log.i(TAG,url.img()))
                            .onErrorReturn(WelcomeResult.LoadWelcomeImage::failure)
                            .toObservable()
                            .subscribeOn(mSchedulerProvider.io())
                            .startWith(WelcomeResult.LoadWelcomeImage.inFlight())
            );

    private ObservableTransformer<WelcomeAction.LoadingUser, WelcomeResult.LoadLocalUser>
            loadingLocalUser = actions ->
            actions.doOnNext(log -> Log.i(TAG, "Loading Local User Action ...")).flatMap(action ->
                    mRepository.getUser()
                            .map(WelcomeResult.LoadLocalUser::success)
                            .doOnSuccess(result -> Log.i(TAG,result.user().getId()))
                            .flatMap(user -> {
                                String id = user.user().getId();
                                String pwd = user.user().getPassword();
                                String token = user.user().getToken();
                                String name = user.user().getName();
                                if(!id.isEmpty() &&
                                        !"cache".equals(id)){
                                    if(!token.isEmpty() &&
                                            (System.currentTimeMillis() - MyApplication.getLastDate())
                                                    /1000/60/60/24 < 24){
                                        // TODO 需要优化 将存储类写到DataSource中操作
                                        MyApplication.getInstance().setToken(id + '_' + token);
                                        //如果token没有过期 则重新激活
                                        return mRepository.refreshToken()
                                                .doOnSuccess(log -> Log.i(TAG,"Token Review Success:"+ log.getId()))
                                                .map(WelcomeResult.LoadLocalUser::success);
                                    }
                                    //否则重新请求token
                                    return mRepository.getToken(id,pwd)
                                            .doOnSuccess(log -> {
                                                MyApplication.getInstance().setToken(log.getUsername() + '_' + log.getToken());
                                                Log.i(TAG,"Token Request Success:"+ log.getToken());
                                            })
                                            .map(result -> new User(id,name,pwd,result.getToken()))
                                            .map(WelcomeResult.LoadLocalUser::success);
                                }
                                return Single.just(user);
                            })
                            .onErrorReturn(WelcomeResult.LoadLocalUser::failure)
                            .toObservable()
                            .subscribeOn(mSchedulerProvider.io())
                            .startWith(WelcomeResult.LoadLocalUser.inFlight())
            );

    ObservableTransformer<WelcomeAction, WelcomeResult> actionProcessor =
            actions -> actions.publish(shared -> Observable.merge(
                    shared.ofType(WelcomeAction.LoadingUrl.class)
                            .doOnNext(log -> Log.i(TAG, "Start Image Action"))
                            .compose(loadingBackground)
                            .doOnNext(log -> Log.i(TAG, "End Image Action -> Return Result:" + log.img()))
                            .cast(WelcomeResult.class),
                    shared.ofType(WelcomeAction.LoadingUser.class)
                            .doOnNext(log -> Log.i(TAG, "Start User Action"))
                            .compose(loadingLocalUser)
                            .doOnNext(log -> Log.i(TAG, "End User Action -> Return Result:" + log.user().getId()))
                            .cast(WelcomeResult.class)
                            .mergeWith(shared.filter(v -> !(v instanceof WelcomeAction.LoadingUrl)
                                    &&  !(v instanceof WelcomeAction.LoadingUser))
                                    .flatMap(w -> Observable.error(
                                            new IllegalArgumentException("Action type" + w))))
                    )
            );

}
