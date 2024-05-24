package com.example.myapplication.ui.main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentBlankBinding;

public class BlankFragment extends Fragment {

    private MainViewModel mViewModel;

    FragmentBlankBinding binding;

    public static BlankFragment newInstance() {
        return new BlankFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding=FragmentBlankBinding.inflate(inflater,container,false);
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this.getActivity()).get(MainViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.mytextview01.setText(s);
            }
        });
    }

}