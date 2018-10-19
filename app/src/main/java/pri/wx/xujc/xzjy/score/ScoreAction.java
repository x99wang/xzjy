package pri.wx.xujc.xzjy.score;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.mvibase.MviAction;

interface ScoreAction extends MviAction {
    @AutoValue
    abstract class InitScoreAction implements ScoreAction{
        public static InitScoreAction create(){
            return new AutoValue_ScoreAction_InitScoreAction();
        }
    }

    @AutoValue
    abstract class InitTermAction implements ScoreAction{
        public static InitTermAction create(){
            return new AutoValue_ScoreAction_InitTermAction();
        }
    }

    @AutoValue
    abstract class LoadScoreAction implements ScoreAction {
        abstract String term();
        public static LoadScoreAction create(@Nullable String term) {
            return new AutoValue_ScoreAction_LoadScoreAction(term);
        }
    }
}
