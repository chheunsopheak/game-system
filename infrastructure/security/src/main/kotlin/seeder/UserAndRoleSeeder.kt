//package seeder
//
//import entity.role.RoleEntity
//import entity.user.UserEntity
//import entity.user.UserRoleEntity
//import jwt.JwtTokenProvider
//import org.springframework.boot.CommandLineRunner
//import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.stereotype.Component
//import repository.role.RoleRepository
//import repository.user.UserRepository
//import repository.user.UserRoleRepository
//import java.time.LocalDateTime
//
//@Component
//class UserAndRoleSeeder(
//    private val roleRepo: RoleRepository,
//    private val userRepo: UserRepository,
//    private val userRoleRepo: UserRoleRepository,
//    private val passwordEncoder: PasswordEncoder,
//    private val jwtTokenProvider: JwtTokenProvider
//) : CommandLineRunner {
//
//    override fun run(vararg args: String?) {
//        val now = LocalDateTime.now()
//
//        // Ensure roles exist
//        val roleMap = mutableMapOf<String, RoleEntity>()
//        val roleDef = listOf(
//            Triple("ADMIN", "R001", "Administrator Role"),
//            Triple("Operator", "R002", "Operator Role"),
//            Triple("User", "R003", "General User Role"),
//            Triple("Merchant", "R004", "Merchant Role"),
//            Triple("Device", "R005", "Device Role")
//        )
//
//        for ((name, code, desc) in roleDef) {
//            val existing = roleRepo.findByName(name)
//            val role = existing ?: roleRepo.save(
//                RoleEntity(name = name, code = code, description = desc)
//            )
//            roleMap[name] = role
//        }
//
//        // User creation function
//        fun createUserIfNotExists(
//            email: String, username: String, name: String, phone: String, password: String, roleName: String
//        ): UserEntity {
//            val existing = userRepo.findByEmail(email)
//                ?: userRepo.findByUsername(username)
//                ?: userRepo.findByPhone(phone)
//            if (existing != null) return existing
//
//            val user = UserEntity(
//                accountCreated = now,
//                email = email,
//                passwordHash = passwordEncoder.encode(password),
//                username = username,
//                name = name,
//                phone = phone,
//                photoUrl = "",
//                coverUrl = "",
//                energy = 0,
//                refreshToken = jwtTokenProvider.generateRefreshToken(),
//                refreshTokenExpiresAt = now.plusDays(30),
//                isEnabled = true,
//                isLocked = false,
//                lockUntil = null,
//                isDeleted = false,
//                deletedAt = null,
//                accessFailCount = 0,
//                lastLogin = now,
//                storeId = null,
//                merchant = null
//            )
//
//            val savedUser = userRepo.save(user)
//            val userRole = UserRoleEntity(user = savedUser, role = roleMap[roleName]!!)
//            userRoleRepo.save(userRole)
//            return savedUser
//        }
//
//        // Create and assign users
//        createUserIfNotExists(
//            email = "superadmin@gmail.com",
//            username = "superadmin",
//            name = "Super Admin",
//            phone = "855111111111",
//            password = "admin@123",
//            roleName = "ADMIN"
//        )
//
//        createUserIfNotExists(
//            email = "operator@gmail.com",
//            username = "operator",
//            name = "Operator",
//            phone = "85522222222",
//            password = "operator@123",
//            roleName = "Operator"
//        )
//
//        createUserIfNotExists(
//            email = "user@gmail.com",
//            username = "user",
//            name = "User",
//            phone = "85533333333",
//            password = "user@123",
//            roleName = "User"
//        )
//
//        createUserIfNotExists(
//            email = "merchant@gmail.com",
//            username = "merchant",
//            name = "Merchant",
//            phone = "85544444444",
//            password = "merchant@123",
//            roleName = "Merchant"
//        )
//
//        createUserIfNotExists(
//            email = "device@gmail.com",
//            username = "device",
//            name = "Device",
//            phone = "85555555555",
//            password = "device@123",
//            roleName = "Device"
//        )
//
//        println("✅ Seeded roles and users if missing.")
//    }
//}
