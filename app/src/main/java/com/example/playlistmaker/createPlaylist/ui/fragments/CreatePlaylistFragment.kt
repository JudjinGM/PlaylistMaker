package com.example.playlistmaker.createPlaylist.ui.fragments

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.createPlaylist.ui.model.CreatePlaylistErrorState
import com.example.playlistmaker.createPlaylist.ui.model.CreatePlaylistState
import com.example.playlistmaker.createPlaylist.ui.viewModel.CreatePlaylistViewModel
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.search.ui.model.TextWatcherJustOnTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

open class CreatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    protected val binding get() = _binding!!

    private var textWatcherName: TextWatcher? = null

    private var textWatcherDescription: TextWatcher? = null
    private var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>? = null

    open val viewModel: CreatePlaylistViewModel by viewModel()

    private var confirmDialog: MaterialAlertDialogBuilder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initConfirmDialog()
        initView()
        initMediaPicker()

        viewModel.observeState().observe(viewLifecycleOwner) {
            renderState(it)
        }

        setOnClicksAndTextListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    private fun renderState(state: CreatePlaylistState) {
        when (state) {
            is CreatePlaylistState.ButtonState -> binding.buttonCreateTextView.isEnabled =
                state.isEnable

            is CreatePlaylistState.Navigate.Back -> {
                if (state.isNeedToConfirm) {
                    confirmDialog?.show()
                } else {
                    findNavController().popBackStack()
                }
            }

            is CreatePlaylistState.SaveSuccess -> showToast(
                requireContext().getString(
                    R.string.playlist_name_created, state.name
                ),
            )

            is CreatePlaylistState.SaveNotSuccess -> {
                when (state.error) {

                    CreatePlaylistErrorState.CANNOT_SAVE_IMAGE -> showToast(
                        requireContext().getString(R.string.cannot_save_image)
                    )

                    CreatePlaylistErrorState.CANNOT_CREATE_PLAYLIST -> showToast(
                        requireContext().getString(
                            R.string.cannot_create_playlist
                        )
                    )
                }
            }

            is CreatePlaylistState.InitState -> {
                binding.playlistNameEditText.setText(state.playlistModel?.playlistName)
                binding.playlistDescriptionEditText.setText(state.playlistModel?.playlistDescription)

                state.playlistModel?.playlistCoverImage?.let { image ->
                    Glide.with(requireContext())
                        .load(image)
                        .into(binding.coverImageView)
                }
            }
        }
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

    private fun initConfirmDialog() {
        confirmDialog = MaterialAlertDialogBuilder(
            requireContext(), R.style.CustomDialogTheme
        ).setTitle(R.string.finish_creating_playlist)
            .setMessage(R.string.all_unsaved_data_will_be_lost)
            .setNegativeButton(R.string.cancel) { _, _ ->

            }.setPositiveButton(R.string.finish) { _, _ ->
                findNavController().popBackStack()
            }
    }

    private fun setOnClicksAndTextListeners() {
        textWatcherName = object : TextWatcherJustOnTextChanged {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textInputLayoutBoxStrokeBehaviour(binding.playlistNameTextLayout, s)
                viewModel.getNameInput(s)
            }
        }

        binding.playlistNameEditText.setOnFocusChangeListener { _, hasFocus ->
            textInputLayoutHintBehaviour(
                binding.playlistNameTextLayout, binding.playlistNameEditText.text, hasFocus
            )
        }

        textWatcherDescription = object : TextWatcherJustOnTextChanged {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textInputLayoutBoxStrokeBehaviour(binding.playlistDescriptionTextLayout, s)
                viewModel.getDescriptionInput(s)
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

        binding.buttonCreateTextView.setOnClickListener {
            if (it.isEnabled) {
                viewModel.createButtonClicked()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.closeScreen()
        }

        binding.backImageView.setOnClickListener {
            viewModel.closeScreen()
        }
    }

    private fun textInputLayoutBoxStrokeBehaviour(
        textInputLayout: TextInputLayout, text: CharSequence?
    ) {
        if (!text.isNullOrEmpty()) {
            ContextCompat.getColorStateList(
                requireContext(), R.color.box_stroke_color_selector_blue
            )?.let { textInputLayout.setBoxStrokeColorStateList(it) }

            textInputLayout.defaultHintTextColor = ContextCompat.getColorStateList(
                requireContext(), R.color.box_stroke_color_selector_blue
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
            textInputLayout.defaultHintTextColor = ContextCompat.getColorStateList(
                requireContext(), R.color.box_stroke_color_selector_blue
            )
        }
    }

    private fun initMediaPicker() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.coverImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.coverImageView.setImageURI(uri)
                viewModel.getCoverImageLoadedUri(uri)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(
            context, message, Toast.LENGTH_LONG
        ).show()
    }
}

