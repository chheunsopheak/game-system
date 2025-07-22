package com.gamesystem.controller.game

import constant.BaseUrl
import org.springframework.web.bind.annotation.*
import request.game.GamePlayRequest
import request.game.UserPlayGameRequest
import service.game.GameService

@RestController
@RequestMapping(BaseUrl.BASE_URL_MOBILE_V1)
class GameController(private val gameService: GameService) {

    @GetMapping("/game-hub/{id}")
    suspend fun getGameById(@PathVariable id: String) = gameService.getGameById(id)

    @GetMapping("/game-hubs")
    suspend fun getAllGameHubs(
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(required = false) searchString: String?
    ) = gameService.getAllActiveGames(pageNumber, pageSize, searchString)

    @PostMapping("/reward/game-play")
    suspend fun playGameReward(@RequestBody request: GamePlayRequest) = gameService.gamePlay(request)

    @PostMapping("/reward/pickup")
    suspend fun pickUpReward(@RequestBody request: UserPlayGameRequest) = gameService.gamePlayPickUp(request)
}