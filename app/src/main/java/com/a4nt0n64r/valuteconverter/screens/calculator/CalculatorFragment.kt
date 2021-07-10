package com.a4nt0n64r.valuteconverter.screens.calculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.a4nt0n64r.valuteconverter.APP_ACTIVITY
import com.a4nt0n64r.valuteconverter.R
import com.a4nt0n64r.valuteconverter.VALUTE
import com.a4nt0n64r.valuteconverter.databinding.CalculatorFragmentBinding
import com.a4nt0n64r.valuteconverter.models.ValuteUI

class CalculatorFragment : Fragment() {


    private var binding: CalculatorFragmentBinding? = null
    private val notNullBinding get() = binding!!

    private lateinit var vievModel: CalculatorViewModel

    private lateinit var observerOnLeftValute: Observer<ValuteUI>

    lateinit var textWatcherLeft: TextWatcher
    lateinit var textWatcherRight: TextWatcher

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = CalculatorFragmentBinding.inflate(layoutInflater, container, false)
        return notNullBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
    }

    private fun initialization() {
        vievModel = ViewModelProvider(requireActivity()).get(CalculatorViewModel::class.java)
        notNullBinding.changeLeftValute.setOnClickListener {
            vievModel.changeLeft()
        }
        notNullBinding.changeRightValute.setOnClickListener {
            vievModel.changeRight()
        }
        getParametersFromBundle()
        initializeLeftObserver()
        initializeRightObserver()

        textWatcherLeft = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                notNullBinding.rightEditText.removeTextChangedListener(textWatcherRight)
                if (!notNullBinding.leftEditText.text.isNullOrEmpty()) {
                    notNullBinding.rightEditText.setText(
                        convertValuteLtoR(
                            notNullBinding.leftEditText.text.toString().toDouble()
                        )
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {
                notNullBinding.rightEditText.addTextChangedListener(textWatcherRight)
            }
        }

        textWatcherRight = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                notNullBinding.leftEditText.removeTextChangedListener(textWatcherLeft)
                if (!notNullBinding.rightEditText.text.isNullOrEmpty()) {
                    notNullBinding.leftEditText.setText(
                        convertValuteRtoL(
                            notNullBinding.rightEditText.text.toString().toDouble()
                        )
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {
                notNullBinding.leftEditText.addTextChangedListener(textWatcherLeft)
            }
        }

        notNullBinding.leftEditText.addTextChangedListener(textWatcherLeft)
        notNullBinding.rightEditText.addTextChangedListener(textWatcherRight)
    }


    private fun getParametersFromBundle() {
        val bundle = this.arguments
        if (bundle != null) {
            if (bundle.getSerializable("L") != null) {
                vievModel.leftValute.value = bundle.getSerializable("L") as ValuteUI
            }
            if (bundle.getSerializable("R") != null) {
                vievModel.rightValute.value = bundle.getSerializable("R") as ValuteUI
            }
        }
    }

    private fun initializeRightObserver() {
        val observerRight: Observer<ValuteUI> = Observer {
            updateRightValuteText(it.charCode)
        }
        vievModel.rightValute.observe(this, observerRight)
    }

    private fun initializeLeftObserver() {
        val observerLeft: Observer<ValuteUI> = Observer {
            updateLeftValuteText(it.charCode)
        }
        vievModel.leftValute.observe(this, observerLeft)
    }

    //Плохо писать логику в UI элементах
    private fun convertValuteLtoR(TV_L: Double): String {
        return if (vievModel.leftValute.value != null && vievModel.rightValute.value != null) {
            (TV_L * vievModel.leftValute.value!!.value * vievModel.rightValute.value!!.nominal / vievModel.rightValute.value!!.value / vievModel.leftValute.value!!.nominal).toString()
        } else "0"
    }

    //Плохо писать логику в UI элементах
    private fun convertValuteRtoL(TV_R: Double): String {
        return if (vievModel.leftValute.value != null && vievModel.rightValute.value != null) {
            (TV_R * vievModel.rightValute.value!!.value * vievModel.leftValute.value!!.nominal / vievModel.leftValute.value!!.value / vievModel.rightValute.value!!.nominal).toString()
        } else "0"
    }

    private fun updateRightValuteText(charCode: String) {
        notNullBinding.rightCurrency.text = charCode
    }

    private fun updateLeftValuteText(charCode: String) {
        notNullBinding.leftCurrency.text = charCode
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        fun changeLeft(valute: ValuteUI?) {
            if (valute != null) {
                val bundle = Bundle()
                bundle.putSerializable(VALUTE, valute)
                navigateL(bundle)
            } else {
                val bundle = Bundle()
                navigateL(bundle)
            }
        }

        private fun navigateL(bundle: Bundle) {
            bundle.putString("type", "L")
            APP_ACTIVITY.navController.navigate(
                R.id.action_calculatorFragment_to_selectValuteFragment,
                bundle
            )
        }

        fun changeRight(valute: ValuteUI?) {
            if (valute != null) {
                val bundle = Bundle()
                bundle.putSerializable(VALUTE, valute)
                navigateR(bundle)
            } else {
                val bundle = Bundle()
                navigateR(bundle)
            }
        }

        private fun navigateR(bundle: Bundle) {
            bundle.putString("type", "R")
            APP_ACTIVITY.navController.navigate(
                R.id.action_calculatorFragment_to_selectValuteFragment,
                bundle
            )
        }
    }
}
