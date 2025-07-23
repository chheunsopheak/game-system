package repository.member

import entity.member.MemberEntity
import org.springframework.stereotype.Repository
import repository.base.BaseRepository

@Repository
interface MemberRepository : BaseRepository<MemberEntity, String> {
}