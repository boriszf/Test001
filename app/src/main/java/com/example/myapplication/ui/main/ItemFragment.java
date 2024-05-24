package com.example.myapplication.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.ui.main.placeholder.PlaceholderContent;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public void showDialog(PlaceholderContent.PlaceholderItem item) {
        MyItemRecyclerViewAdapter.CustomViewDialog dialog = new MyItemRecyclerViewAdapter.CustomViewDialog(item);
        dialog.show(getActivity().getSupportFragmentManager(), "mymitem");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    public  MyItemRecyclerViewAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        //滚动列表
        RecyclerView recyclerView = view.findViewById(R.id.list);
        Context context = recyclerView.getContext();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        DividerItemDecoration mDivider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(mDivider);
        adapter=new MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS,this);
        recyclerView.setAdapter(adapter);

        //上下刷新
        RefreshLayout refreshLayout = (RefreshLayout) view;
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            PlaceholderContent.reflashData();
            refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            adapter.notifyDataSetChanged();
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            PlaceholderContent.reflashData();
            refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            adapter.notifyDataSetChanged();
        });

        return view;
    }
}