package com.a4nt0n64r.valuteconverter.screens.select

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.a4nt0n64r.valuteconverter.APP_ACTIVITY
import com.a4nt0n64r.valuteconverter.R
import com.a4nt0n64r.valuteconverter.models.ValuteUI
import com.a4nt0n64r.valuteconverter.models.toValuteUI
import com.a4nt0n64r.valuteconverter.repository.ApiService
import kotlinx.coroutines.*

class SelectViewModel(app: Application) : AndroidViewModel(app) {
    private val job: Job by lazy { SupervisorJob() }

    var valutesLivaData = MutableLiveData<List<ValuteUI>>()
    var loadingVisible = MutableLiveData(false)

    var selectedValute = MutableLiveData<ValuteUI>()

    fun setSelectedValute(valute: ValuteUI) {
        selectedValute.value = valute
    }

    fun refresh() {
        CoroutineScope(Dispatchers.IO + job).launch {

            loadingVisible.postValue(true)

            val apiService = ApiService()
            var valutesList = mutableListOf(ValuteUI("0", "RUB", 1.0, "Рубль", 1.0))
            val valutesFromApi =
                apiService.getValutes().execute()
                    .body()!!.valute.map { it.toValuteUI() }
            valutesList.addAll(valutesFromApi)
            if (selectedValute.value != null) {
                valutesList.filter { it.name == selectedValute.value!!.name }
                    .forEach { it.isSelected = true }
            }
            Log.d("TAG", valutesList.toString())
            valutesLivaData.postValue(valutesList)

            loadingVisible.postValue(false)
        }
    }

    fun clickBack() {
        CoroutineScope(Dispatchers.Main + job).launch {
            APP_ACTIVITY.navController.navigate(
                R.id.action_selectValuteFragment_to_calculatorFragment
            )
        }
    }
}