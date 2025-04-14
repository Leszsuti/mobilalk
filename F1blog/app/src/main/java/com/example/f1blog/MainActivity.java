package com.example.f1blog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private final static int secret_key=99;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth=FirebaseAuth.getInstance();
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account=task.getResult(ApiException.class);
                Log.i("MainActivity","google_sign_in :)");
                firebaseAuthWithGoogle(account.getIdToken());
            }catch (ApiException e){
                Log.i("MainActivity","hiba google_sign_in");
            }

        }
    }
    public void firebaseAuthWithGoogle(String idToken){
        AuthCredential authCredential= GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startBlog();
                }else{
                    Toast.makeText(MainActivity.this,"Google died",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void login(View view) {
        ButtonAnimation.pressEffect(findViewById(R.id.loginButton));

        EditText email=findViewById(R.id.editTextEmail);
        EditText password=findViewById(R.id.editTextPassword);
        String email_str=email.getText().toString().trim();
        String password_str=password.getText().toString();

        if(email_str.isBlank() || password_str.isBlank()){
            Toast.makeText(MainActivity.this,"Fill the gap",Toast.LENGTH_LONG).show();
            return;
        }
        Log.i("MainActivity","login " + email_str + " " + password_str);
        mAuth.signInWithEmailAndPassword(email_str,password_str).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i("MainActivity","logged in: " + email_str + " " + password_str);
                    startBlog();
                }else{
                    Toast.makeText(MainActivity.this,"Login failed: " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    Log.i("MainActivity","login failed");
                }
            }
        });
    }
    private void startBlog(){
        Intent intent = new Intent(this, Blog.class);
        startActivity(intent);
    }

    public void registJump(View view) {
        Intent intent = new Intent(this, Regist.class);
        intent.putExtra("secret_key", secret_key);
        startActivity(intent);

    }

    public void continueAsGuest(View view) {
        ButtonAnimation.pressEffect(findViewById(R.id.continueAsGuest));
        mAuth.signInAnonymously().addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i("MainActivity","logged in: noname");
                    startBlog();
                }else{
                    Toast.makeText(MainActivity.this,"Login failed: " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    Log.i("MainActivity","login failed");
                }
            }
        });
    }

    public void logInWithGoogle(View view) {
        ButtonAnimation.pressEffect(findViewById(R.id.logInWithGoogle));
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}









