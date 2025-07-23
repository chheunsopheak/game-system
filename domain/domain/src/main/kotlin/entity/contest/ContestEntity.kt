package entity.contest

import contract.BaseEntity
import entity.member.MemberEntity
import jakarta.persistence.*

@Entity
@Table(name = "contest")
class ContestEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: MemberEntity,
    @Column(name = "description", nullable = false)
    val description: String
) : BaseEntity() {
    constructor() : this(
        member = MemberEntity(),
        description = ""
    )
}