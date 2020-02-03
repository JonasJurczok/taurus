package org.linesofcode.taurus.webapp

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class WebSecurityConfiguration: WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests()
                ?.antMatchers("/", "/login", "/css/**", "/js/**", "/img/**", "/webjars/**")?.permitAll()
                ?.anyRequest()?.authenticated()
                ?.and()
                    ?.formLogin()
                    ?.loginPage("/")
                    ?.permitAll()
                ?.and()
                ?.logout()
                    ?.invalidateHttpSession(true)
                    ?.clearAuthentication(true)
                    ?.logoutUrl("/logout")
                    ?.logoutSuccessUrl("/?logout")
                    ?.permitAll()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.inMemoryAuthentication()
                ?.withUser("alice@example.com")
                ?.password("{noop}alice")
                ?.roles("USER")
                ?.and()
                ?.withUser("bob@example.com")
                ?.password("{noop}bob")
                ?.roles("USER")
    }
}