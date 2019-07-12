package sg.toru.vrmediasample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import android.net.Uri
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

        val uri = Uri.parse(url)
        val mediaSource = buildMediaSource(uri)
        exoPlayerInstance.prepare(mediaSource, true, false)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        return ExtractorMediaSource.Factory(
            DefaultHttpDataSourceFactory("exoplayer-codelab")
        ).createMediaSource(uri)
    }

    companion object{
        private const val url = "https://storage.googleapis.com/exoplayer-test-media-1/360/congo.mp4"
    }
}
