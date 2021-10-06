package com.achulkov.curiosityroverphotos.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.achulkov.curiosityroverphotos.R
import com.achulkov.curiosityroverphotos.databinding.FragmentPhotoListBinding
import com.achulkov.curiosityroverphotos.ui.adapters.PhotoListAdapter
import com.achulkov.curiosityroverphotos.ui.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PhotoListFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()

    private lateinit var binding: FragmentPhotoListBinding

    @Inject
    lateinit var adapter: PhotoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPhotoListBinding.bind(view)


        val layoutManager = GridLayoutManager(view.context, 2)
        binding.recyclerPhotos.layoutManager = layoutManager
        binding.recyclerPhotos.adapter = adapter
        binding.recyclerPhotos.setHasFixedSize(true)

        viewModel.roverManifestInfo.observe(viewLifecycleOwner, {
            val test = it
            Timber.d("test")
        })

    }


}