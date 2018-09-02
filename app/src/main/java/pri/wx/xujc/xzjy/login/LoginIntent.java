package pri.wx.xujc.xzjy.login;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.mvibase.MviIntent;

public interface LoginIntent extends MviIntent {
    @AutoValue
    abstract class Login implements LoginIntent {
        @Nullable
        abstract String sno();
        @Nullable
        abstract String pwd();
        public static Login create(@Nullable String sno,
                                   @Nullable String pwd) {
            return new AutoValue_LoginIntent_Login(sno, pwd);
        }
    }
}
