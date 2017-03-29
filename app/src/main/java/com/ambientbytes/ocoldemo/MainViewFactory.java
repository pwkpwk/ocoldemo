package com.ambientbytes.ocoldemo;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by pakarpen on 3/28/17.
 */

public final class MainViewFactory implements IViewFactory {
    @Override
    public int getViewTypeId(Object viewModel) {
        return 0;
    }

    @Override
    public View createView(ViewGroup parent, int typeId) {
        return new TextView(parent.getContext());
    }

    @Override
    public void bindView(View view, int typeId, Object viewModel) {
        ((TextView)view).setText(viewModel.toString());
    }

    @Override
    public void unbindView(View view, int typeId) {
        // do nothing
    }
}
