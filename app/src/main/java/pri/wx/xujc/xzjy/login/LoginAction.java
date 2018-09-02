package pri.wx.xujc.xzjy.login;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.mvibase.MviAction;

public interface LoginAction extends MviAction {
    @AutoValue
    abstract class Login implements LoginAction {
        abstract String sno();
        abstract String pwd();

        public static Login create(@Nullable String sno,
                                   @Nullable String pwd) {
            return new AutoValue_LoginAction_Login(sno, pwd);
        }
    }
}
