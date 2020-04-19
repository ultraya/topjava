package ru.javawebinar.topjava.repository.datajpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import ru.javawebinar.topjava.model.User
import java.util.*

@Transactional(readOnly = true)
interface CrudUserRepository : JpaRepository<User, Int> {
    @Transactional
    @Modifying
    //@Query(name = User.DELETE)
    @Query("DELETE FROM User u WHERE u.id=:id")
    fun delete(@Param("id") id: Int): Int

    fun getByEmail(email: String): User?

    @Query("SELECT u FROM User u LEFT JOIN FETCH u._meals m WHERE u.id =:id ORDER BY m.dateTime DESC")
    fun getOneWithMeals(@Param("id") id: Int): Optional<User>
}