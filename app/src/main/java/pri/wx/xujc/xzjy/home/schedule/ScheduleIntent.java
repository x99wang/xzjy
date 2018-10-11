package pri.wx.xujc.xzjy.home.schedule;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.mvibase.MviIntent;

interface ScheduleIntent extends MviIntent {

    @AutoValue
    abstract class InitScheduleIntent implements ScheduleIntent {
        public static InitScheduleIntent create() {
            return new AutoValue_ScheduleIntent_InitScheduleIntent();
        }
    }

    @AutoValue
    abstract class InitTermIntent implements ScheduleIntent {
        public static InitTermIntent create() {
            return new AutoValue_ScheduleIntent_InitTermIntent();
        }
    }

    @AutoValue
    abstract class InitWeekIntent implements ScheduleIntent {
        public static InitWeekIntent create() {
            return new AutoValue_ScheduleIntent_InitWeekIntent();
        }
    }

    @AutoValue
    abstract class ClassesIntent implements ScheduleIntent {
        @Nullable
        abstract String term();
        public static ClassesIntent create(@Nullable String term) {
            return new AutoValue_ScheduleIntent_ClassesIntent(term);
        }
    }
}
