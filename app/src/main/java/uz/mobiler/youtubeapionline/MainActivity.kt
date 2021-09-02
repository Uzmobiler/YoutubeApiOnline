package uz.mobiler.youtubeapionline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import uz.mobiler.youtubeapionline.adapters.YoutubeAdapter
import uz.mobiler.youtubeapionline.databinding.ActivityMainBinding
import uz.mobiler.youtubeapionline.models.Item
import uz.mobiler.youtubeapionline.utils.Status
import uz.mobiler.youtubeapionline.viewmodels.YoutubeViewModel

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var youtubeViewModel: YoutubeViewModel

    private val TAG = "MainActivity"

    lateinit var youtubeAdapter: YoutubeAdapter
    lateinit var list: ArrayList<Item>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = ArrayList()
        youtubeAdapter = YoutubeAdapter(list, object : YoutubeAdapter.OnItemClickListener {
            override fun onItemClick(videoId: String) {
                val intent = Intent(this@MainActivity, YoutubeActivity::class.java)
                intent.putExtra("video_id", videoId)
                startActivity(intent)
            }
        })
        binding.rv.adapter = youtubeAdapter

        youtubeViewModel = ViewModelProvider(this)[YoutubeViewModel::class.java]
        youtubeViewModel.getYoutubeData(this).observe(this, Observer {
            when (it.status) {
                Status.ERROR -> {
                    binding.progressbar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    binding.progressbar.visibility = View.GONE
                    it.data?.items?.let { it1 -> list.addAll(it1) }
                    youtubeAdapter.notifyDataSetChanged()
                    Log.d(TAG, "onCreate: ${it.data}")
                }
                Status.LOADING -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
            }
        })
    }
}