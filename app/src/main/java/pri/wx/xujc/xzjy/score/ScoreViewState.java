package pri.wx.xujc.xzjy.score;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.data.model.Score;
import pri.wx.xujc.xzjy.data.model.Term;
import pri.wx.xujc.xzjy.mvibase.MviViewState;

import java.util.ArrayList;
import java.util.List;

@AutoValue
abstract class ScoreViewState implements MviViewState {
    abstract boolean isLoading();

    @Nullable
    abstract List<Score> scoreList();

    @Nullable
    abstract List<Term> termList();

    @Nullable
    abstract Throwable error();

    public abstract Builder buildWith();

    static ScoreViewState idle() {
        return new AutoValue_ScoreViewState.Builder().isLoading(false)
                .scoreList(new ArrayList<>())
                .termList(new ArrayList<>())
                .error(null)
                .build();
    }

    @AutoValue.Builder
    static abstract class Builder {
        abstract Builder isLoading(boolean isLoading);

        abstract Builder scoreList(@Nullable List<Score> list);

        abstract Builder termList(@Nullable List<Term> list);

        abstract Builder error(@Nullable Throwable error);

        abstract ScoreViewState build();
    }
}
