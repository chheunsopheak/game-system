package com.gamesystem.controller.member

import constant.BaseUrl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import response.member.MemberResponse
import service.member.MemberService
import wrapper.ApiResult

@RestController
@RequestMapping(BaseUrl.BASE_URL_MOBILE_V1)
class MemberController(private val memberService: MemberService) {
    @GetMapping("members")
    fun getAllMember(
    ): ResponseEntity<ApiResult<List<MemberResponse>>> {
        val response = memberService.getAllMember()
        return ResponseEntity.status(response.statusCode).body(response)
    }
}