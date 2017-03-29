package com.ambientbytes.ocoldemo;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ambientbytes.ocoldemo.databinding.ActivityMainBinding;
import com.ambientbytes.ocoldemo.models.MainModel;
import com.ambientbytes.ocoldemo.viewmodels.MainViewModel;

public class MainActivity extends Activity {

    private static MainModel model = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);
        rv.setLayoutManager(new GridLayoutManager(this, 5));
        binding.setVm(new MainViewModel(getModel()));
    }

    private MainModel getModel() {
        if (model == null) {
            model = new MainModel();
        }
        return model;
    }
}
