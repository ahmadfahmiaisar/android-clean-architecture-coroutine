package io.fajarca.news.data

import androidx.paging.DataSource
import io.fajarca.core.database.dao.NewsDao
import io.fajarca.core.database.entity.NewsEntity
import io.fajarca.core.dispatcher.CoroutineDispatcherProvider
import io.fajarca.core.vo.Result
import io.fajarca.news.data.mapper.NewsMapper
import io.fajarca.news.data.response.NewsDto
import io.fajarca.news.data.source.NewsRemoteDataSource
import io.fajarca.news.domain.entities.News
import io.fajarca.news.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcherProvider,
    private val mapper: NewsMapper,
    private val dao: NewsDao,
    private val remoteDataSource: NewsRemoteDataSource
) : NewsRepository {

    override suspend fun getNewsFromApi(country : String?, category : String?, page : Int, pageSize : Int, onSuccessAction: () -> Unit): Result<NewsDto> {
        return remoteDataSource.getNews(dispatcher.io, country, category, page, pageSize)
    }


    override suspend fun insertNews(news: List<NewsEntity>) {
        dao.insertAll(news)
    }


    override suspend fun findAllNews(country: String?, category : String?, page: Int, pageSize: Int, onSuccessAction: () -> Unit) {
        val apiResult = getNewsFromApi(country, category, page, pageSize, onSuccessAction)
        if (apiResult is Result.Success) {
            onSuccessAction()
            val news =  mapper.map(country, category, apiResult.data)
            insertNews(news)
        }
    }

    override fun getNewsFactory(country: String?, category: String?): DataSource.Factory<Int, News> {
        if (category.isNullOrEmpty()) {
            return dao.findByCountry(country ?: "").map { mapper.mapToDomain(it) }
        }

        return dao.findByCategory(category).map { mapper.mapToDomain(it) }
    }
}