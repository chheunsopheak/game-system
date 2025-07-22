package controller.reward

import constant.BaseUrl
import org.springframework.web.bind.annotation.*
import service.reward.RewardService

@RestController
@RequestMapping(BaseUrl.BASE_URL_MOBILE_V1)
class RewardController(
    private val rewardService: RewardService
) {

    @GetMapping("/rewards")
    suspend fun getAllRewards() = rewardService.getAllReward()

    @GetMapping("/reward-histories")
    suspend fun getUserReward(
        @RequestParam filterBy: String?,
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(required = false) searchString: String?
    ) = rewardService.getUserReward(filterBy, pageNumber, pageSize, searchString)

    @GetMapping("/reward-history/{id}")
    suspend fun getRewardById(@PathVariable id: String) = rewardService.getRewardById(id)
}