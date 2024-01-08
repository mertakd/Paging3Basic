/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.codelabs.paging.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.android.codelabs.paging.data.Article
import com.example.android.codelabs.paging.data.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel for the [ArticleActivity] screen.
 * The ViewModel works with the [ArticleRepository] to get the data.
 */

private const val ITEMS_PER_PAGE = 50


class ArticleViewModel(
    repository: ArticleRepository,
) : ViewModel() {

    /**
     * Stream of [Article]s for the UI.
     */
    val items: Flow<PagingData<Article>> = Pager(
        config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = false),
        pagingSourceFactory = { repository.articlePagingSource() }
    )
        .flow
        .cachedIn(viewModelScope)
}


/*
*ViewModel'de PagingData'yı isteyin ve önbelleğe alın
*PagingData, yüklediğimiz verileri saran ve Paging kitaplığının ne zaman daha fazla veri getireceğine karar vermesine yardımcı olan ve ayrıca aynı sayfayı iki kez istemediğimizden emin olan bir türdür.
* PagingConfig. Bu sınıf, bir PagingSource'tan içeriğin nasıl yükleneceği ile ilgili, örneğin ne kadar ileride yükleneceği, ilk yükleme için boyut isteği ve diğerleri gibi seçenekleri ayarlar.
* PagingSource'un nasıl oluşturulacağını tanımlayan bir işlev. Bizim durumumuzda, bir ArticlePagingSource oluşturacağız, dolayısıyla Paging kütüphanesine bunun nasıl yapılacağını söyleyen bir fonksiyona ihtiyacımız var.
 */