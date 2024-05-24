package com.example.myapplication.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.databinding.MyDataBinding;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    private MyDataBinding binding;
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this.getActivity()).get(MainViewModel.class);
        mViewModel.getLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.message.setText(s);
            }
        });
        //binding.message.setText("dddddddddddddd");
        // TODO: Use the ViewModel

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding=MyDataBinding.inflate(inflater,container,false);
        binding.setMydata(mViewModel);
        binding.mbutton.setOnClickListener(view -> {
            mViewModel.getLiveData().setValue("000");
        });
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}