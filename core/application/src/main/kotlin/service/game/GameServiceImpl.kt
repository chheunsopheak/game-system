package service.game

import entity.game.GameEntity
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import repository.game.GameRepository
import repository.user.UserRepository
import request.game.GamePlayRequest
import request.game.GameRequest
import request.game.UserPlayGameRequest
import request.reward.ClaimRewardRequest
import request.reward.UserRewardRequest
import response.game.GameDetailResponse
import response.game.GamePlayResponse
import response.game.GameResponse
import service.history.GameHistoryService
import service.reward.RewardService
import specification.game.GameFilterSpecification
import wrapper.ApiResult
import wrapper.PaginatedResult

@Service
class GameServiceImpl(
    private val userRepository: UserRepository,
    private val gameRepository: GameRepository,
    private val rewardService: RewardService,
    private val gameHistoryService: GameHistoryService
) : GameService {
    override fun getAll(
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
    ): PaginatedResult<GameResponse> {
        val pageable = PageRequest.of(pageNumber - 1, pageSize)
        val spec = GameFilterSpecification.gameFilterSpecification(searchString)
        val gamesPage = gameRepository.findAll(spec, pageable)
        val data = gamesPage.map(GameResponse::from)
        val response = PaginatedResult.success(
            data = data.toList(),
            pageNumber = pageNumber,
            pageSize = pageSize,
            totalItems = gamesPage.totalElements.toInt()
        )
        return response
    }

    override fun getAllActiveGames(
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
    ): PaginatedResult<GameResponse> {
        val pageable = PageRequest.of(pageNumber - 1, pageSize)
        val spec = GameFilterSpecification.gameFilterSpecification(searchString)
        val gamesPage = gameRepository.findAll(spec, pageable)
        val data = gamesPage
            .filter { it.isActive }
            .map(GameResponse::from)
        val response = PaginatedResult.success(
            data = data.toList(),
            pageNumber = pageNumber,
            pageSize = pageSize,
            totalItems = gamesPage.totalElements.toInt()
        )
        return response
    }

    override fun createGame(request: GameRequest): ApiResult<String> {
        val existGame = gameRepository.existsByName(request.name)
        if (existGame) {
            return ApiResult.failed(HttpStatus.BAD_REQUEST.value(), "Game name already exists")
        }
        val requestGame = GameEntity(
            name = request.name,
            gameIcon = request.gameIcon,
            gameUrl = request.gameUrl,
            energy = request.energy,
            threshold = request.threshold,
            description = request.description,
            category = request.category,
        )
        gameRepository.save(requestGame)
        return ApiResult.success(requestGame.id, "Game created successfully")
    }

    override fun updateGame(
        id: String,
        request: GameRequest
    ): ApiResult<String> {
        val game = gameRepository.findById(id)
        if (game == null) {
            return ApiResult.failed(HttpStatus.NOT_FOUND.value(), "Game not found")
        }
        val requestUpdate = game.get()
        requestUpdate.name = request.name
        requestUpdate.gameIcon = request.gameIcon
        requestUpdate.gameUrl = request.gameUrl
        requestUpdate.energy = request.energy
        requestUpdate.threshold = request.threshold
        requestUpdate.description = request.description
        requestUpdate.category = request.category
        gameRepository.save(requestUpdate)
        return ApiResult.success(requestUpdate.id, "Game updated successfully")
    }

    override fun deleteGame(id: String): ApiResult<String> {
        val game = gameRepository.findById(id)
        if (game == null) {
            return ApiResult.failed(HttpStatus.NOT_FOUND.value(), "Game not found")
        }
        gameRepository.deleteById(id)
        return ApiResult.success(id, "Game deleted successfully")
    }

    override fun toggleGame(id: String): ApiResult<String> {
        val game = gameRepository.findById(id)
        if (game == null) {
            return ApiResult.failed(HttpStatus.NOT_FOUND.value(), "Game not found")
        }
        val requestUpdate = game.get()
        requestUpdate.isActive = !requestUpdate.isActive
        gameRepository.save(requestUpdate)
        return ApiResult.success(requestUpdate.id, "Game updated successfully")
    }

    override fun getGameById(id: String): ApiResult<GameDetailResponse> {
        val game = gameRepository.findById(id)
        if (game == null) {
            return ApiResult.failed(HttpStatus.NOT_FOUND.value(), "Game not found")
        }
        val data = game.get()
        val response = GameDetailResponse.from(data)
        return ApiResult.success(
            data = response,
            message = "Get game successful"
        )
    }

    override suspend fun gamePlay(request: GamePlayRequest): ApiResult<GamePlayResponse> {
        val user = userRepository.findByPhone(request.phone)
            ?: return ApiResult.failed(
                HttpStatus.NOT_FOUND.value(),
                "Oops! That phone number doesn’t seem to be registered."
            )
//        userService.userDeductEnergy(user.id, request.energy)

        val userPlay = gameHistoryService.countUserGamePlay(null, user.id)
        val countData = userPlay.data?.toInt() ?: 0

        val refList = listOf("BL001", "BL002", "BL003", "BL004", "BL005", "BL006", "BL007", "BL008")

        val rewardsResult = rewardService.getAllReward()
        if (rewardsResult.statusCode != HttpStatus.OK.value() || rewardsResult.data.isNullOrEmpty()) {
            return ApiResult.failed(
                HttpStatus.BAD_REQUEST.value(),
                "No rewards available."
            )
        }

        val rewards = rewardsResult.data?.filter { it.ref in refList }
        val selectedReward = rewardService.rewardPicker(10, countData, rewards!!)
        if (selectedReward.statusCode != HttpStatus.OK.value()) {
            return ApiResult.error(
                statusCode = selectedReward.statusCode,
                message = selectedReward.message
            )
        }
        val rewardItem = selectedReward.data
        if (rewardItem == null) {
            return ApiResult.failed(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Selected reward has no items."
            )
        }
        val selectedItem = rewardItem.item.randomOrNull()
            ?: return ApiResult.failed(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Selected reward has no items."
            )

        //  Create and Save User Win Reward
        val rewardSave = UserRewardRequest(
            rewardName = rewardItem.name,
            phone = request.phone,
            photo = rewardItem.photo,
            rewardUrl = selectedItem.data,
            gameId = null,
            deviceId = request.deviceName,
            description = rewardItem.description,
            ref = rewardItem.ref,
            merchantName = rewardItem.merchant.name,
            energy = request.energy,
            gameName = request.gameName ?: "Unknown Game"

        )
        val createReward = rewardService.saveReward(rewardSave)
        if (createReward.statusCode != HttpStatus.OK.value()) {
            return ApiResult.error(
                statusCode = createReward.statusCode,
                message = createReward.message
            )
        }

        // Push Back User Claim Reward
        val claimReward = rewardService.claimReward(
            ClaimRewardRequest(
                phone = request.phone,
                data = selectedItem.data
            )
        )
        if (claimReward.statusCode != HttpStatus.OK.value()) {
            return ApiResult.error(
                statusCode = claimReward.statusCode,
                message = claimReward.message
            )
        }

        val response = GamePlayResponse(
            rewardUrl = selectedItem.data,
            rewardStatus = selectedItem.status,
            photo = selectedReward.data?.photo!!,
            rewardName = selectedReward.data?.name!!,
            ref = selectedReward.data?.ref!!
        )
        return ApiResult.success(response, "Get reward successful")
    }

    override suspend fun gamePlayPickUp(request: UserPlayGameRequest): ApiResult<GamePlayResponse> {
        val user = userRepository.findByPhone(request.phone)
            ?: return ApiResult.failed(
                HttpStatus.NOT_FOUND.value(),
                "Oops! That phone number doesn’t seem to be registered."
            )
        val game = gameRepository.findById(request.gameId!!)
            ?: return ApiResult.failed(
                HttpStatus.NOT_FOUND.value(),
                "Oops! We couldn’t find the game. Please check the game ID and try again."
            )

//        val checkUser = userService.verifyAndDeductEnergy(
//            UserPickupRewardRequest(
//                phone = request.phone,
//                gameId = request.gameId,
//                energy = request.energy,
//                deviceName = request.deviceName,
//            )
//        )
//        if (checkUser.statusCode != 200) {
//            return ApiResult.error(
//                statusCode = checkUser.statusCode,
//                message = checkUser.message
//            )
//        }

        val playCount = gameHistoryService.countUserGamePlay(request.gameId, user.id)
        val playCountData = playCount.data?.toInt() ?: 0

        val rewardsResult = rewardService.getAllReward()
        if (rewardsResult.statusCode != HttpStatus.OK.value() || rewardsResult.data.isNullOrEmpty()) {
            return ApiResult.failed(
                HttpStatus.BAD_REQUEST.value(),
                "Failed to get rewards: ${rewardsResult.message}"
            )
        }
        val rewards = rewardsResult.data
        val selectedReward = rewardService.rewardPicker(game.get().threshold, playCountData, rewards!!)
        val rewardItem = selectedReward.data
        if (rewardItem == null) {
            return ApiResult.failed(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Selected reward has no items."
            )
        }
        val selectedItem = rewardItem.item.randomOrNull()
            ?: return ApiResult.failed(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Selected reward has no items."
            )
        //  Create and Save User Win Reward
        val rewardSave = UserRewardRequest(
            rewardName = rewardItem.name,
            phone = request.phone,
            photo = rewardItem.photo,
            rewardUrl = selectedItem.data,
            gameId = null,
            deviceId = request.deviceName,
            description = rewardItem.description,
            ref = rewardItem.ref,
            merchantName = rewardItem.merchant.name,
            energy = request.energy,
            gameName = request.gameName ?: "Unknown Game"

        )
        val createReward = rewardService.saveReward(rewardSave)
        if (createReward.statusCode != HttpStatus.OK.value()) {
            return ApiResult.error(
                statusCode = createReward.statusCode,
                message = createReward.message
            )
        }

        // Push Back User Claim Reward
        val claimReward = rewardService.claimReward(
            ClaimRewardRequest(
                phone = request.phone,
                data = selectedItem.data
            )
        )
        if (claimReward.statusCode != HttpStatus.OK.value()) {
            return ApiResult.error(
                statusCode = claimReward.statusCode,
                message = claimReward.message
            )
        }

        val response = GamePlayResponse(
            rewardUrl = selectedItem.data,
            rewardStatus = selectedItem.status,
            photo = rewardItem.photo,
            rewardName = rewardItem.name,
            ref = rewardItem.ref
        )
        return ApiResult.success(response, "Get reward successful")
    }
}