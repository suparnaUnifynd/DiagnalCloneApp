package com.example.diagnalcloneapp.view_model

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.diagnalcloneapp.model.PosterItemDTO
import com.example.diagnalcloneapp.core.UIState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PosterViewModel
@Inject constructor(
    private val app: Application,
    private val context: Context
) :AndroidViewModel(app) {

    val defaultlist: MutableLiveData<UIState<PosterItemDTO>> = MutableLiveData()
    private var defaultlistItems:ArrayList<PosterItemDTO.Page.ContentItems.Content> =
        arrayListOf()

    private val _contentItems: MutableLiveData<ArrayList<PosterItemDTO.Page.ContentItems.Content>> = MutableLiveData()
    val searchedList: LiveData<ArrayList<PosterItemDTO.Page.ContentItems.Content>> get() = _contentItems

    fun loadDataFromJson(pageNo:Int) {
        viewModelScope.launch {
            defaultlist.postValue(UIState.Loading())
            var jsonString =""
            try {
                when(pageNo){
                    1->{
                        jsonString = context.assets.open("CONTENTLISTINGPAGE-PAGE1.json").bufferedReader().use {
                            it.readText()
                        }
                    }
                    2->{
                        jsonString = context.assets.open("CONTENTLISTINGPAGE-PAGE2.json").bufferedReader().use {
                            it.readText()
                        }
                    }
                    3->{
                        jsonString = context.assets.open("CONTENTLISTINGPAGE-PAGE3.json").bufferedReader().use {
                            it.readText()
                        }
                    }
                }
                val itemList = Gson().fromJson(jsonString, PosterItemDTO::class.java)
                if(defaultlistItems.isNotEmpty()){
                    itemList.page.contentItems.content.forEach {
                        defaultlistItems.add(it)
                    }
                }else {
                    defaultlistItems = itemList.page.contentItems.content
                }
                defaultlist.postValue(UIState.Success(itemList))
            } catch (e: Exception) {
                defaultlist.postValue(UIState.Error("Something went wrong"))
            }
        }
    }
    // Function to handle search logic
    fun searchItems(query: String) {
        if (query.length >= 3) {
            val contentItems =defaultlistItems.filter { items->
                items.name.contains(query,true)
            }
            _contentItems.value = ArrayList(contentItems)
        } else {
            _contentItems.value = ArrayList(emptyList())
        }
    }

}

