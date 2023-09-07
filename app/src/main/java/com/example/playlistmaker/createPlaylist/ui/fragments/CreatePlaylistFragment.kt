package com.example.playlistmaker.createPlaylist.ui.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.ui.model.BackState
import com.example.playlistmaker.createPlaylist.ui.model.CreateButtonState
import com.example.playlistmaker.createPlaylist.ui.model.CreatePlaylistState
import com.example.playlistmaker.createPlaylist.ui.model.SaveImageState
import com.example.playlistmaker.createPlaylist.ui.viewModel.CreatePlaylistViewModel
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.search.ui.model.TextWatcherJustOnTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!

    private var textWatcherName: TextWatcher? = null

    private var textWatcherDescription: TextWatcher? = null
    private var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>? = null
    private val viewModel: CreatePlaylistViewModel by viewModel()

    private var confirmDialog: MaterialAlertDialogBuilder? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmDialog =
            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.MaterialAlertDialogText
            ).setTitle(R.string.finish_creating_playlist)
                .setMessage(R.string.all_unsaved_data_will_be_lost)
                .setNegativeButton(R.string.cancel) { _, _ ->

                }.setPositiveButton(R.string.finish) { _, _ ->
                    findNavController().popBackStack()
                }

        initView()
        initMediaPicker()

        viewModel.observeCreateButtonState().observe(viewLifecycleOwner) {
            renderCreateButton(it)
        }

        viewModel.observeToastState().observe(viewLifecycleOwner) {
            renderToast(it)
        }

        viewModel.observeBackBehaviourState().observe(viewLifecycleOwner) {
            renderBackBehaviour(it)
        }

        viewModel.saveImageToStorageState().observe(viewLifecycleOwner) {
            renderSaveImageToStorageState(it)
        }

        setOnClicksAndTextListeners()

    }


    private fun initView() {
        binding.playlistNameTextLayout.boxStrokeColor =
            ContextCompat.getColor(requireContext(), R.color.royal_blue)

        binding.playlistNameTextLayout.defaultHintTextColor =
            ContextCompat.getColorStateList(requireContext(), R.color.hint_state_color_selector)

        binding.playlistDescriptionTextLayout.boxStrokeColor =
            ContextCompat.getColor(requireContext(), R.color.royal_blue)

        binding.playlistDescriptionTextLayout.defaultHintTextColor =
            ContextCompat.getColorStateList(requireContext(), R.color.hint_state_color_selector)
    }


    private fun setOnClicksAndTextListeners() {
        textWatcherName = object : TextWatcherJustOnTextChanged {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textInputLayoutBoxStrokeBehaviour(binding.playlistNameTextLayout, s)
                viewModel.getNameInput(s)
                viewModel.backStateBehaviour()
            }
        }

        binding.playlistNameEditText.setOnFocusChangeListener { _, hasFocus ->
            textInputLayoutHintBehaviour(
                binding.playlistNameTextLayout,
                binding.playlistNameEditText.text,
                hasFocus
            )
        }

        textWatcherDescription = object : TextWatcherJustOnTextChanged {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textInputLayoutBoxStrokeBehaviour(binding.playlistDescriptionTextLayout, s)
                viewModel.getDescriptionInput(s)
                viewModel.backStateBehaviour()
            }
        }

        binding.playlistDescriptionEditText.setOnFocusChangeListener { _, hasFocus ->
            textInputLayoutHintBehaviour(
                binding.playlistDescriptionTextLayout,
                binding.playlistDescriptionEditText.text,
                hasFocus
            )
        }

        binding.playlistNameEditText.addTextChangedListener(textWatcherName)
        binding.playlistDescriptionEditText.addTextChangedListener(textWatcherDescription)

        binding.coverImageView.setOnClickListener {
            pickMedia?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.buttonCreateImageView.setOnClickListener {
            if (it.isEnabled) {
                viewModel.createButtonClicked()
                findNavController().popBackStack()
            }
        }
    }

    private fun textInputLayoutBoxStrokeBehaviour(
        textInputLayout: TextInputLayout, text: CharSequence?
    ) {
        if (!text.isNullOrEmpty()) {
            ContextCompat.getColorStateList(
                requireContext(), R.color.box_stroke_color_selector_blue
            )?.let { textInputLayout.setBoxStrokeColorStateList(it) }

            textInputLayout.defaultHintTextColor =
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.box_stroke_color_selector_blue
                )
        } else {
            ContextCompat.getColorStateList(
                requireContext(), R.color.box_stroke_color_selector_grey
            )?.let { textInputLayout.setBoxStrokeColorStateList(it) }
        }
    }

    private fun textInputLayoutHintBehaviour(
        textInputLayout: TextInputLayout, text: CharSequence?, hasFocus: Boolean
    ) {
        if (!hasFocus && text.isNullOrEmpty()) {
            textInputLayout.defaultHintTextColor =
                ContextCompat.getColorStateList(requireContext(), R.color.hint_state_color_selector)
        } else {
            textInputLayout.defaultHintTextColor =
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.box_stroke_color_selector_blue
                )
        }
    }

    private fun renderCreateButton(state: CreateButtonState) {
        when (state) {
            CreateButtonState.Disabled -> {
                binding.buttonCreateImageView.isEnabled = false
            }

            CreateButtonState.Enabled -> binding.buttonCreateImageView.isEnabled = true
        }
    }

    private fun renderToast(state: CreatePlaylistState) {
        when (state) {
            is CreatePlaylistState.Success -> Toast.makeText(
                context,
                requireContext().getString(R.string.playlist_name_created, state.name),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun renderSaveImageToStorageState(state: SaveImageState) {
        when (state) {
            is SaveImageState.Allow -> saveImageToPrivateStorage(state.uri)
        }
    }

    private fun renderBackBehaviour(state: BackState) {
        when (state) {
            BackState.Error -> {
                requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                    confirmDialog?.show()
                }
                binding.backImageView.setOnClickListener {
                    confirmDialog?.show()

                }
            }

            BackState.Success -> {
                requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                    findNavController().popBackStack()
                }
                binding.backImageView.setOnClickListener {
                    findNavController().popBackStack()
                }

            }
        }
    }


    private fun initMediaPicker() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.coverImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.coverImageView.setImageURI(uri)
                viewModel.getCoverImageLoadedUri(uri)
                viewModel.backStateBehaviour()
            }

        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                DIRECTORY_PATH
            )
        if (!filePath.exists()) {
            filePath.mkdir()
        }

        val file = File(filePath, System.currentTimeMillis().toString() + COVER_IMAGE_FILE_NAME)

        requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                BitmapFactory.decodeStream(inputStream)
                    .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
            }
        }

        viewModel.getCoverImageSavedToStorageUri(file.toUri())
    }

    companion object {
        const val DIRECTORY_PATH = "playlist_covers"
        const val COVER_IMAGE_FILE_NAME = "playlist_cover.jpg"
    }
}

