package com.supinfo.app.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.supinfo.app.viewmodel.SupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, viewModel: SupViewModel = viewModel()) {
    val currentCity by viewModel.city.collectAsState()
    var cityInput by remember(currentCity) { mutableStateOf(currentCity) }
    val focusManager = LocalFocusManager.current

    fun save() {
        if (cityInput.isNotBlank()) {
            viewModel.saveCity(cityInput)
            focusManager.clearFocus()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Configure your departure city",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Enter any city name. The app will retrieve local weather, " +
                        "wind and (for coastal cities) wave conditions for Stand Up Paddle.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            OutlinedTextField(
                value = cityInput,
                onValueChange = { cityInput = it },
                label = { Text("City name") },
                leadingIcon = {
                    Icon(Icons.Default.LocationCity, contentDescription = null)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { save() })
            )

            Button(
                onClick = ::save,
                enabled = cityInput.isNotBlank(),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save & Refresh")
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Popular SUP spots",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            listOf("Biarritz", "Lacanau", "Hendaye", "Hossegor", "Noirmoutier").forEach { city ->
                TextButton(
                    onClick = {
                        cityInput = city
                        viewModel.saveCity(city)
                        navController.popBackStack()
                    }
                ) {
                    Text("🌊  $city")
                }
            }
        }
    }
}
