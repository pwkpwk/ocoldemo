package com.ambientbytes.ocoldemo.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.ambientbytes.observables.IReadOnlyObservableList;
import com.ambientbytes.ocoldemo.models.MainModel;

/**
 * Created by pakarpen on 3/28/17.
 */

public class MainViewModel extends BaseObservable {

    private final MainModel model;

    public MainViewModel(MainModel model) {
        this.model = model;
    }

    @Bindable
    public IReadOnlyObservableList<Integer> getData() {
        return model.data();
    }

    public void clear() {
        model.clear();
    }

    public void add() {
        model.add();
    }

    public void changeOrder() {
        model.reorder();
    }
}
