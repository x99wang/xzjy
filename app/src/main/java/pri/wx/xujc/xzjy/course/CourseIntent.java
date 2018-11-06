package pri.wx.xujc.xzjy.course;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.mvibase.MviIntent;

interface CourseIntent extends MviIntent {

    @AutoValue
    abstract class InitIntent implements CourseIntent {
        public static InitIntent create() {
            return new AutoValue_CourseIntent_InitIntent();
        }
    }

    @AutoValue
    abstract class CheckCourseIntent implements CourseIntent {
        @Nullable
        abstract String term();
        public static CheckCourseIntent create(@Nullable String term) {
            return new AutoValue_CourseIntent_CheckCourseIntent(term);
        }
    }


}
