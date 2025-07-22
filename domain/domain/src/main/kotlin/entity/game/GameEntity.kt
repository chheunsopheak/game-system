package entity.game

import contract.BaseEntity
import entity.history.GameHistoryEntity
import entity.reward.UserRewardEntity
import jakarta.persistence.*

@Entity
@Table(name = "game")
class GameEntity(
    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "game_icon")
    var gameIcon: String,

    @Column(name = "game_url")
    var gameUrl: String,

    @Column(name = "energy")
    var energy: Int,

    @Column(name = "description")
    var description: String,

    @Column(name = "category")
    var category: String,

    @Column(name = "threshold", nullable = false)
    var threshold: Int,

    @OneToMany(mappedBy = "game", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var userRewards: MutableList<UserRewardEntity> = mutableListOf(),

    @OneToMany(mappedBy = "game", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var gamePlays: MutableList<GameHistoryEntity> = mutableListOf()
) : BaseEntity() {

    // Default constructor required by JPA
    constructor() : this(
        name = "",
        gameIcon = "",
        gameUrl = "",
        energy = 0,
        description = "",
        category = "",
        threshold = 0,
        userRewards = mutableListOf(),
        gamePlays = mutableListOf()
    )
}
