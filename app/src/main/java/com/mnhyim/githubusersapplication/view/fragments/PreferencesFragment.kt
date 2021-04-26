package com.mnhyim.githubusersapplication.view.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.mnhyim.githubusersapplication.AlarmReceiver
import com.mnhyim.githubusersapplication.R
import java.util.*

class PreferencesFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var REMINDER: String
    private lateinit var LANGUAGE: String

    private lateinit var switchReminderPreferences: SwitchPreference
    private lateinit var languagePreferences: Preference
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)
        alarmReceiver = AlarmReceiver()

        init()
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference.key) {
            LANGUAGE -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                languagePreferences.summary = Locale.getDefault().displayLanguage
                startActivity(mIntent)
                true
            }
            else -> false
        }
    }

    private fun init() {
        REMINDER = resources.getString(R.string.reminder)
        LANGUAGE = resources.getString(R.string.language)
        switchReminderPreferences = findPreference<SwitchPreference>(REMINDER) as SwitchPreference
        switchReminderPreferences.isChecked =
            preferenceManager.sharedPreferences.getBoolean(REMINDER, false)
        languagePreferences = findPreference<Preference>(LANGUAGE) as Preference
        languagePreferences.summary = Locale.getDefault().displayLanguage
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        val dailyReminderState =
            PreferenceManager.getDefaultSharedPreferences(context).getBoolean(REMINDER, false)

        if (key == REMINDER) {
            switchReminderPreferences.isChecked = sharedPreferences.getBoolean(REMINDER, false)
        }

        if (dailyReminderState) {
            alarmReceiver.setRepeatingAlarm(
                requireContext(),
                getString(R.string.reminder_title),
                getString(R.string.reminder_message)
            )
        } else {
            alarmReceiver.unsetRepeatingAlarm(requireContext())
        }
    }
}