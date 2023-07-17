package com.example.playlistmaker.settings.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeSwitchCompatState().observe(viewLifecycleOwner) {
            render(it)
        }

        setOnClicks()
    }

    private fun render(switchCompatState: Boolean) {
        binding.switchToDarkTheme.isChecked = switchCompatState
    }

    private fun setOnClicks() {

        val shareClickListener = View.OnClickListener {
            viewModel.shareApp()
        }

        binding.imageShare.setOnClickListener(shareClickListener)
        binding.textShareApp.setOnClickListener(shareClickListener)

        val supportClickListener = View.OnClickListener {
            viewModel.writeToSupport()
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
}