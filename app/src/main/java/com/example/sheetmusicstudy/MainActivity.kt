package com.example.sheetmusicstudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SheetMusicApp(modifier = Modifier)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SheetMusicApp(modifier: Modifier = Modifier) {
    SheetMusicPage(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)
        .background(Color(0xFF669ccc))
    )
}

@Composable
fun SheetMusicPage(modifier: Modifier = Modifier) {
    /*
    *  TODO: Find better solution to store these notes and heights.
    */
    val notes = arrayOf("D4", "E4", "F4", "G4", "A4", "B4", "C5", "D5", "E5", "F5", "G5")
    val heights = arrayOf(141, 149,  158,  166,  175,  183,  192,  200,  209,  217,  226)

    // 184 is B4, so maybe increments of 8?
    // 192 is C with 1.8f scaling
    // Not quite 9px, sometimes 8px

    // Default note is D4
    val defaultNoteHeight by remember { mutableStateOf(141) }
    var noteHeight by remember { mutableStateOf(defaultNoteHeight) }

    // Ideally, these would be in a class somewhere, but for now they're here.
    var noteName by remember { mutableStateOf("D4") }
    var userGuess by remember { mutableStateOf("") }

    // States: "None", "NoteSelection", "OctaveSelection".
    var noteInputPopupState by remember { mutableStateOf("None") }

    var revealBtnText by remember { mutableStateOf(R.string.reveal_button_text) }
    // These should really be in a class
    val inputButtonFn: () -> Unit = {
        noteInputPopupState = "NoteSelection"
    }
    val noteButtonFn: (String) -> Unit = { note ->
        userGuess = note
        noteInputPopupState = "OctaveSelection"
    }
    val octaveButtonFn: (String) -> Unit = { octave ->
        userGuess += octave
        noteInputPopupState = "None"
    }


    Box(modifier = modifier) {
        Column (
            modifier = modifier.fillMaxSize(),
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
                        .padding(start = 70.dp, bottom = noteHeight.dp)
                        .align(Alignment.BottomStart)
                )
            }

            Text(
                text = userGuess,
                fontSize = 24.sp,
                modifier = Modifier
                    .height(50.dp)

            )

            when (noteInputPopupState) {
                "None" -> InputButton(fn = inputButtonFn)
                "NoteSelection" -> NoteSelectionPopup(fn = noteButtonFn)
                "OctaveSelection" -> OctaveSelectionPopup(fn = octaveButtonFn)
            }


            Spacer(modifier = Modifier.height(50.dp))

            Button(onClick = {
                if (userGuess.length == 2) {
                    revealBtnText = R.string.next_note_button_text
                    if (userGuess == noteName) {
                        userGuess = "Correct! It was: " + noteName
                    } else {
                        userGuess = "Incorrect, it was: " + noteName
                    }

                } else if (userGuess.length > 2) {
                    revealBtnText = R.string.reveal_button_text
                    val n = (0..10).random()
                    noteHeight = heights[n]
                    noteName = notes[n]
                    userGuess = ""
                }
            }) {
                Text(text = stringResource(revealBtnText), fontSize = 24.sp)
            }
        }


    }
}

@Composable
fun InputButton(fn: () -> Unit) {
    Button(onClick = fn) {
        Text(text = stringResource(R.string.guess_note_button_text), fontSize = 24.sp)
    }
    Spacer(modifier = Modifier.height(48.dp))
}

@Composable
fun NoteSelectionPopup(modifier: Modifier = Modifier, fn: (String) -> Unit) {
    // A row containing buttons with texts C-B
    Column (
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = modifier) {
            NoteButton(note = "C", fn = fn)
            NoteButton(note = "D", fn = fn)
            NoteButton(note = "E", fn = fn)
            NoteButton(note = "F", fn = fn)
        }
        Row {
            NoteButton(note = "G", fn = fn)
            NoteButton(note = "A", fn = fn)
            NoteButton(note = "B", fn = fn)
        }
    }
}


@Composable
fun NoteButton(note: String, fn: (String) -> Unit) {
    Button(onClick = { fn(note) },
        modifier = Modifier.padding(0.dp)
    ) {
        Text(text = note, fontSize = 16.sp)
    }
}

@Composable
fun OctaveSelectionPopup(modifier: Modifier = Modifier, fn: (String) -> Unit) {
    Row(modifier = modifier) {
        OctaveButton(octave = "4", fn = fn)
        OctaveButton(octave = "5", fn = fn)
    }
}

@Composable
fun OctaveButton(octave: String, fn: (String) -> Unit) {
    Button(onClick = { fn(octave) },
        modifier = Modifier.padding(24.dp)
    ) {
        Text(text = octave, fontSize = 16.sp)
    }
}
