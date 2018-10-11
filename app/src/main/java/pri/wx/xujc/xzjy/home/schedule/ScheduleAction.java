package pri.wx.xujc.xzjy.home.schedule;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.mvibase.MviAction;

interface ScheduleAction extends MviAction {
    @AutoValue
    abstract class InitScheduleAction implements ScheduleAction {
        public static InitScheduleAction create() {
            return new AutoValue_ScheduleAction_InitScheduleAction();
        }
    }

    @AutoValue
    abstract class InitTermAction implements ScheduleAction {
        public static InitTermAction create() {
            return new AutoValue_ScheduleAction_InitTermAction();
        }
    }

    @AutoValue
    abstract class InitWeekAction implements ScheduleAction {
        public static InitWeekAction create() {
            return new AutoValue_ScheduleAction_InitWeekAction();
        }
    }

    @AutoValue
    abstract class LoadScheduleAction implements ScheduleAction {
        abstract String term();
        public static LoadScheduleAction create(@Nullable String term) {
            return new AutoValue_ScheduleAction_LoadScheduleAction(term);
        }
    }

}
