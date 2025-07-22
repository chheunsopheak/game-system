package service.history

import entity.history.GameHistoryEntity
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import repository.game.GameRepository
import repository.history.GameHistoryRepository
import repository.user.UserRepository
import request.history.GameHistoryRequest
import response.history.UserGameHistoryResponse
import wrapper.ApiResult
import wrapper.PaginatedResult
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class GameHistoryServiceImpl(
    private val gameHistoryRepository: GameHistoryRepository,
    private val userRepository: UserRepository,
    private val gameRepository: GameRepository,
) : GameHistoryService {
    override fun getUserGamePlayById(id: String): ApiResult<UserGameHistoryResponse> {
        TODO("Not yet implemented")
    }

    override fun getUserGamePlayByUserId(
        userId: String,
        pageNumber: Int,
        pageSize: Int
    ): PaginatedResult<UserGameHistoryResponse> {
        val pageable = PageRequest.of(pageNumber - 1, pageSize)
        val gameHistoryPage = gameHistoryRepository.findByUser_Id(userId, pageable)
        val data = gameHistoryPage
            .map(UserGameHistoryResponse::from)
            .toList()

        val response = PaginatedResult.success(
            data = data,
            pageNumber = pageNumber,
            pageSize = pageSize,
            totalItems = gameHistoryPage.totalElements.toInt()
        )
        return response
    }

    override fun createUserGamePlay(request: GameHistoryRequest): ApiResult<String> {
        val user = userRepository.findById(request.userId)
        if (user.isEmpty) {
            return ApiResult.failed(HttpStatus.NOT_FOUND, "User not found")
        }

        val gameEntity = request.gameId?.let { gameRepository.findById(it).orElse(null) }

        val history = GameHistoryEntity(
            user = user.get(),
            game = gameEntity,
            device = request.device,
            energy = request.energy,
            playedAt = LocalDateTime.now(),
            gameName = request.gameName ?: (gameEntity?.name ?: "Unknown Game")
        )

        val saved = gameHistoryRepository.save(history)
        return ApiResult.success(saved.id, "Game created successfully")
    }


    override fun countUserGamePlay(gameId: String?, userId: String): ApiResult<String> {
        val user = userRepository.findById(userId)
        if (user.isEmpty) {
            return ApiResult.failed(HttpStatus.NOT_FOUND, "User not found")
        }

        val today = LocalDate.now()

        val history = gameHistoryRepository.findAllByUser_Id(user.get().id)
            .filter {
                it.playedAt.toLocalDate().isEqual(today) && (gameId == null || it.game?.id == gameId)
            }.toList()

        return ApiResult.success(history.size.toString(), "User play count retrieved successfully")
    }

}