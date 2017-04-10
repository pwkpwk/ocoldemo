package com.ambientbytes.ocoldemo.models;

import com.ambientbytes.observables.IReadOnlyObservableList;
import com.ambientbytes.observables.IReadWriteMonitor;
import com.ambientbytes.observables.LockTool;
import com.ambientbytes.observables.MergingList;
import com.ambientbytes.observables.ObservableCollections;
import com.ambientbytes.observables.ObservableList;

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
    private final ObservableList<IModel> humans;
    private final ObservableList<IModel> robots;
    private final MergingList<IModel> everyone;

    public MainModel() {
        this.monitor = LockTool.createReadWriteMonitor(new ReentrantReadWriteLock());
        this.threadPool = new ScheduledThreadPoolExecutor(4);
        this.humans = ObservableCollections.createObservableList(this.monitor);
        this.robots = ObservableCollections.createObservableList(this.monitor);
        this.everyone = ObservableCollections.createMergingObservableList(this.monitor);
        this.everyone.lists().add(this.humans.list());
        this.everyone.lists().add(this.robots.list());
    }

    public IReadWriteMonitor monitor() {
        return monitor;
    }

    public IReadOnlyObservableList<IModel> everyone() {
        return everyone.list();
    }

    public void addYoungRobot() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                robots.mutator().add(new RobotModel(random.nextInt(18), "Robot " + Integer.toString(random.nextInt(1024 * 4))));
            }
        });
    }

    public void addOldRobot() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                robots.mutator().add(new RobotModel(18 + random.nextInt(200), "Robot " + Integer.toString(random.nextInt(1024 * 4))));
            }
        });
    }

    public void clearRobots() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                robots.mutator().clear();
            }
        });
    }

    public void addYoungHuman() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                humans.mutator().add(new HumanModel(random.nextInt(18), random));
            }
        });
    }

    public void addOldHuman() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                humans.mutator().add(new HumanModel(18 + random.nextInt(100), random));
            }
        });
    }

    public void clearHumans() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                humans.mutator().clear();
            }
        });
    }
}
