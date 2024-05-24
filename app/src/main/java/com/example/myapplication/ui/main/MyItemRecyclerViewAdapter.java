package com.example.myapplication.ui.main;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.FragmentItemBinding;
import com.example.myapplication.ui.main.placeholder.PlaceholderContent.PlaceholderItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;

    private final ItemFragment _manager;
    public MyItemRecyclerViewAdapter(List<PlaceholderItem> items,ItemFragment manager) {
        mValues = items;
        _manager=manager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);
        holder.mDetails.setText(mValues.get(position).details);

        holder.mLinearLayout.setOnClickListener(view -> {
            //mValues.remove(holder.mItem);
            //this.notifyItemRemoved(position);
            //简单对话框
            _manager.showDialog(holder.mItem);

        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class CustomViewDialog extends DialogFragment{
        PlaceholderItem _item;
        public  CustomViewDialog(PlaceholderItem item)
        {
            _item=item;
        }
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity())
                    .setMessage(_item.toString())
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                        //执行代码
                        dialogInterface.dismiss();//销毁对话框
                    })
                    .setNegativeButton("取消", (dialogInterface, i) -> {
                        //执行代码
                        dialogInterface.dismiss();//销毁对话框
                    });
            return builder.create();
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mDetails;
        public final LinearLayout mLinearLayout;
        public PlaceholderItem mItem;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            mDetails = binding.details;
            mLinearLayout = binding.linearlayout1;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}