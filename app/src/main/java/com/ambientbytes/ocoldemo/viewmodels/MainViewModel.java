package com.ambientbytes.ocoldemo.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Handler;

import com.ambientbytes.observables.IAction;
import com.ambientbytes.observables.IDispatcher;
import com.ambientbytes.observables.IItemFilter;
import com.ambientbytes.observables.IItemMapper;
import com.ambientbytes.observables.IReadOnlyObservableList;
import com.ambientbytes.observables.ObservableCollections;
import com.ambientbytes.ocoldemo.models.HumanModel;
import com.ambientbytes.ocoldemo.models.IModel;
import com.ambientbytes.ocoldemo.models.MainModel;
import com.ambientbytes.ocoldemo.models.RobotModel;

/**
 * Created by pakarpen on 3/28/17.
 */

public class MainViewModel extends BaseObservable {

    private final Handler handler;
    private final MainModel model;
    private final IReadOnlyObservableList<WorkerViewModel> mappedModels;
    private final IReadOnlyObservableList<WorkerViewModel> data;
    private final IReadOnlyObservableList<WorkerViewModel> youngRobots;
    private final IReadOnlyObservableList<WorkerViewModel> oldHumans;

    private final static IItemFilter<IModel> youngRobotsFilter = new IItemFilter<IModel>() {
        @Override
        public boolean isIn(IModel item) {
            return item instanceof RobotModel && item.getAge() < 18;
        }
    };

    private final static IItemFilter<IModel> oldHumansFilter = new IItemFilter<IModel>() {
        @Override
        public boolean isIn(IModel item) {
            return item instanceof HumanModel && item.getAge() >= 18;
        }
    };

    public MainViewModel(MainModel model) {
        IDispatcher dispatcher = new IDispatcher() {
            @Override
            public void dispatch(final IAction action) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        action.execute();
                    }
                });
            }
        };
        IItemMapper<IModel, WorkerViewModel> mapper = new IItemMapper<IModel, WorkerViewModel>() {
            @Override
            public WorkerViewModel map(IModel item) {
                WorkerViewModel viewModel = null;

                if (item instanceof RobotModel) {
                    viewModel = new RobotViewModel((RobotModel) item);
                } else if (item instanceof HumanModel) {
                    viewModel = new HumanViewModel((HumanModel) item);
                }
                return viewModel;
            }
        };

        this.handler = new Handler();
        this.model = model;

        this.mappedModels = ObservableCollections.createMappingObservableList(model.everyone(), mapper);
        this.data = ObservableCollections.createDispatchingObservableList(this.mappedModels, dispatcher, model.lock());

        IReadOnlyObservableList<IModel> filteredModels = ObservableCollections.createFilteredObservableList(model.everyone(), youngRobotsFilter, model.lock()).list();
        IReadOnlyObservableList<WorkerViewModel> mappedViewModels = ObservableCollections.createMappingObservableList(filteredModels, mapper);
        this.youngRobots = ObservableCollections.createDispatchingObservableList(mappedViewModels, dispatcher, model.lock());

        filteredModels = ObservableCollections.createFilteredObservableList(model.everyone(), oldHumansFilter, model.lock()).list();
        mappedViewModels = ObservableCollections.createMappingObservableList(filteredModels, mapper);
        this.oldHumans = ObservableCollections.createDispatchingObservableList(mappedViewModels, dispatcher, model.lock());
    }

    @Bindable
    public IReadOnlyObservableList<WorkerViewModel> getData() {
        return data;
    }

    @Bindable
    public IReadOnlyObservableList<WorkerViewModel> getYoungRobots() {
        return youngRobots;
    }

    @Bindable
    public IReadOnlyObservableList<WorkerViewModel> getOldHumans() {
        return oldHumans;
    }

    public void addYoungRobot() {
        model.addYoungRobot();
    }

    public void addOldRobot() {
        model.addOldRobot();
    }

    public void clearRobots() {
        model.clearRobots();
    }

    public void addYoungHuman() {
        model.addYoungHuman();
    }

    public void addOldHuman() {
        model.addOldHuman();
    }

    public void clearHumans() {
        model.clearHumans();
    }
}
