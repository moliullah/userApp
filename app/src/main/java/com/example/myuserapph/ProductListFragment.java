package com.example.myuserapph;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myuserapph.adapters.ProductAdapter;
import com.example.myuserapph.callbacks.AddRemoveCartItemListener;
import com.example.myuserapph.callbacks.OnActionCompleteListener;
import com.example.myuserapph.callbacks.OnProductItemClickListener;
import com.example.myuserapph.databinding.FragmentProductListBinding;
import com.example.myuserapph.databinding.FragmentUserLoginBinding;
import com.example.myuserapph.models.CartModel;
import com.example.myuserapph.viewmodels.LoginViewModel;
import com.example.myuserapph.viewmodels.ProductViewModel;
public class ProductListFragment extends Fragment {
    private String TAG= ProductListFragment.class.getSimpleName();
    private FragmentProductListBinding binding;
    private LoginViewModel loginViewModel;
    private ProductViewModel productViewModel;
    private  ProductAdapter adapter;
    public ProductListFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(inflater);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        loginViewModel.getAuthenticateStateLiveData().observe(getViewLifecycleOwner(), new Observer<LoginViewModel.AuthenticateState>() {
            @Override
            public void onChanged(LoginViewModel.AuthenticateState authenticateState) {
                Log.e(TAG,"Welcome plf");
                if (authenticateState== LoginViewModel.AuthenticateState.UNAUTHENTICATED){
                    Navigation.findNavController(container).navigate(R.id.plf_to_ulf);
                    Log.e(TAG,"Welcome plf");
                }
            }
        });
        productViewModel = new ViewModelProvider(requireActivity())
                .get(ProductViewModel.class);
        adapter = new ProductAdapter(productId -> {
            // TODO: 2/3/2022 go to details page with this id
        }, cartItemListener);
        binding.productRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.productRV.setAdapter(adapter);
        productViewModel.productListLiveData.observe(getViewLifecycleOwner(),
                productList -> {
                    //adapter.submitList(productList);
                    if (!productList.isEmpty()){
                        productViewModel.getAllCartItems(loginViewModel.getUser().getUid());
                    }
                });

        productViewModel.userProductListLiveData.observe(getViewLifecycleOwner(),userpm->{
            adapter.submitList(userpm);
        });
        return binding.getRoot();
    }
    private AddRemoveCartItemListener cartItemListener=new AddRemoveCartItemListener() {
        @Override
        public void onCartItemAdd(CartModel cartModel, int position) {
            productViewModel.addToCart(cartModel, loginViewModel.getUser().getUid(), new OnActionCompleteListener() {
                @Override
                public void onSuccess() {
                    adapter.notifyItemChanged(position);
                }

                @Override
                public void onFailure() {

                }
            });

        }

        @Override
        public void onCartItemRemove(String productId, int position) {
            productViewModel.removeFromCart(
                    loginViewModel.getUser().getUid(),
                    productId, new OnActionCompleteListener() {
                        @Override
                        public void onSuccess() {
                            adapter.notifyItemChanged(position);
                        }

                        @Override
                        public void onFailure() {

                        }
                    }
            );

        }
    };
}