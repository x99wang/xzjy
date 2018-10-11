package pri.wx.xujc.xzjy.home;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;

@AutoValue
abstract class HomeViewState {
    abstract boolean isNewVersion();

    @Nullable
    abstract String url();

    @Nullable
    abstract Throwable error();

    public abstract Builder buildWith();

    static HomeViewState idle() {
        return new AutoValue_HomeViewState.Builder()
                .isNewVersion(false)
                .url(null)
                .error(null)
                .build();
    }


    @AutoValue.Builder
    static abstract class Builder {
        abstract Builder isNewVersion(boolean isNewVersion);

        abstract Builder url(@Nullable String url);

        abstract Builder error(@Nullable Throwable error);

        abstract HomeViewState build();
    }
}
