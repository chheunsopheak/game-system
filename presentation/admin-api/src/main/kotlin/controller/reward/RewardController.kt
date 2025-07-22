package com.gamesystem.controller.reward

import constant.BaseUrl
import org.springframework.web.bind.annotation.*
import service.reward.RewardService

@RestController
@RequestMapping(BaseUrl.BASE_URL_ADMIN_V1)
class RewardController(private val rewardService: RewardService) {

    @GetMapping("/user/{userId}/rewards")
    suspend fun getUserReward(
        @PathVariable userId: String,
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(required = false) searchString: String?
    ) = rewardService.getAllRewardByUser(userId, pageNumber, pageSize, searchString)

    @GetMapping("/reward-histories")
    suspend fun getAllRewards(
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(required = false) searchString: String?
    ) = rewardService.getAllRewards(pageNumber, pageSize, searchString)

    @GetMapping("/reward-histories/{id}")
    suspend fun getRewardById(@PathVariable id: String) = rewardService.getRewardById(id)

}