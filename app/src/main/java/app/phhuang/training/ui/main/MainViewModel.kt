package app.phhuang.training.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.phhuang.training.model.ResultX

class MainViewModel : ViewModel() {
    val areaList = MutableLiveData<List<ResultX>>()

    fun setAreaList(areaList: List<ResultX>) {
        this.areaList.value = areaList
    }
}
