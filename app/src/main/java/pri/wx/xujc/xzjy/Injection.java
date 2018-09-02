package pri.wx.xujc.xzjy;

import android.content.Context;
import pri.wx.xujc.xzjy.data.source.DataRepository;
import pri.wx.xujc.xzjy.data.source.DataSource;
import pri.wx.xujc.xzjy.data.source.local.LocalDataSource;
import pri.wx.xujc.xzjy.data.source.remote.RemoteDataSource;
import pri.wx.xujc.xzjy.util.schedulers.SchedulerProvider;

public class Injection {

    public static DataSource provideWelcomeRepository(Context context) {
        LocalDataSource local = LocalDataSource.getInstance(context);
        RemoteDataSource remote = RemoteDataSource.getInstance();
        return DataRepository.getInstance(local, remote);
    }

    public static SchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

}
