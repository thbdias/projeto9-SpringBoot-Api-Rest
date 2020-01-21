package curso.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

import curso.api.rest.service.ImplementacaoUserDetailsService;

//mapeia URL, endereços, autoriza ou bloqueia acessos a URL...

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	

	@Override //configura as solicitaçoes de acesso por Http
	protected void configure(HttpSecurity http) throws Exception {		
		http
		
		//ativando a protecao contra usuario que nao estao validados por token
		.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) 
		
		//desativa as configuracoes padrao de memoria
		.disable() 
		
		//permitir restringir acessos
		.authorizeRequests() 
		
		//Qualquer usuario acessa a pagina inicial
		.antMatchers(HttpMethod.GET, "/").permitAll() 
		
		//Qualquer usuario acessa ao index
		.antMatchers(HttpMethod.GET, "/index").permitAll() 
		
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		
		.anyRequest().authenticated()
		
		//redireciona após o user deslogar do sistema		
		.and().logout().logoutSuccessUrl("/index") 
		
		//mapeia URL de Logout e invalida usuário autenticado - ao passar url de logout encerra cessao
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) 
		
		//filtra requisicoes de login para autenticar
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class) 
		
		//filtra demais requisicoes para verificar a presenca do TOKEN JWT no HEADER HTTP
		.addFilterBefore(new JwtApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class); 
	}
	
	@Override //cria autenticacao do usuario com banco de dados ou em memoria
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {		
		//service quer irá consultar o usuário no banco de dados
		auth.userDetailsService(implementacaoUserDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder()); //padrao de condificacao de senha
	}		
	
}
