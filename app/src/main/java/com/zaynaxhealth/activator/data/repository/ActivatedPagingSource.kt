package com.zaynaxhealth.activator.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zaynaxhealth.activator.data.api.ApiException
import com.zaynaxhealth.activator.data.api.ApiResponse
import com.zaynaxhealth.activator.data.model.responsemodels.HistoryResponseModel
import com.zaynaxhealth.activator.utilities.apiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


class ActivatedPagingSource(
    private val token: String,
    private val type: String,
    private val fromDate: String,
    private val toDate: String
) : PagingSource<Int, HistoryResponseModel>() {
    override fun getRefreshKey(state: PagingState<Int, HistoryResponseModel>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HistoryResponseModel> {
        val nextPage = params.key ?: 1
        return try {
            val response = withContext(Dispatchers.IO) {
                ApiResponse.getResult {
                    apiCall?.getActivationHistory(
                        type,
                        nextPage,
                        fromDate,
                        toDate
                    )!!
                }
            }
            LoadResult.Page(
                data = response.data.data,
                prevKey = if (nextPage > 1) nextPage - 1 else null,
                nextKey = if (nextPage < response.data.totalPages) nextPage + 1 else null
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: ApiException) {
            LoadResult.Error(e)
        }
    }
}