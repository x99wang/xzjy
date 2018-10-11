package pri.wx.xujc.xzjy.home;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.data.model.VersionInfo;
import pri.wx.xujc.xzjy.mvibase.MviResult;
import pri.wx.xujc.xzjy.util.LceStatus;

interface HomeResult extends MviResult {
    @AutoValue
    abstract class VersionResult implements HomeResult {
        @NonNull
        abstract LceStatus status();

        @Nullable
        abstract VersionInfo versionInfo();

        @Nullable
        abstract Throwable error();

        @NonNull
        static VersionResult success(VersionInfo info) {
            return new AutoValue_HomeResult_VersionResult((LceStatus.SUCCESS), info, null);
        }

        @NonNull
        static VersionResult failure(Throwable error) {
            return new AutoValue_HomeResult_VersionResult((LceStatus.FAILURE), null, error);
        }

        @NonNull
        static VersionResult inFlight() {
            return new AutoValue_HomeResult_VersionResult((LceStatus.IN_FLIGHT), null, null);
        }

    }
}
