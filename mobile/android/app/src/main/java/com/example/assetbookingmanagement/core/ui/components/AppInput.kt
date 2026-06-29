package com.example.assetbookingmanagement.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assetbookingmanagement.core.ui.theme.InputFocusBorder
import com.example.assetbookingmanagement.core.ui.theme.InputPlaceholderDark
import com.example.assetbookingmanagement.core.ui.theme.InputPlaceholderLight
import com.example.assetbookingmanagement.core.ui.theme.InputSurfaceDark
import com.example.assetbookingmanagement.core.ui.theme.InputSurfaceLight
import com.example.assetbookingmanagement.core.ui.theme.InputShadow
import com.example.assetbookingmanagement.core.ui.theme.InputTextDark
import com.example.assetbookingmanagement.core.ui.theme.InputTextLight

data class AppInputConfig(
    val enabled: Boolean = true,
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    val visualTransformation: VisualTransformation = VisualTransformation.None,
    val passwordVisibilityToggle: @Composable (() -> Unit)? = null
)

@Composable
fun AppInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    config: AppInputConfig = AppInputConfig()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val isDarkTheme = isSystemInDarkTheme()

    val textColor = if (isDarkTheme) InputTextDark else InputTextLight
    val placeholderColor = if (isDarkTheme) InputPlaceholderDark else InputPlaceholderLight
    val containerColor = if (isDarkTheme) InputSurfaceDark else InputSurfaceLight
    val borderColor = if (isFocused) InputFocusBorder else Color.Transparent

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawWithContent {
                    if (config.enabled) {
                        drawIntoCanvas { canvas ->
                            val paint = android.graphics.Paint().apply {
                                color = android.graphics.Color.TRANSPARENT
                                setShadowLayer(3.dp.toPx(), 0f, 3.dp.toPx(), InputShadow.toArgb())
                            }
                            canvas.nativeCanvas.drawRoundRect(
                                0f,
                                0f,
                                size.width,
                                size.height,
                                8.dp.toPx(),
                                8.dp.toPx(),
                                paint
                            )
                        }
                    }
                    drawContent()
                }
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(
                    color = containerColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .alpha(if (config.enabled) 1f else 0.5f)
        ) {
            // Each color must be defined for all three states: focused, unfocused and disabled
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = config.enabled,
                singleLine = true,
                textStyle = TextStyle(
                    color = textColor,
                    fontSize = 14.sp,
                    lineHeight = 14.sp
                ),
                placeholder = {
                    Text(
                        text = placeholder,
                        color = placeholderColor,
                        fontSize = 14.sp,
                        letterSpacing = 2.8.sp
                    )
                },
                trailingIcon = config.passwordVisibilityToggle,
                keyboardOptions = config.keyboardOptions,
                visualTransformation = config.visualTransformation,
                interactionSource = interactionSource,
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    disabledTextColor = textColor,
                    focusedPlaceholderColor = placeholderColor,
                    unfocusedPlaceholderColor = placeholderColor,
                    disabledPlaceholderColor = placeholderColor
                )
            )
        }
    }
}
