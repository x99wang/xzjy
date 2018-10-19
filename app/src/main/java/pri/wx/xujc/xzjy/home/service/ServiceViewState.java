package pri.wx.xujc.xzjy.home.service;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.data.model.Image;
import pri.wx.xujc.xzjy.mvibase.MviViewState;

import java.util.ArrayList;
import java.util.List;

@AutoValue
abstract class ServiceViewState implements MviViewState {
    abstract boolean isLoading();

    abstract boolean isSuccess();

    abstract List<Image> icons();

    @Nullable
    abstract Throwable error();

    public abstract Builder buildWith();

    static ServiceViewState idle() {
        return new AutoValue_ServiceViewState.Builder()
                .isLoading(false)
                .isSuccess(false)
                .icons(new ArrayList<>())
                .error(null)
                .build();
    }

    @AutoValue.Builder
    static abstract class Builder {
        abstract Builder isLoading(boolean isLoading);
        abstract Builder isSuccess(boolean isSuccess);
        abstract Builder icons(List<Image> icons);
        abstract Builder error(Throwable error);
        abstract ServiceViewState build();
    }
}
