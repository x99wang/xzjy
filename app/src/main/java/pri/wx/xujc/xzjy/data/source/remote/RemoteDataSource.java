package pri.wx.xujc.xzjy.data.source.remote;

import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.Single;
import pri.wx.xujc.xzjy.MyApplication;
import pri.wx.xujc.xzjy.data.model.*;
import pri.wx.xujc.xzjy.data.source.DataSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class RemoteDataSource implements DataSource {

    private static final String TAG = "RemoteData";

//    private static final String API_URL = "http://localhost:8080";

    private static final String API_URL = "http://139.199.153.220";

    private static RemoteDataSource INSTANCE;

    private ApiService api;

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }

    private RemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                //.client()//添加拦截器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //添加自定义格式适配(String)
                //.addConverterFactory(StringConverterFactory.create())
                //如果返回类型为json格式 则需要添加Gson适配
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ApiService.class);
    }

    @Override
    public Single<String> getWelcomeImage() {
        return api.welcomeImage()
                .doOnNext(log -> Log.i(TAG, "Remote Request Welcome Image:" + log.getMessage()))
                .map(result -> result.getResult().getLink())
                .firstOrError()
                ;
    }

    @Override
    public void saveWelcomeImage(String url) { }

    @Override
    public Single<User> getUser() {
        String token = MyApplication.getInstance().getToken();
        return api.info(token)
                .doOnNext(log -> Log.i(TAG,"Token:" + token))
                .map(result -> result.getResult().toUser())
                .doOnNext(user -> user.setToken(token.substring(9)))
                .firstOrError();
    }

    @Override
    public Single<StuInfoEntity> getStuInfo() {
        String token = MyApplication.getInstance().getToken();
        return api.info(token)
                .doOnNext(log -> Log.i(TAG,"Token:" + token))
                .map(result -> result.getResult())
                .firstOrError();
    }

    @Override
    public Single<TokenModel> getToken(String sno, String pwd) {
        return api.login(sno,pwd)
                .doOnNext(log -> Log.i(TAG,"Login:" + log.getMessage()))
                .map(Result::getResult)
                .firstOrError();
    }



    @Override
    public void saveUser(User user) {

    }

    @Override
    public Single<User> refreshToken() {
        String token = MyApplication.getInstance().getToken();
        return api.review(token)
                .doOnNext(log -> Log.i(TAG,"Token:" + token))
                .map(result -> result.getResult().toUser())
                .doOnNext(user -> user.setToken(token.substring(9)))
                .firstOrError();
    }

    @Override
    public Single<List<CourseClass>> getSchedule(String tmId) {
        String token = MyApplication.getInstance().getToken();
        return api.classes(token,tmId)
                .map(Result::getResult)
                .filter(list -> !list.isEmpty())
                .firstOrError();
    }

    @Override
    public Single<List<Term>> getTerm() {
        String token = MyApplication.getInstance().getToken();

        return api.term(token)
                .map(Result::getResult)
                .filter(list -> !list.isEmpty())
                .firstOrError();
    }

    @Override
    public Single<String> getWeek() {
        String token = MyApplication.getInstance().getToken();
        return Single.just("5");
    }

    @Override
    public Single<List<Score>> getScore(String tmId){
        String token = MyApplication.getInstance().getToken();
        return api.score(token,tmId)
                .map(Result::getResult)
                .filter(list -> !list.isEmpty())
                .firstOrError();
    }

    @Override
    public Single<List<Image>> getServiceIcons() {
        List<Image> list = new ArrayList<>();
        return Observable.just(list)
                .firstOrError();
    }

}
