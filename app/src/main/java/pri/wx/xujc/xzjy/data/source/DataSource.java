package pri.wx.xujc.xzjy.data.source;

import io.reactivex.Single;
import pri.wx.xujc.xzjy.data.model.StuInfoEntity;
import pri.wx.xujc.xzjy.data.model.TokenModel;
import pri.wx.xujc.xzjy.data.model.User;

public interface DataSource {

    Single<String> getWelcomeImage();

    void saveWelcomeImage(String url);

    Single<User> getUser();

    void saveUser(User user);

    Single<StuInfoEntity> getStuInfo();

    Single<TokenModel> getToken(String sno, String pwd);

    Single<User> refreshToken();
}
