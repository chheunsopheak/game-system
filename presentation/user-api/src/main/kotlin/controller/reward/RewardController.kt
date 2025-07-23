package controller.reward

import constant.BaseUrl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import response.reward.RewardDetailResponse
import response.reward.RewardResponse
import response.reward.UserRewardResponse
import service.reward.RewardService
import wrapper.ApiResult
import wrapper.PaginatedResult

@RestController
@RequestMapping(BaseUrl.BASE_URL_MOBILE_V1)
class RewardController(
    private val rewardService: RewardService
) {

    @GetMapping("/rewards")
    suspend fun getAllRewards(): ResponseEntity<ApiResult<List<RewardResponse>>> {
        val response = rewardService.getAllReward()
        return ResponseEntity.status(response.statusCode).body(response)
    }

    @GetMapping("/reward-histories")
    suspend fun getUserReward(
        @RequestParam filterBy: String?,
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(required = false) searchString: String?
    ): ResponseEntity<PaginatedResult<UserRewardResponse>> {
        val response = rewardService.getUserReward(filterBy, pageNumber, pageSize, searchString)
        return ResponseEntity.status(response.statusCode).body(response)
    }

    @GetMapping("/reward-history/{id}")
    suspend fun getRewardById(@PathVariable id: String): ResponseEntity<ApiResult<RewardDetailResponse>> {
        val response = rewardService.getRewardById(id)
        return ResponseEntity.status(response.statusCode).body(response)
    }
}