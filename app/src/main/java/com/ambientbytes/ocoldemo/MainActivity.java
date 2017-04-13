package com.ambientbytes.ocoldemo;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.ambientbytes.ocoldemo.databinding.ActivityMainBinding;
import com.ambientbytes.ocoldemo.models.MainModel;
import com.ambientbytes.ocoldemo.viewmodels.MainViewModel;

public class MainActivity extends Activity {

    private MainViewModel viewModel;

    public MainActivity() {
        viewModel = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final DemoApplication app = (DemoApplication)getApplicationContext();
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //
        // Obtain the main model from the application object that has composed it.
        //
        final MainModel model = app.getMainModel();
        viewModel = new MainViewModel(model);
        binding.setVm(viewModel);
    }

    @Override
    protected void onDestroy() {
        //
        // Unlinking is not needed for the main view model, but any activity that may be created and destroyed
        // while application is still running must unlink its view model from the model.
        // If unlinking is not done, any objects in the view model observing the model may be retained
        // which will cause hard to detect memory leaks.
        //
        viewModel.unlink();
        viewModel = null;
        super.onDestroy();
    }
}
