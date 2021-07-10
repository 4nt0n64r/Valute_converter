package com.a4nt0n64r.valuteconverter.screens.calculator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.a4nt0n64r.valuteconverter.models.ValuteUI

class CalculatorViewModel(app: Application) : AndroidViewModel(app) {

    var leftValute = MutableLiveData<ValuteUI>()
    var rightValute = MutableLiveData<ValuteUI>()

    fun changeLeft() {
        CalculatorFragment.changeLeft(leftValute.value)
    }

    fun changeRight() {
        CalculatorFragment.changeRight(rightValute.value)
    }
}

// Формула перевода левой валюты в правую (заменить индексы и будет правой в левую):
//  TV_L * V_L * N_R / V_R / N_L = TV_R
//  N - номинал, V - value