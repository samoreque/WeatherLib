package com.samoreque.weather.network.scopes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

internal class ViewModelScope(private var viewModel: ViewModel?) : NetworkScope {
    override val scope
        get() = viewModel?.viewModelScope

    override fun dispose() {
        viewModel = null
    }
}