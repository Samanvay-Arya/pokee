package com.samanvay.pokee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.samanvay.pokee.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        replaceFragment(profile())
        binding.bottomNavigation.background = null
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.BottomBarHome -> replaceFragment(home())
                R.id.BottomBarMessages ->replaceFragment(messages())
                R.id.BottomBarLikes ->replaceFragment(Likes())
                R.id.BottomBarProfile ->replaceFragment(profile())
            }
            true
        }
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_out,
                R.anim.slide_in
            )
            .setReorderingAllowed(true)
            .replace(R.id.Frame_layout,fragment).commit()
    }
}