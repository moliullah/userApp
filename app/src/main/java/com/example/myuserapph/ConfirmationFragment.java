package com.example.myuserapph;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myuserapph.adapters.CartConfarmationAdapter;
import com.example.myuserapph.databinding.FragmentConfirmationBinding;
import com.example.myuserapph.viewmodels.LoginViewModel;
import com.example.myuserapph.viewmodels.ProductViewModel;

public class ConfirmationFragment extends Fragment {
    private FragmentConfirmationBinding binding;
    public ConfirmationFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return binding.getRoot();
    }
}