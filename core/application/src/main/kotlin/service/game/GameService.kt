package service.game

import request.game.GamePlayRequest
import request.game.GameRequest
import request.game.UserPlayGameRequest
import response.game.GameDetailResponse
import response.game.GamePlayResponse
import response.game.GameResponse
import wrapper.ApiResult
import wrapper.PaginatedResult

interface GameService {
    fun getAll(pageNumber: Int, pageSize: Int, searchString: String?): PaginatedResult<GameResponse>
    fun getAllActiveGames(
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
    ): PaginatedResult<GameResponse>

    fun createGame(request: GameRequest): ApiResult<String>
    fun updateGame(id: String, request: GameRequest): ApiResult<String>
    fun deleteGame(id: String): ApiResult<String>
    fun toggleGame(id: String): ApiResult<String>
    fun getGameById(id: String): ApiResult<GameDetailResponse>
    suspend fun gamePlay(request: GamePlayRequest): ApiResult<GamePlayResponse>
    suspend fun gamePlayPickUp(request: UserPlayGameRequest): ApiResult<GamePlayResponse>
}