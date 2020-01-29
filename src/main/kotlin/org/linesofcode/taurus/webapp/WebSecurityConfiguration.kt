package org.linesofcode.taurus.webapp

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@EnableWebSecurity
class WebSecurityConfiguration: WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests()
                ?.antMatchers("/", "/login", "/css/**", "/js/**", "/img/**", "/webjars/**")?.permitAll()
                ?.anyRequest()?.authenticated()
                ?.and()
                    ?.formLogin()
                    ?.successForwardUrl("/?login_success")
                    ?.failureForwardUrl("/?login_error&login")
                    ?.loginPage("/?login")
                    ?.permitAll()
                ?.and()
                ?.logout()
                    ?.invalidateHttpSession(true)
                    ?.clearAuthentication(true)
                    ?.logoutRequestMatcher(AntPathRequestMatcher("/logout"))
                    ?.logoutSuccessUrl("/login?logout")
                    ?.permitAll()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.inMemoryAuthentication()
                ?.withUser("alice@example.com")
                ?.password("alice")
                ?.roles("USER")
                ?.and()
                ?.withUser("bob@example.com")
                ?.password("bob")
                ?.roles("USER")
    }
}