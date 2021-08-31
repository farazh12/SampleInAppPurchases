package com.soloftech.inapplibraryv4;

import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;

import org.jetbrains.annotations.NotNull;

public interface OnBillingProcessorListener {
    void onBillingProcessorConnected();
    void onBillingProcessorConnectionFailed(int responseCode, String message);
    void onPurchasedSuccessful();
    void onPurchasedFailed();
    void onBillingProcessorDisconnected();
}
