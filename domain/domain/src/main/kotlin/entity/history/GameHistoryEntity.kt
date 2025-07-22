package entity.history

import contract.BaseEntity
import entity.game.GameEntity
import entity.user.UserEntity
import jakarta.persistence.*
import net.minidev.json.annotate.JsonIgnore
import java.time.LocalDateTime

@Entity
@Table(name = "game_history")
class GameHistoryEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    @JsonIgnore
    var user: UserEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = true)
    @JsonIgnore
    var game: GameEntity? = null,

    @Column(name = "game_name", nullable = true)
    var gameName: String,

    @JoinColumn(name = "device", nullable = false)
    var device: String,

    @Column(name = "played_at", nullable = false)
    var playedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "energy", nullable = false)
    var energy: Int = 0,
) : BaseEntity() {
    constructor() : this(
        user = null,
        game = null,
        gameName = "",
        device = "",
        playedAt = LocalDateTime.now(),
        energy = 0
    )
}
