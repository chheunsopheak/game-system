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

    @Column(name = "no")
    val no: Int = 0,

    @OneToMany(mappedBy = "member")
    var contests: MutableList<ContestEntity> = mutableListOf()
) : BaseEntity()
