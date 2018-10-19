package pri.wx.xujc.xzjy.home.service;

import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.mvibase.MviIntent;

interface ServiceIntent extends MviIntent {
    @AutoValue
    abstract class InitIcon implements ServiceIntent{
        public static InitIcon create(){
            return new AutoValue_ServiceIntent_InitIcon();
        }

    }
}
