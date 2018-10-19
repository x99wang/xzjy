package pri.wx.xujc.xzjy.home.service;

import com.google.auto.value.AutoValue;
import pri.wx.xujc.xzjy.mvibase.MviAction;

interface ServiceAction extends MviAction {
    @AutoValue
    abstract class InitIcon implements ServiceAction{
        public static InitIcon create(){
            return new AutoValue_ServiceAction_InitIcon();
        }
    }


}
