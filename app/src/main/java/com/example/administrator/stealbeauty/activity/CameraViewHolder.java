package com.example.administrator.stealbeauty.activity;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by BigGod on 2016/10/21.
 */
public class CameraViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder{
    private T mBinding;
    public CameraViewHolder(T binding) {
        super(binding.getRoot());
        mBinding = binding;
    }
    public T getmBinding(){
        return mBinding;
    }
}
