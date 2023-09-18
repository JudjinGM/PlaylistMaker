package com.example.playlistmaker.editPlaylist.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.ui.fragments.CreatePlaylistFragment
import com.example.playlistmaker.editPlaylist.ui.viewModel.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlaylistFragment : CreatePlaylistFragment() {

    private val args: EditPlaylistFragmentArgs by navArgs()
    override val viewModel: EditPlaylistViewModel by viewModel {
        parametersOf(args.playlistId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        //переделать инициализацию, дождаться запроса с бд
        binding.toolbarPlaylist.text = resources.getString(R.string.edit)
        binding.buttonCreateImageView.text = resources.getString(R.string.save)
        binding.playlistNameEditText.setText(viewModel.setNameInput())
        binding.playlistDescriptionEditText.setText(viewModel.setNameDescription())

        Glide.with(requireContext()).load(viewModel.setCoverImage())
            .placeholder(R.drawable.add_photo).centerInside().into(
                binding.coverImageView
            )
    }
}