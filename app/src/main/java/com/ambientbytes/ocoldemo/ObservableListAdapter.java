package com.ambientbytes.ocoldemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.ambientbytes.observables.IListObserver;
import com.ambientbytes.observables.IReadOnlyObservableList;

/**
 * Created by pakarpen on 3/28/17.
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
            //notifyItemRange
        }

        @Override public void resetting() {}

        @Override public void reset() {
            notifyDataSetChanged();
        }
    }

    private final static class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }

    }

    private final IViewFactory viewFactory;
    private final IReadOnlyObservableList<T> observableList;
    private final IListObserver listObserver;

    public static RecyclerView.Adapter createAdapterForList(Object list, IViewFactory viewFactory) {
        return new ObservableListAdapter((IReadOnlyObservableList)list, viewFactory);
    }

    public ObservableListAdapter(IReadOnlyObservableList<T> observableList, IViewFactory viewFactory) {
        this.viewFactory = viewFactory;
        this.observableList = observableList;
        this.listObserver = new Observer();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = viewFactory.createView(parent, viewType);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        viewFactory.bindView(holder.itemView, holder.getItemViewType(), observableList.getAt(position));
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        viewFactory.unbindView(holder.itemView, holder.getItemViewType());
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
