package org.linesofcode.taurus.webapp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

@EnableWebSecurity
class WebSecurityConfiguration: WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var authenticationSuccessHandler: AuthenticationSuccessHandler

    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests()
                ?.antMatchers("/", "/login", "/css/**", "/js/**", "/images/**")?.permitAll()
                ?.antMatchers("/**")?.authenticated()
                ?.and()
                ?.formLogin()
                ?.loginPage("/login")
                ?.loginProcessingUrl("/login")
                ?.failureUrl("/login?login_error")
                ?.successHandler(authenticationSuccessHandler);
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.inMemoryAuthentication()
                ?.withUser("user")
                ?.password("user")
                ?.roles("USER")
                ?.and()
                ?.withUser("admin")
                ?.password("admin")
                ?.roles("ADMIN");
    }
}