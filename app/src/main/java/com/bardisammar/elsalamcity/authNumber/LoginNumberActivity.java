package com.bardisammar.elsalamcity.authNumber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.auth.LoginActivity;
import com.bardisammar.elsalamcity.commen.Commen;
import com.bardisammar.elsalamcity.databinding.ActivityLoginNumberBinding;
import com.bardisammar.elsalamcity.home.HomeActivity;
import com.bardisammar.elsalamcity.shardprefranse.SharedEditor;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginNumberActivity extends AppCompatActivity {
ActivityLoginNumberBinding binding;
    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    SharedEditor sharedEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedEditor = new SharedEditor(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_number);

        firebaseAuth = FirebaseAuth.getInstance();
        checkSignIn();
        binding.buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeybeard();
                if (Commen.isNetworkOnline(LoginNumberActivity.this)){
                    if (!isValidPhoneNumber(binding.editTextMobile.getText().toString())|| binding.editTextMobile.getText().toString().trim().isEmpty()){
                        Toast.makeText(LoginNumberActivity.this, "أدخل رقم الهاتف", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    binding.progressbar.setVisibility(View.VISIBLE);
                    binding.buttonSign.setVisibility(View.INVISIBLE);
                    sharedEditor.saveData(binding.editTextMobile.getText().toString());
                    Intent intent=new Intent(LoginNumberActivity.this,HomeActivity.class);

                    startActivity(intent);
                    finish();
//                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                            "+2" + binding.editTextMobile.getText().toString(),                 //phoneNo that is given by user
//                            60,                             //Timeout Duration
//                            TimeUnit.SECONDS,                   //Unit of Timeout
//                            TaskExecutors.MAIN_THREAD,          //Work done on main Thread
//                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                                @Override
//                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                    binding.progressbar.setVisibility(View.GONE);
//                                    binding.buttonSign.setVisibility(View.VISIBLE);
//                                }
//
//                                @Override
//                                public void onVerificationFailed(@NonNull FirebaseException e) {
//                                    binding.progressbar.setVisibility(View.GONE);
//                                    binding.buttonSign.setVisibility(View.VISIBLE);
//                                    Toast.makeText(LoginNumberActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//
//                                @Override
//                                public void onCodeSent(@NonNull String verficationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                    binding.progressbar.setVisibility(View.GONE);
//                                    binding.buttonSign.setVisibility(View.VISIBLE);
//                                    Intent intent=new Intent(LoginNumberActivity.this,ValidatNumberActivity.class);
//                                    intent.putExtra("mobil",binding.editTextMobile.getText().toString());
//                                    intent.putExtra("verficationId",verficationId);
//                                    startActivity(intent);
//                                }
//                            });
//

                }else {
                    Toast.makeText(LoginNumberActivity.this, "لايوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkSignIn();
    }

    private void checkSignIn() {
        if (!sharedEditor.loadData().get(SharedEditor.KEY_USER_PHONE).equalsIgnoreCase("")){
            Intent intent = new Intent(LoginNumberActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user!=null){
//            Intent intent = new Intent(LoginNumberActivity.this, HomeActivity.class);
//            startActivity(intent);
//            finish();
//        }

    }

    private boolean isValidPhoneNumber(String phone) {

        if (!phone.trim().equals("") && phone.length() > 10) {
            return Patterns.PHONE.matcher(phone).matches();
        }

        return false;
    }
    void hidekeybeard() {
        // Then just use the following:
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.layout.getWindowToken(), 0);
    }
}