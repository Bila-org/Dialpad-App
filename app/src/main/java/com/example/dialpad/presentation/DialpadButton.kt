package com.example.dialpad.presentation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dialpad.R
import com.example.dialpad.ui.theme.DialpadTheme

@Composable
fun DialpadButton(
    number: String,
    letters: String,
    onPress: () -> Unit,
    onRelease: () -> Unit,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium
        ), label = "button_scale"
    )

    val ripple = rememberRipple(
        bounded = true,
        color = MaterialTheme.colorScheme.primary
    )

    Box(
        modifier = modifier
            .height(dimensionResource(R.dimen.button_height))
            .width(dimensionResource(R.dimen.button_width))
            .scale(scale)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .indication(
                interactionSource = interactionSource,
                indication = ripple
            )
            .pointerInput(Unit) {
                detectTapGestures(onPress = { offset ->
                    val press = PressInteraction.Press(offset)
                    interactionSource.tryEmit(press)
                    onPress()
                    try {
                        awaitRelease()
                    } finally {
                        interactionSource.tryEmit(PressInteraction.Release(press))
                        onRelease()
                    }
                }, onLongPress = {
                    onLongPress()
                })
            }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = number,
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Text(
                text = letters.uppercase(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialpadIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    tint: Color,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val ripple = rememberRipple(
        bounded = true,
        color = MaterialTheme.colorScheme.primary
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "button_scale"
    )

    Box(
        modifier = modifier
            .height(dimensionResource(R.dimen.button_height))
            .width(dimensionResource(R.dimen.button_width))
            .scale(scale)
            .clip(RoundedCornerShape(50.dp))
            .background(backgroundColor)
            .indication(interactionSource, ripple)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
                interactionSource = interactionSource,
                indication = ripple
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(dimensionResource(R.dimen.button_icon_size)),
            tint = tint
        )
    }
}


@Preview(
    showBackground = true,
    showSystemUi = false,
)
@Composable
fun DialpadButtonPreview() {
    DialpadTheme {
        DialpadButton(number = "1", letters = "ABC", onPress = {}, onRelease = {}, onLongPress = {})
    }
}


@Preview(
    showBackground = true,
    showSystemUi = false,
)
@Composable
fun DialpadIconButtonPreview() {
    DialpadTheme {
        Row() {
            DialpadIconButton(
                icon = Icons.Outlined.Contacts,
                contentDescription = "Contacts",
                onClick = { },
                tint = Color.Black.copy(alpha = 0.6f),
                backgroundColor = Color.Transparent
            )

            DialpadIconButton(
                icon = Icons.Filled.Call,
                contentDescription = "Call",
                onClick = { },
                tint = Color.White,
                backgroundColor = Color(0xFF008400).copy(alpha = 0.6f)
            )
            DialpadIconButton(
                icon = Icons.AutoMirrored.Outlined.Backspace,
                contentDescription = "Backspace",
                onClick = { },
                onLongClick = { },
                tint = Color.Black.copy(alpha = 0.6f),
                backgroundColor = Color.Transparent,
            )
        }
    }
}