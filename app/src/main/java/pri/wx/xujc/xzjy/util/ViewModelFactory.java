package pri.wx.xujc.xzjy.util;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import pri.wx.xujc.xzjy.Injection;
import pri.wx.xujc.xzjy.home.schedule.ScheduleProcessorHolder;
import pri.wx.xujc.xzjy.home.schedule.ScheduleViewModel;
import pri.wx.xujc.xzjy.home.service.ServiceProcessorHolder;
import pri.wx.xujc.xzjy.home.service.ServiceViewModel;
import pri.wx.xujc.xzjy.login.LoginActionProcessorHolder;
import pri.wx.xujc.xzjy.login.LoginViewModel;
import pri.wx.xujc.xzjy.score.ScoreProcessorHolder;
import pri.wx.xujc.xzjy.score.ScoreViewModel;
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
                            Injection.provideRepository(applicationContext),
                            Injection.provideSchedulerProvider()));
        } else if (modelClass == LoginViewModel.class) {
            return (T) new LoginViewModel(
                    new LoginActionProcessorHolder(
                            Injection.provideRepository(applicationContext),
                            Injection.provideSchedulerProvider()));
        } else if (modelClass == ScheduleViewModel.class) {
            return (T) new ScheduleViewModel(
                    new ScheduleProcessorHolder(
                            Injection.provideRepository(applicationContext),
                            Injection.provideSchedulerProvider()));
        } else if (modelClass == ServiceViewModel.class) {
            return (T) new ServiceViewModel(
                    new ServiceProcessorHolder(
                            Injection.provideRepository(applicationContext),
                            Injection.provideSchedulerProvider()));
        } else if (modelClass == ScoreViewModel.class) {
            return (T) new ScoreViewModel(
                    new ScoreProcessorHolder(
                            Injection.provideRepository(applicationContext),
                            Injection.provideSchedulerProvider()));
        }
        throw new IllegalArgumentException("unknown model class " + modelClass);
    }
}
