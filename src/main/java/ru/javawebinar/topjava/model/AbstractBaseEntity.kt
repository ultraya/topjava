package ru.javawebinar.topjava.model

import javax.persistence.*

@MappedSuperclass  //Она используется когда вы хотите “спрятать” общие поля для нескольких
// сущностей объектной модели. При этом сам аннотированный класс не рассматривается как отдельная сущность.
// http://stackoverflow.com/questions/594597/hibernate-annotations-which-is-better-field-or-property-access
//https://kotlinexpertise.com/hibernate-with-Kotlin-spring-boot/
@Access(AccessType.FIELD)
abstract class AbstractBaseEntity protected constructor(
        @Id
        @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1, initialValue = START_SEQ)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
        var id: Int?) {

    companion object {
        const val START_SEQ = 100000
    }

    val isNew: Boolean
        get() = id == null

    override fun toString() = javaClass.simpleName + ":" + id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbstractBaseEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id ?: 0
    }
}