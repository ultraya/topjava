package ru.javawebinar.topjava.repository.datajpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import ru.javawebinar.topjava.model.User
import ru.javawebinar.topjava.repository.UserRepository

@Repository
class DataJpaUserRepository : UserRepository {

    @Autowired
    private lateinit var crudRepository: CrudUserRepository

    companion object {
        //private static final
        val SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email")
    }

    override fun save(user: User): User? {
        return crudRepository.save(user)
    }

    override fun delete(id: Int): Boolean {
        return crudRepository.delete(id) != 0
    }

    override fun get(id: Int): User? {
        return crudRepository.findById(id).orElse(null)
    }

    override fun getByEmail(email: String): User? {
        return crudRepository.getByEmail(email)
    }

    override fun getAll(): List<User> {
        return crudRepository.findAll(SORT_NAME_EMAIL)
    }

    override fun getWithMeals(id: Int): User? {
        return crudRepository.getOneWithMeals(id).orElse(null)
    }
}