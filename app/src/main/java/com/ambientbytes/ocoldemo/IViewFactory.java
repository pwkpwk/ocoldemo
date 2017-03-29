package com.ambientbytes.ocoldemo;

import android.view.View;
import android.view.ViewGroup;

/**
 * @Author Pavel Karpenko
 */

public interface IViewFactory {
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
    View createView(ViewGroup parent, int typeId);

    /**
     * Attach a view model to a view.
     * @param view view created earlier by createView.
     * @param typeId identifier of the type of the view returned earlier for the view model by getViewTypeId.
     * @param viewModel view model for the type of which the view had been created.
     */
    void bindView(View view, int typeId, Object viewModel);

    /**
     * Detach a view model from a view.
     * @param view view created earlier by createView.
     * @param typeId identifier of the type of the view returned earlier for the view model by getViewTypeId.
     */
    void unbindView(View view, int typeId, Object viewModel);
}
