package com.example.myuserapph;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myuserapph.callbacks.OnActionCompleteListener;
import com.example.myuserapph.databinding.FragmentCheckoutBinding;
import com.example.myuserapph.utils.Constant;
import com.example.myuserapph.viewmodels.LoginViewModel;
import com.example.myuserapph.viewmodels.ProductViewModel;

public class CheckoutFragment extends Fragment {
    private String TAG=CheckoutFragment.class.getSimpleName();
    private FragmentCheckoutBinding binding;
    private ProductViewModel productViewModel;
    private LoginViewModel loginViewModel;
   // private String paymentmethod= Constant.PaymentMehod.COD;
    public CheckoutFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentCheckoutBinding.inflate(inflater);
        productViewModel = new ViewModelProvider(requireActivity())
                .get(ProductViewModel.class);
        loginViewModel = new ViewModelProvider(requireActivity())
                .get(LoginViewModel.class);
        binding.paymentRG.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton rb=container.findViewById(i);
            //paymentmethod=rb.getText().toString();
            productViewModel.paymentMethod = rb.getText().toString();
        });
        loginViewModel.getUserData().observe(getViewLifecycleOwner(), ecomUser -> {
            if (ecomUser.getDeleveryAddress()!= null) {
                binding.deleveryAddressET.setText(ecomUser.getDeleveryAddress());
            }
        });
        binding.nextBtn.setOnClickListener(v->{
            Log.e(TAG,"OnClick");
            final String address=binding.deleveryAddressET.getText().toString();
            if (address.isEmpty()){
                Log.e(TAG,"isEmpty");
                binding.deleveryAddressET.setError("provide a valid or require address");
                return;
            }

            loginViewModel.updateDeliveryAddress(address, new OnActionCompleteListener() {
                @Override
                public void onSuccess() {
                    Log.e(TAG,"fragmentChange");
                    //Toast.makeText(getActivity(), "address saved", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(container).navigate(R.id.cof_to_To_cf);

                }

                @Override
                public void onFailure() {
                    Toast.makeText(getActivity(), "could not save address", Toast.LENGTH_SHORT).show();
                }
            });

        });
        return binding.getRoot();
    }
}