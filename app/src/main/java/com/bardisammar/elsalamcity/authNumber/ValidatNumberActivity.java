package com.bardisammar.elsalamcity.authNumber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.bardisammar.elsalamcity.R;
import com.bardisammar.elsalamcity.auth.pojo.Users;
import com.bardisammar.elsalamcity.commen.Commen;
import com.bardisammar.elsalamcity.databinding.ActivityValidatNumberBinding;
import com.bardisammar.elsalamcity.home.HomeActivity;
import com.bardisammar.elsalamcity.shardprefranse.SharedEditor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.poovam.pinedittextfield.PinField;

import java.util.concurrent.TimeUnit;

public class ValidatNumberActivity extends AppCompatActivity {

    private String mVerificationId, mobil;

SharedEditor sharedEditor;
    private EditText editTextCode;
    DatabaseReference Rf;
    FirebaseDatabase db;

    private FirebaseAuth mAuth;
    ActivityValidatNumberBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedEditor = new SharedEditor(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_validat_number);

        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        mVerificationId = intent.getStringExtra("verficationId");
        mobil = intent.getStringExtra("mobil");
        setupInputs();
        binding.buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidekeybeard();
                if (Commen.isNetworkOnline(ValidatNumberActivity.this)){
                    if (binding.inputCod1.getText().toString().trim().isEmpty() ||
                            binding.inputCod2.getText().toString().trim().isEmpty() ||
                            binding.inputCod3.getText().toString().trim().isEmpty() ||
                            binding.inputCod4.getText().toString().trim().isEmpty() ||
                            binding.inputCod5.getText().toString().trim().isEmpty() ||
                            binding.inputCod6.getText().toString().trim().isEmpty()
                    ) {

                        Toast.makeText(ValidatNumberActivity.this, "من فضلك أدخل الكود ", Toast.LENGTH_SHORT).show();

                        return;
                    }
                    String code = binding.inputCod1.getText().toString() +
                            binding.inputCod2.getText().toString() +
                            binding.inputCod3.getText().toString() +
                            binding.inputCod4.getText().toString() +
                            binding.inputCod5.getText().toString() +
                            binding.inputCod6.getText().toString();

                    if (mVerificationId != null) {
                        binding.progressbar.setVisibility(View.VISIBLE);
                        binding.buttonSign.setVisibility(View.INVISIBLE);
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

                        mAuth.signInWithCredential(credential)
                                .addOnCompleteListener(ValidatNumberActivity.this,
                                        new OnCompleteListener<AuthResult>() {

                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                binding.progressbar.setVisibility(View.GONE);
                                                binding.buttonSign.setVisibility(View.VISIBLE);
                                                if (task.isSuccessful()) {


                                                    sharedEditor.saveData(mobil);
                                                    Intent intent = new Intent(ValidatNumberActivity.this, HomeActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                                    startActivity(intent);
                                                } else {

                                                    //verification unsuccessful.. display an error message
                                                    Log.d("succesapp", "faild");
                                                    String message = "Somthing is wrong, we will fix it soon...";

                                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                                        message = "Invalid code entered...";
                                                    }
                                                    Toast.makeText(ValidatNumberActivity.this, message, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                    }



                }else {
                    Toast.makeText(ValidatNumberActivity.this, "لا يوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
                }


            }


        });

        binding.tvRsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Commen.isNetworkOnline(ValidatNumberActivity.this)){
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+2" + mobil,                 //phoneNo that is given by user
                            60,                             //Timeout Duration
                            TimeUnit.SECONDS,                   //Unit of Timeout
                            TaskExecutors.MAIN_THREAD,          //Work done on main Thread
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {

                                    Toast.makeText(ValidatNumberActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String newverficationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    mVerificationId=newverficationId;
                                    Toast.makeText(ValidatNumberActivity.this, "تم الارسال ", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else {

                    Toast.makeText(ValidatNumberActivity.this, "لا يوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void setupInputs() {

        binding.inputCod1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.inputCod2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCod2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.inputCod3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCod3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.inputCod4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCod4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.inputCod5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputCod5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.inputCod6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

//    private void sendVerificationCode(String mobile) {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                "+2" + mobile,                 //phoneNo that is given by user
//                60,                             //Timeout Duration
//                TimeUnit.SECONDS,                   //Unit of Timeout
//                TaskExecutors.MAIN_THREAD,          //Work done on main Thread
//                mCallbacks);                       // OnVerificationStateChangedCallbacks
//    }
//
//
//    //the callback to detect the verification status
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
//            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                @Override
//                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//
//                    //Getting the code sent by SMS
//                    String code = phoneAuthCredential.getSmsCode();
//
//                    //sometime the code is not detected automatically
//                    //in this case the code will be null
//                    //so user has to manually enter the code
//                    if (code != null) {
//
//                        //verifying the code
//                        verifyVerificationCode(code);
//                    }
//                }
//
//                @Override
//                public void onVerificationFailed(FirebaseException e) {
//                    Toast.makeText(ValidatNumberActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                    Log.e("TAG", e.getMessage());
//                }
//
//                //when the code is generated then this method will receive the code.
//                @Override
//                public void onCodeSent(String s, @org.jetbrains.annotations.NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
////                super.onCodeSent(s, forceResendingToken);
//
//                    //storing the verification id that is sent to the user
//                    mVerificationId = s;
//                }
//            };
//
//    private void verifyVerificationCode(String code) {
//        //creating the credential
//        try {
//            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
//            signInWithPhoneAuthCredential(credential);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "no", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//    //used for signing the user
//    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(ValidatNumberActivity.this,
//                        new OnCompleteListener<AuthResult>() {
//
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//
//
//
//
//                                } else {
//
//                                    //verification unsuccessful.. display an error message
//                                    Log.d("succesapp", "faild");
//                                    String message = "Somthing is wrong, we will fix it soon...";
//
//                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                        message = "Invalid code entered...";
//                                    }
//                                    Toast.makeText(ValidatNumberActivity.this, message, Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//    }

    void hidekeybeard() {
        // Then just use the following:
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.layout.getWindowToken(), 0);
    }
}