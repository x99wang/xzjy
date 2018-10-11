package pri.wx.xujc.xzjy.home.service;


import android.arch.lifecycle.LifecycleRegistry;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.disposables.CompositeDisposable;
import pri.wx.xujc.xzjy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends Fragment {

    public static final String TAG = "ServiceFragment";

    private LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    //private ScheduleViewModel mViewModel;

    private CompositeDisposable mDisposables;

    public static ServiceFragment newInstance() {
        return new ServiceFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //mViewModel = ViewModelProviders.of(this, ToDoViewModelFactory.getInstance(getContext()))
        //        .get(StatisticsViewModel.class);
        mDisposables = new CompositeDisposable();
        bind();
    }

    private void bind() {
        // Subscribe to the ViewModel and call render for every emitted state
        //mDisposables.add(mViewModel.states().subscribe(this::render));
        // Pass the UI's intents to the ViewModel
        //mViewModel.processIntents(intents());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposables.dispose();
    }
}
