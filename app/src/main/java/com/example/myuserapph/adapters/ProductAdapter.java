package com.example.myuserapph.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myuserapph.callbacks.AddRemoveCartItemListener;
import com.example.myuserapph.callbacks.OnProductItemClickListener;
import com.example.myuserapph.databinding.ProductRowItemBinding;
import com.example.myuserapph.models.CartModel;
import com.example.myuserapph.models.ProductModel;
import com.example.myuserapph.models.UserProductModel;
public class ProductAdapter extends ListAdapter<UserProductModel, ProductAdapter.ProductViewHolder> {
    private OnProductItemClickListener listener;
    private AddRemoveCartItemListener cartItemListener;
    public ProductAdapter(OnProductItemClickListener listener
            ,AddRemoveCartItemListener cartItemListenerr) {
        super(new ProductDiff());
        this.listener = listener;
        this.cartItemListener=cartItemListenerr;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ProductRowItemBinding binding = ProductRowItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ProductViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
       final UserProductModel userProductModel=getItem(position);
       holder.bind(userProductModel);
       // holder.bind(getItem(position));
        if (getItem(position).isInCart()){
            holder.binding.imageViewAddPId.setVisibility(View.GONE);
            holder.binding.imageViewRemovePId.setVisibility(View.VISIBLE);
        }else {
                holder.binding.imageViewAddPId.setVisibility(View.VISIBLE);
                holder.binding.imageViewRemovePId.setVisibility(View.GONE);

        }
        holder.binding.imageViewAddPId.setOnClickListener(view -> {
            userProductModel.setInCart(true);
           // notifyItemChanged(position,userProductModel);
            final CartModel cartModel=new CartModel(userProductModel.getProductId(),
                    userProductModel.getProductName(),userProductModel.getPrice(),1);
            cartItemListener.onCartItemAdd(cartModel,position);

        });
        holder.binding.imageViewRemovePId.setOnClickListener(view -> {
            userProductModel.setInCart(false);
           // notifyItemChanged(position,userProductModel);
            cartItemListener.onCartItemRemove(userProductModel.getProductId(),position);
        });

    }
    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ProductRowItemBinding binding;
        public ProductViewHolder(ProductRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.productRowCardView.setOnClickListener(v -> {
                listener.onProductItemClicked(
                        getItem(getAdapterPosition()).getProductId());
            });
        }
        public void bind(UserProductModel userProductModel) {
            binding.setProduct(userProductModel);
        }
    }
    static class ProductDiff extends DiffUtil.ItemCallback<UserProductModel>{
        @Override
        public boolean areItemsTheSame(@NonNull UserProductModel oldItem, @NonNull UserProductModel newItem) {
            return oldItem.getProductId().equals(newItem.getProductId());
        }
        @Override
        public boolean areContentsTheSame(@NonNull UserProductModel oldItem, @NonNull UserProductModel newItem) {
            return oldItem.getProductId().equals(newItem.getProductId());
        }
    }
}
