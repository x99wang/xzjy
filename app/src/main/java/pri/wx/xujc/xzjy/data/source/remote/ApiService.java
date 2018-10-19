package pri.wx.xujc.xzjy.data.source.remote;

import io.reactivex.Observable;
import pri.wx.xujc.xzjy.data.model.*;
import retrofit2.http.*;

import java.util.List;

public interface ApiService {
    String AUTH = "Authorization";



    @GET("/xzjy-api/bgimg")
    Observable<Result<Image>> welcomeImage();

    @POST("/xzjy-api/v1/user")
    @FormUrlEncoded
    Observable<Result<TokenModel>> login(@Field("sno") String sno, @Field("pwd") String pwd);

    @GET("/xzjy-api/v1/user")
    Observable<Result<StuInfoEntity>> info(@Header(AUTH) String token);

    @GET("/xzjy-api/account")
    Observable<Result<Account>> review(@Header(AUTH) String token);

    @GET("/xzjy-api/class/{tm_id}")
    Observable<Result<List<CourseClass>>> classes(@Header(AUTH) String token,
                                                  @Path("tm_id") String tmId);

    @GET("/xzjy-api/coursename/{kcb_id}")
    Observable<Result<String>> courseName(@Header(AUTH) String token,
                                          @Path("kcb_id") String id);

    @GET("/xzjy-api/term")
    Observable<Result<List<Term>>> term(@Header(AUTH) String token);


    @GET("/xzjy-api/score/{tm_id}")
    Observable<Result<List<Score>>> score(@Header(AUTH) String token,@Path("tm_id") String tmId);
}
