package pri.wx.xujc.xzjy.data.source;

import io.reactivex.Single;
import pri.wx.xujc.xzjy.data.model.*;

import java.util.List;

public interface DataSource {

    Single<String> getWelcomeImage();

    void saveWelcomeImage(String url);

    Single<User> getUser();

    void saveUser(User user);

    Single<StuInfoEntity> getStuInfo();

    Single<TokenModel> getToken(String sno, String pwd);

    Single<User> refreshToken();

    Single<List<CourseClass>> getSchedule(String tmId);

    Single<List<Term>> getTerm();

    Single<String> getWeek();

}
