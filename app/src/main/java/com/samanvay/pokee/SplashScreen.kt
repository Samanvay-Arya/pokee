package com.samanvay.pokee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.samanvay.pokee.databinding.ActivitySplashScreenBinding



class SplashScreen : AppCompatActivity() {
    private var auth= FirebaseAuth.getInstance()
    private lateinit var binding:ActivitySplashScreenBinding
    private var userDetails=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.visibility=View.VISIBLE
        loadSharedPreferences()
        tim()
        binding.createAccount.setOnClickListener {
            startActivity(Intent(this@SplashScreen,PhoneAuthentication::class.java))
        }
        binding.signIn.setOnClickListener {
            startActivity(Intent(this@SplashScreen,MainActivity::class.java))
        }
    }
    private fun tim(){
        val timer=object : CountDownTimer(1000,2000){
            override fun onTick(millisUntilFinished:Long){
            }
            override fun onFinish(){
                val currentUser = auth.currentUser

                if(currentUser!=null){
                    if(userDetails==1) startActivity(Intent(this@SplashScreen,MainActivity::class.java))
                    else startActivity(Intent(this@SplashScreen,UserDetails::class.java))
                    finish()
                }
                else{
                    binding.createAccount.visibility=View.VISIBLE
                    binding.progressBar.visibility=View.GONE
                    binding.signIn.visibility=View.VISIBLE
                }
            }
        }
        timer.start()

    }
    private fun loadSharedPreferences() {
        val sharedPreferences = getSharedPreferences("Shared Preferences", MODE_PRIVATE)
        userDetails = sharedPreferences?.getInt("userDetails", 0)!!

    }
}