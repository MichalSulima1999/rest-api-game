package com.michael1099.rest_rpg.command_line_app_startup_runner

import com.michael1099.rest_rpg.auth.config.CommandLineAppStartupRunner
import com.michael1099.rest_rpg.configuration.TestBase
import org.springframework.beans.factory.annotation.Autowired

class CommandLineAppStartupRunnerTest extends TestBase {

    @Autowired
    CommandLineAppStartupRunner runner

    def "should create default admin account"() {
        when:
            runner.run()
            def user = authenticationServiceHelper.getUserByUsername("admin")
        then:
            user.username == "admin"
            user.email == "admin@gmail.com"
            user.password != "12345678"
    }
}
