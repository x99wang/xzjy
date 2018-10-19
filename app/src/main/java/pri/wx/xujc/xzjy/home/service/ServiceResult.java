package pri.wx.xujc.xzjy.home.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.data.model.Image;
import pri.wx.xujc.xzjy.mvibase.MviResult;
import pri.wx.xujc.xzjy.util.LceStatus;

import java.util.ArrayList;
import java.util.List;

import static pri.wx.xujc.xzjy.util.LceStatus.FAILURE;
import static pri.wx.xujc.xzjy.util.LceStatus.IN_FLIGHT;
import static pri.wx.xujc.xzjy.util.LceStatus.SUCCESS;

public interface ServiceResult extends MviResult {
    @AutoValue
    abstract class ServiceIcons implements ServiceResult {
        @NonNull
        abstract LceStatus status();

        @Nullable
        abstract List<Image> icons();

        @Nullable
        abstract Throwable error();

        @NonNull
        static ServiceIcons success(@NonNull List<Image> icons) {
            return new AutoValue_ServiceResult_ServiceIcons(SUCCESS, icons, null);
        }
        @NonNull
        static ServiceIcons failure(Throwable error) {
            return new AutoValue_ServiceResult_ServiceIcons(FAILURE, new ArrayList<>(), error);
        }
        @NonNull
        static ServiceIcons inFlight() {
            return new AutoValue_ServiceResult_ServiceIcons(IN_FLIGHT, new ArrayList<>(), null);
        }
    }

}
