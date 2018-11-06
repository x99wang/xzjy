package pri.wx.xujc.xzjy.course;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.data.model.CourseEntity;
import pri.wx.xujc.xzjy.data.model.Term;
import pri.wx.xujc.xzjy.mvibase.MviViewState;

import java.util.ArrayList;
import java.util.List;

@AutoValue
abstract class CourseViewState implements MviViewState {
    abstract boolean isLoading();

    @Nullable
    abstract List<CourseEntity> courseList();

    @Nullable
    abstract List<Term> termList();

    @Nullable
    abstract Throwable error();

    public abstract Builder buildWith();

    static CourseViewState idle() {
        return new AutoValue_CourseViewState.Builder().isLoading(false)
                .courseList(new ArrayList<>())
                .termList(new ArrayList<>())
                .error(null)
                .build();
    }

    @AutoValue.Builder
    static abstract class Builder {
        abstract Builder isLoading(boolean isLoading);

        abstract Builder courseList(@Nullable List<CourseEntity> list);

        abstract Builder termList(@Nullable List<Term> list);

        abstract Builder error(@Nullable Throwable error);

        abstract CourseViewState build();
    }
}
