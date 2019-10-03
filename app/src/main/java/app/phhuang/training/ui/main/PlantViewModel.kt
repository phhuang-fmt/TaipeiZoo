package app.phhuang.training.ui.main

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlantViewModel : ViewModel() {
    var imgUrl : MutableLiveData<String> = MediatorLiveData<String>()
    var name : MutableLiveData<String> = MediatorLiveData<String>()
    var nameLatin : MutableLiveData<String> = MediatorLiveData<String>()
    var alsoKnown : MutableLiveData<String> = MediatorLiveData<String>()
    var brief : MutableLiveData<String> = MediatorLiveData<String>()
    var feature : MutableLiveData<String> = MediatorLiveData<String>()
    var function : MutableLiveData<String> = MediatorLiveData<String>()
    var update : MutableLiveData<String> = MediatorLiveData<String>()
}