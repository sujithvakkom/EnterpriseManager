package sujithvakkomfreelance.com.enterprisemanager;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sujithvakkomfreelance.com.components.AppLiterals;
import sujithvakkomfreelance.com.components.BiometricUtils;
import sujithvakkomfreelance.com.components.IUpdateTokenCallback;
import sujithvakkomfreelance.com.components.Util;
import sujithvakkomfreelance.com.components.localdata.dataproviders.DeliveryHeaderProvider;
import sujithvakkomfreelance.com.components.localdata.dataproviders.DeliveryLineProvider;
import sujithvakkomfreelance.com.components.localdata.dataproviders.UserDetail;
import sujithvakkomfreelance.com.components.localdata.dataproviders.delivery;
import sujithvakkomfreelance.com.components.localdata.dataproviders.sync;
import sujithvakkomfreelance.com.components.models.LoginToken;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryHeader;
import sujithvakkomfreelance.com.components.web.IWebClient;
import sujithvakkomfreelance.com.enterprisemanager.customviews.DeliveredJobsFragment;
import sujithvakkomfreelance.com.enterprisemanager.customviews.DeliveryAcceptedJobFragment;
import sujithvakkomfreelance.com.enterprisemanager.customviews.ImageDeliveryFragment;

import static sujithvakkomfreelance.com.components.AppLiterals.DELIVERY_HEADER_CLASS_NAME;
import static sujithvakkomfreelance.com.components.AppLiterals.DELIVERY_STATUS.KEY_NAME;
import static sujithvakkomfreelance.com.components.AppLiterals.REQUEST_FORGROUND_SERVICE;
import static sujithvakkomfreelance.com.components.AppLiterals.REQUEST_USE_BIOMETRIC;

