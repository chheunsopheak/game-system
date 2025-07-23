package entity.member

import contract.BaseEntity
import entity.contest.ContestEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "member")
class MemberEntity(
    @Column(name = "name")
    val name: String,
    @Column(name = "email")
    val email: String?,
    @Column(name = "phone")
    val phone: String,
    @Column(name = "ticket")
    val ticket: Int,
    @Column(name = "reward_type")
    val rewardType: String,
    @OneToMany(mappedBy = "member")
    var contests: MutableList<ContestEntity> = mutableListOf()
) : BaseEntity() {
    constructor() : this(
        name = "",
        email = null,
        phone = "",
        ticket = 0,
        rewardType = "",
        contests = mutableListOf()
    )
}
