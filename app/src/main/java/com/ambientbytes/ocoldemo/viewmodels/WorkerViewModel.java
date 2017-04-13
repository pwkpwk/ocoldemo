package com.ambientbytes.ocoldemo.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.ambientbytes.ocoldemo.models.IModel;

/**
 * View model of an abstract worker.
 * @author Pavel Karpenko
 */

public abstract class WorkerViewModel extends BaseObservable {

    private IModel model;

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

    public void unlink() {
        // If there are any references that the model holds to this view model,
        // this is the place where the model should be told to release those references.
    }
}