public class DeliveryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DeliveryAcceptedJobFragment.OnFragmentInteractionListener,
        ImageDeliveryFragment.OnFragmentInteractionListener,
        DeliveredJobsFragment.OnListFragmentInteractionListener
{
    private static final String TAG = "DeliveryActivity";
    private View parentView;
    private ProgressBar appBarDeliveryJobProgress;
    private BiometricPrompt mBiometricPrompt;
    private String mToBeSignedMessage;
    private Signature signature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try{
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.FOREGROUND_SERVICE)) {}
            else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.FOREGROUND_SERVICE},
                            REQUEST_FORGROUND_SERVICE);
                }
            }
        }else {
            startServices();
        }}catch (Exception e){e.printStackTrace();}
        try{
            startServices();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getStatusBarColor();
        }

        parentView = findViewById(R.id.delivery_job_viewpager);
        appBarDeliveryJobProgress = findViewById(R.id.appBarDeliveryJobProgress);

        FloatingActionButton fab = findViewById(R.id.delivery_bacdode_scanner);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadBarcodeScanner();
            }
        });

        DrawerLayout drawer = findViewById(R.id.delivery_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.delivery_nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        AppCompatTextView delivery_profileNameView;
        delivery_profileNameView = navigationView.getHeaderView(0).findViewById(R.id.delivery_profileNameView);
        AppCompatTextView delivery_profileDescriptionView;
        delivery_profileDescriptionView = navigationView.getHeaderView(0).findViewById(R.id.delivery_profileDescriptionView);
        delivery_profileNameView.setText(Util.getToken(getApplicationContext()).full_name);
        delivery_profileDescriptionView.setText(Util.getToken(getApplicationContext()).vehicle_code);
        if (savedInstanceState == null) loadDeliveryJobFragment();
    }

    private void loadDeliveryJobFragment() {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.delivery_job_content, new DeliveryAcceptedJobFragment()).commit();
    }

    private void loadDeliveryJobCompletedFragment() {
        DeliveredJobsFragment fragment = DeliveredJobsFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.delivery_job_content,fragment).commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void loadCheckinFragment() {
        final LoginToken token = Util.getToken(getApplicationContext());
        if(token.signature!=null && token.signature.isEmpty()){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.USE_BIOMETRIC)) {
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.USE_BIOMETRIC},
                                REQUEST_USE_BIOMETRIC);
                    }
                }
            } else {

                mBiometricPrompt = new BiometricPrompt.Builder(this)
                        .setTitle(getString(R.string.biometric_enroll))
                        .setSubtitle(getString(R.string.save_your_finger_auth))
                        .setDescription(getString(R.string.scan_fingerprint))
                        .setNegativeButton(getString(R.string.cancel), this.getMainExecutor(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .build();
                android.os.CancellationSignal cancellationSignal = getCancellationSignal();
                BiometricPrompt.AuthenticationCallback authenticationCallback = getAuthenticationCallback(false);
            }

        }else {
            Snackbar snackbar =
                    Util.SimpleSnackBar(parentView, "Please enroll.", Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            view.setBackgroundColor(getResources().getColor(R.color.error_background));
            ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                    getResources().getColor(R.color.error_foreground)
            );
            snackbar.show();
        }
    }

    private void loadBioMetricEnrollFragment() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.USE_BIOMETRIC)) {
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.USE_BIOMETRIC},
                            REQUEST_USE_BIOMETRIC);
                }
            }
        } else {
            final LoginToken token = Util.getToken(getApplicationContext());
            Util.updateToken(token, getApplicationContext(),
                    new IUpdateTokenCallback() {
                        @Override
                        public void updated(UserDetail userDetail) {
                            if(userDetail!=null) {
                                userDetail.save(getApplicationContext());
                                enrollBiometric(userDetail);
                            }
                        }
                    }
            );
        }
    }

    private void enrollBiometric(UserDetail token) {
        if(token!=null) {
            if ((token.signature == null ? "" : token.signature).isEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if (isSupportBiometricPrompt()) {


                        // Init signature
                        Signature signature;
                        try {
                            // Send key name and challenge to the server, this message will be verified with registered public key on the server
                            mToBeSignedMessage = new StringBuilder()
                                    .append(KEY_NAME)
                                    .append(":")
                                    // Generated by the server to protect against replay attack
                                    .append("12345")
                                    .toString();
                            signature = initSignature(KEY_NAME);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        // Create biometricPrompt
                        mBiometricPrompt = new BiometricPrompt.Builder(this)
                                .setDescription("Description")
                                .setTitle("Title")
                                .setSubtitle("Subtitle")
                                .setNegativeButton("Cancel", getMainExecutor(), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Log.i(TAG, "Cancel button clicked");
                                    }
                                })
                                .build();
                        CancellationSignal cancellationSignal = getCancellationSignal();
                        BiometricPrompt.AuthenticationCallback authenticationCallback = getAuthenticationCallback(true);

                        // Show biometric prompt
                        if (signature != null) {
                            Log.i(TAG, "Show biometric prompt");
                            mBiometricPrompt.authenticate(new BiometricPrompt.CryptoObject(signature), cancellationSignal, getMainExecutor(), authenticationCallback);
                        }
                    } else {
                        Snackbar snackbar =
                                Util.SimpleSnackBar(parentView, "Not supported.", Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.error_background));
                        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                                getResources().getColor(R.color.error_foreground)
                        );
                        snackbar.show();
                    }
                }

            } else {
                Snackbar snackbar =
                        Util.SimpleSnackBar(parentView, "All ready enrolled.", Snackbar.LENGTH_LONG);
                View view = snackbar.getView();
                view.setBackgroundColor(getResources().getColor(R.color.error_background));
                ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                        getResources().getColor(R.color.error_foreground)
                );
                snackbar.show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private BiometricPrompt.AuthenticationCallback getAuthenticationCallback(final boolean isEnroll) {
        // Callback for biometric authentication result
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                Snackbar snackbar =
                        Util.SimpleSnackBar(parentView, errString.toString(), Snackbar.LENGTH_LONG);
                View view = snackbar.getView();
                view.setBackgroundColor(getResources().getColor(R.color.error_background));
                ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                        getResources().getColor(R.color.error_foreground)
                );
                snackbar.show();
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                Log.i(TAG, "onAuthenticationSucceeded");
                super.onAuthenticationSucceeded(result);
                Signature signature = result.getCryptoObject().getSignature();
                try {
                    signature.update(mToBeSignedMessage.getBytes());
                    String signatureString = Base64.encodeToString(signature.sign(), Base64.URL_SAFE);
                    LoginToken tempUser = Util.getToken(getApplicationContext());
                    tempUser.signature = signatureString;
                    if(isEnroll) {
                        if (updateBiometric(tempUser.user_name, signatureString, tempUser.session))
                            tempUser.save(getApplicationContext());
                    }
                    else {
                        validateUser(signatureString);
                    }
                    Snackbar snackbar =
                            Util.SimpleSnackBar(parentView, "Success", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    view.setBackgroundColor(getResources().getColor(R.color.success_background));
                    ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                            getResources().getColor(R.color.success_foreground)
                    );
                    snackbar.show();
                    Log.i("Finger Print String",signatureString);
                    // Normally, ToBeSignedMessage and Signature are sent to the server and then verified
                } catch (SignatureException e) {
                    throw new RuntimeException();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        };
    }

    private void validateUser(String signatureString) {
        LoginToken token = Util.getToken(getApplicationContext());
        if(signatureString == token.signature){
            Snackbar snackbar =
                    Util.SimpleSnackBar(parentView, "Success", Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            view.setBackgroundColor(getResources().getColor(R.color.success_background));
            ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                    getResources().getColor(R.color.success_foreground)
            );
            snackbar.show();
        }
        else {
            Snackbar snackbar =
                    Util.SimpleSnackBar(parentView, "Failed", Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            view.setBackgroundColor(getResources().getColor(R.color.error_background));
            ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                    getResources().getColor(R.color.error_foreground)
            );
            snackbar.show();}
    }

    private boolean updateBiometric(String user_name, final String signature, final String session) {
        final boolean[] result = new boolean[1];
        try {
            String baseUrl =
                    Util.getSettingById(DeliveryActivity.this,R.string.pref_head_office_key);
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(
                            AppLiterals.APPLICATION_GSON_BUILDER));

            Retrofit retrofit = builder.build();

            IWebClient client = retrofit.create(IWebClient.class);

            Call<String> call = client.UpdateBioMetric(user_name,signature,session);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.body() != null) {
                        result[0] = response.body().equals(signature);

                        LoginToken tocken = Util.getToken(getApplicationContext());
                        tocken.signature = signature;
                        tocken.save(getApplicationContext());
                        Snackbar snackbar = Util.SimpleSnackBar(parentView,
                                response.body().equals("") ? "Cannot enroll" : response.body().equals(signature) ? "Enrolled" : "Already enrolled",
                                Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(response.body().equals("") ? R.color.error_background : response.body().equals(signature) ? R.color.success_background : R.color.error_background));
                        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                                getResources().getColor(response.body().equals(signature) ? R.color.success_foreground : R.color.error_foreground)
                        );
                        snackbar.show();
                    } else {

                        Snackbar snackbar = Util.SimpleSnackBar(parentView,
                                "Server did not reply",
                                Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.error_background));
                        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(getResources().getColor(R.color.error_foreground)
                        );
                        snackbar.show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Snackbar snackbar = Util.SimpleSnackBar(parentView, "Error in connection.", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    view.setBackgroundColor(getResources().getColor(R.color.error_background));
                    ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                            getResources().getColor(R.color.error_foreground)
                    );
                    snackbar.show();
                    result[0] = false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            result[0] = false;
        }
        return result[0];
    }

    private android.os.CancellationSignal getCancellationSignal() {
        // With this cancel signal, we can cancel biometric prompt operation
        android.os.CancellationSignal cancellationSignal = new android.os.CancellationSignal();
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {

            }
        });
        return cancellationSignal;
    }

    /**
     * Generate NIST P-256 EC Key pair for signing and verification
     * @param keyName
     * @param invalidatedByBiometricEnrollment
     * @return
     * @throws Exception
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private KeyPair generateKeyPair(String keyName, boolean invalidatedByBiometricEnrollment) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");

        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyName,
                KeyProperties.PURPOSE_SIGN)
                .setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1"))
                .setDigests(KeyProperties.DIGEST_SHA256,
                        KeyProperties.DIGEST_SHA384,
                        KeyProperties.DIGEST_SHA512)
                // Require the user to authenticate with a biometric to authorize every use of the key
                .setUserAuthenticationRequired(true)
                // Generated keys will be invalidated if the biometric templates are added more to user device
                .setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);

        keyPairGenerator.initialize(builder.build());

        return keyPairGenerator.generateKeyPair();
    }

    @Nullable
    private KeyPair getKeyPair(String keyName) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        if (keyStore.containsAlias(keyName)) {
            // Get public key
            PublicKey publicKey = keyStore.getCertificate(keyName).getPublicKey();
            // Get private key
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyName, null);
            // Return a key pair
            return new KeyPair(publicKey, privateKey);
        }
        return null;
    }

    @Nullable
    private Signature initSignature (String keyName) throws Exception {
        KeyPair keyPair = getKeyPair(keyName);

        if (keyPair != null) {
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(keyPair.getPrivate());
            return signature;
        }
        return null;
    }

    /**
     * Before generating a key pair with biometric prompt, we need to check system feature to ensure that the device supports fingerprint, iris, or face.
     * Currently, there is no FEATURE_IRIS and FEATURE_FACE constant on PackageManager
     * So, only check FEATURE_FINGERPRINT
     * @return
     */
    private boolean isSupportBiometricPrompt() {
        PackageManager packageManager = this.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT) &&
                BiometricUtils.isPermissionGranted(this) &&
                BiometricUtils.isBiometricPromptEnabled() &&
                BiometricUtils.isSdkVersionSupported() &&
                BiometricUtils.isBiometricPromptEnabled()
        ) {
            return true;
        }
        return false;
    }

    private void loadBarcodeScanner() {
        Intent intent = new Intent(getApplicationContext(), BarcodeScannerActivity.class);
        startActivityForResult(intent, AppLiterals.BARCODE_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_delivery_home:
                loadDeliveryJobFragment();
                break;
            case R.id.nav_completed_jobs:
                loadDeliveryJobCompletedFragment();
                break;
            case R.id.nav_biometric_enroll:
                try {
                    loadBioMetricEnrollFragment();
                }catch (Exception e){
                    Snackbar snackbar = Util.SimpleSnackBar(parentView, "Operation cannot perform.", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    view.setBackgroundColor(getResources().getColor(R.color.error_background));
                    ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                            getResources().getColor(R.color.error_foreground)
                    );
                    snackbar.show();
                }
                break;
            case R.id.nav_checkin:
                loadCheckinFragment();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.delivery_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.delivery_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delivery_job, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logoff) {
            Util.Logoff(getApplicationContext());
            finish();
            return true;
        }

        if(id == R.id.action_sync){
            refreshOrders();
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void refreshOrders() {
        final LoginToken user = Util.getToken(getApplicationContext());
        String role = Util.getJsonValue(user.profile, AppLiterals.PROFILE_ROOT, AppLiterals.PROFILE_ROLE);
        if (role == null) role = "";
        if (role.equals(AppLiterals.DRIVER)) {
            try {
                ArrayList<sync> synchList = sync.getSynchList(getApplicationContext(), false);
                if (synchList != null)
                    for (final sync sync : synchList)
                        if (sync.getTag() == DELIVERY_HEADER_CLASS_NAME) {

                            try {
                                DeliveryHeader delivery = (DeliveryHeader) sync.getValueBase();
                                String baseUrl =
                                        Util.getSettingById(getApplicationContext(), R.string.pref_head_office_key);
                                Retrofit.Builder builder = new Retrofit.Builder()
                                        .baseUrl(baseUrl)
                                        .addConverterFactory(GsonConverterFactory.create(
                                                AppLiterals.APPLICATION_GSON_BUILDER));

                                Retrofit retrofit = builder.build();

                                IWebClient client = retrofit.create(IWebClient.class);

                                Call<DeliveryHeader> call = client.SetDeliveryJobs(delivery);

                                call.enqueue(new Callback<DeliveryHeader>() {
                                    @Override
                                    public void onResponse(Call<DeliveryHeader> call, Response<DeliveryHeader> response) {
                                        sync.setSynced(true);
                                        sync.save(getApplicationContext());
                                    }

                                    @Override
                                    public void onFailure(Call<DeliveryHeader> call, Throwable t) {
                                        sync.setSynced(false);
                                        sync.save(getApplicationContext());
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                String baseUrl = Util.getSettingById(getApplicationContext(), R.string.pref_head_office_key);
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create(
                                AppLiterals.APPLICATION_GSON_BUILDER));

                Retrofit retrofit = builder.build();

                IWebClient client = retrofit.create(IWebClient.class);

                Call<List<DeliveryHeader>> call = client.GetDeliveryJobs(
                        Util.getToken(getApplicationContext()).vehicle_code,
                        Integer.toString(AppLiterals.DELIVERY_STATUS.DISPATCHED));
                showProgress(true);
                call.enqueue(new Callback<List<DeliveryHeader>>() {
                    @Override
                    public void onResponse(Call<List<DeliveryHeader>> call, Response<List<DeliveryHeader>> response) {
                        List<DeliveryHeader> result = response.body();
                        if (result != null) {
                            DeliveryHeaderProvider provider = new DeliveryHeaderProvider();
                            for (DeliveryHeader x : result) {
                                x.setDriver(user.user_name);
                                provider.save(getApplicationContext(), x);
                            }
                        }
                        loadDeliveryJobFragment();
                        showProgress(false);
                    }

                    @Override
                    public void onFailure(Call<List<DeliveryHeader>> call, Throwable t) {
                        showProgress(false);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppLiterals.BARCODE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    String returnValue = data.getStringExtra(AppLiterals.BARCODE);
                    if (!returnValue.isEmpty()) {
                        Snackbar.make(parentView, "Packing List: " + returnValue, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        loadReceiptJob(returnValue);
                    }
                }
                break;
            case AppLiterals.CUSTOMER_SURVEY:
                if (resultCode == RESULT_OK && (data.getStringExtra(AppLiterals.YES_OR_NO) == AppLiterals.YES)) {
                    reloadDeliveries();
                }
                break;
            case AppLiterals.DELIVERY_ACTIONS:
                if (data != null) {
                    try {
                        String yesOrNo = data.getStringExtra(AppLiterals.YES_OR_NO);

                        String message = data.getStringExtra(AppLiterals.MESSAGE);
                        Snackbar.make(parentView, message, Snackbar.LENGTH_LONG)
                                .setAction("Message", null)
                                .show();
                        if (resultCode == RESULT_OK && (yesOrNo == AppLiterals.YES)) {
                            try {
                                Snackbar.make(parentView, message, Snackbar.LENGTH_LONG)
                                        .setAction("Message", null)
                                        .show();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            reloadDeliveries();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
        }
        reloadDeliveries();
    }

    private void reloadDeliveries() {
        try {
            DeliveryAcceptedJobFragment fragment = (DeliveryAcceptedJobFragment)
                    getSupportFragmentManager().findFragmentById(R.id.delivery_job_content);
            if(fragment!=null ) fragment.loadDeliveries(AppLiterals.DELIVERY_STATUS.DISPATCHED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void loadReceiptJob(String receipt){
        /*
        DeliveryHeaderProvider headerProvider = new DeliveryHeaderProvider();
        DeliveryHeader header = headerProvider.getDeliveryJob(getApplicationContext(),receipt);
        if(header!=null)if(header.Status == AppLiterals.DELIVERY_STATUS.DISPATCHED) {
            DeliveryLineProvider provider = new DeliveryLineProvider();
        }
        else{}
        */
        try {
            String baseUrl =
                    Util.getSettingById(getApplicationContext(),R.string.pref_head_office_key);
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(
                            AppLiterals.APPLICATION_GSON_BUILDER));

            Retrofit retrofit = builder.build();

            IWebClient client = retrofit.create(IWebClient.class);

            Call<DeliveryHeader> call = client.GetDeliveryJob(receipt,
                    Util.getToken(getApplicationContext()).vehicle_code,
                    String.valueOf(AppLiterals.DELIVERY_STATUS.READY)
                    );
            showProgress(true);

            call.enqueue(new Callback<DeliveryHeader>() {
                @Override
                public void onResponse(Call<DeliveryHeader> call, Response<DeliveryHeader> response) {
                    DeliveryHeader result = response.body();
                    if (result != null) if (!result.DeliveryLines.isEmpty()) {
                        loadAccept(result);
                        Snackbar.make(parentView, result.getCustomerLabel(), Snackbar.LENGTH_LONG)
                                .show();
                    } else
                        Snackbar.make(parentView, "No receipt found.", Snackbar.LENGTH_LONG)
                                .setAction("Not Found", null).show();
                    showProgress(false);
                }

                @Override
                public void onFailure(Call<DeliveryHeader> call, Throwable t) {
                    Log.e(TAG, t.getMessage(), t);
                    Snackbar.make(parentView, t.getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Error", null).show();
                    showProgress(false);
                }
            });

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }

    }

    /*
    private void loadReceipt(String receipt) {
        try {
            String baseUrl =
                    Util.getSettingById(getApplicationContext(),R.string.pref_head_office_key);

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(
                            AppLiterals.APPLICATION_GSON_BUILDER));

            Retrofit retrofit = builder.build();

            IWebClient client = retrofit.create(IWebClient.class);

            Call<List<delivery>> call = client.GetDelivery(receipt, AppLiterals.DELIVERY_STATUS.READY,
                    Util.getToken(getApplicationContext()).vehicle_code);
            showProgress(true);

            call.enqueue(new Callback<List<delivery>>() {
                @Override
                public void onResponse(Call<List<delivery>> call, Response<List<delivery>> response) {
                    List<delivery> result = response.body();
                    if (result != null) if (!result.isEmpty()) {
                        loadAccept(result.get(0));
                        Snackbar.make(parentView, result.get(0).getDescription(), Snackbar.LENGTH_LONG)
                                .setAction("Item Description", null).show();
                    } else
                        Snackbar.make(parentView, "No receipt found.", Snackbar.LENGTH_LONG)
                                .setAction("Not Found", null).show();
                    showProgress(false);
                }

                @Override
                public void onFailure(Call<List<delivery>> call, Throwable t) {
                    Log.e(TAG, t.getMessage(), t);
                    Snackbar.make(parentView, t.getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Error", null).show();
                    showProgress(false);
                }
            });

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }
    */
    private void loadAccept(DeliveryHeader delivery) {
        Intent intent = new Intent(getApplicationContext(), AcceptJobActivity.class);
        intent.putExtra(AppLiterals.INSTANCE_STATE_KEYS.DELIVERY_DETAILS, delivery);
        this.startActivityForResult(intent, AppLiterals.JOB_ACCEPTANCE);
    }
    /*
    private void loadAccept(delivery delivery) {
        Intent intent = new Intent(getApplicationContext(), AcceptJobActivity.class);
        intent.putExtra(AppLiterals.INSTANCE_STATE_KEYS.DELIVERY_DETAILS, delivery);
        this.startActivityForResult(intent, AppLiterals.JOB_ACCEPTANCE);
    }

 */

    public void loadDeliveryActions(DeliveryHeader delivery) {
        Intent intent = new Intent(getApplicationContext(),DeliveryActionsActivity.class);
        intent.putExtra(AppLiterals.INSTANCE_STATE_KEYS.DELIVERY_DETAILS,delivery);
        this.startActivityForResult(intent,AppLiterals.DELIVERY_ACTIONS);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            parentView.setVisibility(show ? View.GONE : View.VISIBLE);
            parentView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    parentView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            appBarDeliveryJobProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            appBarDeliveryJobProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    appBarDeliveryJobProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            appBarDeliveryJobProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            parentView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void startServices() {
        if (!isServiceRunning(GSDeliveryService.class.getName())) {
            Intent intentGSMerchantService = new Intent(this, GSDeliveryService.class);
            ContextCompat.startForegroundService(this, intentGSMerchantService);
        }
    }

    @Override
    public void onListFragmentInteraction(DeliveryHeader item) {

    }

    private boolean isServiceRunning(String serviceName){
        boolean serviceRunning = false;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> l = am.getRunningServices(50);
        Iterator<ActivityManager.RunningServiceInfo> i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningServiceInfo runningServiceInfo = i
                    .next();

            if(runningServiceInfo.service.getClassName().equals(serviceName)){
                serviceRunning = true;

                if(runningServiceInfo.foreground)
                {
                    //service run in foreground
                }
            }
        }
        return serviceRunning;
    }
}