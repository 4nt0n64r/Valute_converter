package com.a4nt0n64r.valuteconverter.screens.select

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a4nt0n64r.valuteconverter.APP_ACTIVITY
import com.a4nt0n64r.valuteconverter.R
import com.a4nt0n64r.valuteconverter.VALUTE
import com.a4nt0n64r.valuteconverter.databinding.FragmentSelectValuteBinding
import com.a4nt0n64r.valuteconverter.models.ValuteUI


class SelectValuteFragment : Fragment() {

    private var binding: FragmentSelectValuteBinding? = null
    private val notNullBinding get() = binding!!

    private lateinit var vievModel: SelectViewModel

    private lateinit var changingType: ChangingType

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelectAdapter
    private lateinit var observerOnList: Observer<List<ValuteUI>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSelectValuteBinding.inflate(layoutInflater, container, false)
        return notNullBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
    }

    private fun initialization() {
        adapter = SelectAdapter()
        recyclerView = notNullBinding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        observerOnList = Observer {
            val list = it
            adapter.setData(list)
        }

        vievModel = ViewModelProvider(this).get(SelectViewModel::class.java)

        val bundle = this.arguments
        if (bundle != null) {
            when (bundle.getString("type")) {
                "L"-> changingType = ChangingType.LEFT
                "R"-> changingType = ChangingType.RIGHT
            }
            if(bundle.getSerializable(VALUTE) != null){
                vievModel.setSelectedValute(bundle.getSerializable(VALUTE) as ValuteUI)
            }
        }

        adapter.setChangingType(changingType)



        vievModel.valutesLivaData.observe(this, observerOnList)

        notNullBinding.cancel.setOnClickListener {
            vievModel.clickBack()
        }

        initializeLoadingObserver()

        vievModel.refresh()
    }

    private fun initializeLoadingObserver() {
        val loadingObserver: Observer<Boolean> = Observer {
            loadingVisibility(it)
        }
        vievModel.loadingVisible.observe(this, loadingObserver)
    }

    private fun loadingVisibility(it: Boolean?) {
        when(it){
            true -> notNullBinding.progressBar.visibility = View.VISIBLE
            else -> notNullBinding.progressBar.visibility = View.INVISIBLE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        vievModel.valutesLivaData.removeObserver(observerOnList)
        recyclerView.adapter = null
    }

    companion object {
        fun click(valute: ValuteUI, changingType: ChangingType) {
            val bundle = Bundle()
            when (changingType) {
                ChangingType.LEFT -> {
                    bundle.putSerializable("L", valute)
                }
                ChangingType.RIGHT -> {
                    bundle.putSerializable("R", valute)
                }
            }
            APP_ACTIVITY.navController.navigate(
                R.id.action_selectValuteFragment_to_calculatorFragment,
                bundle
            )
        }
    }


}

enum class ChangingType {
    LEFT, RIGHT
}
