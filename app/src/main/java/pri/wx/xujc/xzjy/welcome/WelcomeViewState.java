package pri.wx.xujc.xzjy.welcome;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.data.model.User;
import pri.wx.xujc.xzjy.mvibase.MviViewState;

@AutoValue
abstract class WelcomeViewState implements MviViewState {
    abstract boolean isLoading();

    @Nullable
    abstract String image();

    @Nullable
    abstract User user();

    @Nullable
    abstract Throwable error();

    public abstract Builder buildWith();

    static WelcomeViewState idle() {
        return new AutoValue_WelcomeViewState.Builder().isLoading(false)
                .image("")
                .user(new User("cache","cache"))
                .error(null)
                .build();
    }

    @AutoValue.Builder
    static abstract class Builder {
        abstract Builder isLoading(boolean isLoading);

        abstract Builder image(@Nullable String image);

        abstract Builder user(@Nullable User user);

        abstract Builder error(@Nullable Throwable error);

        abstract WelcomeViewState build();
    }
}
