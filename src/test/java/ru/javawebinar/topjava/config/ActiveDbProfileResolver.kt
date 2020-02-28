package ru.javawebinar.topjava.config

import org.springframework.test.context.ActiveProfilesResolver
import ru.javawebinar.topjava.Profiles

class ActiveDbProfileResolver: ActiveProfilesResolver {
    override fun resolve(aClass: Class<*>): Array<String> = arrayOf(Profiles.activeProfile)
}