package com.ambientbytes.ocoldemo;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ambientbytes.observables.IListObserver;
import com.ambientbytes.observables.IReadOnlyObservableList;

/**
 * Universal adapter that binds an observable list to a RecyclerView
 * @author Pavel Karpenko
 */
public class ObservableListAdapter<T> extends RecyclerView.Adapter {

    private final class Observer implements IListObserver {

        @Override public void added(int startIndex, int count) {
            notifyItemRangeInserted(startIndex, count);
        }

        @Override public void changing(int startIndex, int count) {}

        @Override public void changed(int startIndex, int count) {
            notifyItemRangeChanged(startIndex, count);
        }

        @Override public void removing(int startIndex, int count) {}

        @Override public void removed(int startIndex, int count) {
            notifyItemRangeRemoved(startIndex, count);
        }

        @Override public void moved(int oldStartIndex, int newStartIndex, int count) {
            if (count == 1) {
                notifyItemMoved(oldStartIndex, newStartIndex);
            } else {
                //
                // RecyclerView does not support moves or ranges, so we simply update the entire range of items
                // affected by the reported move.
                //
                if (oldStartIndex < newStartIndex) {
                    notifyItemRangeChanged(oldStartIndex, newStartIndex + count - oldStartIndex);
                } else {
                    notifyItemRangeChanged(newStartIndex, oldStartIndex + count - newStartIndex);
                }
            }
        }

        @Override public void resetting() {}

        @Override public void reset() {
            notifyDataSetChanged();
        }
    }
    private final IViewHolderFactory<T> viewHolderFactory;
    private final IReadOnlyObservableList<T> observableList;
    private final IListObserver listObserver;

    public ObservableListAdapter(IReadOnlyObservableList<T> observableList, IViewHolderFactory<T> factory) {
        this.viewHolderFactory = factory;
        this.observableList = observableList;
        this.listObserver = new Observer();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewHolderFactory.createViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        viewHolderFactory.bind(holder, holder.getItemViewType(), observableList.getAt(position));
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        viewHolderFactory.unbind(holder, holder.getItemViewType());
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return observableList.getSize();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        observableList.addObserver(listObserver);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        observableList.removeObserver(listObserver);
    }

    @Override
    public int getItemViewType(int position) {
        return viewHolderFactory.getViewTypeId(observableList.getAt(position));
    }
}
