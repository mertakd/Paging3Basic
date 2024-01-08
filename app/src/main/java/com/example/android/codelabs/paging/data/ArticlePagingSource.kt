package com.example.android.codelabs.paging.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import kotlin.math.max


private const val STARTING_KEY = 0
private const val LOAD_DELAY_MILLIS = 3_000L

private val firstArticleCreatedTime = LocalDateTime.now()




class ArticlePagingSource : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        // Bu ilk yüklemeyse sayfalamayı STARTING_KEY ile başlatın
        val startKey = params.key ?: STARTING_KEY
        // params.loadSize tarafından belirtilen sayıda öğe yükleyin
        val range = startKey.until(startKey + params.loadSize)


        if (startKey != STARTING_KEY) delay(LOAD_DELAY_MILLIS)


        //Sonuç başarılıysa LoadResult.Page
        return LoadResult.Page(
            data = range.map { number ->
                Article(
                    // Makale kimliği olarak ardışık artan sayılar oluşturun
                    id = number,
                    title = "Article $number",
                    description = "This describes article $number",
                    created = firstArticleCreatedTime.minusDays(number.toLong())
                )
            },

            // Öğeleri STARTING_KEY'in arkasına yüklemeye çalışmadığımızdan emin olun
            prevKey = when (startKey) {
                STARTING_KEY -> null
                else -> when (val prevKey = ensureValidKey(key = range.first - params.loadSize)) {
                    // We're at the start, there's nothing more to load
                    STARTING_KEY -> null
                    else -> prevKey
                }
            },
            nextKey = range.last + 1
        )
    }






    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        val anchorPosition = state.anchorPosition ?: return null //state.anchorPosition ile UI'nin anlık konumunu alır.
        val article = state.closestItemToPosition(anchorPosition) ?: return null // state.closestItemToPosition(anchorPosition) ile anchorPosition'a en yakın öğeyi alır
        return ensureValidKey(key = article.id - (state.config.pageSize / 2)) // ensureValidKey fonksiyonu, geçerli bir anahtarı sağlamak için kullanılır
    }

    /*
    * Şu anki UI'nin belirli bir konumundaki makaleye yakın olan makalenin id'sini alır ve bu id'yi kullanarak yeni bir PagingSource'un başlaması gereken anahtar değerini belirler. Bu, kullanıcının sayfalama işleminden sonra listenin başka bir sayfaya geçmesini önlemeye yardımcı olur ve daha tutarlı bir kullanıcı deneyimi sağlar.
    * */


    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)
}