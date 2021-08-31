package com.soloftech.inapplibraryv4;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
// library created by Yasir khan date 29/07/2021
public class BillingProcessor {

    private static final String TAG = "BillingProcessor";

    private BillingClient billingClient;
    private Context context;
    private String licenceKey;
    private OnBillingProcessorListener onBillingProcessorListener;
    private String productId;

    private List<String> skuList;
    private SkuDetails sku = null;

    public BillingProcessor(Context context, String licenceKey,String productId, OnBillingProcessorListener onBillingProcessorListener) {
        this.context = context;
        this.licenceKey = licenceKey;
        this.productId=productId;
        this.onBillingProcessorListener = onBillingProcessorListener;
        setProductId(productId);
        setBillingClient(context);
    }


    public void setProductId(String productId) {
        this.skuList =new ArrayList<>();
        skuList.add(productId);
    }

    public void setBillingClient(Context context) {
        if(skuList==null){
            Log.d(TAG, "setBillingClient: Provide product id First by calling setProductId(String ProductId);!!!");
            return;
        }

        PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, purchases) -> {
            // To be implemented in a later section.
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (Purchase purchase : purchases) {
                    handlePurchase(purchase);
                }
            }else if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.USER_CANCELED){
                if(onBillingProcessorListener !=null){
                    onBillingProcessorListener.onPurchasedFailed();
                }
            }
        };

        billingClient = BillingClient.newBuilder(context)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NotNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    if(onBillingProcessorListener!=null){
                        onBillingProcessorListener.onBillingProcessorConnected();
                        getSkuDetails();
                    }
                }else{
                    if(onBillingProcessorListener!=null){
                        onBillingProcessorListener.onBillingProcessorConnectionFailed(billingResult.getResponseCode(), billingResult.getDebugMessage());
                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                if(onBillingProcessorListener!=null)
                onBillingProcessorListener.onBillingProcessorDisconnected();
            }
        });
    }


    public void getSkuDetails() {
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
            billingClient.querySkuDetailsAsync(params.build(),
                    (billingResult, skuDetailsList) -> {
//                                    // Process the result.
                        sku = skuDetailsList.get(0);
                    });
    }

    public void checkIsPurchased(){
        if(billingClient==null){
            Log.d(TAG, "checkIsPurchased: Billing Process is not initialized");
            return;
        }
        if(!billingClient.isReady()){
            Log.d(TAG, "checkIsPurchased: Billing Process is not Ready yet");
            return;
        }
        billingClient.queryPurchaseHistoryAsync(productId, (billingResult, list) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                if(onBillingProcessorListener!=null){
                    onBillingProcessorListener.onPurchasedSuccessful();
                }
            }else{
                onBillingProcessorListener.onBillingProcessorConnected();
            }
        });
    }

    public void endConnection(){
        if(billingClient!=null)
        billingClient.endConnection();
    }

    public void purchase() {
        if(sku==null){
            Log.d(TAG, "purchase: SKU must not be null");
            getSkuDetails();
            return;
        }

        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(sku)
                .build();
        billingClient.launchBillingFlow((Activity) context, billingFlowParams);
    }

    void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if(onBillingProcessorListener !=null){
                onBillingProcessorListener.onPurchasedSuccessful();
            }
        }
    }


    public boolean isReady() {
        return billingClient.isReady();
    }
}
