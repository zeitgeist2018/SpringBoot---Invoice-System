package com.bolsadeideas.springboot.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccessHandler;
import com.bolsadeideas.springboot.app.models.service.JPAUserDetailsService;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private LoginSuccessHandler successHandler;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JPAUserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//Aquí especificamos los roles necesarios para acceder a
		//cada ruta. La forma "larga" es especificar cada acceso a rutas aquí,
		//pero tambien se puede hacer directamente en el controlador, utilizando
		//la anotación @Secured(role) en cada método
		http.authorizeRequests()
		.antMatchers("/", "/css/**", "/js/**", "/img/**", "/clientes", "/locale").permitAll()
		//.antMatchers("/ver/**").hasAnyRole("USER")
		//.antMatchers("/uploads/**").hasAnyRole("USER")
		//.antMatchers("/form/**").hasAnyRole("ADMIN")
		//.antMatchers("/eliminar/**").hasAnyRole("ADMIN")
		//.antMatchers("/factura/**").hasAnyRole("ADMIN")
		.anyRequest().authenticated()
		.and()
		.formLogin().successHandler(successHandler)
		.loginPage("/login").permitAll()	//El método .loginPage(ruta) sirve
		.and()											//para indicarle a Spring qué ruta utilizar
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/error_403");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder build) throws Exception{
		//Este código permite implementar usuarios "in memory"
		//UserBuilder users = User.withDefaultPasswordEncoder(); //Spring Boot 1.5.10
		/*PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder(); //Spring Boot 2
		UserBuilder users = User.builder().passwordEncoder(encoder::encode);	//Spring Boot 2
		build.inMemoryAuthentication()
		.withUser(
				users.username("admin")
				.password("admin")
				.roles("ADMIN", "USER"))
		.withUser(
				users.username("cristian")
				.password("cristian")
				.roles("USER"));*/

		//Este código permite implementar usuarios con base de datos a través de JDBC
		//Hay que indicarle las queries a ejecutar para hacer login y obtener los roles del usuario
		/*build.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder)
		.usersByUsernameQuery("SELECT USERNAME, PASSWORD, ENABLED FROM USERS WHERE USERNAME=?")
		.authoritiesByUsernameQuery(
				"SELECT U.USERNAME, A.AUTHORITY "
						+ "FROM AUTHORITIES A INNER JOIN USERS U "
						+ "ON (A.USER_ID = U.ID) "
						+ "WHERE U.USERNAME=?");*/
		
		//Este código permite implementar usuarios con base de datos a través de JPA.
		build.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder);
	}

}
