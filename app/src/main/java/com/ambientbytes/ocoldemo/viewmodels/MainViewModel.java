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
import com.ambientbytes.observables.IReadWriteMonitor;
import com.ambientbytes.observables.ITrigger;
import com.ambientbytes.observables.ImmutableObservableReference;
import com.ambientbytes.observables.ListBuilder;
import com.ambientbytes.ocoldemo.models.HumanModel;
import com.ambientbytes.ocoldemo.models.IModel;
import com.ambientbytes.ocoldemo.models.MainModel;
import com.ambientbytes.ocoldemo.models.RobotModel;

/**
 * View model of the main view.
 * @author Pavel Karpenko
 */

public class MainViewModel extends BaseObservable {

    private final Handler handler;
    private final MainModel model;
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
        this.data = ListBuilder.<IModel>create(model.unlinker(), model.monitor()).source(model.everyone())
                .order(new ImmutableObservableReference<IItemsOrder<IModel>>(order))
                .map(mapper)
                .dispatch(dispatcher)
                .build();

        this.youngRobots = createFilteredDispatchingList(model.everyone(), youngRobotsFilter, mapper, dispatcher, model.unlinker(), model.monitor());
        this.oldHumans = createFilteredDispatchingList(model.everyone(), oldHumansFilter, mapper, dispatcher, model.unlinker(), model.monitor());
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

    public final void unlink() {
        model.unlinker().trigger();
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
            ITrigger unlinker,
            IReadWriteMonitor monitor) {

        return ListBuilder.<IModel>create(unlinker, monitor).source(source)
                .filter(new ImmutableObservableReference<IItemFilter<IModel>>(filter))
                .order(new ImmutableObservableReference<IItemsOrder<IModel>>(order))
                .map(mapper)
                .dispatch(dispatcher)
                .build();
    }
}
