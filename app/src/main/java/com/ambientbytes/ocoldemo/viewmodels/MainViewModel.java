package com.ambientbytes.ocoldemo.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Handler;

import com.ambientbytes.observables.IAction;
import com.ambientbytes.observables.IDispatcher;
import com.ambientbytes.observables.IItemFilter;
import com.ambientbytes.observables.IItemMapper;
import com.ambientbytes.observables.IItemsOrder;
import com.ambientbytes.observables.IReadOnlyObservableList;
import com.ambientbytes.observables.ObservableCollections;
import com.ambientbytes.ocoldemo.models.HumanModel;
import com.ambientbytes.ocoldemo.models.IModel;
import com.ambientbytes.ocoldemo.models.MainModel;
import com.ambientbytes.ocoldemo.models.RobotModel;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * Created by pakarpen on 3/28/17.
 */

public class MainViewModel extends BaseObservable {

    private final Handler handler;
    private final MainModel model;
    private final IReadOnlyObservableList<IModel> linkToModel;
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

    private static IItemsOrder<IModel> order = new IItemsOrder<IModel>() {
        @Override
        public boolean isLess(IModel lesser, IModel greater) {
            boolean isLess = lesser.getAge() > greater.getAge();

            if (!isLess && lesser.getAge() == greater.getAge()) {
                isLess = lesser.getName().compareTo(greater.getName()) < 0;
            }

            return isLess;
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
        this.linkToModel = ObservableCollections.createOrderedObservableList(model.everyone(), order, model.lock()).list();
        IReadOnlyObservableList<WorkerViewModel> mapped = ObservableCollections.createMappingObservableList(this.linkToModel, mapper);
        this.data = ObservableCollections.createDispatchingObservableList(mapped, dispatcher, model.lock());

        this.youngRobots = createFilteredDispatchingList(model.everyone(), youngRobotsFilter, mapper, dispatcher, model.lock());
        this.oldHumans = createFilteredDispatchingList(model.everyone(), oldHumansFilter, mapper, dispatcher, model.lock());
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

    private static IReadOnlyObservableList<WorkerViewModel> createFilteredDispatchingList(
            IReadOnlyObservableList<IModel> source,
            IItemFilter<IModel> filter,
            IItemMapper<IModel, WorkerViewModel> mapper,
            IDispatcher dispatcher,
            ReadWriteLock lock) {

        IReadOnlyObservableList<IModel> filteredModels = ObservableCollections.createFilteredObservableList(source, filter, lock).list();
        IReadOnlyObservableList<IModel> orderedModels = ObservableCollections.createOrderedObservableList(filteredModels, order, lock).list();
        IReadOnlyObservableList<WorkerViewModel> mappedViewModels = ObservableCollections.createMappingObservableList(orderedModels, mapper);

        return ObservableCollections.createDispatchingObservableList(mappedViewModels, dispatcher, lock);
    }
}
