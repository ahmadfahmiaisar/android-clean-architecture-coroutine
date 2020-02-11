package io.fajarca.news.presentation.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.fajarca.core.vo.UiState
import io.fajarca.news.domain.entities.News

data class SearchResult(val searchState : LiveData<UiState>, val initialLoadingState : LiveData<UiState>, val news : LiveData<PagedList<News>> )