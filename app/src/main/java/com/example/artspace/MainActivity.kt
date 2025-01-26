package com.example.artspace

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artspace.ui.theme.ArtSpaceTheme
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArtSpaceTheme {
                Surface() {
                    ArtSpace()
                }
            }
        }
    }
}
val regular = FontFamily(
    Font(R.font.regular, FontWeight.Normal)
)
val bold = FontFamily(
    Font(R.font.bold, FontWeight.Normal)
)

fun formatTime(seconds: Float): String {
    val minutes = (seconds / 60).toInt()
    val secs = (seconds % 60).toInt()
    return String.format("%02d:%02d", minutes, secs)
}

@Composable
fun ArtSpace(modifier: Modifier = Modifier) {
    var screen by remember { mutableIntStateOf(1) }
    var isPlaying by remember { mutableStateOf(false) }
    var isSwiping by remember { mutableStateOf(false) }
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var totalTime by remember { mutableFloatStateOf(0f) }
    val context = LocalContext.current
    LaunchedEffect(screen) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(
            context, when (screen) {
                1 -> R.raw.bandana_song
                2 -> R.raw.lithium_nirvana
                3 -> R.raw.bohemian_rhapsody_song
                else -> R.raw.master_of_pupets_song
            }
        ).apply {
            totalTime = (duration.toFloat() / 1000)
            if (isPlaying) {
                start()
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(when(screen){
                1 -> 0xFF232323
                2 -> 0xFF0C4664
                3 -> 0xFF232420
                else -> 0xFF412323
            }))
            .padding(
                top = 35.dp,
                bottom = 55.dp
            )
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        isSwiping = true
                    },
                    onDragEnd = {
                        isSwiping = false
                    }
                ) { _, dragAmount ->
                    if (isSwiping) {
                        when {
                            dragAmount > 50 -> {
                                sliderPosition = 0f
                                if(screen == 1) screen = 4 else screen -= 1
                                isSwiping = false
                            }
                            dragAmount < -50 -> {
                                sliderPosition = 0f
                                if(screen == 4) screen = 1 else screen += 1
                                isSwiping = false
                            }
                        }
                    }
                }
            }
    ) {
        Box() {
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp)

                ) {
                    Image(
                        painter = painterResource(R.drawable.down),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.devices),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.history),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Text(
                text = stringResource(R.string.volna),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = bold
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Column() {
                Image(
                    painter = painterResource(when(screen){
                        1 -> R.drawable.bandana
                        2 -> R.drawable.nirvana
                        3 -> R.drawable.queen
                        else -> R.drawable.metallica
                    }),
                    contentDescription = null,
                    modifier = Modifier.size(300.dp)
                )
                Spacer(modifier = Modifier.height(13.dp))
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.width(300.dp)) {
                    Column {
                        Text(
                            text = when(screen){
                                1 -> stringResource(R.string.bandana)
                                2 -> stringResource(R.string.lithium)
                                3 -> stringResource(R.string.bohemian_rapsody)
                                else -> stringResource(R.string.master_of_puppets)
                            },
                            fontSize = 22.sp,
                            color = Color.White,
                            fontFamily = regular,
                            modifier = Modifier.padding(start = 0.dp) // Убедитесь, что отступы равны 0
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when(screen){
                                1 -> stringResource(R.string.bbt_kizaru)
                                2 -> stringResource(R.string.nirvana)
                                3 -> stringResource(R.string.queen)
                                else -> stringResource(R.string.metallica)
                            },
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontFamily = regular,
                            modifier = Modifier.padding(start = 0.dp) // Убедитесь, что отступы равны 0
                        )
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.settings),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                }
                Spacer(modifier = Modifier.height(20.dp))
                Column {
                    Slider(
                        value = sliderPosition,
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = Color.White,
                            inactiveTrackColor = Color.Gray
                        ),
                        onValueChange = { newPosition ->
                            sliderPosition = newPosition
                            mediaPlayer?.seekTo((newPosition * 1000).toInt()) },
                        valueRange = 0f..totalTime,
                        modifier = Modifier.width(300.dp)
                    )
                    Row(horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.width(300.dp)){
                        Text(
                            text = formatTime(sliderPosition),
                            color = Color.Gray,
                            fontFamily = regular
                        )
                        Text(
                            text = formatTime(totalTime),
                            color = Color.Gray,
                            fontFamily = regular
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically){
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.heart_minus),
                            contentDescription = null,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                    Button(
                        onClick = {sliderPosition =0f
                            if(screen == 1) screen = 4 else screen -= 1},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.skip_previous),
                            contentDescription = null,
                            modifier = Modifier.size(45.dp)
                        )
                    }
                    Button(
                        onClick = {if(isPlaying){
                            mediaPlayer?.pause()
                            isPlaying = false
                        } else {
                            mediaPlayer?.start()
                            isPlaying = true
                        }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Image(
                            painter = if(isPlaying) {
                                painterResource(R.drawable.pause_circle)
                            } else{
                                painterResource(R.drawable.play_circle)
                            },
                            contentDescription = null,
                            modifier = Modifier.size(70.dp)
                        )
                    }
                    LaunchedEffect(isPlaying, screen) {
                        if (isPlaying) {
                            while (sliderPosition < totalTime) {
                                delay(1000)
                                sliderPosition += 1f
                            }
                            if (screen == 4) screen = 1 else screen += 1
                            sliderPosition = 0f
                        }
                    }
                    Button(
                        onClick = {
                            sliderPosition =0f
                            if(screen == 4) screen = 1 else screen += 1
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.skip_next),
                            contentDescription = null,
                            modifier = Modifier.size(45.dp)
                        )
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.heart_plus),
                            contentDescription = null,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.shuffle),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.tune),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.lyrics),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.timer),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun App(modifier: Modifier = Modifier){
    ArtSpace()
}