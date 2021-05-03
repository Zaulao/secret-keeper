package com.example.secretkeeper.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.secretkeeper.*
import com.example.secretkeeper.data.SecureData
import com.example.secretkeeper.databinding.ActivityMainBinding
import com.example.secretkeeper.securedata.SecureDataAdapter
import com.example.secretkeeper.securedata.SecureDataViewHolder
import com.example.secretkeeper.securedata.SecureDataViewModel
import com.example.secretkeeper.securedata.SecureDataViewModelFactory
import com.example.secretkeeper.utils.FILE_PROVIDER
import com.example.secretkeeper.utils.IMAGE_PATH
import com.example.secretkeeper.utils.ImageHelper.ImageHelper.getExternalImageFile
import com.example.secretkeeper.utils.ImageHelper.ImageHelper.getImageFile
import com.example.secretkeeper.utils.REQUEST_IMAGE_CAPTURE
import java.io.File
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SecureDataViewModel
    private lateinit var dataListLiveData: LiveData<List<SecureData>>
    private var dataList = mutableListOf<SecureData>()
    lateinit var currentPhotoPath: String

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

        binding.takePhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }

        initSwipeToDelete()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    getExternalImageFile().apply { currentPhotoPath = absolutePath }
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            FILE_PROVIDER,
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Move image from external file to internal application storage
            var imageFile = getImageFile()
            val externalFile = File(currentPhotoPath)
            externalFile.copyTo(imageFile)
            externalFile.delete()

            val intent = Intent(this, PhotoActivity::class.java)
            intent.putExtra(IMAGE_PATH, imageFile.toString())

            startActivity(intent)
        }
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