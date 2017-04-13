package com.ambientbytes.ocoldemo;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ambientbytes.ocoldemo.databinding.HumanViewBinding;
import com.ambientbytes.ocoldemo.databinding.RobotViewBinding;
import com.ambientbytes.ocoldemo.viewmodels.HumanViewModel;
import com.ambientbytes.ocoldemo.viewmodels.RobotViewModel;
import com.ambientbytes.ocoldemo.viewmodels.WorkerViewModel;

/**
 * View holders factory for "workers" objects - humans and robots.
 * The factory inflates one of the two layouts and binds them to the view models passed
 * by ObservableListAdapter that does not know anything about particular vies and view holders.
 * For each type oif a view model the factory creates a view holder of its own type - RobotViewHolder
 * for RobotViewModel objects and HumanViewHolder for HumanViewModel objects.
 * @author Pavel Karpenko
 */

public final class MainViewFactory implements IViewHolderFactory<WorkerViewModel> {

    private static final int HUMAN = R.layout.human_view;
    private static final int ROBOT = R.layout.robot_view;

    private static final class RobotViewHolder extends RecyclerView.ViewHolder {

        private final RobotViewBinding binding;

        public RobotViewHolder(RobotViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RobotViewModel robot) {
            binding.setVm(robot);
            binding.executePendingBindings();
        }

        public void unbind() {
            binding.getVm().unlink();
        }
    }

    private static final class HumanViewHolder extends RecyclerView.ViewHolder {

        private final HumanViewBinding binding;

        public HumanViewHolder(HumanViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(HumanViewModel human) {
            binding.setVm(human);
            binding.executePendingBindings();
        }

        public void unbind() {
            binding.getVm().unlink();
        }
    }

    @Override
    public int getViewTypeId(Object viewModel) {
        int id = -1;

        if (viewModel instanceof RobotViewModel) {
            id = ROBOT;
        } else if (viewModel instanceof HumanViewModel) {
            id = HUMAN;
        }

        return id;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int typeId) {

        RecyclerView.ViewHolder vh = null;

        switch (typeId) {
            case ROBOT:
                vh = new RobotViewHolder(createRobotBinding(parent));
                break;

            case HUMAN:
                vh = new HumanViewHolder(createHumanBinding(parent));
                break;
        }

        return vh;
    }

    @Override
    public void bind(RecyclerView.ViewHolder holder, int typeId, WorkerViewModel viewModel) {
        switch (typeId) {
            case ROBOT:
                bindRobot((RobotViewHolder)holder, (RobotViewModel)viewModel);
                break;

            case HUMAN:
                bindHuman((HumanViewHolder)holder, (HumanViewModel)viewModel);
                break;
        }
    }

    @Override
    public void unbind(RecyclerView.ViewHolder holder, int typeId) {
        switch (typeId) {
            case ROBOT:
                unbindRobot((RobotViewHolder)holder);
                break;

            case HUMAN:
                unbindHuman((HumanViewHolder)holder);
                break;
        }
    }

    private static RobotViewBinding createRobotBinding(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return DataBindingUtil.inflate(layoutInflater, R.layout.robot_view, parent, false);
    }

    private static HumanViewBinding createHumanBinding(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return DataBindingUtil.inflate(layoutInflater, R.layout.human_view, parent, false);
    }

    private static void bindRobot(RobotViewHolder holder, RobotViewModel robot) {
        holder.bind(robot);
    }

    private static void bindHuman(HumanViewHolder holder, HumanViewModel human) {
        holder.bind(human);
    }

    private static void unbindRobot(RobotViewHolder robot) {
        robot.unbind();
    }

    private static void unbindHuman(HumanViewHolder human) {
        human.unbind();
    }
}
