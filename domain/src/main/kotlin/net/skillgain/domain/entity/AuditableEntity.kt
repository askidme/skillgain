package net.skillgain.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
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
    @Column(name = "created_by")
    var createdBy: Long? = null

    @LastModifiedBy
    @Column(name = "updated_by")
    var updatedBy: Long? = null
}