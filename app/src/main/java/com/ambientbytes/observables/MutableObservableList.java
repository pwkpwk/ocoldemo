package com.ambientbytes.observables;

import java.util.Collection;
import java.util.concurrent.locks.ReadWriteLock;

class MutableObservableList<T> implements IReadOnlyObservableList<T> {
	
	private final ReadWriteLock lock;
	private final ArrayListEx<T> data;
	private final ListObservers<T> observers;
	private final IListMutator<T> mutator;
	
	private final class Mutator implements IListMutator<T> {
		
		@Override
		public final void add(T value) {
			IResource res = LockTool.acquireWriteLock(lock);
			
			try {
				insertUnsafe(data.size(), value);
			} finally {
				res.release();
			}
		}

		@Override
		public final void add(int index, T value) {
			IResource res = LockTool.acquireWriteLock(lock);
			
			try {
				insertUnsafe(index, value);
			} finally {
				res.release();
			}
		}
		
		@Override
		public final void add(int index, Collection<T> values) {
			IResource res = LockTool.acquireWriteLock(lock);
			
			try {
				insertUnsafe(index, values);
			} finally {
				res.release();
			}
		}
		
		@Override
		public final void set(int index, T value) {
			IResource res = LockTool.acquireWriteLock(lock);
			
			try {
				setUnsafe(index, value);
			} finally {
				res.release();
			}
		}
		
		@Override
		public final void set(int index, Collection<T> values) {
			IResource res = LockTool.acquireWriteLock(lock);
			
			try {
				setUnsafe(index, values);
			} finally {
				res.release();
			}
		}

		@Override
		public final int remove(int index, int count) {
			IResource res = LockTool.acquireWriteLock(lock);
			final int length;
			
			try {
				length = removeUnsafe(index, count);
			} finally {
				res.release();
			}
			
			return length;
		}

		@Override
		public final void clear() {
			IResource res = LockTool.acquireWriteLock(lock);
			
			try {
				clearUnsafe();
			} finally {
				res.release();
			}
		}

		@Override
		public final void move(int startIndex, int newIndex, int count) {
			IResource res = LockTool.acquireWriteLock(lock);
			
			try {
				moveUnsafe(startIndex, newIndex, count);
			} finally {
				res.release();
			}
		}
		
		@Override
		public final void reset(Collection<T> newItems) {
			IResource res = LockTool.acquireWriteLock(lock);
			
			try {
				resetUnsafe(newItems);
			} finally {
				res.release();
			}
		}

		private int removeUnsafe(int index, int count) {
			if (index < 0 || index >= data.size()) {
				throw new IndexOutOfBoundsException();
			}
			
			int length = count;
			
			if (index + length > data.size()) {
				length = data.size() - index;
			}
			
			if (length > 0) {
				observers.removing(index, length);
				data.remove(index, length);
				observers.removed(index, length);
			}
			
			return length;
		}

		private void insertUnsafe(int index, T value) {
			data.add(index, value);
			observers.added(index, 1);
		}

		private void insertUnsafe(int index, Collection<T> values) {
			if (values.size() != 0) {
				data.addAll(index, values);
				observers.added(index, values.size());
			}
		}
		
		private void setUnsafe(final int index, final T value) {
			if (index < 0 || index >= data.size()) {
				throw new IndexOutOfBoundsException();
			}
			
			observers.changing(index, 1);
			data.set(index, value);
			observers.changed(index, 1);
		}
		
		private void setUnsafe(final int index, final Collection<T> values) {
			final int count = values.size();
			
			if (index < 0 || index + count > data.size()) {
				throw new IndexOutOfBoundsException();
			}
						
			observers.changing(index, count);
			int i = index;
			for (T value : values) {
				data.set(i++, value);
			}
			observers.changed(index, count);
		}

		private void clearUnsafe() {
			final int size = data.size();
			
			if (size > 0) {
				observers.removing(0, size);
				data.clear();
				observers.removed(0, size);
			}
		}

		private void moveUnsafe(int startIndex, int newIndex, int count) {
			data.move(startIndex, newIndex, count);
			
			if (startIndex != newIndex && count > 0) {
				observers.moved(startIndex, newIndex, count);
			}
		}
		
		private void resetUnsafe(Collection<T> newItems) {
			observers.resetting();
			data.clear();
			data.addAll(newItems);
			observers.reset();
		}
	}
	
	public MutableObservableList(final ReadWriteLock lock) {
		if (lock == null) {
			throw new IllegalArgumentException("lock cannot be null");
		}
		
		this.lock = lock;
		this.data = new ArrayListEx<T>();
		this.observers = new ListObservers<T>(lock);
		this.mutator = new Mutator();
	}
	
	public final IListMutator<T> getMutator() {
		return mutator;
	}

	@Override
	public final void addObserver(IListObserver observer) {
		observers.add(observer);
	}

	@Override
	public final void removeObserver(IListObserver observer) {
		observers.remove(observer);
	}

	@Override
	public final T getAt(int index) {
		return data.get(index);
	}

	@Override
	public final int getSize() {
		return data.size();
	}
}