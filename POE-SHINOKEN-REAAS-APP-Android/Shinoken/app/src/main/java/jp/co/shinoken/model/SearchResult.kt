package jp.co.shinoken.model

data class SearchResult(
    val tags: List<Tag>
)

data class Tag(
    val name: String,
    val pageId: Int
)