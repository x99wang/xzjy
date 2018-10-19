package pri.wx.xujc.xzjy.welcome;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import android.widget.Toast;
import com.squareup.picasso.Picasso;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import pri.wx.xujc.xzjy.R;
import pri.wx.xujc.xzjy.home.HomeActivity;
import pri.wx.xujc.xzjy.login.LoginActivity;
import pri.wx.xujc.xzjy.mvibase.MviView;
import pri.wx.xujc.xzjy.util.ViewModelFactory;
import pri.wx.xujc.xzjy.util.schedulers.SchedulerProvider;

import java.util.concurrent.TimeUnit;

public class WelcomeActivity extends AppCompatActivity
        implements MviView<WelcomeIntent,WelcomeViewState>,LifecycleRegistryOwner {

    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    private WelcomeViewModel mViewModel;

    private CompositeDisposable mDisposables;

    private ImageView imgWelcome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(this))
                .get(WelcomeViewModel.class);

        mDisposables = new CompositeDisposable();

        imgWelcome = findViewById(R.id.img_welcome);

        bind();
    }

    private void bind() {
        mDisposables.add(mViewModel.states()
                .observeOn(SchedulerProvider.getInstance().ui())
                .subscribe(this::render));
        mViewModel.processIntents(intents());
    }

    @Override
    public Observable<WelcomeIntent> intents() {
        return Observable.concat(initialIntent(), loadUserIntent());
    }

    private Observable<WelcomeIntent> initialIntent() {
        return Observable.just(WelcomeIntent.ImageIntent.create());
    }

    private Observable<WelcomeIntent> loadUserIntent() {
        return Observable.just(WelcomeIntent.LocalUserIntent.create());
    }

    @Override
    public void render(WelcomeViewState state) {
        if(!state.isLoading()){
            if(null == state.user()){
                if (null != state.image() && !state.image().isEmpty()){
                    Log.i("WelcomeActivity", "Image State -> UI :" + state.image());
                    Picasso.get()
                            .load(state.image())
                            .placeholder(R.drawable.test_background)
                            .error(R.drawable.test_background)
                            .centerCrop()
                            .fit()
                            .into(imgWelcome);
                }
            } else if (null == state.image()) {
                Log.i("WelcomeActivity", "Local User State -> UI :" + state.image());
                if (!"cache".equals(state.user().getId())) {
                    if (state.user().getId().isEmpty() && state.user().getPassword().isEmpty()) {
                        Observable.timer(3, TimeUnit.SECONDS)
                                .observeOn(SchedulerProvider.getInstance().ui())
                                .doOnComplete(this::finish)
                                .subscribe(action -> startLogin());
                    } else {
                        Observable.timer(3, TimeUnit.SECONDS)
                                .observeOn(SchedulerProvider.getInstance().ui())
                                .doOnComplete(this::finish)
                                .subscribe(action -> startHome());
                    }
//                    Observable.timer(5, TimeUnit.SECONDS)
//                            .observeOn(SchedulerProvider.getInstance().ui())
//                            .subscribe(action -> finish());
                }
            }
        }


    }

    private void startLogin() {
        Toast.makeText(this,"user is null",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
    }

    private void startHome() {
        // Toast.makeText(this,"user is exists",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(WelcomeActivity.this,HomeActivity.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposables.dispose();
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }

}
