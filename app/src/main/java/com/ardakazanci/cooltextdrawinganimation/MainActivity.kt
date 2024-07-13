package com.ardakazanci.cooltextdrawinganimation


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ardakazanci.cooltextdrawinganimation.ui.theme.CoolTextDrawingAnimationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoolTextDrawingAnimationTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AnimatedWords()
                }
            }
        }
    }
}

@Composable
fun AnimatedWords() {
    var selectedLetters by remember { mutableStateOf(listOf<String>()) }
    var isCardVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4D03F)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            listOf("ANDROID", "IS", "COOL").forEach { word ->
                AnimatedWord(word = word) { letter ->
                    selectedLetters = selectedLetters + letter
                    isCardVisible = true
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        if (isCardVisible) {
            SelectedLettersCard(letters = selectedLetters, onDismiss = {
                isCardVisible = false
                selectedLetters = emptyList()
            })
        }
    }
}

@Composable
fun AnimatedWord(word: String, onLetterClick: (String) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        word.forEachIndexed { index, letter ->
            AnimatedLetter(
                letter = letter.toString(),
                delayMillis = index * 500L,
                onClick = { onLetterClick(letter.toString()) }
            )
        }
    }
}

@Composable
fun AnimatedLetter(letter: String, delayMillis: Long, onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val translateX by infiniteTransition.animateFloat(
        initialValue = -30f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                delayMillis = delayMillis.toInt(),
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val translateY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                delayMillis = delayMillis.toInt(),
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val textShadow by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                delayMillis = delayMillis.toInt(),
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(
        modifier = Modifier
            .offset(x = translateX.dp, y = translateY.dp)
            .padding(horizontal = 4.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() }
                )
            }
    ) {
        BasicText(
            text = letter,
            style = TextStyle(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF4D03F),
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.75f),
                    offset = with(LocalDensity.current) { Offset(0.dp.toPx(), textShadow) },
                    blurRadius = textShadow
                )
            )
        )
    }
}

@Composable
fun SelectedLettersCard(letters: List<String>, onDismiss: () -> Unit) {
    val density = LocalDensity.current
    val transitionState = remember { MutableTransitionState(false) }
    transitionState.targetState = true

    val offsetY by updateTransition(transitionState, label = "CardTransition")
        .animateDp(
            transitionSpec = { tween(durationMillis = 1000, easing = LinearOutSlowInEasing) },
            label = "CardOffsetY"
        ) { state ->
            if (state) with(density) { 200.dp } else with(density) { -200.dp }
        }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .offset(y = offsetY)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onDismiss() })
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            letters.forEach { letter ->
                AnimatedLetterInCard(letter = letter)
            }
        }
    }
}

@Composable
fun AnimatedLetterInCard(letter: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val color by infiniteTransition.animateColor(
        initialValue = Color(0xFFFF5733),
        targetValue = Color(0xFF3CFF57),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    BasicText(
        text = letter,
        style = TextStyle(
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            fontFamily = FontFamily.Cursive
        ),
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .scale(scale)
    )
}

