package pri.wx.xujc.xzjy.util;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import pri.wx.xujc.xzjy.Injection;
import pri.wx.xujc.xzjy.home.schedule.ScheduleProcessorHolder;
import pri.wx.xujc.xzjy.home.schedule.ScheduleViewModel;
import pri.wx.xujc.xzjy.login.LoginActionProcessorHolder;
import pri.wx.xujc.xzjy.login.LoginViewModel;
import pri.wx.xujc.xzjy.welcome.WelcomeActionProcessorHolder;
import pri.wx.xujc.xzjy.welcome.WelcomeViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    @SuppressLint("StaticFieldLeak")
    private static ViewModelFactory INSTANCE;

    private final Context applicationContext;

    private ViewModelFactory(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static ViewModelFactory getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ViewModelFactory(context.getApplicationContext());
        }
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass == WelcomeViewModel.class) {
            return (T) new WelcomeViewModel(
                    new WelcomeActionProcessorHolder(
                            Injection.provideWelcomeRepository(applicationContext),
                            Injection.provideSchedulerProvider()));
        } else if (modelClass == LoginViewModel.class) {
            return (T) new LoginViewModel(
                    new LoginActionProcessorHolder(
                            Injection.provideWelcomeRepository(applicationContext),
                            Injection.provideSchedulerProvider()));
        } else if (modelClass == ScheduleViewModel.class) {
            return (T) new ScheduleViewModel(
                    new ScheduleProcessorHolder(
                            Injection.provideWelcomeRepository(applicationContext),
                            Injection.provideSchedulerProvider()));
        }
        throw new IllegalArgumentException("unknown model class " + modelClass);
    }
}
