package ir.andromeda.cartoonabad.feature.player

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.TextView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import ir.andromeda.cartoonabad.R
import ir.andromeda.cartoonabad.common.CartoonAbadActivity
import ir.andromeda.cartoonabad.common.EXTRA_KEY_DATA
import ir.andromeda.cartoonabad.data.episode.Episode
import ir.andromeda.cartoonabad.databinding.ActivityPlayerBinding
import java.io.File


class PlayerActivity : CartoonAbadActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private var player: SimpleExoPlayer? = null

    //for play from the last position
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    private lateinit var mediaItem: MediaItem
    private lateinit var episode: Episode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        episode = intent.getParcelableExtra<Episode>(EXTRA_KEY_DATA) as Episode

        binding.playerView.findViewById<View>(R.id.ivBack).setOnClickListener { onBackPressed() }
        binding.playerView.findViewById<TextView>(R.id.tvEpisodeName).text = episode.name


        //making screen landscape
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

    }


    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                binding.playerView.player = exoPlayer

                val filePath =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .toString() + "/CartoonAbad/" + episode.url.substringAfterLast('/')

                mediaItem = if (File(filePath).exists())
                    MediaItem.fromUri(filePath)
                else
                    MediaItem.fromUri(episode.url)

                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.prepare()
            }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        binding.playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }
}