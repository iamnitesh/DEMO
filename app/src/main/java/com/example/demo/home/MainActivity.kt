package com.example.demo.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import com.example.demo.MyApplication
import com.example.demo.adapter.MainAdapter
import com.example.demo.databinding.ActivityMainBinding
import com.example.demo.detail.DetailActivity
import com.example.demo.listeners.ItemClickListeners
import com.example.demo.model.Photos
import kotlinx.coroutines.*
import javax.inject.Inject
import com.example.demo.utils.CheckInternetAvailability
import com.example.demo.utils.Constants.toast


class MainActivity : AppCompatActivity(), ItemClickListeners {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var mainAdapter: MainAdapter

    var list= emptyList<Photos>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)



        (application as MyApplication).appComponent.inject(this)

        mainAdapter = MainAdapter(this@MainActivity, emptyList(),this)
        mainBinding.recPhotos.adapter = mainAdapter
        CheckInternetAvailability(this).observe(this, Observer {
            Log.i("*-*-*", "is internet available? - ${if (it) "Yes" else "No"}")
            if (it) {
                loadData(true)
            } else {
                toast("NO CONNECTION AVAILABLE")

                if (mainAdapter.differ.currentList.size == 0)
                    loadData(false)
            }
        })

    }

    private fun loadData(fromServer: Boolean) {


        mainViewModel.photosListLiveData.observe(this@MainActivity,
            Observer<List<Photos>> {
                Log.e("API CALLED", "RESPONSE "+it.size)
                list = it
                mainAdapter.differ.submitList(it)

            })

        
         CoroutineScope(Dispatchers.Main).launch {
                mainViewModel.getPhotos(fromServer)
            }
        

        mainViewModel._isLoading.observe(this@MainActivity, Observer {

            if (it) {
                mainBinding.progressCircular.visibility = View.VISIBLE
            } else
                mainBinding.progressCircular.visibility = View.GONE
        })

        mainViewModel._error.observe(this@MainActivity, Observer {

            if (it.isNotEmpty()) {
                toast(it)
            }
        })


    }

    override fun onItemClick(pos: Int) {
        
        startActivity(Intent(this@MainActivity,DetailActivity::class.java)
            .putExtra("PostID",list[pos].id)
            .putExtra("img",list[pos].url),
            )
    }


}