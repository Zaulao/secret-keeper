package com.example.secretkeeper

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.secretkeeper.data.SecureData
import com.example.secretkeeper.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SecureDataViewModel
    private lateinit var dataList: LiveData<List<SecureData>>
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, SecureDataViewModelFactory(application)).get(SecureDataViewModel::class.java)

        dataList = viewModel.getSecureData()
//        var str = "ok"
//        insertFileOnDatabase(str)

        val secureObserver = Observer<List<SecureData>> { secureDataList ->
            val list: List<SecureData> = secureDataList
            while (list.iterator().hasNext()) {
                val secureData = list.iterator().next()
                println(secureData.name)
            }
        }

        dataList.observe(this, secureObserver)

        setContentView(R.layout.activity_main)
    }

    private fun insertFileOnDatabase(str: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            coroutineScope.launch {
                viewModel.insertData(
                    SecureData(
                        0,
                        "nameToFindInLogcat",
                        "txt",
                        str.toByteArray(),
                        LocalDateTime.now().toString()
                    )
                )
            }
        }
    }
}