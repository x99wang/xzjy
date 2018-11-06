package pri.wx.xujc.xzjy.course;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.mvibase.MviAction;

interface CourseAction extends MviAction {
    @AutoValue
    abstract class InitCourseAction implements CourseAction {
        public static InitCourseAction create(){
            return new AutoValue_CourseAction_InitCourseAction();
        }
    }

    @AutoValue
    abstract class InitTermAction implements CourseAction {
        public static InitTermAction create(){
            return new AutoValue_CourseAction_InitTermAction();
        }
    }

    @AutoValue
    abstract class LoadCourseAction implements CourseAction {
        abstract String term();
        public static LoadCourseAction create(@Nullable String term) {
            return new AutoValue_CourseAction_LoadCourseAction(term);
        }
    }
}
