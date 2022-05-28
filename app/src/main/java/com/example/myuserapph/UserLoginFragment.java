package com.example.myuserapph;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myuserapph.databinding.FragmentUserLoginBinding;
import com.example.myuserapph.viewmodels.LoginViewModel;

public class UserLoginFragment extends Fragment {
    private String TAG=UserLoginFragment.class.getSimpleName();
    private FragmentUserLoginBinding binding;
    private LoginViewModel loginViewModel;
    private boolean isLogin;
    private String phoneNumber;
    public UserLoginFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentUserLoginBinding.inflate(inflater);
        loginViewModel=new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        binding.userLBtn.setOnClickListener(view -> {
            isLogin=true;
            authenticate();
        });
        binding.userRBtn.setOnClickListener(view -> {
            isLogin=false;
            authenticate();
        });
       /* loginViewModel.getAuthenticateStateLiveData().observe(getViewLifecycleOwner(), new Observer<LoginViewModel.AuthenticateState>() {
            @Override
            public void onChanged(LoginViewModel.AuthenticateState authenticateState) {
                if (authenticateState== LoginViewModel.AuthenticateState.AUTHENTICATED){
                    Navigation.findNavController(container).navigate(R.id.ulf_to_plf);
                    Log.e(TAG,"Welcome plf");
                }
            }
        });

        */

        loginViewModel.getErrorMsgLiveData().observe(getViewLifecycleOwner(), s -> {
            binding.utvTv.setText(s);
        });
        return binding.getRoot();
    }

    private void authenticate() {
        final String email = binding.userEmailET.getText().toString();
        final String password = binding.userPasswordET.getText().toString();

        if (isLogin) {
            loginViewModel.userLogin(email, password);
        }else {
           // loginViewModel.userRegistration(email,password);
           // createPhoneAuthFlow();
        }
    }

    private void createPhoneAuthFlow() {

    }
}