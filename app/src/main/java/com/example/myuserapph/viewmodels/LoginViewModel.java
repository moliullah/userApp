package com.example.myuserapph.viewmodels;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myuserapph.callbacks.OnActionCompleteListener;
import com.example.myuserapph.models.EcomUser;
import com.example.myuserapph.utils.Constant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginViewModel extends ViewModel {
    public  enum AuthenticateState{
        AUTHENTICATED,  UNAUTHENTICATED
    }
    private MutableLiveData <AuthenticateState> authenticateStateLiveData;
    private MutableLiveData<EcomUser> ecomUserMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorMsgLiveData;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    public LoginViewModel(){
        authenticateStateLiveData=new MutableLiveData<>();
        errorMsgLiveData=new MutableLiveData<>();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        if (user==null){
            authenticateStateLiveData.postValue(AuthenticateState.UNAUTHENTICATED);
        }else {
            authenticateStateLiveData.postValue(AuthenticateState.AUTHENTICATED);
        }

    }

    public MutableLiveData<EcomUser> getEcomUserMutableLiveData() {
        return ecomUserMutableLiveData;
    }

    public LiveData<AuthenticateState> getAuthenticateStateLiveData() {
        return authenticateStateLiveData;
    }
    public LiveData<String> getErrorMsgLiveData() {
        return errorMsgLiveData;
    }

    public FirebaseUser getUser() {
        return user;
    }
    public void userLogin(String email,String password){
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
            user=authResult.getUser();
            authenticateStateLiveData.postValue(AuthenticateState.AUTHENTICATED);
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                errorMsgLiveData.postValue(e.getLocalizedMessage());

            }
        });

    }
    public void userRegistration(String email,String password){
        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
            user=authResult.getUser();
            authenticateStateLiveData.postValue(AuthenticateState.AUTHENTICATED);
            addUserDatabase();

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                errorMsgLiveData.postValue(e.getLocalizedMessage());
            }
        });

    }

    private void addUserDatabase() {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        final DocumentReference documentReference
                =db.collection(Constant.DbCollection.COLLECTION_USERS).document(user.getUid());
        final EcomUser ecomUser=new EcomUser(user.getUid(),null,user.getEmail(),null,null);
        documentReference.set(ecomUser).addOnSuccessListener(unused -> {

        }).addOnFailureListener(e -> {
            errorMsgLiveData.postValue(e.getLocalizedMessage());
        });
    }
    public void updateDeliveryAddress(String address, OnActionCompleteListener actionCompleteListener) {
        final DocumentReference doc =
                db.collection(Constant.DbCollection.COLLECTION_USERS)
                        .document(user.getUid());
        doc.update("deleveryAddress",address)
                .addOnSuccessListener(unused -> {
                    actionCompleteListener.onSuccess();
                })
                .addOnFailureListener(unused -> {
                    actionCompleteListener.onFailure();
                   // Toast.makeText(ge, "", Toast.LENGTH_SHORT).show();
                });
    }

    public LiveData<EcomUser> getUserData() {
        final MutableLiveData<EcomUser> userLiveData = new MutableLiveData<>();
        db.collection(Constant.DbCollection.COLLECTION_USERS)
                .document(user.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    final EcomUser ecomUser = documentSnapshot.toObject(EcomUser.class);
                   userLiveData.postValue(ecomUser);
                  ecomUserMutableLiveData.postValue(ecomUser);

                }).addOnFailureListener(e -> {

        });
       return userLiveData;
    }

    public void logout() {
        if (user != null) {
            auth.signOut();
            authenticateStateLiveData.postValue(AuthenticateState.UNAUTHENTICATED);
        }
    }
}
