package kh.sample.compose.refreshing_list.ui.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kh.sample.compose.refreshing_list.ui.mock.StoreInstanceMockDataManger
import kh.sample.compose.refreshing_list.ui.model.TextModel
import kh.sample.compose.refreshing_list.ui.model.TextResponse

class SamplePagingSource(
    private val remoteDataSource: TextRemoteDataSource,
) : PagingSource<Int, TextModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TextModel> {
        return try {
            val currentPage = params.key ?: 0
            val textResponse = remoteDataSource.getTextList(currentPage, params.loadSize)
            LoadResult.Page(
                data = textResponse.textList,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = if (textResponse.textList.isEmpty()) null else textResponse.currentPage + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TextModel>): Int? {

        val anchorPosition = state.anchorPosition ?: return null

        val closestPage = state.closestPageToPosition(anchorPosition)

        val keyPage = closestPage?.prevKey?.plus(1)
            ?: closestPage?.nextKey?.minus(1)
            ?: 0
        return keyPage
    }

}

class TextRemoteDataSource {

    val mockList = List<Int>(100) { it }


    fun getTextList(pageNumber: Int, pageSize: Int): TextResponse {
        val fromIndex = pageNumber * pageSize
        val toIndex = minOf(fromIndex + pageSize, mockList.size)

        if (fromIndex >= mockList.size) {
            return TextResponse(textList = emptyList(), currentPage = pageNumber)
        }

        val pageData = mockList.subList(fromIndex, toIndex)
            .map {
                /**
                 * Using for tricky data has been updated status
                 */
                val mockStatus = if (StoreInstanceMockDataManger.isRefresh) {
                    true
                } else {
                    false
                }
                TextModel(
                    index = it,
                    status = mockStatus,
                    currentPage = pageNumber
                )
            }

        return TextResponse(
            textList = pageData,
            currentPage = pageNumber
        )
    }

}