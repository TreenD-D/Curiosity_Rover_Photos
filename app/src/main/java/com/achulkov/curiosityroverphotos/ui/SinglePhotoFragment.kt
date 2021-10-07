package com.achulkov.curiosityroverphotos.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.achulkov.curiosityroverphotos.R
import com.achulkov.curiosityroverphotos.databinding.FragmentSinglePhotoBinding
import com.achulkov.curiosityroverphotos.ui.viewModel.MainViewModel
import com.achulkov.curiosityroverphotos.util.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SinglePhotoFragment : Fragment() {

    private lateinit var binding: FragmentSinglePhotoBinding

    private val viewModel : MainViewModel by activityViewModels()

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSinglePhotoBinding.bind(view)

        viewModel.selectedPhoto.observe(viewLifecycleOwner, {
            imageLoader
                .load(it.img_src)
                .placeholder(R.drawable.missing_image)
                .fit()
                .into(binding.photoView)
        })

    }


}