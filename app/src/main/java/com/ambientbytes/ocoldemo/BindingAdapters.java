package com.ambientbytes.ocoldemo;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ambientbytes.observables.IReadOnlyObservableList;

/**
 * Created by pakarpen on 3/28/17.
 */

public class BindingAdapters {

    @BindingAdapter("plop")
    public static void setPlop(View view, String string) {
        Log.v("PLOP", "Plop");
    }

    @BindingAdapter({"itemsSource", "viewFactory"})
    public static <TItem> void setItemsSource(
            RecyclerView view,
            IReadOnlyObservableList<TItem> oldItems,
            IViewFactory oldFactory,
            IReadOnlyObservableList<TItem> newItems,
            IViewFactory newFactory
    ) {
        view.setAdapter(ObservableListAdapter.createAdapterForList(newItems, newFactory));
    }
}
