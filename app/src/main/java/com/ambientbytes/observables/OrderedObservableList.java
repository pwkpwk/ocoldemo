package com.ambientbytes.observables;

/**
 * @Author Pavel Karpenko
 */

public final class OrderedObservableList<T> implements IObservableListContainer<T> {

    private final OrderingReadOnlyObservableList<T> list;

    OrderedObservableList(OrderingReadOnlyObservableList<T> list) {
        this.list = list;
    }

    @Override
    public IReadOnlyObservableList<T> list() {
        return list;
    }

    public IItemsOrderContainer<T> order() {
        return list;
    }
}
