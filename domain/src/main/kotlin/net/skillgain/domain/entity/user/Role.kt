package net.skillgain.domain.entity.user

import jakarta.persistence.*

@Entity
@Table(name = "roles")
class Role(

    @Id
    val id: Long,

    @Column(nullable = false, unique = true)
    val name: String,

    val description: String? = null

)
