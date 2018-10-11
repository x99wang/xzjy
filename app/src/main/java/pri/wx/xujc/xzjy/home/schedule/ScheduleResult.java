package pri.wx.xujc.xzjy.home.schedule;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.data.model.CourseClass;
import pri.wx.xujc.xzjy.data.model.Term;
import pri.wx.xujc.xzjy.mvibase.MviResult;
import pri.wx.xujc.xzjy.util.LceStatus;

import java.util.ArrayList;
import java.util.List;

interface ScheduleResult extends MviResult {
    @AutoValue
    abstract class LoadSchedule implements ScheduleResult {
        @NonNull
        abstract LceStatus status();

        abstract List<CourseClass> course();

        @Nullable
        abstract Throwable error();

        @NonNull
        static LoadSchedule success(List<CourseClass> list) {
            return new AutoValue_ScheduleResult_LoadSchedule(LceStatus.SUCCESS, list, null);
        }

        @NonNull
        static LoadSchedule failure(Throwable error) {
            return new AutoValue_ScheduleResult_LoadSchedule(LceStatus.FAILURE, new ArrayList<>(), error);
        }

        @NonNull
        static LoadSchedule inFlight() {
            return new AutoValue_ScheduleResult_LoadSchedule(LceStatus.IN_FLIGHT, new ArrayList<>(), null);
        }
    }

    @AutoValue
    abstract class LoadTerms implements ScheduleResult {
        @NonNull
        abstract LceStatus status();

        abstract List<Term> termList();

        @Nullable
        abstract Throwable error();

        @NonNull
        static LoadTerms success(List<Term> list) {
            return new AutoValue_ScheduleResult_LoadTerms(LceStatus.SUCCESS, list, null);
        }

        @NonNull
        static LoadTerms failure(Throwable error) {
            return new AutoValue_ScheduleResult_LoadTerms(LceStatus.FAILURE, new ArrayList<>(), error);
        }

        @NonNull
        static LoadTerms inFlight() {
            return new AutoValue_ScheduleResult_LoadTerms(LceStatus.IN_FLIGHT, new ArrayList<>(), null);
        }
    }

    @AutoValue
    abstract class LoadWeek implements ScheduleResult {
        @NonNull
        abstract LceStatus status();

        abstract String week();

        @Nullable
        abstract Throwable error();

        @NonNull
        static LoadWeek success(String list) {
            return new AutoValue_ScheduleResult_LoadWeek(LceStatus.SUCCESS, list, null);
        }

        @NonNull
        static LoadWeek failure(Throwable error) {
            return new AutoValue_ScheduleResult_LoadWeek(LceStatus.FAILURE, "0", error);
        }

        @NonNull
        static LoadWeek inFlight() {
            return new AutoValue_ScheduleResult_LoadWeek(LceStatus.IN_FLIGHT, "0", null);
        }
    }
}
