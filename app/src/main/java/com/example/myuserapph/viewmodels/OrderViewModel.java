package com.example.myuserapph.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myuserapph.callbacks.OnActionCompleteListener;
import com.example.myuserapph.models.CartModel;
import com.example.myuserapph.models.OrderModel;
import com.example.myuserapph.models.OrderSettingsModel;
import com.example.myuserapph.utils.Constant;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;


public class OrderViewModel extends ViewModel {



    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OrderSettingsModel orderSettingsModel;
    private MutableLiveData<OrderSettingsModel> settingsModelMutableLiveData =
            new MutableLiveData<>();
    public MutableLiveData<OrderSettingsModel> getSettingsModelMutableLiveData() {
        return settingsModelMutableLiveData;
    }

    private MutableLiveData<List<OrderModel>> orderListLiveData = new MutableLiveData<>();

    public void getOrderSettings() {

        db.collection(Constant.DbCollection.COLLECTION_ORDER_SETTINGS)
                .document(Constant.DbCollection.DOCUMENT_ORDER_SETTING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    settingsModelMutableLiveData.postValue(
                            value.toObject(OrderSettingsModel.class));
                });

    }



    public double getDiscountAmount(double price, double discount) {
        return (price * discount) / 100;
    }

    public double getVatAmount(double price, double vat) {
        return (price * vat) / 100;
    }

    public double getGrandTotal(double price, double vat, double discount, double deliveryCharge) {
        return (price - getDiscountAmount(price, discount))
                +getVatAmount(price, vat) + deliveryCharge;
    }
    public OrderSettingsModel getOrderSettingsModel() {
        return orderSettingsModel;
    }

    public void setOrderSettingsModel(OrderSettingsModel orderSettingsModel) {
        this.orderSettingsModel = orderSettingsModel;
    }

    public void addNewOrder(OrderModel orderModel, List<CartModel> cartModelList, OnActionCompleteListener actionCompleteListener) {
        final WriteBatch writeBatch = db.batch();
        final DocumentReference orderDoc =
                db.collection(Constant.DbCollection.COLLECTION_ORDERS)
                        .document();
        orderModel.setOrderId(orderDoc.getId());
        writeBatch.set(orderDoc, orderModel);

        for (CartModel c : cartModelList) {
            final DocumentReference doc =
                    db.collection(Constant.DbCollection.COLLECTION_ORDERS)
                            .document(orderDoc.getId())
                            .collection(Constant.DbCollection.COLLECTION_ORDER_DETAILS)
                            .document();
            writeBatch.set(doc, c);
        }

        writeBatch.commit()
                .addOnSuccessListener(unused -> {
                    actionCompleteListener.onSuccess();
                })
                .addOnFailureListener(unused -> {
                    actionCompleteListener.onFailure();
                });
    }



    public void getAllOrders(String userId) {
        db.collection(Constant.DbCollection.COLLECTION_ORDERS)
                .whereEqualTo("userId", userId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;

                    final List<OrderModel> items = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        items.add(doc.toObject(OrderModel.class));
                    }

                    orderListLiveData.postValue(items);
                });
    }


    public MutableLiveData<List<OrderModel>> getOrderListLiveData() {
        return orderListLiveData;
    }
}

