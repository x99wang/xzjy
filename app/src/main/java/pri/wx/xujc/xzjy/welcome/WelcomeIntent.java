package pri.wx.xujc.xzjy.welcome;

import pri.wx.xujc.xzjy.mvibase.MviIntent;
import com.google.auto.value.AutoValue;
public interface WelcomeIntent extends MviIntent {

    @AutoValue
    abstract class ImageIntent implements WelcomeIntent {
       public static ImageIntent create() {
            return new AutoValue_WelcomeIntent_ImageIntent();
        }
    }

    @AutoValue
    abstract class LocalUserIntent implements WelcomeIntent {
       public static LocalUserIntent create() {
            return new AutoValue_WelcomeIntent_LocalUserIntent();
        }
    }
}
