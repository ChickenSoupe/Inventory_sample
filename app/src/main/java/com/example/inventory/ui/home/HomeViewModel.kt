package com.example.inventory.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve all items in the Room database.
 */
class HomeViewModel(itemsRepository: ItemsRepository) : ViewModel() {

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow = _messageFlow.asSharedFlow()

    private var lastItemCount = 0
    /**
     * Holds home ui state. The list of items are retrieved from [ItemsRepository] and mapped to
     * [HomeUiState]
     */
    val homeUiState: StateFlow<HomeUiState> =
        itemsRepository.getAllItemsStream().map { items ->
            if (items.size > lastItemCount) {
                viewModelScope.launch {
                    _messageFlow.emit("Item added successfully!")
                }
            }
            lastItemCount = items.size
            HomeUiState(items)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(val itemList: List<Item> = listOf())
