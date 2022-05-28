package com.example.myuserapph.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myuserapph.databinding.CardConfarmationRowBinding;
import com.example.myuserapph.models.CartModel;

public class CartConfarmationAdapter extends ListAdapter<CartModel, CartConfarmationAdapter.CartConfarmationViewHolder> {


    public CartConfarmationAdapter() {
        super(new CartConfDiff());
    }

    @NonNull
    @Override
    public CartConfarmationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final CardConfarmationRowBinding binding=CardConfarmationRowBinding.
                inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CartConfarmationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartConfarmationViewHolder holder, int position) {
        holder.bind(getItem(position));

    }

    public class CartConfarmationViewHolder extends RecyclerView.ViewHolder {
        private CardConfarmationRowBinding binding;
        public CartConfarmationViewHolder(CardConfarmationRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }

        public void bind(CartModel item) {
            binding.setCartModel(item);
        }
    }


    public static class CartConfDiff extends DiffUtil.ItemCallback<CartModel>{
        @Override
        public boolean areItemsTheSame(@NonNull CartModel oldItem, @NonNull CartModel newItem) {
            return oldItem.getProductId().equals(newItem.getProductId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartModel oldItem, @NonNull CartModel newItem) {
            return oldItem.getProductId().equals(newItem.getProductId());
        }
    }
}
