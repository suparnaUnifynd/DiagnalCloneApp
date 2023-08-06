package com.example.diagnalcloneapp.ui

import android.app.Application
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diagnalcloneapp.R
import com.example.diagnalcloneapp.adapter.SearchPosterListingAdapter
import com.example.diagnalcloneapp.databinding.FragmentSearchPosterBinding
import com.example.diagnalcloneapp.view_model.PosterViewModel
import com.example.diagnalcloneapp.view_model.PosterViewModelFectory


class SearchPosterFragment : DialogFragment(),View.OnClickListener{
    lateinit var searchPosterListingAdapter: SearchPosterListingAdapter
    private var gridLayoutManager: GridLayoutManager ?=null
    private lateinit var binding : FragmentSearchPosterBinding
    private lateinit var factory : PosterViewModelFectory
    private lateinit var viewModel: PosterViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_AppCompat_Light_NoActionBar_FullScreen)
        enterTransition=0
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentSearchPosterBinding.inflate(inflater,container,false)
        initializeVariables()
        setUpAdapters()
        return  binding.root
    }
    private fun initializeVariables(){
        factory= PosterViewModelFectory(Application(),binding.root.context)
        viewModel= ViewModelProvider(requireActivity(),factory).get(PosterViewModel::class.java)
        binding.btnClearSearch.setOnClickListener(this)

        binding.etSearch.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()) {
                    binding.btnClearSearch.visibility=View.VISIBLE
                    viewModel.searchItems(s.toString().trim())
                }else{
                    searchPosterListingAdapter.clearAllData()
                }
            }

        })

        viewModel.searchedList.observe(this){ content->
            if(!content.isNullOrEmpty()) {
                searchPosterListingAdapter.addData(content,binding.etSearch.text!!.trim().toString())
            }else{
                searchPosterListingAdapter.clearAllData()
            }
        }
    }
    private fun setUpAdapters(){
        gridLayoutManager= GridLayoutManager(
            binding.root.context,
            3,
            LinearLayoutManager.VERTICAL,
            false
        )
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape mode
            gridLayoutManager!!.spanCount = 7
        } else {
            // Portrait mode
            gridLayoutManager!!.spanCount = 3
        }
        searchPosterListingAdapter= SearchPosterListingAdapter()
        binding.rvSearchList.layoutManager = gridLayoutManager
        binding.rvSearchList.adapter = searchPosterListingAdapter
        binding.rvSearchList.itemAnimator!!.changeDuration=0
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btnClearSearch ->{
                binding.etSearch.editableText.clear()
                binding.etSearch.clearFocus()
            }
        }
    }
}