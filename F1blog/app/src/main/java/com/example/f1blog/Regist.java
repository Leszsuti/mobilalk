package com.example.f1blog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Regist extends AppCompatActivity {
    private static final int secret_key=69;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_regist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if(getIntent().getIntExtra("secret_key",0)!=99){
            finish();
        }
        mAuth=FirebaseAuth.getInstance();
    }

    public void regist(View view) {
        ButtonAnimation.pressEffect(findViewById(R.id.registButton_regist));

        EditText username_regist=findViewById(R.id.editTextUserName_regist);
        EditText email_regist=findViewById(R.id.editTextEmail_regist);
        EditText password_regist=findViewById(R.id.editTextPassword_regist);
        EditText password2_regist=findViewById(R.id.editTextPassword2_regist);

        String username_regist_str=username_regist.getText().toString().trim();
        String email_regist_str=email_regist.getText().toString().trim();
        String password_regist_str=password_regist.getText().toString();
        String password2_regist_str=password2_regist.getText().toString();
        if(username_regist_str.isBlank() || email_regist_str.isBlank() || password2_regist_str.isBlank()){
            Toast.makeText(Regist.this,"Fill the gap",Toast.LENGTH_LONG).show();
            return;
        }

        if(!password_regist_str.equals(password2_regist_str)){
            Toast.makeText(Regist.this,"Registration failed: passwords do not match",Toast.LENGTH_LONG).show();
            return;
        }

        Log.i("Regist_log",username_regist_str + " " + email_regist_str + " " + password_regist_str + " " + password2_regist_str);
        mAuth.createUserWithEmailAndPassword(email_regist_str,password_regist_str).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i("Regist_log","sikeres regist");
                    startBlog();
                }else{
                    Log.i("Regist_log","sikertelen regist :(");
                    Toast.makeText(Regist.this,"Registration failed: " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void loginJump(View view) {
        finish();
    }

    private void startBlog(){
        Intent intent = new Intent(this, Blog.class);
        startActivity(intent);
    }
}








