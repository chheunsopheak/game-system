package service.member

import entity.member.MemberEntity
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import repository.member.MemberRepository
import request.member.MemberRequest
import response.member.MemberResponse
import wrapper.ApiResult
import wrapper.PaginatedResult
import java.io.InputStreamReader

@Service
class MemberServiceImpl(
    private val memberRepository: MemberRepository
) : MemberService {
    override fun getAllMember(): ApiResult<List<MemberResponse>> {
        val members = memberRepository.findAll()
        val response = members
            .sortedByDescending { it.ticket }
            .map { member ->
                MemberResponse(
                    id = member.id,
                    name = member.name,
                    phone = member.phone,
                    ticket = member.ticket,
                    email = member.email,
                    rewardType = member.rewardType,
                    createdAt = member.createdAt
                )
            }
            .toList()
        return ApiResult.success(data = response, message = "Members retrieved successfully")
    }

    override fun addMember(request: MemberRequest): ApiResult<String> {
        val requestAddMember = MemberEntity(
            name = request.name,
            phone = request.phone,
            ticket = request.ticket,
            rewardType = request.rewardType,
            email = null
        )
        val savedMember = memberRepository.save(requestAddMember)
        return ApiResult.success(savedMember.id, message = "Member added successfully")
    }

    override fun addAllMember(request: List<MemberRequest>): ApiResult<String> {
        val requestAddMember = request.map { memberRequest ->
            MemberEntity(
                name = memberRequest.name,
                phone = memberRequest.phone,
                ticket = memberRequest.ticket,
                rewardType = memberRequest.rewardType,
                email = null
            )
        }
        val savedMember = memberRepository.saveAll(requestAddMember)
        return ApiResult.success(null, message = "Member added successfully")
    }

    override fun parseMember(file: MultipartFile): ApiResult<List<MemberRequest>> {
        return try {
            val members = mutableListOf<MemberRequest>()

            InputStreamReader(file.inputStream).use { reader ->
                val records = CSVParser.parse(
                    reader,
                    CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim()
                )

                for ((index, record) in records.withIndex()) {
                    val name = record.get("Customer Name")?.trim()
                    val phone = record.get("Customer Phone")?.trim()
                    val ticket = record.get("Ticket")?.trim()?.toIntOrNull()
                    val type = record.get("Type of Reward")?.trim()

                    if (name.isNullOrBlank() || phone.isNullOrBlank() || ticket == null || type.isNullOrBlank()) {
                        return ApiResult.failed(
                            HttpStatus.BAD_REQUEST.value(),
                            message = "Invalid data on row ${index + 2} (check name, phone, or ticket)"
                        )
                    }

                    members.add(MemberRequest(name = name, phone = phone, ticket = ticket, rewardType = type))
                }
            }

            ApiResult.success(
                data = members,
                message = "Successfully parsed ${members.size} members"
            )
        } catch (ex: Exception) {
            ApiResult.failed(
                HttpStatus.BAD_REQUEST.value(),
                message = "Failed to parse CSV: ${ex.localizedMessage}"
            )
        }
    }

    override fun getAllMemberPage(
        pageNumber: Int,
        pageSize: Int,
        searchString: String?
    ): PaginatedResult<MemberResponse> {
        val pageable = PageRequest.of(pageNumber - 1, pageSize)
        val members = memberRepository.findAll(pageable)
        val data = members
            .content
            .filter { it ->
                searchString.isNullOrEmpty()
                        || it.name.contains(searchString)
                        || it.phone.contains(searchString)
            }
            .map {
                MemberResponse(
                    id = it.id,
                    name = it.name,
                    phone = it.phone,
                    ticket = it.ticket,
                    email = it.email,
                    rewardType = it.rewardType,
                    createdAt = it.createdAt,
                )
            }
            .toList()

        return PaginatedResult.success(
            data = data,
            pageSize = pageSize,
            pageNumber = pageNumber,
            totalItems = members.totalElements.toInt(),
            message = "Members retrieved successfully"
        )
    }
}