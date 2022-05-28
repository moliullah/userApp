package com.example.myuserapph;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.myuserapph.callbacks.OnActionCompleteListener;
import com.example.myuserapph.databinding.FragmentConfirmationToBinding;
import com.example.myuserapph.models.CartModel;
import com.example.myuserapph.models.EcomUser;
import com.example.myuserapph.models.OrderModel;
import com.example.myuserapph.models.OrderSettingsModel;
import com.example.myuserapph.utils.Constant;
import com.example.myuserapph.viewmodels.LoginViewModel;
import com.example.myuserapph.viewmodels.OrderViewModel;
import com.example.myuserapph.viewmodels.ProductViewModel;

import java.util.Calendar;
import java.util.Locale;

public class ConfirmationToFragment extends Fragment {
    private String TAG=ConfirmationToFragment.class.getSimpleName();
    private FragmentConfirmationToBinding binding;
    private ProductViewModel productViewModel;
    private LoginViewModel loginViewModel;
    private OrderViewModel orderViewModel;

    public ConfirmationToFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentConfirmationToBinding.inflate(inflater);
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        orderViewModel.getOrderSettings();
        orderViewModel.getSettingsModelMutableLiveData().observe(getViewLifecycleOwner(),
                orderSettingsModel -> {
                    if (orderSettingsModel != null) {
                        orderViewModel.setOrderSettingsModel(orderSettingsModel);
                        updateUI(orderSettingsModel);
                    }
                });
        for (CartModel c : productViewModel.cartModelList) {
            final CardView cv = (CardView) inflater.inflate(R.layout.cart_item_row_simple, null, true);
            final TextView nameTV = cv.findViewById(R.id.cartConfirmRowProductNameTV);
            final TextView priceTV = cv.findViewById(R.id.itemTotalPlusQtyTV);
            nameTV.setText(c.getProductName());
            priceTV.setText(c.getProductQty()+"x"+c.getProductPrice());
            binding.cartItemLayout.addView(cv);
        }
        final double total = productViewModel.calculateTotalPrice(productViewModel.cartModelList);
        binding.totalTV.setText(Constant.TAKA+total);
        binding.confirmPaymentMethodTV.setText(productViewModel.paymentMethod);
        binding.orderBtn.setOnClickListener(v -> {

            placeOrder();
        });
        return binding.getRoot();
    }
    private void placeOrder() {
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
        final OrderModel orderModel = new OrderModel();
        orderModel.setUserId(loginViewModel.getUser().getUid());
        orderModel.setOrderTimeStamp(calendar.getTimeInMillis());
        orderModel.setOrderDay(calendar.get(Calendar.DAY_OF_MONTH));
        orderModel.setOrderMonth(calendar.get(Calendar.MONTH));
        orderModel.setOrderYear(calendar.get(Calendar.YEAR));
        orderModel.setOrderStatus(Constant.OrderStatus.PENDING);
        orderModel.setDiscount(orderViewModel.getOrderSettingsModel().getDiscount());
        orderModel.setVat(orderViewModel.getOrderSettingsModel().getVat());
        orderModel.setDeliveryCharge(orderViewModel.getOrderSettingsModel().getDeliveryCharge());
        orderModel.setShippingAddress(loginViewModel.getEcomUserMutableLiveData().getValue().getDeleveryAddress());
        orderModel.setGrandTotal(orderViewModel.getGrandTotal(
                productViewModel.calculateTotalPrice(
                        productViewModel.cartModelList),
                orderViewModel.getOrderSettingsModel().getVat(),
                orderViewModel.getOrderSettingsModel().getDiscount(),
                orderViewModel.getOrderSettingsModel().getDeliveryCharge()
        ));

        orderViewModel.addNewOrder(orderModel, productViewModel.cartModelList, new OnActionCompleteListener() {
            @Override
            public void onSuccess() {
                productViewModel.clearCart(
                        loginViewModel.getUser().getUid(),
                        productViewModel.cartModelList
                );
                Log.e(TAG,"Changing Frgment");
                Navigation.findNavController(getActivity(), R.id.fragmentContainerView2)
                        .navigate(R.id.cfTo_to_sf);
                Log.e(TAG,"Changed Frgment");
            }
            @Override
            public void onFailure() {

            }
        });
    }
    private void updateUI(OrderSettingsModel orderSettingsModel) {
        binding.deliveryChargeTV.setText(String.valueOf(orderSettingsModel.getDeliveryCharge()));
        binding.vatLabelTV.setText("VAT("+orderSettingsModel.getVat()+"%)");
        binding.discountLabelTV.setText("Discount("+orderSettingsModel.getDiscount()+"%)");
        binding.discountTV.setText(Constant.TAKA+String.format("%.1f",
                orderViewModel.getDiscountAmount(
                        productViewModel.calculateTotalPrice(productViewModel.cartModelList),
                        orderSettingsModel.getDiscount()
                )));
        binding.vatTV.setText(Constant.TAKA+String.format("%.1f",
                orderViewModel.getVatAmount(
                        productViewModel.calculateTotalPrice(productViewModel.cartModelList),
                        orderSettingsModel.getVat()
                )));
        binding.grandTotalTV.setText(Constant.TAKA+String.format("%.1f",
                orderViewModel.getGrandTotal(
                        productViewModel.calculateTotalPrice(
                                productViewModel.cartModelList),
                        orderSettingsModel.getVat(),
                        orderSettingsModel.getDiscount(),
                        orderSettingsModel.getDeliveryCharge()
                )));
    }
}