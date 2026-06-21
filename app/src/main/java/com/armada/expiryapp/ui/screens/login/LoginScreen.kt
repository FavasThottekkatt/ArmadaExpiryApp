package com.armada.expiryapp.ui.screens.login

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.armada.expiryapp.ui.theme.ArmadaColors

@Composable
fun LoginScreen(
    uiState: LoginViewModel.UiState,
    onPasswordSubmit: (String) -> Unit,
    onErrorHandled: () -> Unit,
) {
    var password by remember { mutableStateOf("") }

    val isLoading = uiState == LoginViewModel.UiState.Verifying
    val isError   = uiState == LoginViewModel.UiState.LoginError

    val shakeOffset = remember { Animatable(0f) }

    LaunchedEffect(isError) {
        if (isError) {
            // Oscillate left/right, then settle
            for (i in 0 until 4) {
                shakeOffset.animateTo(
                    targetValue    = if (i % 2 == 0) 16f else -16f,
                    animationSpec  = tween(durationMillis = 60, easing = LinearEasing),
                )
            }
            shakeOffset.animateTo(0f, tween(60))
            password = ""
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ArmadaColors.BgApp)
            .imePadding(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(48.dp))

            // Announcement pill
            Surface(
                shape  = RoundedCornerShape(50),
                color  = Color.White,
                border = BorderStroke(1.dp, ArmadaColors.BrandAccent.copy(alpha = 0.5f)),
            ) {
                Text(
                    text     = "SUBMIT BEFORE 20TH EVERY MONTH",
                    color    = ArmadaColors.BrandAccent,
                    style    = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text       = "EXPIRY TRACKING",
                color      = ArmadaColors.BrandTitle,
                fontWeight = FontWeight.Bold,
                fontSize   = 28.sp,
            )

            Text(
                text     = "Armada Distribution",
                color    = ArmadaColors.TextPrimary,
                fontSize = 20.sp,
            )

            // Region badge
            Surface(
                shape  = RoundedCornerShape(50),
                color  = Color.Transparent,
                border = BorderStroke(1.dp, ArmadaColors.Border),
            ) {
                Text(
                    text     = "ABU DHABI & AL AIN",
                    color    = ArmadaColors.TextSecondary,
                    style    = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                )
            }

            Spacer(Modifier.height(24.dp))

            // Login card
            Card(
                modifier  = Modifier
                    .fillMaxWidth()
                    .offset(x = shakeOffset.value.dp),
                shape     = RoundedCornerShape(12.dp),
                colors    = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
                Column(
                    modifier            = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // Invisible password field — VisualTransformation.None + transparent text color
                    // so characters are present but not visible; cursor remains visible.
                    OutlinedTextField(
                        value         = password,
                        onValueChange = {
                            if (isError) onErrorHandled()
                            if (!isLoading) password = it
                        },
                        label         = { Text("Password") },
                        leadingIcon   = {
                            Icon(
                                imageVector        = Icons.Filled.Lock,
                                contentDescription = null,
                                tint               = ArmadaColors.FieldActiveBorder,
                            )
                        },
                        visualTransformation = VisualTransformation.None,
                        keyboardOptions      = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction    = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (!isLoading && password.isNotEmpty()) onPasswordSubmit(password)
                            }
                        ),
                        singleLine = true,
                        enabled    = !isLoading,
                        isError    = isError,
                        modifier   = Modifier.fillMaxWidth(),
                        colors     = OutlinedTextFieldDefaults.colors(
                            focusedTextColor     = Color.Transparent,
                            unfocusedTextColor   = Color.Transparent,
                            disabledTextColor    = Color.Transparent,
                            errorTextColor       = Color.Transparent,
                            cursorColor          = ArmadaColors.FieldActiveBorder,
                            errorCursorColor     = ArmadaColors.FieldActiveBorder,
                            focusedBorderColor   = ArmadaColors.FieldActiveBorder,
                            unfocusedBorderColor = ArmadaColors.Border,
                            focusedLabelColor    = ArmadaColors.FieldActiveLabel,
                            errorBorderColor     = Color(0xFFD32F2F),
                            errorLabelColor      = Color(0xFFD32F2F),
                        ),
                    )

                    if (isError) {
                        Text(
                            text  = "Incorrect password. Please try again.",
                            color = Color(0xFFD32F2F),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }

                    Button(
                        onClick  = {
                            if (!isLoading && password.isNotEmpty()) onPasswordSubmit(password)
                        },
                        enabled  = !isLoading && password.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(8.dp),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor         = ArmadaColors.BtnLogin,
                            disabledContainerColor = ArmadaColors.Disabled,
                            disabledContentColor   = ArmadaColors.DisabledText,
                        ),
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier    = Modifier.size(20.dp),
                                color       = Color.White,
                                strokeWidth = 2.dp,
                            )
                        } else {
                            Text(
                                text       = "Access App →",
                                style      = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(48.dp))
        }
    }
}
