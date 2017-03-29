package com.ambientbytes.observables;

import java.util.concurrent.locks.ReadWriteLock;

public final class ObservableCollections {

	/**
	 * Create a new observable list with a mutator without a thread lock.
	 * @return A new ObservableList<T> with non-null list and mutator properties.
	 */
	public static <T> ObservableList<T> createObservableList() {
		MutableObservableList<T> list = new MutableObservableList<>(new DummyReadWriteLock());
		return new ObservableList<T>(list, list.getMutator());
	}
	
	/**
	 * Create a new observable list with a mutator with a thread lock. The list locks
	 * the write lock of the supplied ReadWriteLock object for each mutation, and emits
	 * all mutation events while holding the write lock.
	 * @return A new ObservableList<T> with non-null list and mutator properties.
	 */
	public static <T> ObservableList<T> createObservableList(ReadWriteLock lock) {
		MutableObservableList<T> list = new MutableObservableList<>(lock);
		return new ObservableList<T>(list, list.getMutator());
	}

	public static <T> OrderedList<T> createOrderedObservableList(IReadOnlyObservableList<T> source, IItemsOrder<T> order, ReadWriteLock lock) {
		return new OrderedList<>(new OrderingReadOnlyObservableList<>(source, order, lock));
	}

	public static <T> MergingList<T> createMergingObservableList(ReadWriteLock lock) {
        return new MergingList<>(lock);
    }

    public static <T> IReadOnlyObservableList<T> createDispatchingObservableList(IReadOnlyObservableList<T> source, IDispatcher dispatcher, ReadWriteLock lock) {
        return new DispatchingObservableList<>(source, dispatcher, lock);
    }

    public static <TSource, TMapped> IReadOnlyObservableList<TMapped> createMappingObservableList(IReadOnlyObservableList<TSource> source, IItemMapper<TSource, TMapped> mapper) {
        return new MappingReadOnlyObservableList<>(source, mapper);
    }

    public static <T> FilteredList<T> createFilteredObservableList(IReadOnlyObservableList<T> source, IItemFilter<T> filter, ReadWriteLock lock) {
		return new FilteredList(source, filter, lock);
	}
}
