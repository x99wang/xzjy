package pri.wx.xujc.xzjy.home;

import com.google.auto.value.AutoValue;

interface HomeIntent {
    @AutoValue
    abstract class InitIntent implements HomeIntent {
        public static InitIntent create() {
            return new AutoValue_HomeIntent_InitIntent();
        }
    }
}
