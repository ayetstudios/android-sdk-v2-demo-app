package com.example.androidsdkv2demoapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ayet.sdk.AyetSdk
import com.example.androidsdkv2demoapp.ui.theme.AndroidSDKV2DemoAppTheme
import com.example.androidsdkv2demoapp.ui.theme.AyetOrange
import com.example.androidsdkv2demoapp.ui.theme.AyetPurple
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSDKV2DemoAppTheme {
                AyetDemoApp()
            }
        }
    }
}

@Composable
fun AyetDemoApp() {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("AyetDemoPrefs", Context.MODE_PRIVATE)

    var externalId by remember {
        mutableStateOf(
            prefs.getString("external_id", null) ?: run {
                val newId = "user_${Random.nextInt(100000, 999999)}"
                prefs.edit().putString("external_id", newId).apply()
                newId
            }
        )
    }
    var showEditDialog by remember { mutableStateOf(false) }
    var isInitialized by remember { mutableStateOf(false) }

    // Initialize SDK
    if (!isInitialized) {
        AyetSdk.setDebug(true)
        AyetSdk.setFullscreenMode(true)
        AyetSdk.init(context, AyetConfig.PLACEMENT_ID, externalId)
        AyetSdk.setGender(AyetConfig.DEFAULT_GENDER)
        AyetSdk.setAge(AyetConfig.DEFAULT_AGE)
        AyetSdk.setTrackingCustom1(AyetConfig.DEFAULT_CUSTOM_1)
        isInitialized = true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderSection()

            Spacer(modifier = Modifier.height(24.dp))

            ExternalIdCard(
                externalId = externalId,
                onEditClick = { showEditDialog = true }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ActionButton(
                    text = "Show Offerwall",
                    icon = Icons.AutoMirrored.Filled.List,
                    color = AyetOrange,
                    onClick = { AyetSdk.showOfferwall(context, AyetConfig.ADSLOT_OFFERWALL) }
                )

                ActionButton(
                    text = "Show Surveywall",
                    icon = Icons.Default.CheckCircle,
                    color = AyetPurple,
                    onClick = { AyetSdk.showSurveywall(context, AyetConfig.ADSLOT_SURVEYWALL) }
                )

                ActionButton(
                    text = "Reward Status",
                    icon = Icons.Default.Star,
                    color = MaterialTheme.colorScheme.primary,
                    onClick = { AyetSdk.showRewardStatus(context) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                GetOffersButton(context)
            }

            Spacer(modifier = Modifier.height(32.dp))

            FooterInfo()

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showEditDialog) {
        EditExternalIdDialog(
            currentId = externalId,
            onDismiss = { showEditDialog = false },
            onConfirm = { newId ->
                externalId = newId
                prefs.edit().putString("external_id", newId).apply()
                
                AyetSdk.init(context, AyetConfig.PLACEMENT_ID, newId)
                AyetSdk.setGender(AyetConfig.DEFAULT_GENDER)
                AyetSdk.setAge(AyetConfig.DEFAULT_AGE)
                AyetSdk.setTrackingCustom1(AyetConfig.DEFAULT_CUSTOM_1)
                showEditDialog = false
                Toast.makeText(context, "External ID updated", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Ayet Logo",
                modifier = Modifier.size(120.dp, 64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "SDK Demo",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
fun ExternalIdCard(
    externalId: String,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "External Identifier",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = externalId,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun GetOffersButton(context: Context) {
    var isLoading by remember { mutableStateOf(false) }

    Button(
        onClick = {
            isLoading = true
            Log.d("AyetDemo", "Getting offers...")
            AyetSdk.getOffers(AyetConfig.ADSLOT_FEED) { offersJson ->
                isLoading = false
                if (offersJson != null) {
                    Log.d("AyetDemo", "Offers received: $offersJson")
                    Toast.makeText(
                        context,
                        "Offers received! Check Logcat for details",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Log.w("AyetDemo", "Failed to get offers")
                    Toast.makeText(
                        context,
                        "Failed to get offers",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp
        )
    ) {
        AnimatedVisibility(
            visible = !isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = "Get Offers (API)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
        }
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun FooterInfo() {
    val genderText = when (AyetConfig.DEFAULT_GENDER) {
        AyetSdk.Gender.MALE -> "Male"
        AyetSdk.Gender.FEMALE -> "Female"
        AyetSdk.Gender.NON_BINARY -> "Non-Binary"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SDK v${AyetConfig.SDK_VERSION}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Gender: $genderText • Age: ${AyetConfig.DEFAULT_AGE}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EditExternalIdDialog(
    currentId: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var textValue by remember { mutableStateOf(currentId) }
    val focusManager = LocalFocusManager.current

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = "Edit External ID",
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column {
                Text(
                    text = "The SDK will be re-initialized with the new identifier.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    label = { Text("External Identifier") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (textValue.isNotBlank()) {
                        onConfirm(textValue.trim())
                    }
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
