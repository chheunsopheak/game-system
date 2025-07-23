package service.member

import org.springframework.web.multipart.MultipartFile
import request.member.MemberRequest
import response.member.MemberResponse
import wrapper.ApiResult
import wrapper.PaginatedResult

interface MemberService {
    fun getAllMember(): ApiResult<List<MemberResponse>>
    fun addMember(request: MemberRequest): ApiResult<String>
    fun addAllMember(request: List<MemberRequest>): ApiResult<String>
    fun parseMember(file: MultipartFile): ApiResult<List<MemberRequest>>
    fun getAllMemberPage(pageNumber: Int, pageSize: Int, searchString: String?): PaginatedResult<MemberResponse>
}