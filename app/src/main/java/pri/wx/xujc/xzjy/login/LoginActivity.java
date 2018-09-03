package pri.wx.xujc.xzjy.login;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import pri.wx.xujc.xzjy.R;
import pri.wx.xujc.xzjy.home.HomeActivity;
import pri.wx.xujc.xzjy.mvibase.MviView;
import pri.wx.xujc.xzjy.util.ViewModelFactory;
import pri.wx.xujc.xzjy.util.schedulers.SchedulerProvider;

public class LoginActivity extends AppCompatActivity
        implements MviView<LoginIntent,LoginViewState>,LifecycleRegistryOwner {

    private static final String TAG = "LoginActivity";

    private EditText sno;
    private EditText pwd;
    private Button login;

    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
    private LoginViewModel mViewModel;
    private CompositeDisposable mDisposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(this))
                .get(LoginViewModel.class);

        mDisposables = new CompositeDisposable();
        sno = findViewById(R.id.et_sno);
        pwd = findViewById(R.id.et_pwd);
        login = findViewById(R.id.btn_login);

        bind();
    }

    private void bind() {
        mDisposables.add(mViewModel.states()
                .observeOn(SchedulerProvider.getInstance().ui())
                .subscribe(this::render));
        mViewModel.processIntents(intents());
    }

    @Override
    public Observable<LoginIntent> intents() {
        return RxView.clicks(login).map(action ->
                LoginIntent.Login.create(
                        sno.getText().toString(), pwd.getText().toString()
                ));
    }

    @Override
    public void render(LoginViewState state) {
        Log.i(TAG, "Login Result State -> UI " + state.isSuccess());
        if (!state.isLoading()) {
            if(state.isSuccess())
                startHome();
            else
                Toast.makeText(this,"登陆失败 " + state.msg(),Toast.LENGTH_LONG).show();
        }
    }

    private void startHome() {
        startActivity(new Intent(this, HomeActivity.class));
        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(SchedulerProvider.getInstance().ui())
                .subscribe(action -> finish());
    }

    @NonNull
    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }
}
