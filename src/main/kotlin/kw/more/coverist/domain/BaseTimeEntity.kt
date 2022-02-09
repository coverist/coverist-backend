package kw.more.coverist.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@EntityListeners(value = [AuditingEntityListener::class])
@MappedSuperclass
open class BaseTimeEntity (
    @CreatedDate
    @Column(name = "created_date", insertable = false, updatable = false)
    var createdDate: LocalDateTime? = null,

    @LastModifiedDate
    @Column(name = "modified_date", insertable = false, updatable = false)
    var modifiedDate: LocalDateTime? = null
)