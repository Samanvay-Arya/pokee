package com.samanvay.pokee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.samanvay.pokee.databinding.ActivityPhoneAuthenticationBinding
import com.samanvay.pokee.databinding.ActivitySplashScreenBinding

class PhoneAuthentication : AppCompatActivity() {
    private lateinit var binding: ActivityPhoneAuthenticationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPhoneAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.nextButton.setOnClickListener {
            val phone=binding.phoneNumber.text.toString()
            binding.errorInfo.visibility=View.GONE
            if(phone.length==10){
                val intent=Intent(this@PhoneAuthentication,PhoneAuthentication2::class.java)
                intent.putExtra("phone",binding.phoneNumber.text.toString())
                startActivity(intent)
            }
            else{
                binding.errorInfo.visibility=View.VISIBLE
            }

        }
    }
}