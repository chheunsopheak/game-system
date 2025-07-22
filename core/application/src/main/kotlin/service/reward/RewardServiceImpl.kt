package service.reward

import entity.reward.UserRewardEntity
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import repository.game.GameRepository
import repository.reward.RewardRepository
import repository.user.UserRepository
import request.history.GameHistoryRequest
import request.reward.ClaimRewardRequest
import request.reward.UserRewardRequest
import response.reward.*
import service.auth.AuthClientService
import service.history.GameHistoryService
import specification.reward.RewardFilterSpecification
import wrapper.ApiResult
import wrapper.PaginatedResult
import java.time.LocalDateTime

@Service
class RewardServiceImpl(
    private val rewardRepository: RewardRepository,
    private val userRepository: UserRepository,
    private val gameRepository: GameRepository,
    private val gameHistoryService: GameHistoryService,
    private val rewardClient: RewardClientService,
    private val authApiClient: AuthClientService
) : RewardService {
    override suspend fun getAllReward(): ApiResult<List<RewardResponse>> {
        val token = authApiClient.getToken()
        if (token.statusCode != HttpStatus.OK) {
            return ApiResult.error(
                statusCode = token.statusCode,
                message = "Failed to get token: ${token.message}"
            )
        }
        val data = rewardClient.getAllReward(token.data.toString())
        if (data.response.status != HttpStatus.OK) {
            return ApiResult.error(
                data.response.status,
                "Failed to ${data.response.message}"
            )
        }

        val result = data.results
            .filter { it ->
                it.item.any { item -> item.status == "PUBLISHED" }
            }.map {
                RewardResponse(
                    name = it.name,
                    photo = it.photo,
                    description = it.description,
                    ref = it.ref,
                    merchant = MerchantRewardResponse(
                        name = it.merchant.name, photo = it.merchant.photo
                    ),
                    startDate = it.startDate,
                    endDate = it.endDate,
                    weight = it.item.filter { item -> item.status == "PUBLISHED" }.size,
                    item = it.item.filter { item -> item.status == "PUBLISHED" }
                        .map { item ->
                            RewardItemResponse(
                                data = item.data,
                                status = item.status
                            )
                        }
                )
            }

        return ApiResult.success(data = result, message = "Get all reward successful")
    }

    override suspend fun getUserReward(
        filterBy: String?,
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
    ): PaginatedResult<UserRewardResponse> {
        val pageable = PageRequest.of(pageNumber - 1, pageSize)
        val spec = RewardFilterSpecification.rewardFilterSpecification(searchString)
        val rewardsPage = rewardRepository.findAll(spec, pageable)
        val data = rewardsPage.map(UserRewardResponse::from)
        val response = PaginatedResult.success(
            data = data.toList(),
            pageNumber = pageNumber,
            pageSize = pageSize,
            totalItems = rewardsPage.totalElements.toInt()
        )
        return response
    }

    override suspend fun getRewardById(id: String): ApiResult<RewardDetailResponse> {
        val reward = rewardRepository.findById(id)
        if (reward == null) {
            return ApiResult.error(HttpStatus.NOT_FOUND, "Reward not found")
        }
        val data = reward.get()
        val response = RewardDetailResponse.from(data)
        return ApiResult.success(
            data = response,
            message = "Get reward successful"
        )
    }

    override suspend fun getAllRewardByUser(
        userId: String,
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
    ): PaginatedResult<RewardByUserResponse> {
        val pageable = PageRequest.of(pageNumber - 1, pageSize)
        val spec = RewardFilterSpecification.rewardFilterSpecification(searchString)
        val rewardsPage = rewardRepository.findAll(spec, pageable)
        val data = rewardsPage.map(RewardByUserResponse::from)
        val response = PaginatedResult.success(
            data = data.toList(),
            pageNumber = pageNumber,
            pageSize = pageSize,
            totalItems = rewardsPage.totalElements.toInt()
        )
        return response
    }

    override suspend fun getAllRewards(
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
    ): PaginatedResult<RewardHistoryResponse> {
        val pageable = PageRequest.of(pageNumber - 1, pageSize)
        val spec = RewardFilterSpecification.rewardFilterSpecification(searchString)
        val rewardsPage = rewardRepository.findAll(spec, pageable)
        val data = rewardsPage.map(RewardHistoryResponse::from)
        val response = PaginatedResult.success(
            data = data.toList(),
            pageNumber = pageNumber,
            pageSize = pageSize,
            totalItems = rewardsPage.totalElements.toInt()
        )
        return response
    }

    override suspend fun saveReward(request: UserRewardRequest): ApiResult<String> {
        val user = userRepository.findByPhone(request.phone)
            ?: return ApiResult.failed(
                HttpStatus.NOT_FOUND,
                "Oops! That phone number doesn’t seem to be registered."
            )
        val game = request.gameId?.let { gameRepository.findById(it).orElse(null) }

        val userReward = UserRewardEntity(
            rewardName = request.rewardName,
            phone = request.phone,
            photo = request.photo,
            rewardUrl = request.rewardUrl,
            game = game,
            user = user,
            deviceId = request.deviceId,
            description = request.description,
            ref = request.ref,
            merchantName = request.merchantName,
            claimedAt = LocalDateTime.now(),
            isClaimed = true,
            gameName = request.gameName ?: "Unknown Game"
        )

        val userHistory = GameHistoryRequest(
            gameId = request.gameId,
            userId = user.id,
            device = request.deviceId,
            energy = request.energy,
            gameName = request.gameName ?: "Unknown Game"
        )

        gameHistoryService.createUserGamePlay(userHistory)
        rewardRepository.save(userReward)

        return ApiResult.success(userReward.id, "Reward created successfully")
    }


    override suspend fun claimReward(request: ClaimRewardRequest): ApiResult<String> {
        val user = userRepository.findByPhone(request.phone)
        if (user == null) {
            return ApiResult.failed(
                HttpStatus.NOT_FOUND,
                "Oops! That phone number doesn’t seem to be registered."
            )
        }

        val token = authApiClient.getToken()
        if (token.statusCode != HttpStatus.OK) {
            return ApiResult.error(
                statusCode = token.statusCode,
                message = "Failed to get token: ${token.message}"
            )
        }
        val requestData = ClaimRewardRequest(
            phone = request.phone,
            data = request.data
        )
        val claimRequest = rewardClient.claimReward(token.data.toString(), requestData)
        if (claimRequest.response.status != HttpStatus.OK) {
            return ApiResult.error(
                statusCode = claimRequest.response.status,
                message = "Failed to claim reward: ${claimRequest.response.message}"
            )
        }
        return ApiResult.success(
            data = null,
            message = "Reward claimed successfully"
        )
    }

    override suspend fun rewardPicker(
        threshold: Int,
        playCount: Int,
        rewards: List<RewardResponse>
    ): ApiResult<RewardResponse> {
        if (playCount + 1 >= threshold) {
            val progress = (playCount - threshold).coerceAtLeast(0)
            val maxProgress = 10
            val startPercent = 0.5
            val endPercent = 0.05
            val cutoffPercent = if (progress >= maxProgress) {
                endPercent
            } else {
                startPercent - (progress.toDouble() / maxProgress) * (startPercent - endPercent)
            }

            val sortedWeights = rewards.map { it.weight }.sorted()
            val cutoffIndex = (sortedWeights.size * cutoffPercent)
                .toInt()
                .coerceAtLeast(1)
                .coerceAtMost(sortedWeights.size - 1)

            val cutoffWeight = sortedWeights.getOrNull(cutoffIndex)
                ?: return ApiResult.notFound("No reward found with calculated cutoff.")

            val candidates = rewards.filter { it.weight <= cutoffWeight }
            val selected = candidates.randomOrNull()
                ?: return ApiResult.notFound("No eligible reward in cutoff candidates.")

            return ApiResult.success(selected, "Reward picked based on progress.")
        }

        // Else, pick weighted random reward normally
        val totalWeight = rewards.sumOf { it.weight }
        if (totalWeight == 0) {
            return ApiResult.notFound("No rewards available with positive weight.")
        }

        val rand = (1..totalWeight).random()
        var cumulative = 0
        for (reward in rewards) {
            cumulative += reward.weight
            if (rand <= cumulative) {
                return ApiResult.success(reward, "Reward picked based on weight.")
            }
        }

        return ApiResult.notFound("Failed to pick a reward.")
    }

}