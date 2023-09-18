package com.example.playlistmaker.editPlaylist.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.ui.fragments.CreatePlaylistFragment
import com.example.playlistmaker.createPlaylist.ui.model.BackState
import com.example.playlistmaker.createPlaylist.ui.model.CreatePlaylistState
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

        viewModel.observePlaylistInitState().observe(viewLifecycleOwner) {
            binding.playlistNameEditText.setText(it.playlistName)
            binding.playlistDescriptionEditText.setText(it.playlistDescription)

            Glide.with(requireContext()).load(it.playlistCoverImage)
                .placeholder(R.drawable.add_photo).into(
                    binding.coverImageView
                )
        }
    }

    private fun initView() {
        binding.toolbarPlaylist.text = resources.getString(R.string.edit)
        binding.buttonCreateImageView.text = resources.getString(R.string.save)
    }

    override fun renderToast(state: CreatePlaylistState) {

    }


    override fun renderBackBehaviour(state: BackState) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.closeScreen()
        }

        binding.backImageView.setOnClickListener {
            viewModel.closeScreen()
        }
    }
}

