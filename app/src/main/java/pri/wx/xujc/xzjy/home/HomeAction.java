package pri.wx.xujc.xzjy.home;

import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.mvibase.MviAction;

interface HomeAction extends MviAction {
    @AutoValue
    abstract class RemoteVersion implements HomeAction {
        public static RemoteVersion create() {
            return new AutoValue_HomeAction_RemoteVersion();
        }
    }
}
