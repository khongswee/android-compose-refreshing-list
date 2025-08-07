package kh.sample.compose.refreshing_list.ui.model

data class TextModel(val index: Int, val status: Boolean, val currentPage: Int)
data class TextResponse(val textList: List<TextModel>, val currentPage: Int)
