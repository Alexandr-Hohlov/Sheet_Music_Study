package com.example.sheetmusicstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sheetmusicstudy.ui.theme.SheetMusicStudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SheetMusicApp()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SheetMusicApp() {
    SheetMusicPage(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center))
}

@Composable
fun SheetMusicPage(modifier: Modifier = Modifier) {
    val notes = arrayOf("D4", "E4", "F4", "G4", "A4", "B4", "C5", "D5", "E5", "F5", "G5")
    val heights = arrayOf(141, 149,  158,  166,  175,  183,  192,  200,  209,  217,  226)

    // Default note is D4
    val defaultNoteHeight by remember { mutableStateOf(141) }
    var noteHeight by remember { mutableStateOf(defaultNoteHeight) }
    var noteName by remember { mutableStateOf("D4") }

    // 184 is B4, so maybe increments of 8?
    // 192 is C with 1.8f scaling
    // Maybe 9px difference?

    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box (modifier = Modifier.height(500.dp)) {
            Image(painter = painterResource(R.drawable.music_staff),
                contentDescription = "the music staff",
                modifier = Modifier
                    .align(Alignment.Center))

            Image(painter = painterResource(R.drawable.quarter_note_upwards),
                contentDescription = "quarter note with upward stem",
                modifier = Modifier
                    .scale(1.8f)
                    .padding(start = 75.dp, bottom = noteHeight.dp)
                    .align(Alignment.BottomStart)
            )
        }
        Spacer(modifier = Modifier.height(50.dp))

        Button(onClick = {
            val n = (0..10).random()
            noteHeight = heights[n]
            noteName = notes[n]
        }) {
            Text(text = stringResource(R.string.reveal_button_text), fontSize = 24.sp)
        }
    }
}

