package com.samanvay.pokee

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.samanvay.pokee.databinding.ActivityUserDetailsBinding
import java.util.ArrayList

class UserDetails : AppCompatActivity() {
    private lateinit var binding:ActivityUserDetailsBinding
    var fieldKey = ArrayList<String>()
    var fieldValue = ArrayList<String>(3)
    private var idx=0
    private var details=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        idx=0
        fieldKey.add("What's your first name")
        fieldKey.add("What's your lastname")
        fieldKey.add("User Name")
        binding.title.text=fieldKey[idx]
        fieldValue.add("")
        fieldValue.add("")
        fieldValue.add("")

        binding.nextButton.setOnClickListener {
            binding.errorInfo.visibility=View.GONE
            details=binding.userDetails.text.toString()

            if(details.length!=0 && idx<3){
                fieldValue[idx]=details
                if(idx>=3){
                    saveDetails()
                }
                else{
                    idx=idx+1
                    if(idx>=3) saveDetails()
                    else{
                        binding.userDetails.text.clear()
                        binding.title.text=fieldKey[idx]
                        if(idx==2) binding.nextButton.text="Submit"
                        binding.test.text=idx.toString()
                    }
                }

            }
            else if(details.length==0){
                binding.errorInfo.text="field can't be empty"
                binding.errorInfo.visibility=View.VISIBLE
            }
        }


    }

    override fun onBackPressed() {
        if(idx==0){
            super.onBackPressed()
        }
        else{
            binding.errorInfo.visibility=View.GONE
            if(idx>=3) idx=1
            else idx=idx-1
            binding.test.text=idx.toString()
            binding.title.text=fieldKey[idx]
            binding.userDetails.text.clear()
            binding.nextButton.text="next"
        }

    }

    fun saveDetails(){
        val sharedPreferences = getSharedPreferences("Shared Preferences", MODE_PRIVATE)
        @SuppressLint("CommitPrefEdits") val editor = sharedPreferences.edit()
        editor.putString("first_name",fieldValue[0])
        editor.putString("last_name",fieldValue[1])
        editor.putString("userName",fieldValue[2])
        editor.putInt("userDetails",1)
        editor.apply()
        startActivity(Intent(this@UserDetails,MainActivity::class.java))
        finish()
    }
}