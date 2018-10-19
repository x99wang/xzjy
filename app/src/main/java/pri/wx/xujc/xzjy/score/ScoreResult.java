package pri.wx.xujc.xzjy.score;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.data.model.Score;
import pri.wx.xujc.xzjy.data.model.Term;
import pri.wx.xujc.xzjy.mvibase.MviResult;
import pri.wx.xujc.xzjy.util.LceStatus;

import java.util.List;

interface ScoreResult extends MviResult {
    @AutoValue
    abstract class ScoreList implements ScoreResult{
        @NonNull
        abstract LceStatus status();

        @Nullable
        abstract List<Score> list();

        @Nullable
        abstract Throwable error();
        @NonNull
        static ScoreList success(List<Score> list) {
            return new AutoValue_ScoreResult_ScoreList(LceStatus.SUCCESS,list, null);
        }

        @NonNull
        static ScoreList failure(Throwable error) {
            return new AutoValue_ScoreResult_ScoreList(LceStatus.FAILURE,null, error);
        }

        @NonNull
        static ScoreList inFlight() {
            return new AutoValue_ScoreResult_ScoreList(LceStatus.IN_FLIGHT, null,null);
        }
    }

    @AutoValue
    abstract class TermList implements ScoreResult{
        @NonNull
        abstract LceStatus status();

        @Nullable
        abstract List<Term> list();

        @Nullable
        abstract Throwable error();
        @NonNull
        static TermList success(List<Term> list) {
            return new AutoValue_ScoreResult_TermList(LceStatus.SUCCESS,list, null);
        }

        @NonNull
        static TermList failure(Throwable error) {
            return new AutoValue_ScoreResult_TermList(LceStatus.FAILURE,null, error);
        }

        @NonNull
        static TermList inFlight() {
            return new AutoValue_ScoreResult_TermList(LceStatus.IN_FLIGHT, null,null);
        }
    }
}
