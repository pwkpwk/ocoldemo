package com.ambientbytes.ocoldemo.models;

import com.ambientbytes.observables.IReadOnlyObservableList;
import com.ambientbytes.observables.IReadWriteMonitor;
import com.ambientbytes.observables.ListBuilder;
import com.ambientbytes.observables.ListMutator;
import com.ambientbytes.observables.LockTool;
import com.ambientbytes.observables.MutableListSet;
import com.ambientbytes.observables.Trigger;

import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Model of the main activity.
 * @author Pavel Karpenko
 */

public final class MainModel {
    private final Random random = new Random();
    private final IReadWriteMonitor monitor;
    private final ThreadPoolExecutor threadPool;
    private final ListMutator<IModel> humans;
    private final ListMutator<IModel> robots;
    private final Trigger unlinker;
    private final IReadOnlyObservableList<IModel> everyone;

    public MainModel() {
        this.monitor = LockTool.createReadWriteMonitor(new ReentrantReadWriteLock());
        this.threadPool = new ScheduledThreadPoolExecutor(4);

        this.unlinker = new Trigger(monitor);
        this.humans = new ListMutator<>(monitor);
        this.robots = new ListMutator<>(monitor);
        IReadOnlyObservableList<IModel> humansList = ListBuilder.<IModel>create(this.unlinker, this.monitor).mutable(this.humans).build();
        IReadOnlyObservableList<IModel> robotsList = ListBuilder.<IModel>create(this.unlinker, this.monitor).mutable(this.robots).build();

        MutableListSet<IModel> lists = new MutableListSet<IModel>(monitor());
        lists.add(humansList);
        lists.add(robotsList);
        this.everyone = ListBuilder.<IModel>create(unlinker, monitor).merge(lists).build();
    }

    public IReadWriteMonitor monitor() {
        return monitor;
    }

    public Trigger unlinker() {
        return unlinker;
    }

    public IReadOnlyObservableList<IModel> everyone() {
        return everyone;
    }

    public void addYoungRobot() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                robots.add(new RobotModel(random.nextInt(18), "Robot " + Integer.toString(random.nextInt(1024 * 4))));
            }
        });
    }

    public void addOldRobot() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                robots.add(new RobotModel(18 + random.nextInt(200), "Robot " + Integer.toString(random.nextInt(1024 * 4))));
            }
        });
    }

    public void clearRobots() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                robots.clear();
            }
        });
    }

    public void addYoungHuman() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                humans.add(new HumanModel(random.nextInt(18), random));
            }
        });
    }

    public void addOldHuman() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                humans.add(new HumanModel(18 + random.nextInt(100), random));
            }
        });
    }

    public void clearHumans() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                humans.clear();
            }
        });
    }
}
