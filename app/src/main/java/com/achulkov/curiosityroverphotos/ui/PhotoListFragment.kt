package com.achulkov.curiosityroverphotos.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.achulkov.curiosityroverphotos.R
import com.achulkov.curiosityroverphotos.data.models.RoverPhoto
import com.achulkov.curiosityroverphotos.databinding.FragmentPhotoListBinding
import com.achulkov.curiosityroverphotos.ui.adapters.PhotoListAdapter
import com.achulkov.curiosityroverphotos.ui.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PhotoListFragment : Fragment(), PhotoListAdapter.AdapterItemClickListener, ActionMode.Callback {

    private val viewModel : MainViewModel by activityViewModels()

    private lateinit var binding: FragmentPhotoListBinding

    private var actionMode : ActionMode? = null

    private var currentListSize : Int = 0

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
        adapter.setListener(this)
        binding.recyclerPhotos.setHasFixedSize(true)

        viewModel.photos.observe(viewLifecycleOwner, {list ->
            adapter.submitList(list)
            currentListSize = list.size
        })

    }

    override fun onPause() {
        actionMode?.finish()
        super.onPause()
    }

    override fun onAdapterItemClick(photo: RoverPhoto, position: Int) {
        if (actionMode == null) {
            viewModel.selectedPhoto.postValue(photo)
            requireActivity().findNavController(R.id.main_fragments_host).navigate(R.id.singlePhotoFragment)
        }
        else {
            adapter.toggleItem(position)
            actionMode!!.title = resources.getString(
                    R.string.selected) +
                        adapter.getSelectedSize(currentListSize).toString()
        }
    }

    override fun onAdapterLongItemClick(photo: RoverPhoto, position: Int) {
        if (actionMode == null) {
            requireActivity().startActionMode(this)
        }
        adapter.toggleItem(position)
        if (actionMode != null) {
            actionMode!!.title = resources.getString(
                R.string.selected) +
                    adapter.getSelectedSize(currentListSize).toString()

        }
    }

    override fun onCreateActionMode(mActionMode: ActionMode?, mMenu: Menu?): Boolean {
        actionMode = mActionMode
        return true
    }

    override fun onPrepareActionMode(mActionMode: ActionMode?, mMenu: Menu?): Boolean {
        val menuInflater = MenuInflater(activity)
        menuInflater.inflate(R.menu.menu_delete, mMenu)
        return true
    }

    override fun onActionItemClicked(mActionMode: ActionMode?, mMenuItem: MenuItem?): Boolean {
        if (mMenuItem?.itemId == R.id.remove) {
            AlertDialog.Builder(context)
                .setMessage(R.string.confirmation)
                .setPositiveButton(R.string.remove
                ) { _, _ ->

                    val photosList = mutableListOf<RoverPhoto>()
                    for((i, photo) in viewModel.photos.value!!.withIndex()){
                        if(adapter.getItemsSelection().get(i)){
                            photosList.add(photo)
                        }
                    }
                    viewModel.deletePhoto(photosList)


                    actionMode?.finish()
                }
                .setNegativeButton(R.string.cancel_text){ _, _ ->
                    //just dismiss
                }
                .show()
        }
        return true
    }

    override fun onDestroyActionMode(mActionMode: ActionMode?) {
        actionMode = null
        adapter.clearSelections()
    }


}