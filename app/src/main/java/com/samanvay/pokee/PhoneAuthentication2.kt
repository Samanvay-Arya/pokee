package com.samanvay.pokee

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.samanvay.pokee.databinding.ActivityPhoneAuthentication2Binding
import com.samanvay.pokee.databinding.ActivityPhoneAuthenticationBinding
import java.util.concurrent.TimeUnit

class PhoneAuthentication2 : AppCompatActivity() {
    private lateinit var binding: ActivityPhoneAuthentication2Binding
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var auth:FirebaseAuth
    private lateinit var phoneNumber:String
    private lateinit var resendToken:PhoneAuthProvider.ForceResendingToken
    private lateinit var storedVerificationId:String
    private lateinit var code:String
    private lateinit var uid:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPhoneAuthentication2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        phoneNumber= "+91"+intent.extras!!.getString("phone","")
        initiateSMS()
        binding.submitButton.setOnClickListener {
            code=binding.verificationCode.text.toString()
            binding.errorInfo.visibility=View.GONE
            if(code.length==6){
                val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
                signInWithPhoneAuthCredential(credential)
            }
            else{
                binding.errorInfo.visibility= View.VISIBLE
            }

        }
    }
    private fun initiateSMS(){
        callbackFunctions()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private fun callbackFunctions(){
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("TAG", "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w("TAG", "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                } else if (e is FirebaseTooManyRequestsException) {
                    Toast.makeText(this@PhoneAuthentication2, "we are processing- too many request! please try again later", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken, ) {
                Log.d("TAG", "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "signInWithCredential:success")
                        val user = task.result?.user
                        uid=user!!.uid
                        saveSharedPreferences()
                        startActivity(Intent(this@PhoneAuthentication2,UserDetails::class.java))
                        finish()
                    } else {
                        Log.w("TAG", "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            binding.errorInfo.text="Incorrect OTP"
                            binding.errorInfo.visibility=View.VISIBLE
                        }
                    }
                }
        }
    private fun saveSharedPreferences() {
        val sharedPreferences = getSharedPreferences("Shared Preferences", MODE_PRIVATE)
        @SuppressLint("CommitPrefEdits") val editor = sharedPreferences.edit()
        editor.putString("uid",uid)
        editor.putString("phone",phoneNumber)
        editor.apply()
    }



}