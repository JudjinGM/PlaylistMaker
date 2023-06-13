package com.example.playlistmaker.settings.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        viewModel.observeSwitchCompatState().observe(this) {
            render(it)
        }

        setOnClicks()
    }

    private fun setOnClicks() {
        binding.backSettingImageView.setOnClickListener {
            finish()
        }

        val shareClickListener = View.OnClickListener {
            viewModel.shareApp()
        }

        binding.imageShare.setOnClickListener(shareClickListener)
        binding.textShareApp.setOnClickListener(shareClickListener)


        val supportClickListener = View.OnClickListener {
            viewModel.openSupport()
        }

        binding.imageSupport.setOnClickListener(supportClickListener)
        binding.textSupport.setOnClickListener(supportClickListener)

        val agreementClickListener = View.OnClickListener {
            viewModel.openTerms()
        }

        binding.imageTerm.setOnClickListener(agreementClickListener)
        binding.textTerm.setOnClickListener(agreementClickListener)

        binding.switchToDarkTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleDarkThemeSwitch(isChecked)
        }
    }

    private fun render(switchCompatState: Boolean) {
        binding.switchToDarkTheme.isChecked = switchCompatState
    }

}
