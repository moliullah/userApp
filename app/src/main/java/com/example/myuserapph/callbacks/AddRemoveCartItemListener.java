package com.example.myuserapph.callbacks;


import com.example.myuserapph.models.CartModel;

public interface AddRemoveCartItemListener {
    void onCartItemAdd(CartModel cartModel, int position);
    void onCartItemRemove(String productId, int position);
}
