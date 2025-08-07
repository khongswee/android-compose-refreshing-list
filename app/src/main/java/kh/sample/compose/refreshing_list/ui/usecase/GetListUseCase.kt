package kh.sample.compose.refreshing_list.ui.usecase

import androidx.paging.PagingData
import kh.sample.compose.refreshing_list.ui.model.TextModel
import kh.sample.compose.refreshing_list.ui.paging.SampleRepository
import kotlinx.coroutines.flow.Flow

class GetSampleUseCase(
    private val repository: SampleRepository
) : CommonUseCase<Unit, Flow<PagingData<TextModel>>>() {
    override fun execute(parameters: Unit): Flow<PagingData<TextModel>> {
        return repository.getTextList()
    }
}

abstract class CommonUseCase<in P, R>(
    val tagName: String = CommonUseCase::class.java.simpleName
) {

    operator fun invoke(parameters: P): R {
        return execute(parameters)
    }

    @Throws(RuntimeException::class)
    protected abstract fun execute(parameters: P): R
}