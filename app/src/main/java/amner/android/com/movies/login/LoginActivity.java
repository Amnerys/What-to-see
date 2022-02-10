package amner.android.com.movies.login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import amner.android.com.movies.SplashActivity;
import amner.android.com.movies.utils.User;
import amner.android.com.movies.utils.DatabaseUtils;
import amner.android.com.movies.BasicActivity;
import amner.android.com.movies.R;

public class LoginActivity extends BasicActivity implements View.OnClickListener {

    private TextView loginStatus;
    private TextView firebaseUserID;
    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth firebaseAuth;
    private DatabaseUtils dbUtils = new DatabaseUtils();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initializing the views for the login activity
        loginStatus = findViewById(R.id.login_status);
        firebaseUserID = findViewById(R.id.firebase_user_id);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);

        // Initializing the buttons for the login activity
        findViewById(R.id.button_sign).setOnClickListener(this);
        findViewById(R.id.button_create_account).setOnClickListener(this);
        findViewById(R.id.button_sign_out).setOnClickListener(this);
        findViewById(R.id.button_verify_email).setOnClickListener(this);
        findViewById(R.id.access_movies).setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // If user is signed = not null. Update UI according to status.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        findViewById(R.id.access_movies).setVisibility(View.GONE);
        updateStatus(currentUser);

    }

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // Create an account using data provided by user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            User userObj = new User(firebaseUser.getEmail());
                            dbUtils.getUser(firebaseAuth.getCurrentUser().getUid(), userObj);
                            updateStatus(user);
                        } else {
                            // Display message if the account creation fails.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateStatus(null);
                        }
                        hideProgressDialog();
                    }
                });
    }

    //Method for login into an existing account
    private void login(String email, String password) {
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        //Login in into an existing account
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // If the login doesn't fail, load the user info
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateStatus(user);
                        } else {
                            // If login fails, display message.
                            Toast.makeText(LoginActivity.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateStatus(null);
                        }
                        if (!task.isSuccessful()) {
                            loginStatus.setText(R.string.authentication_failed);
                        }
                        hideProgressDialog();
                    }
                });
    }

    //Method to sign out
    private void signOut() {
        firebaseAuth.signOut();
        updateStatus(null);
    }

    // Method to send a verification code through email to the user.
    private void sendEmailVerification() {
        findViewById(R.id.button_verify_email).setEnabled(false);
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        findViewById(R.id.button_verify_email).setEnabled(true);
                        findViewById(R.id.access_movies).setEnabled(false);

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Verification code sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Failed to send verification code.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //Validate email and password
    private boolean validateForm() {
        boolean valid = true;
        //email
        String email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError(getString(R.string.Required_error));
            valid = false;
        } else {
            emailEditText.setError(null);
        }
        //password
        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.Required_error));
            valid = false;
        } else {
            passwordEditText.setError(null);
        }
        //If all are correct, return 'valid'
        return valid;
    }

    //Method to update user states if logged in
    private void updateStatus(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            loginStatus.setText(getString(R.string.main_screen_status,
                    user.getEmail(), user.isEmailVerified()));
            firebaseUserID.setText(getString(R.string.firebase_user, user.getUid()));
            //set visibility and access according to user state
            findViewById(R.id.buttons_linear_layout).setVisibility(View.GONE);
            findViewById(R.id.edit_texts_linear_layout).setVisibility(View.GONE);
            findViewById(R.id.login_linear_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.access_movies).setVisibility(View.VISIBLE);
            findViewById(R.id.access_movies).setEnabled(user.isEmailVerified());
            findViewById(R.id.button_verify_email).setEnabled(!user.isEmailVerified());
        } else {
            loginStatus.setText(R.string.signed_out);
            firebaseUserID.setText(null);

            findViewById(R.id.buttons_linear_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.edit_texts_linear_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.login_linear_layout).setVisibility(View.GONE);
            findViewById(R.id.access_movies).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.button_create_account:
                createAccount(emailEditText.getText().toString(), passwordEditText.getText().toString());
                break;
            case R.id.button_sign:
                login(emailEditText.getText().toString(), passwordEditText.getText().toString());
                break;
            case R.id.button_sign_out:
                signOut();
                break;
            case R.id.button_verify_email:
                sendEmailVerification();
                break;
            case R.id.access_movies:

                Intent intent = new Intent(this, SplashActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } else {
                    startActivity(intent);
                }
                break;
        }
    }
}
