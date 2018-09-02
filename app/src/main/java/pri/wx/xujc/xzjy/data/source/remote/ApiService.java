package pri.wx.xujc.xzjy.data.source.remote;

import io.reactivex.Observable;
import pri.wx.xujc.xzjy.MyApplication;
import pri.wx.xujc.xzjy.data.model.*;
import retrofit2.http.*;

public interface ApiService {




    @GET("/xzjy-api/bgimg")
    Observable<Result<Image>> welcomeImage();

    @POST("/xzjy-api/v1/user")
    @FormUrlEncoded
    Observable<Result<TokenModel>> login(@Field("sno") String sno, @Field("pwd") String pwd);

    @GET("/xzjy-api/v1/user")
    Observable<Result<StuInfoEntity>> info(@Header("Authorization") String token);

    @GET("/xzjy-api/account")
    Observable<Result<Account>> review(@Header("Authorization") String token);

}
