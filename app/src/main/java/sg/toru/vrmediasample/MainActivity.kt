package sg.toru.vrmediasample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.source.MediaSource
import android.net.Uri
import android.os.Build
import android.util.Log
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource

class MainActivity : AppCompatActivity() {

    private lateinit var videoPlayer:PlayerView
    private lateinit var exoPlayerInstance:SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        videoPlayer = findViewById(R.id.exoplayer)
        exoPlayerInstance = ExoPlayerFactory.newSimpleInstance(this)
        videoPlayer.player = exoPlayerInstance
        videoPlayer.player.addListener(object: Player.EventListener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when(playbackState){
                    Player.STATE_IDLE->{
                        Log.i("TORU", "STATE IDLE")
                    }
                    Player.STATE_BUFFERING->{
                        isPlaying = false
                        Log.i("TORU", "STATE BUFFERING")
                    }
                    Player.STATE_ENDED->{
                        isPlaying = false
                        Log.i("TORU", "STATE ENDED")
                    }
                    Player.STATE_READY->{
                        isPlaying = true
                        Log.i("TORU", "STATE READY")
                    }
                }
            }

            override fun onPlayerError(error: ExoPlaybackException?) {
                error?.printStackTrace()
            }
        })

        val uri = Uri.parse(url)
        val mediaSource = buildMediaSource(uri)
        exoPlayerInstance.prepare(mediaSource, true, false)
        videoPlayer.player.playWhenReady = true
        videoPlayer.player.seekTo(0)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        return ExtractorMediaSource.Factory(
            DefaultHttpDataSourceFactory("vs-singapore")
        ).createMediaSource(uri)
    }

    private var isPlaying = false

    override fun onStart() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            videoPlayer.onResume()
            if(isPlaying){
                val uri = Uri.parse(url)
                val mediaSource = buildMediaSource(uri)
                exoPlayerInstance.prepare(mediaSource, !isPlaying, false)
            }
        }
        super.onStart()
    }

    override fun onResume() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            videoPlayer.onResume()
            if(isPlaying){
                val uri = Uri.parse(url)
                val mediaSource = buildMediaSource(uri)
                exoPlayerInstance.prepare(mediaSource, !isPlaying, false)
            }
        }
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            videoPlayer.player.stop(false)
            videoPlayer.onPause()
        }
    }

    override fun onStop() {
        super.onStop()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            videoPlayer.player.stop(false)
            videoPlayer.onPause()
        }
    }

    override fun onDestroy() {
        videoPlayer.player.release()
        super.onDestroy()
    }

    companion object{
        private const val url = "https://storage.googleapis.com/exoplayer-test-media-1/360/congo.mp4"
    }
}
