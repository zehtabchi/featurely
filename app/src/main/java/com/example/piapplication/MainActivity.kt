package com.example.piapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.piapplication.ui.theme.PiApplicationTheme
import com.mindfulpayments.featurely.FeatureFlagRegistry
import com.mindfulpayments.featurely.FeatureFlags


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PiApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DemoScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun DemoScreen(modifier: Modifier = Modifier) {
    val featureAState = remember { mutableStateOf(FeatureFlagRegistry.getFlag(FeatureFlags.FeatureA)) }
    val featureBState = remember { mutableStateOf(FeatureFlagRegistry.getFlag(FeatureFlags.FeatureB)) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Feature Flag Demo",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))

        FeatureFlagCard(
            name = "Feature A",
            isEnabled = featureAState.value,
            onToggle = {
                val newValue = !featureAState.value
                FeatureFlagRegistry.setFlag(FeatureFlags.FeatureA, newValue)
                featureAState.value = newValue
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        FeatureFlagCard(
            name = "Feature B",
            isEnabled = featureBState.value,
            onToggle = {
                val newValue = !featureBState.value
                FeatureFlagRegistry.setFlag(FeatureFlags.FeatureB, newValue)
                featureBState.value = newValue
            }
        )
    }
}

@Composable
fun FeatureFlagCard(
    name: String,
    isEnabled: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = name, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "Status: ${if (isEnabled) "Enabled" else "Disabled"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
            Switch(
                checked = isEnabled,
                onCheckedChange = { onToggle() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DemoScreenPreview() {
    PiApplicationTheme {
        DemoScreen()
    }
}