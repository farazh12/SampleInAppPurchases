# SampleInAppPurchases
In App Library Version 4
library created by Faraz Hussain & Yasir khan


1: Import YFinapplibraryV4 as a Module in your App
2: Implement this in gradle

   android{
    compileOptions {
            sourceCompatibility JavaVersion.VERSION_1_8
            targetCompatibility JavaVersion.VERSION_1_8
        }
    }

    implementation project(":YFinapplibraryV4")

3: Implementation in Java

    BillingProcessor billingProcessor = new BillingProcessor(this, "licence_key_if_needed", "android.test.purchased"), new OnBillingProcessorListener() {
        @Override
        public void onBillingProcessorConnected() {
        }

        @Override
        public void onBillingProcessorConnectionFailed(int responseCode, String message) {

        }

        @Override
        public void onPurchasedSuccessful() {
            billingRestartDialog();
        }

        @Override
        public void onPurchasedFailed() {

        }

        @Override
        public void onBillingProcessorDisconnected() {

        }
    });

4: Make Purchases using purchase method
    billingProcessor.purchase();


5: End Connection before Destroy Activity
    @Override
    protected void onDestroy() {
        billingProcessor.endConnection();
        super.onDestroy();
    }




