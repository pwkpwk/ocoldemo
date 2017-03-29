package com.ambientbytes.ocoldemo;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ambientbytes.ocoldemo.viewmodels.HumanViewModel;
import com.ambientbytes.ocoldemo.viewmodels.RobotViewModel;
import com.ambientbytes.ocoldemo.viewmodels.WorkerViewModel;

/**
 * Created by pakarpen on 3/28/17.
 */

public final class MainViewFactory implements IViewFactory {

    @Override
    public int getViewTypeId(Object viewModel) {
        int id = -1;

        if (viewModel instanceof RobotViewModel) {
            id = 0;
        } else if (viewModel instanceof HumanViewModel) {
            id = 1;
        }

        return id;
    }

    @Override
    public View createView(ViewGroup parent, int typeId) {

        TextView tv = new TextView(parent.getContext());

        switch (typeId) {
            case 0: // robot
                tv.setBackgroundColor(Color.YELLOW);
                break;

            case 1: // human
                tv.setBackgroundColor(Color.GREEN);
                break;
        }

        tv.setPadding(8, 8, 8, 8);

        return tv;
    }

    @Override
    public void bindView(View view, int typeId, Object viewModel) {
        TextView tv = (TextView) view;

        WorkerViewModel vm = (WorkerViewModel)viewModel;

        tv.setText(vm.getName() + '/' + Integer.toString(vm.getAge()));
    }

    @Override
    public void unbindView(View view, int typeId, Object viewModel) {
        // do nothing
    }
}
