package pri.wx.xujc.xzjy.home.schedule;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.data.model.Course;
import pri.wx.xujc.xzjy.data.model.Term;
import pri.wx.xujc.xzjy.mvibase.MviViewState;

import java.util.ArrayList;
import java.util.List;

@AutoValue
abstract class ScheduleViewState implements MviViewState {

    abstract boolean isLoading();

    abstract String week();

    abstract List<Term> terms();

    abstract List<Course> course();

    @Nullable
    abstract Throwable error();

    public abstract Builder buildWith();

    static ScheduleViewState idle() {
        return new AutoValue_ScheduleViewState.Builder()
                .isLoading(false)
                .week("0")
                .terms(new ArrayList<>())
                .course(new ArrayList<>(42))
                .error(null)
                .build();
    }

    @AutoValue.Builder
    static abstract class Builder{

        abstract Builder isLoading(boolean isLoading);

        abstract Builder week(String week);

        abstract Builder terms(List<Term> terms);

        abstract Builder course(List<Course> course);

        abstract Builder error(@Nullable Throwable error);

        abstract ScheduleViewState build();
    }

}
