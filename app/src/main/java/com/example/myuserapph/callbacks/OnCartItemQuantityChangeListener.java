package com.example.myuserapph.callbacks;



import com.example.myuserapph.models.CartModel;

import java.util.List;

public interface OnCartItemQuantityChangeListener {
    void onCartItemQuantityChange(List<CartModel> models);
}
