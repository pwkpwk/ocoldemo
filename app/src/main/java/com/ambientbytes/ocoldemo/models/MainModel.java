package com.ambientbytes.ocoldemo.models;

import com.ambientbytes.observables.IItemsOrder;
import com.ambientbytes.observables.IReadOnlyObservableList;
import com.ambientbytes.observables.ObservableCollections;
import com.ambientbytes.observables.ObservableList;
import com.ambientbytes.observables.OrderedObservableList;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by pakarpen on 3/28/17.
 */

public final class MainModel {
    private final ReadWriteLock lock;
    private final ObservableList<Integer> primaryData;
    private final OrderedObservableList<Integer> orderedData;
    private int nextValue;

    private final static IItemsOrder<Integer> directOrder = new IItemsOrder<Integer>() {
        @Override
        public boolean isLess(Integer lesser, Integer greater) {
            return lesser.compareTo(greater) < 0;
        }
    };

    private final static IItemsOrder<Integer> reverseOrder = new IItemsOrder<Integer>() {
        @Override
        public boolean isLess(Integer lesser, Integer greater) {
            return lesser.compareTo(greater) > 0;
        }
    };

    public MainModel() {
        this.lock = new ReentrantReadWriteLock();
        this.primaryData = ObservableCollections.createObservableList(this.lock);
        this.orderedData = ObservableCollections.createOrderedObservableList(this.primaryData.list(), reverseOrder, this.lock);
        this.nextValue = 1;
    }

    public IReadOnlyObservableList<Integer> data() {
        return orderedData.list();
    }

    public void clear() {
        primaryData.mutator().clear();
    }

    public void add() {
        primaryData.mutator().add(Integer.valueOf(nextValue++));
    }

    public void reorder() {
        if (orderedData.order().getOrder() == reverseOrder) {
            orderedData.order().setOrder(directOrder);
        } else {
            orderedData.order().setOrder(reverseOrder);
        }
    }
}
