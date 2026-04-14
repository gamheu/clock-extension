package com.supinfo.app.viewmodel

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.supinfo.app.data.repository.SupRepository
import com.supinfo.app.domain.SupConditions
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Top-level DataStore extension — must be at file scope
val android.content.Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = "sup_settings")

private object PreferencesKeys {
    val CITY = stringPreferencesKey("city")
}

// Default to Biarritz — one of the best SUP spots in Europe
private const val DEFAULT_CITY = "Biarritz"

sealed class SupUiState {
    data object Loading : SupUiState()
    data class Success(val conditions: SupConditions) : SupUiState()
    data class Error(val message: String) : SupUiState()
}

class SupViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SupRepository()
    private val dataStore = application.dataStore

    /** City name persisted in DataStore */
    val city: StateFlow<String> = dataStore.data
        .map { prefs -> prefs[PreferencesKeys.CITY] ?: DEFAULT_CITY }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DEFAULT_CITY)

    private val _uiState = MutableStateFlow<SupUiState>(SupUiState.Loading)
    val uiState: StateFlow<SupUiState> = _uiState.asStateFlow()

    init {
        // Reload whenever the city changes
        viewModelScope.launch {
            city.collect { cityName ->
                fetchConditions(cityName)
            }
        }
    }

    /** Manually trigger a refresh (e.g. pull-to-refresh or refresh button) */
    fun refresh() {
        fetchConditions(city.value)
    }

    /** Persist a new city name; the collector above will trigger a fetch */
    fun saveCity(newCity: String) {
        if (newCity.isBlank()) return
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[PreferencesKeys.CITY] = newCity.trim()
            }
        }
    }

    private fun fetchConditions(cityName: String) {
        viewModelScope.launch {
            _uiState.value = SupUiState.Loading
            repository.getSupConditions(cityName)
                .onSuccess { conditions ->
                    _uiState.value = SupUiState.Success(conditions)
                }
                .onFailure { error ->
                    _uiState.value = SupUiState.Error(
                        error.message ?: "Unable to fetch data. Check your connection."
                    )
                }
        }
    }
}
