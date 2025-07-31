package entity.reward

import contract.BaseEntity
import entity.game.GameEntity
import entity.user.UserEntity
import jakarta.persistence.*
import net.minidev.json.annotate.JsonIgnore
import java.time.LocalDateTime

@Entity
@Table(name = "user_rewards")
class UserRewardEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    var user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = true)
    @JsonIgnore
    val game: GameEntity?,

    @Column(name = "game_name", nullable = true)
    val gameName: String?,

    @Column(name = "device_id", nullable = true)
    val deviceId: String?,

    @Column(name = "photo", nullable = false)
    val photo: String,

    @Column(name = "phone", nullable = false)
    val phone: String,

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    val description: String,

    @Column(name = "ref", nullable = false)
    val ref: String,

    @Column(name = "merchant_name", nullable = false)
    val merchantName: String,

    @Column(name = "reward_name", nullable = false)
    val rewardName: String,

    @Column(name = "reward_url", nullable = false)
    val rewardUrl: String,

    @Column(name = "claimed_at")
    var claimedAt: LocalDateTime,

    @Column(name = "is_claimed", nullable = false)
    var isClaimed: Boolean = false,
) : BaseEntity()