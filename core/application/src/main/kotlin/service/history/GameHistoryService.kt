package service.history

import request.history.GameHistoryRequest
import response.history.UserGameHistoryResponse
import wrapper.ApiResult
import wrapper.PaginatedResult

interface GameHistoryService {
    fun getUserGamePlayById(id: String): ApiResult<UserGameHistoryResponse>
    fun getUserGamePlayByUserId(
        userId: String,
        pageNumber: Int,
        pageSize: Int
    ): PaginatedResult<UserGameHistoryResponse>

    fun createUserGamePlay(request: GameHistoryRequest): ApiResult<String>
    fun countUserGamePlay(gameId: String?, userId: String): ApiResult<String>
}