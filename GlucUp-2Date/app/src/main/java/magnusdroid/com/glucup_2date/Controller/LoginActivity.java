package magnusdroid.com.glucup_2date.Controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import magnusdroid.com.glucup_2date.Model.MLogin;
import magnusdroid.com.glucup_2date.Model.ViewDialog;
import magnusdroid.com.glucup_2date.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // Keep track of the login task to ensure we can cancel it if requested.
    private UserLoginTask mAuthTask = null;
    // Get shared preferences
    private PrefManager prefManager;
    // Call model for login
    private MLogin mLogin;
    // JsonObject response from server
    private JSONObject jObject;
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mIpserverView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up shared preferences.
        prefManager = new PrefManager(this);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.document);
        mPasswordView = (EditText) findViewById(R.id.password);
        mIpserverView = (EditText) findViewById(R.id.ipserver);
        mIpserverView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    checkNetwork();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkNetwork()){
                    attemptLogin();
                }else{
                    dialog();
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    //Check network connection
    private boolean checkNetwork(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
        /*ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // HTTP Login
            attemptLogin();
        } else {
            // Show toast
            dialog();
        }*/
    }

    private void dialog() {
        String alert= "Revisa las unidades y el valor";
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this)
                .setTitle("Algo no anda bien....")
                .setIcon(R.mipmap.alert)
                .setMessage(alert)
                .setNeutralButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        builder1.show();
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(400);
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
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mIpserverView.setError(null);

        // Store values at the time of the login attempt.
        String document = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String ipserver = mIpserverView.getText().toString();
        Context context = this;

        prefManager.setIpServer(ipserver);

        Log.i("Pref",""+prefManager.IpServer());

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) && TextUtils.isEmpty(document) && TextUtils.isEmpty(ipserver)) {
            error(getString(R.string.field_empties));
            focusView = mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(document) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(ipserver)){
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!TextUtils.isEmpty(document) && TextUtils.isEmpty(password) && !TextUtils.isEmpty(ipserver)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!TextUtils.isEmpty(document) && !TextUtils.isEmpty(password) && TextUtils.isEmpty(ipserver)){
            mIpserverView.setError(getString(R.string.error_field_required));
            focusView = mIpserverView;
            cancel = true;
        }

        /*/ Check for a valid document address.
        if (TextUtils.isEmpty(document)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(document)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(document, password, context);
            mAuthTask.execute((Void) null);
        }
    }

    private  void error(String msj){
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setTitle("Alerta");
        alertDialog.setMessage(msj);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);

    }

    private void server(){
        ViewDialog alert = new ViewDialog();
        alert.showDialog(LoginActivity.this, "Error de conexión al servidor");
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

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mDoc;
        private final String mPassword;
        private final Context mContext;

        UserLoginTask(String doc, String password, Context context) {
            mDoc = doc;
            mPassword = password;
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            mLogin = new MLogin();
            try {
                jObject = mLogin.validateLogin(mDoc,mPassword, mContext);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            Intent intent;
            String userLogin;

            try {
                int status = jObject.getInt("status");
                if (status == 0) {
                    userLogin = jObject.getString("name");
                    prefManager.setUser(userLogin);
                    prefManager.setPassword(mPassword);
                    prefManager.setDoc(mDoc);
                    prefManager.setRol("pacient");
                    Log.i("Prefs",""+prefManager.getUser());
                    Log.i("Prefs",""+prefManager.getDoc());
                    Log.i("Prefs",""+prefManager.getPassword());
                    intent = new Intent(LoginActivity.this, PacientActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                    Toast.makeText(LoginActivity.this, getString(R.string.welcome_login), Toast.LENGTH_SHORT).show();
                    finish();
                }else if (status == 1) {
                    userLogin = jObject.getString("name");
                    prefManager.setUser(userLogin);
                    prefManager.setPassword(mPassword);
                    prefManager.setDoc(mDoc);
                    prefManager.setRol("personal");
                    Log.i("Prefs",""+prefManager.getUser());
                    Log.i("Prefs",""+prefManager.getDoc());
                    Log.i("Prefs",""+prefManager.getPassword());
                    intent = new Intent(LoginActivity.this, PersonalActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                    Toast.makeText(LoginActivity.this, getString(R.string.welcome_login), Toast.LENGTH_SHORT).show();
                    finish();
                }else if (status == 2){
                    error(getString(R.string.user_no_found));
                }else{
                    //error(getString(R.string.server_no_found));
                    server();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

