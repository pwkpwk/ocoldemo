package com.ambientbytes.ocoldemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.ambientbytes.observables.IListObserver;
import com.ambientbytes.observables.IReadOnlyObservableList;

/**
 * Universal adapter that binds an observable list to a RecyclerView
 * @Author Pavel Karpenko
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

    private final static class ViewHolder extends RecyclerView.ViewHolder {

        private Object viewModel;

        public ViewHolder(View itemView) {
            super(itemView);
            viewModel = null;
        }

        void attachViewModel(Object viewModel) {
            this.viewModel = viewModel;
        }

        Object detachViewModel() {
            Object vm = viewModel;
            viewModel = null;
            return vm;
        }

    }

    private final IViewFactory viewFactory;
    private final IReadOnlyObservableList<T> observableList;
    private final IListObserver listObserver;

    public ObservableListAdapter(IReadOnlyObservableList<T> observableList, IViewFactory viewFactory) {
        this.viewFactory = viewFactory;
        this.observableList = observableList;
        this.listObserver = new Observer();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = viewFactory.createView(parent, viewType);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        viewFactory.bindView(holder.itemView, holder.getItemViewType(), observableList.getAt(position));
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        ViewHolder vh = (ViewHolder) holder;

        viewFactory.unbindView(holder.itemView, holder.getItemViewType(), vh.detachViewModel());

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
        return viewFactory.getViewTypeId(observableList.getAt(position));
    }
}
