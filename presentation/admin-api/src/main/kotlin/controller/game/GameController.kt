package com.gamesystem.controller.game


import constant.BaseUrl
import org.springframework.web.bind.annotation.*
import request.game.GameRequest
import service.game.GameService


@RestController
@RequestMapping(BaseUrl.BASE_URL_ADMIN_V1)
class GameController(private val gameService: GameService) {

    @GetMapping("/game/{id}")
    fun getGameById(@PathVariable id: String) = gameService.getGameById(id)

    @GetMapping("/games")
    fun getAllGames(
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(required = false) searchString: String?
    ) = gameService.getAll(pageNumber, pageSize, searchString)

    @PostMapping("/game")
    fun createGame(@RequestBody request: GameRequest) = gameService.createGame(request)

    @PutMapping("/game/{id}")
    fun updateGame(@PathVariable id: String, @RequestBody request: GameRequest) =
        gameService.updateGame(id, request)

    @DeleteMapping("/game/{id}")
    fun deleteGame(@PathVariable id: String) = gameService.deleteGame(id)

    @PatchMapping("/game/{id}")
    fun toggleGame(@PathVariable id: String) = gameService.toggleGame(id)
}