package pri.wx.xujc.xzjy.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.data.model.User;
import pri.wx.xujc.xzjy.mvibase.MviResult;
import pri.wx.xujc.xzjy.util.LceStatus;

import static pri.wx.xujc.xzjy.util.LceStatus.FAILURE;
import static pri.wx.xujc.xzjy.util.LceStatus.IN_FLIGHT;
import static pri.wx.xujc.xzjy.util.LceStatus.SUCCESS;

public interface LoginResult extends MviResult {
    @AutoValue
    abstract class LoginUser implements LoginResult {
        @NonNull
        abstract LceStatus status();

        @Nullable
        abstract String msg();

        @Nullable
        abstract User user();

        @Nullable
        abstract Throwable error();

        @NonNull
        static LoginUser success(@NonNull String msg,@NonNull User user) {
            return new AutoValue_LoginResult_LoginUser(SUCCESS, msg, user, null);
        }
        @NonNull
        static LoginUser failure(Throwable error) {
            return new AutoValue_LoginResult_LoginUser(FAILURE, "Error", null, error);
        }
        @NonNull
        static LoginUser inFlight() {
            return new AutoValue_LoginResult_LoginUser(IN_FLIGHT, "Checking", null, null);
        }
    }
}
