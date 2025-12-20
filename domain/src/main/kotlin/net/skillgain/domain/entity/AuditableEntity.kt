package net.skillgain.domain.entity

import jakarta.persistence.*
import net.skillgain.domain.entity.user.User
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class AuditableEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    var createdBy: User? = null

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    var updatedBy: User? = null
}