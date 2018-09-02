package pri.wx.xujc.xzjy.welcome;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.squareup.picasso.RequestCreator;
import pri.wx.xujc.xzjy.data.model.User;
import pri.wx.xujc.xzjy.mvibase.MviResult;
import pri.wx.xujc.xzjy.util.LceStatus;

interface WelcomeResult extends MviResult {
    @AutoValue
    abstract class LoadWelcomeImage implements WelcomeResult{
        @NonNull
        abstract LceStatus status();

        @Nullable
        abstract String img();

        @Nullable
        abstract Throwable error();
        @NonNull
        static LoadWelcomeImage success(String img) {
            return new AutoValue_WelcomeResult_LoadWelcomeImage(LceStatus.SUCCESS,img, null);
        }

        @NonNull
        static LoadWelcomeImage failure(Throwable error) {
            return new AutoValue_WelcomeResult_LoadWelcomeImage(LceStatus.FAILURE,null, error);
        }

        @NonNull
        static LoadWelcomeImage inFlight() {
            return new AutoValue_WelcomeResult_LoadWelcomeImage(LceStatus.IN_FLIGHT, null,null);
        }
    }

    @AutoValue
    abstract class LoadLocalUser implements WelcomeResult{
        @NonNull
        abstract LceStatus status();

        @Nullable
        abstract User user();

        @Nullable
        abstract Throwable error();

        @NonNull
        static LoadLocalUser success(User user) {
            return new AutoValue_WelcomeResult_LoadLocalUser(LceStatus.SUCCESS,user, null);
        }

        @NonNull
        static LoadLocalUser failure(Throwable error) {
            return new AutoValue_WelcomeResult_LoadLocalUser(LceStatus.FAILURE,new User(), error);
        }

        @NonNull
        static LoadLocalUser inFlight() {
            return new AutoValue_WelcomeResult_LoadLocalUser(LceStatus.IN_FLIGHT, new User("cache","cache"),null);
        }
    }

}
