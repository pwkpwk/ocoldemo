package com.ambientbytes.ocoldemo;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ambientbytes.observables.IReadOnlyObservableList;

/**
 * Collection of binding adapters picked up by the Android Data Binding Library.
 * @author Pavel Karpenko
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
            IViewHolderFactory<TItem> oldFactory,
            IReadOnlyObservableList<TItem> newItems,
            IViewHolderFactory<TItem> newFactory) {
        view.setAdapter(new ObservableListAdapter<>(newItems, newFactory));
    }
}
