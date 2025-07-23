package com.gamesystem.controller.game

import constant.BaseUrl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import request.game.GamePlayRequest
import request.game.UserPlayGameRequest
import response.game.GameDetailResponse
import response.game.GamePlayResponse
import response.game.GameResponse
import service.game.GameService
import wrapper.ApiResult
import wrapper.PaginatedResult

@RestController
@RequestMapping(BaseUrl.BASE_URL_MOBILE_V1)
class GameController(private val gameService: GameService) {

    @GetMapping("/game-hub/{id}")
    fun getGameById(@PathVariable id: String): ResponseEntity<ApiResult<GameDetailResponse>> {
        val response = gameService.getGameById(id)
        return ResponseEntity.status(response.statusCode).body(response)
    }

    @GetMapping("/game-hubs")
    fun getAllGameHubs(
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(required = false) searchString: String?
    ): ResponseEntity<PaginatedResult<GameResponse>> {
        val response = gameService.getAllActiveGames(pageNumber, pageSize, searchString)
        return ResponseEntity.status(response.statusCode).body(response)
    }

    @PostMapping("/reward/game-play")
    suspend fun playGameReward(@RequestBody request: GamePlayRequest): ResponseEntity<ApiResult<GamePlayResponse>> {
        val response = gameService.gamePlay(request)
        return ResponseEntity.status(response.statusCode).body(response)
    }

    @PostMapping("/reward/pickup")
    suspend fun pickUpReward(@RequestBody request: UserPlayGameRequest): ResponseEntity<ApiResult<GamePlayResponse>> {
        val response = gameService.gamePlayPickUp(request)
        return ResponseEntity.status(response.statusCode).body(response)
    }
}