package pri.wx.xujc.xzjy.home.service;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Picasso;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import pri.wx.xujc.xzjy.R;
import pri.wx.xujc.xzjy.course.CourseActivity;
import pri.wx.xujc.xzjy.data.model.Image;
import pri.wx.xujc.xzjy.mvibase.MviView;
import pri.wx.xujc.xzjy.score.ScoreActivity;
import pri.wx.xujc.xzjy.util.ViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends Fragment
        implements MviView<ServiceIntent,ServiceViewState>, LifecycleRegistryOwner {

    public static final String TAG = "ServiceFragment";

    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    private ServiceViewModel mViewModel;

    private CompositeDisposable mDisposables;

    private View mView;

    private ImageButton scoreBtn;
    private ImageButton courseBtn;

    public static ServiceFragment newInstance() {
        return new ServiceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_service, container, false);
        }
        scoreBtn = mView.findViewById(R.id.btn_score);
        courseBtn = mView.findViewById(R.id.btn_course);

        return mView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(getContext()))
                .get(ServiceViewModel.class);
        mDisposables = new CompositeDisposable();

        mDisposables.add(
                RxView.clicks(scoreBtn)
                        .subscribe(action -> startScore()));
        mDisposables.add(
                RxView.clicks(courseBtn)
                        .subscribe(action -> startCourse()));

        bind();
    }

    private void bind() {
        mDisposables.add(mViewModel.states().subscribe(this::render));
        mViewModel.processIntents(intents());
    }

    public void render(ServiceViewState state) {
        if(!state.isLoading() && null == state.error()){
            if(state.icons().size() > 0){
                for(Image image:state.icons()){
                    if ("score".equals(image.getName())) {
                        Picasso.get()
                                .load(image.getLink())
                                .placeholder(R.drawable.item_bg_s2)
                                .error(R.drawable.item_bg_s2)
                                .centerCrop()
                                .fit()
                                .into(scoreBtn);
                    }
                }
            }
        }
    }

    public Observable<ServiceIntent> intents() {
        return initIcons();
    }

    private Observable<ServiceIntent> initIcons() {
        return Observable.just(ServiceIntent.InitIcon.create());
    }

    private void startScore(){
        startActivity(new Intent(getActivity(),ScoreActivity.class));
    }

    private void startCourse(){
        startActivity(new Intent(getActivity(),CourseActivity.class));
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mLifecycleRegistry;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposables.dispose();
    }
}
