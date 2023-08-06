package com.example.diagnalcloneapp.ui

import android.app.Application
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diagnalcloneapp.*
import com.example.diagnalcloneapp.adapter.PosterListingAdapter
import com.example.diagnalcloneapp.core.UIState
import com.example.diagnalcloneapp.databinding.ActivityMainBinding
import com.example.diagnalcloneapp.view_model.PosterViewModel
import com.example.diagnalcloneapp.view_model.PosterViewModelFectory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() ,View.OnClickListener{

    private lateinit var binding :ActivityMainBinding
    private lateinit var factory : PosterViewModelFectory
    private lateinit var viewModel: PosterViewModel
    lateinit var listingAdapter: PosterListingAdapter
    private var currentPage = 1
    private var totalPages = 3
    private var isScrollDataLoading = false
    private var gridLayoutManager: GridLayoutManager ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_main)
        initializeVariables()
        registerScrollListener()
        setUpListeners()
        setUpAdapters()
        setUpViewModel()
    }

    private fun initializeVariables(){
     factory= PosterViewModelFectory(Application(),this)
     binding.lifecycleOwner = this
     viewModel=ViewModelProvider(this,factory).get(PosterViewModel::class.java)
    }
    override fun onResume() {
        resetListParams()
        viewModel.loadDataFromJson(currentPage)
        super.onResume()
    }


    private fun setUpViewModel(){
        viewModel.defaultlist.observe(this){ state->
            when(state){
                is UIState.Success -> {
                    hideProgressBar()
                    isScrollDataLoading = false
                    listingAdapter.addData(state.data!!.page.contentItems.content)
                }
                is UIState.Error -> {
                    hideProgressBar()
                }
                is UIState.Loading -> {
                    showProgressBar()
                }
            }
        }
    }
    private fun resetListParams(){
        currentPage = 1
        totalPages = 3
        isScrollDataLoading = false
        listingAdapter.clearAllData()
    }

    private fun registerScrollListener() {
        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!binding.rvList.canScrollVertically(1) &&
                    newState == RecyclerView.SCROLL_STATE_IDLE &&
                    !isScrollDataLoading &&
                    (currentPage < totalPages)
                ) {
                    currentPage++
                    isScrollDataLoading = true
                    viewModel.loadDataFromJson(currentPage)
                }
                //show shadow
                if(gridLayoutManager!!.findFirstCompletelyVisibleItemPosition()==0){
                    binding.ivShadow.visibility=View.GONE
                }else{
                    binding.ivShadow.visibility=View.VISIBLE
                }
            }
        })
    }


    private fun setUpListeners(){
        binding.ivClose.setOnClickListener(this)
        binding.ivSearch.setOnClickListener(this)
    }
    private fun setUpAdapters(){
        gridLayoutManager=GridLayoutManager(
            this,
            3,
            LinearLayoutManager.VERTICAL,
            false
        )
        // Check the screen orientation and adjust the span count accordingly
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape mode
            gridLayoutManager!!.spanCount = 7 // Set the number of columns you want in landscape mode
        } else {
            // Portrait mode
            gridLayoutManager!!.spanCount = 3 // Set the number of columns you want in portrait mode
        }
        listingAdapter= PosterListingAdapter()
        binding.rvList.layoutManager = gridLayoutManager
        binding.rvList.adapter = listingAdapter
    }


    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.ivClose ->{
                this.onBackPressed()
            }
            R.id.ivSearch ->{

                val searchPosterFragment =  SearchPosterFragment()
                if(!searchPosterFragment.isVisible)
                    searchPosterFragment.show(supportFragmentManager,searchPosterFragment.tag)
            }
        }
    }
}