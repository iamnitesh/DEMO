package com.example.demo.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.demo.MyApplication
import com.example.demo.databinding.ActivityDetailBinding
import com.example.demo.model.Photos
import com.example.demo.model.Posts
import com.example.demo.utils.CheckInternetAvailability
import com.example.demo.utils.Constants.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

class DetailActivity : AppCompatActivity() {

    lateinit var detailBinding: ActivityDetailBinding

    @Inject
    lateinit var detailViewModel: DetailViewModel

    var postID by Delegates.notNull<Int>()
    var img =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        postID = intent.getIntExtra("PostID",-1)
        img = intent.getStringExtra("img")!!


        (application as MyApplication).appComponent.inject(this)


        CheckInternetAvailability(this).observe(this, Observer {
            Log.i("*-*-*", "is internet available? - ${if (it) "Yes" else "No"}")
            if (it) {
                loadData(true)
            } else {
                toast("NO CONNECTION AVAILABLE")
            }
        })


        Glide.with(this)
            .load(img) // image url
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .into(detailBinding.srcImg);

    }

    private fun loadData(b: Boolean) {

        detailViewModel.postsListLiveData.observe(this@DetailActivity,
            Observer<List<Posts>> {
                Log.e("API CALLED", "RESPONSE "+it.size)
                if (it.isEmpty()){
                    toast("NO DATA FOUND")
                }else {
                    setData(it[0])
                }
            })


        CoroutineScope(Dispatchers.Main).launch {
            detailViewModel.getAllPosts(b,postID)
        }


        detailViewModel._isLoading.observe(this@DetailActivity, Observer {
            if (it) {
                detailBinding.progressCircular.visibility = View.VISIBLE
            } else
                detailBinding.progressCircular.visibility = View.GONE
        })

        detailViewModel._error.observe(this@DetailActivity, Observer {

            if (it.isNotEmpty()) {
                toast(it)
            }
        })
    }

    private fun setData(posts: Posts) {

        detailBinding.txtName.text = posts.name
        detailBinding.txtEmail.text = posts.email
        detailBinding.txtDescription.text = posts.body

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}