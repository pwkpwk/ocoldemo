package com.ambientbytes.ocoldemo;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * @author Pavel Karpenko
 */

public interface IViewHolderFactory<VM> {
    /**
     * Return a unique identifier of the view type that presents instances of the specified view model.
     * Returned identifier will be later passed to createView.
     * @param viewModel View model object retrieved from an observable collection.
     * @return unique identifier of a type of a view.
     */
    int getViewTypeId(Object viewModel);

    /**
     * Create a view of the type identified by a unique identifier returned earlier by getViewTypeId.
     * @param parent Parent view group to that the created view must be attached.
     * @param typeId identifier of the view type returned earlier by getViewTypeId.
     * @return anew view included in the specified group.
     */
    RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int typeId);

    /**
     * Attach a view model to a view.
     * @param viewHolder view holder created earlier by createViewHolder.
     * @param typeId identifier of the type of the view returned earlier for the view model by getViewTypeId.
     * @param viewModel view model for the type of which the view had been created.
     */
    void bind(RecyclerView.ViewHolder viewHolder, int typeId, VM viewModel);

    /**
     * Detach a view model from a view.
     * @param viewHolder view holder created earlier by createViewHolder.
     * @param typeId identifier of the type of the view returned earlier for the view model by getViewTypeId.
     */
    void unbind(RecyclerView.ViewHolder viewHolder, int typeId);
}
