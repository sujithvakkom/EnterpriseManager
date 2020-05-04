package sujithvakkomfreelance.com.enterprisemanager;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sujithvakkomfreelance.com.components.AppLiterals;
import sujithvakkomfreelance.com.components.Util;
import sujithvakkomfreelance.com.components.localdata.dataproviders.DeliveryHeaderProvider;
import sujithvakkomfreelance.com.components.localdata.dataproviders.DeliveryLineProvider;
import sujithvakkomfreelance.com.components.localdata.dataproviders.UserDetail;
import sujithvakkomfreelance.com.components.models.LoginToken;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryHeader;
import sujithvakkomfreelance.com.components.models.homedelivery.DeliveryLine;
import sujithvakkomfreelance.com.components.web.IWebClient;
import sujithvakkomfreelance.com.components.web.WebResources;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_CONTACTS;
import static sujithvakkomfreelance.com.components.AppLiterals.REQUEST_READ_CONTACTS;
import static sujithvakkomfreelance.com.components.AppLiterals.REQUEST_READ_INTERNET;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUserNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private AppCompatImageView mCompanyLogoView;
    private View parentView;
    private boolean connectionError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getStatusBarColor();
        }

        parentView = findViewById(R.id.login_activity_parent_view);
        // Set up the login form.
        mUserNameView = findViewById(R.id.user_name);
        populateAutoComplete();

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mCompanyLogoView = findViewById(R.id.companyLogo);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                    Manifest.permission.INTERNET)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{INTERNET},
                        REQUEST_READ_INTERNET);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            getAppLogo();
        }
        String hQ =

                Util.getSettingById(LoginActivity.this, R.string.pref_head_office_key);
    }

    private void getAppLogo() {
        try {
            mCompanyLogoView.setImageBitmap(WebResources.getOnlineImageResources("https://offers.mashreq.com/images/others/GrandStores_L.png", null
            ));
        } catch (MalformedURLException e) {
            if (e.getMessage() != null)
                Log.e("LoginActiviy.getAppLogo", e.getMessage());
            mCompanyLogoView.setImageResource(R.drawable.ic_users);
        } catch (Exception e) {
            if (e.getMessage() != null)
                Log.e("LoginActiviy.getAppLogo", e.getMessage());
            mCompanyLogoView.setImageResource(R.drawable.ic_users);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(LoginActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUserNameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        } else if (requestCode == REQUEST_READ_INTERNET) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAppLogo();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUserNameView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, UserDetail> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected UserDetail doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {

                String baseUrl =
                        Util.getSettingById(LoginActivity.this, R.string.pref_head_office_key);
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create());

                Retrofit retrofit = builder.build();

                IWebClient client = retrofit.create(IWebClient.class);

                Call<UserDetail> call = client.GetAuthFor(mEmail, mPassword);
                UserDetail user = call.execute().body();
                return user;
            } catch (Exception e) {
                e.printStackTrace();
                connectionError = true;
            }

            // TODO: register the new account here.
            return null;
        }

        @Override
        protected void onPostExecute(final UserDetail user) {
            mAuthTask = null;
            if (user != null) {
                String role = user.getRole();
                if (role.equals(AppLiterals.DRIVER)) {
                    user.save(getApplicationContext());
                    Snackbar.make(parentView, "Loading home...", Snackbar.LENGTH_LONG).show();
                    loadProfile();
                } else {
                    mUserNameView.setError(getString(R.string.error_invalid_user));
                    mUserNameView.requestFocus();

                    Snackbar snackbar =
                            Util.SimpleSnackBar(parentView, "Not a driver.", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    view.setBackgroundColor(getResources().getColor(R.color.error_background));
                    ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                            getResources().getColor(R.color.error_foreground)
                    );
                    showProgress(false);
                    snackbar.show();
                }
            } else if (!connectionError) {
                mPasswordView.setText("");
                mUserNameView.setError(getString(R.string.error_invalid_user));
                mUserNameView.requestFocus();

                Snackbar snackbar =
                        Util.SimpleSnackBar(parentView, "Username or password is wrong", Snackbar.LENGTH_LONG);
                View view = snackbar.getView();
                view.setBackgroundColor(getResources().getColor(R.color.error_background));
                ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                        getResources().getColor(R.color.error_foreground)
                );
                showProgress(false);
                snackbar.show();
            } else {
                Snackbar snackbar =
                        Util.SimpleSnackBar(parentView, "Error in connection", Snackbar.LENGTH_LONG);
                View view = snackbar.getView();
                view.setBackgroundColor(getResources().getColor(R.color.error_background));
                ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                        getResources().getColor(R.color.error_foreground)
                );
                showProgress(false);
                snackbar.show();
                connectionError = false;
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void loadProfile() {
        final LoginToken user = Util.getToken(getApplicationContext());
        if (user != null) {
            String role = Util.getJsonValue(user.profile, AppLiterals.PROFILE_ROOT, AppLiterals.PROFILE_ROLE);
            if (role == null) role = "";
            if (role.equals(AppLiterals.DRIVER)) {
                try {
                    final DeliveryHeaderProvider headerProvider = new DeliveryHeaderProvider();
                    final DeliveryLineProvider lineProvider = new DeliveryLineProvider();
                    ArrayList<DeliveryHeader> headerList = headerProvider.getList(getApplicationContext(), user.user_name, AppLiterals.DELIVERY_STATUS.DELIVERED);
                    for (DeliveryHeader header : headerList
                    ) {
                        if (header.DeliveryLines.isEmpty()) {
                            header.DeliveryLines = lineProvider.getList(getApplicationContext(), header.HeaderId, AppLiterals.DELIVERY_STATUS.DELIVERED);
                        }
                    }

                    String baseUrl = Util.getSettingById(getApplicationContext(), R.string.pref_head_office_key);
                    Retrofit.Builder builder = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create(
                                    AppLiterals.APPLICATION_GSON_BUILDER));

                    Retrofit retrofit = builder.build();

                    IWebClient client = retrofit.create(IWebClient.class);

                    for (final DeliveryHeader header : headerList
                    ) {
                        Call<DeliveryHeader> call = client.SetDeliveryJobs(header);

                        call.enqueue(new Callback<DeliveryHeader>() {
                            @Override
                            public void onResponse(Call<DeliveryHeader> call, Response<DeliveryHeader> response) {
                                DeliveryHeader headerTemp;
                                for (DeliveryLine line : header.DeliveryLines) {
                                    line.Status = AppLiterals.DELIVERY_STATUS.POSTED;
                                    lineProvider.save(getApplicationContext(), line, header.HeaderId);
                                }
                                header.Status = AppLiterals.DELIVERY_STATUS.POSTED;
                                headerProvider.save(getApplicationContext(), header);
                            }

                            @Override
                            public void onFailure(Call<DeliveryHeader> call, Throwable t) {
                            }
                        });
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {
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

                    call.enqueue(new Callback<List<DeliveryHeader>>() {
                        @Override
                        public void onResponse(Call<List<DeliveryHeader>> call, Response<List<DeliveryHeader>> response) {
                            List<DeliveryHeader> result = response.body();
                            if (result != null) {
                                DeliveryHeaderProvider provider = new DeliveryHeaderProvider();
                                for (DeliveryHeader x : result) {
                                    x.setDriver(user.user_name);
                                    if (provider.getDeliveryJob(getApplicationContext(), x.HeaderId) == null)
                                        provider.save(getApplicationContext(), x);
                                }
                            }
                            loadActivity(user);
                        }

                        @Override
                        public void onFailure(Call<List<DeliveryHeader>> call, Throwable t) {
                            loadActivity(user);
                        }
                    });

                } catch (Exception e) {
                    loadActivity(user);
                    e.printStackTrace();
                }
            }
        } else {
            Snackbar snackbar =
                    Util.SimpleSnackBar(parentView, "Unknown error occurred.", Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            view.setBackgroundColor(getResources().getColor(R.color.error_background));
            ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(
                    getResources().getColor(R.color.error_foreground)
            );
            showProgress(false);
            snackbar.show();
        }
    }

    private void loadActivity(LoginToken user) {
        Intent activityIntent;
        activityIntent = new Intent(this, DeliveryActivity.class);
        activityIntent.putExtra(AppLiterals.USER_DETAIL, user);
        showProgress(false);
        startActivity(activityIntent);
        finish();
    }
}