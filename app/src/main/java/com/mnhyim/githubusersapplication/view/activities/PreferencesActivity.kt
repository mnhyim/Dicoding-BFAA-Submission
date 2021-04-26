package com.mnhyim.githubusersapplication.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mnhyim.githubusersapplication.R
import com.mnhyim.githubusersapplication.view.fragments.PreferencesFragment

class PreferencesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        supportFragmentManager.beginTransaction()
            .add(R.id.preferences_holder, PreferencesFragment()).commit()
    }
}