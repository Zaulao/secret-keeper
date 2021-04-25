package com.example.secretkeeper

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.secretkeeper.data.SecureData
import com.example.secretkeeper.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SecureDataViewModel
    private lateinit var dataListLiveData: LiveData<List<SecureData>>
    private var dataList = mutableListOf<SecureData>()

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, SecureDataViewModelFactory(application)).get(SecureDataViewModel::class.java)

        dataListLiveData = viewModel.getSecureData()

        val secureObserver = Observer<List<SecureData>> { secureDataList ->
            secureDatabaseListUpdated(secureDataList)
        }

        dataListLiveData.observe(this, secureObserver)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataRecycleView = binding.filesList

        dataRecycleView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
            adapter = SecureDataAdapter(dataList,layoutInflater)
        }

        binding.addFile.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)

            startActivity(intent)
        }

        initSwipeToDelete()
    }

    private fun secureDatabaseListUpdated(secureDataList: List<SecureData>) {
        dataList.removeAll(dataList)
        dataList.addAll(secureDataList)
        binding.filesList.adapter?.notifyDataSetChanged()
    }

    private fun initSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
            ): Int {
                val secureDataViewHolder = viewHolder as SecureDataViewHolder
                return if (secureDataViewHolder.secureData != null) {
                    makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
                } else {
                    makeMovementFlags(0, 0)
                }
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                (viewHolder as SecureDataViewHolder).secureData?.let {
                    viewModel.remove(it)
                }
            }
        }).attachToRecyclerView(binding.filesList)
    }
}