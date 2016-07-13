package com.afinos.api.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afinos.api.binder.ItemBinder;
import com.afinos.api.listener.ClickHandler;

import java.lang.ref.WeakReference;
import java.util.Collection;

public class BindingPagerAdapter<T> extends PagerAdapter implements View.OnClickListener{
    private static final int ITEM_MODEL = -124;
    private final WeakReferenceOnListChangedCallback onListChangedCallback;
    private final ItemBinder<T> itemBinder;
    private ClickHandler<T> clickHandler;
    private ObservableList<T> items;
    private LayoutInflater inflater;
    private float mPageWidth = 1f;

    public BindingPagerAdapter(ItemBinder<T> itemBinder, @Nullable Collection<T> items) {
        this.itemBinder = itemBinder;
        this.onListChangedCallback = new WeakReferenceOnListChangedCallback<>(this);
        setItems(items);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        if (inflater == null) {
            inflater = LayoutInflater.from(collection.getContext());
        }

        final T item = items.get(position);
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, itemBinder.getLayoutRes(item), null, false);
        binding.setVariable(itemBinder.getBindingVariable(item), item);
        binding.getRoot().setTag(ITEM_MODEL, item);
        binding.getRoot().setOnClickListener(this);
        binding.executePendingBindings();
        collection.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public float getPageWidth(int position) {
        return mPageWidth;
    }

    public void setPageWidth(float pageWidth){
        this.mPageWidth = pageWidth;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (clickHandler != null)
        {
            T item = (T) v.getTag(ITEM_MODEL);
            clickHandler.onClick(item,v);
        }
    }

    public void setClickHandler(ClickHandler<T> clickHandler)
    {
        this.clickHandler = clickHandler;
    }

    public ObservableList<T> getItems() {
        return items;
    }

    public void setItems(@Nullable Collection<T> items) {
        if (this.items == items) {
            return;
        }

        if (this.items != null) {
            this.items.removeOnListChangedCallback(onListChangedCallback);
            notifyDataSetChanged();
        }

        if (items instanceof ObservableList) {
            this.items = (ObservableList<T>) items;
            notifyDataSetChanged();
            this.items.addOnListChangedCallback(onListChangedCallback);
        } else if (items != null) {
            this.items = new ObservableArrayList<>();
            this.items.addOnListChangedCallback(onListChangedCallback);
            this.items.addAll(items);
        } else {
            this.items = null;
        }
    }

    private static class WeakReferenceOnListChangedCallback<T> extends ObservableList.OnListChangedCallback {
        private final WeakReference<BindingPagerAdapter<T>> adapterReference;

        public WeakReferenceOnListChangedCallback(BindingPagerAdapter<T> bindingRecyclerViewAdapter) {
            this.adapterReference = new WeakReference<>(bindingRecyclerViewAdapter);
        }

        @Override
        public void onChanged(ObservableList sender) {
            PagerAdapter adapter = adapterReference.get();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
            PagerAdapter adapter = adapterReference.get();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
            PagerAdapter adapter = adapterReference.get();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
            PagerAdapter adapter = adapterReference.get();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
            PagerAdapter adapter = adapterReference.get();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }
}