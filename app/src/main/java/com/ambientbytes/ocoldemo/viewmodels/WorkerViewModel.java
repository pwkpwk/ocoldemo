package com.ambientbytes.ocoldemo.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.ambientbytes.ocoldemo.models.IModel;

/**
 * Created by pakarpen on 3/29/17.
 */

public abstract class WorkerViewModel extends BaseObservable {

    IModel model;

    protected WorkerViewModel(IModel model) {
        this.model = model;
    }

    @Bindable
    public int getAge() {
        return model.getAge();
    }

    @Bindable
    public String getName() {
        return model.getName();
    }
}
