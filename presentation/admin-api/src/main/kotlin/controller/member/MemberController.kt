package com.gamesystem.controller.member

import constant.BaseUrl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import request.member.MemberRequest
import response.member.MemberResponse
import service.member.MemberService
import wrapper.ApiResult
import wrapper.PaginatedResult

@RestController
@RequestMapping(BaseUrl.BASE_URL_ADMIN_V1)
class MemberController(private val memberService: MemberService) {

    // Get All Member
    @PostMapping("member/file")
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<ApiResult<List<MemberRequest>>> {
        val result = memberService.parseMember(file)
        if (result.statusCode != 200)
            return ResponseEntity.status(result.statusCode).body(result)

        val request = result.data
        if (request.isNullOrEmpty())
            return ResponseEntity.status(400).body(ApiResult.error(400, "File is empty"))

        memberService.addAllMember(request)
        return ResponseEntity.status(200).body(ApiResult.success(null, "Success"))
    }

    @GetMapping("members")
    fun getAllMember(
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam("searchString") searchString: String?
    ): ResponseEntity<PaginatedResult<MemberResponse>> {
        val response = memberService.getAllMemberPage(pageNumber, pageSize, searchString)
        return ResponseEntity.status(response.statusCode).body(response)
    }
}