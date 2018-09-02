package pri.wx.xujc.xzjy.welcome;

import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.mvibase.MviAction;

public interface WelcomeAction  extends MviAction {

    @AutoValue
    abstract class LoadingUrl implements WelcomeAction {
        public static LoadingUrl create(){
            return new AutoValue_WelcomeAction_LoadingUrl();
        }
    }

    @AutoValue
    abstract class LoadingUser implements WelcomeAction {
        public static LoadingUser create(){
            return new AutoValue_WelcomeAction_LoadingUser();
        }
    }


}
