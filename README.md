# SampleInAppPurchases
In App Library Version 4
library created by Yasir khan


implement this in gradle

       android{

        compileOptions {
            sourceCompatibility JavaVersion.VERSION_1_8
            targetCompatibility JavaVersion.VERSION_1_8
        }

}

        implementation project(":YFinapplibraryV4")







            BillingProcessor billingProcessor = new BillingProcessor(this, "", getString(R.string.SKU_PRODUCT_ID), new OnBillingProcessorListener() {
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

    }



    public void billingRestartDialog() {
        Toast.makeText(HomeActivity.this, "Purchased", Toast.LENGTH_SHORT).show();
        MySharedPreferences.setIsPurchased(this, true);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setIcon(R.drawable.main_icon)
                .setMessage("Remove Ads App Purchased Restart needed for applying Changes to app.")
                .setTitle("Purchased")
                .setCancelable(false)
                .create();
        builder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(HomeActivity.this, Splash_MenuActivity.class));
                HomeActivity.this.finish();
            }
        });
        builder.show();
    }



    public void removeAd(View view) {
        if (isOnline()){
        billingProcessor.purchase();
    }else {
           AlertDialog dialog= new AlertDialog.Builder(this)
                    .setTitle("connection failed")
                    .setMessage("not connected to the internet")
                    .setIcon(R.drawable.main_icon)
                    .setPositiveButton("close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create();
            dialog.show();
        }
    }





    private boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }






    @Override
    protected void onDestroy() {
        super.onDestroy();
        billingProcessor.endConnection();
    }




