package pri.wx.xujc.xzjy.course;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.data.model.CourseEntity;
import pri.wx.xujc.xzjy.data.model.Term;
import pri.wx.xujc.xzjy.mvibase.MviResult;
import pri.wx.xujc.xzjy.util.LceStatus;

import java.util.List;

interface CourseResult extends MviResult {
    @AutoValue
    abstract class CourseList implements CourseResult {
        @NonNull
        abstract LceStatus status();

        @Nullable
        abstract List<CourseEntity> list();

        @Nullable
        abstract Throwable error();
        @NonNull
        static CourseList success(List<CourseEntity> list) {
            return new AutoValue_CourseResult_CourseList(LceStatus.SUCCESS,list, null);
        }

        @NonNull
        static CourseList failure(Throwable error) {
            return new AutoValue_CourseResult_CourseList(LceStatus.FAILURE,null, error);
        }

        @NonNull
        static CourseList inFlight() {
            return new AutoValue_CourseResult_CourseList(LceStatus.IN_FLIGHT, null,null);
        }
    }

    @AutoValue
    abstract class TermList implements CourseResult {
        @NonNull
        abstract LceStatus status();

        @Nullable
        abstract List<Term> list();

        @Nullable
        abstract Throwable error();
        @NonNull
        static TermList success(List<Term> list) {
            return new AutoValue_CourseResult_TermList(LceStatus.SUCCESS,list, null);
        }

        @NonNull
        static TermList failure(Throwable error) {
            return new AutoValue_CourseResult_TermList(LceStatus.FAILURE,null, error);
        }

        @NonNull
        static TermList inFlight() {
            return new AutoValue_CourseResult_TermList(LceStatus.IN_FLIGHT, null,null);
        }
    }
}
