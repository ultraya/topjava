package ru.javawebinar.topjava.model

import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@MappedSuperclass
abstract class AbstractNamedEntity protected constructor(
        id: Int?,
        @NotBlank
        @Size(min = 2, max = 100)
        @Column(name = "name", nullable = false)
        var name: String
) : AbstractBaseEntity(id) {

    override fun toString() = super.toString() + '(' + name + ')'

}