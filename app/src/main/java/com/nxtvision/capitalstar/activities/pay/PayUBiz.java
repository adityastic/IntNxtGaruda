package com.nxtvision.capitalstar.activities.pay;


import androidx.appcompat.app.AppCompatActivity;

public class PayUBiz extends AppCompatActivity {
//    private String merchantKey = "2uICKv", salt = "BVShe7B4", userCredentials;
//    // These will hold all the payment parameters
//    private PaymentParams mPaymentParams;
//    // This sets the configuration
//    private PayuConfig payuConfig;
//    // Used when generating hash from SDK
//    private PayUChecksum checksum;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_payusdk);
//
//        Button onlinePayment = (Button) findViewById(R.id.btnPayNow);
//
//        Payu.setInstance(this);
//
//        int environment = PayuConstants.PRODUCTION_ENV;
//        payuConfig = new PayuConfig();
//        payuConfig.setEnvironment(environment);
//
//        mPaymentParams = new PaymentParams();
//
//        onlinePayment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                setParameter();
//                generateHashFromSDK(mPaymentParams, salt);
//            }
//        });
//    }
//
//    public void setParameter() {
//        mPaymentParams.setKey(merchantKey);
//        mPaymentParams.setAmount("100");
//        mPaymentParams.setProductInfo("product_info");
//
//        mPaymentParams.setFirstName("Cutomer Name");
//
//        mPaymentParams.setEmail("test@gmail.com");
//        mPaymentParams.setPhone("8448217808");
//        mPaymentParams.setAddress1("Customer Address");
//        mPaymentParams.setZipCode("Customer ZipCode");
//        //Set Your transaction ID. example- "USER ID "+System.currentTimeMillis()
//        mPaymentParams.setTxnId(System.currentTimeMillis()+"");
//
//        /**
//         * Surl --> Success url is where the transaction response is posted by PayU on successful transaction
//         * Furl --> Failre url is where the transaction response is posted by PayU on failed transaction
//         */
//        mPaymentParams.setSurl("https://payu.herokuapp.com/success");
//        mPaymentParams.setFurl("https://payu.herokuapp.com/failure");
//        mPaymentParams.setNotifyURL(mPaymentParams.getSurl());  //for lazy pay
//
//        mPaymentParams.setUdf1("");
//        mPaymentParams.setUdf2("");
//        mPaymentParams.setUdf3("");
//        mPaymentParams.setUdf4("");
//        mPaymentParams.setUdf5("");
//
//        /**
//         * These are used for store card feature. If you are not using it then user_credentials = "default"
//         * user_credentials takes of the form like user_credentials = "merchant_key : user_id"
//         * here merchant_key = your merchant key,
//         * user_id = unique id related to user like, email, phone number, etc.
//         * */
//        userCredentials = merchantKey + ":" + "Your CUSTOMER UNIQUE ID";
//        mPaymentParams.setUserCredentials(userCredentials);
//        //mPaymentParams.setUserCredentials(PayuConstants.DEFAULT);
//    }
//
//    public void generateHashFromSDK(PaymentParams mPaymentParams, String salt) {
//        PayuHashes payuHashes = new PayuHashes();
//        PostData postData;
//
//        // payment Hash;
//        checksum = null;
//        checksum = new PayUChecksum();
//        checksum.setAmount(mPaymentParams.getAmount());
//        checksum.setKey(mPaymentParams.getKey());
//        checksum.setTxnid(mPaymentParams.getTxnId());
//        checksum.setEmail(mPaymentParams.getEmail());
//        checksum.setSalt(salt);
//        checksum.setProductinfo(mPaymentParams.getProductInfo());
//        checksum.setFirstname(mPaymentParams.getFirstName());
//        checksum.setUdf1(mPaymentParams.getUdf1());
//        checksum.setUdf2(mPaymentParams.getUdf2());
//        checksum.setUdf3(mPaymentParams.getUdf3());
//        checksum.setUdf4(mPaymentParams.getUdf4());
//        checksum.setUdf5(mPaymentParams.getUdf5());
//
//        postData = checksum.getHash();
//        if (postData.getCode() == PayuErrors.NO_ERROR) {
//            payuHashes.setPaymentHash(postData.getResult());
//        }
//
//        // checksum for payemnt related details
//        // var1 should be either user credentials or default
//        String var1 = mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials();
//        String key = mPaymentParams.getKey();
//
//        if ((postData = calculateHash(key, PayuConstants.PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // Assign post data first then check for success
//            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(postData.getResult());
//        //vas
//        if ((postData = calculateHash(key, PayuConstants.VAS_FOR_MOBILE_SDK, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
//            payuHashes.setVasForMobileSdkHash(postData.getResult());
//
//        // getIbibocodes
//        if ((postData = calculateHash(key, PayuConstants.GET_MERCHANT_IBIBO_CODES, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
//            payuHashes.setMerchantIbiboCodesHash(postData.getResult());
//
//        if (!var1.contentEquals(PayuConstants.DEFAULT)) {
//            // get user card
//            if ((postData = calculateHash(key, PayuConstants.GET_USER_CARDS, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // todo rename storedc ard
//                payuHashes.setStoredCardsHash(postData.getResult());
//            // save user card
//            if ((postData = calculateHash(key, PayuConstants.SAVE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
//                payuHashes.setSaveCardHash(postData.getResult());
//            // delete user card
//            if ((postData = calculateHash(key, PayuConstants.DELETE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
//                payuHashes.setDeleteCardHash(postData.getResult());
//            // edit user card
//            if ((postData = calculateHash(key, PayuConstants.EDIT_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
//                payuHashes.setEditCardHash(postData.getResult());
//        }
//
//        if (mPaymentParams.getOfferKey() != null) {
//            postData = calculateHash(key, PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey(), salt);
//            if (postData.getCode() == PayuErrors.NO_ERROR) {
//                payuHashes.setCheckOfferStatusHash(postData.getResult());
//            }
//        }
//
//        if (mPaymentParams.getOfferKey() != null && (postData = calculateHash(key, PayuConstants.CHECK_OFFER_STATUS, mPaymentParams.getOfferKey(), salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) {
//            payuHashes.setCheckOfferStatusHash(postData.getResult());
//        }
//
//        // we have generated all the hases now lest launch sdk's ui
//        launchSdkUI(payuHashes);
//    }
//
//    private PostData calculateHash(String key, String command, String var1, String salt) {
//        checksum = null;
//        checksum = new PayUChecksum();
//        checksum.setKey(key);
//        checksum.setCommand(command);
//        checksum.setVar1(var1);
//        checksum.setSalt(salt);
//        return checksum.getHash();
//    }
//
//    public void launchSdkUI(PayuHashes payuHashes) {
//
//        mPaymentParams.setHash(payuHashes.getPaymentHash());
//        Intent intent = new Intent(this, PayUBaseActivity.class);
//        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
//        intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
//        intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);
//        startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
//
//    }
}
