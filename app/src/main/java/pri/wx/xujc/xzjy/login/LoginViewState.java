package pri.wx.xujc.xzjy.login;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.mvibase.MviViewState;

@AutoValue
abstract class LoginViewState implements MviViewState {

    abstract boolean isSuccess();

    abstract String msg();

    abstract String name();

    @Nullable
    abstract Throwable error();

    public abstract Builder buildWith();

    static LoginViewState idle() {
        return new AutoValue_LoginViewState.Builder()
                .isSuccess(false)
                .msg("")
                .name("")
                .error(null)
                .build();
    }

    @AutoValue.Builder
    static abstract class Builder {
        abstract Builder isSuccess(boolean isSuccess);
        abstract Builder msg(String msg);
        abstract Builder name(String name);
        abstract Builder error(Throwable error);
        abstract LoginViewState build();
    }
}
