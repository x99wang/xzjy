package pri.wx.xujc.xzjy.score;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.mvibase.MviIntent;

interface ScoreIntent extends MviIntent {

    @AutoValue
    abstract class InitIntent implements ScoreIntent {
        public static InitIntent create() {
            return new AutoValue_ScoreIntent_InitIntent();
        }
    }

    @AutoValue
    abstract class CheckScoreIntent implements ScoreIntent {
        @Nullable
        abstract String term();
        public static CheckScoreIntent create(@Nullable String term) {
            return new AutoValue_ScoreIntent_CheckScoreIntent(term);
        }
    }


}
