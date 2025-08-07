package kh.sample.compose.refreshing_list.ui.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kh.sample.compose.refreshing_list.ui.model.TextModel
import kotlinx.coroutines.flow.Flow

class SampleRepositoryImpl(
    private val remoteDataSource: TextRemoteDataSource
) : SampleRepository {

    override fun getTextList(
    ): Flow<PagingData<TextModel>> {

        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 10),
            pagingSourceFactory = {
                SamplePagingSource(remoteDataSource)
            }
        ).flow
    }
}

interface SampleRepository {
    fun getTextList(): Flow<PagingData<TextModel>>
}