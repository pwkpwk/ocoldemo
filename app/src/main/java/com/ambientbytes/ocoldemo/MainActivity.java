package com.ambientbytes.ocoldemo;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.ambientbytes.ocoldemo.databinding.ActivityMainBinding;
import com.ambientbytes.ocoldemo.models.MainModel;
import com.ambientbytes.ocoldemo.viewmodels.MainViewModel;

public class MainActivity extends Activity {

    private static MainModel model = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DemoApplication app = (DemoApplication)getApplicationContext();
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //
        // Obtain the main model from the application object that has composwed it.
        //
        binding.setVm(new MainViewModel(app.getMainModel()));
    }
}
