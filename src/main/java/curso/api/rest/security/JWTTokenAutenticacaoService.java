package curso.api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import curso.api.rest.ApplicationContextLoad;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoService {

	//tem de validade do token 2 dias
	private static final long EXPIRATION_TIME = 172800000;
	
	//uma senha unica para compor a autenticacao e ajudar na seguranca
	private static final String SECRET = "SenhaExtremamenteSecreta";
	
	//prefixo padrao de Token
	private static final String TOKEN_PREFIX = "Bearer";
	
	//cabecalho
	private static final String HEADER_STRING = "Authorization";
	
	
	
	
	//gerando token de autenticacao e adicionando ao cabecalho e resposta http
	public void addAuthentication(HttpServletResponse response, String username) throws IOException {
		
		//montagem do token - gerando token para um determinado usuario
		String JWT = Jwts.builder() //chama o gerador de token
						.setSubject(username) //add o usuario
						.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //add tempo de validade do token
						.signWith(SignatureAlgorithm.HS512, SECRET).compact();//compacacao e algoritmos de geracao de senha
		
		//junta o token com o prefixo
		String token = TOKEN_PREFIX + " " + JWT; //Bearer 8785as4874w8487w548w74848w78484w8484d84f8d4
		
		//add no cabecalho http
		response.addHeader(HEADER_STRING, token); //Authorization: Bearer 8785as4874w8487w548w74848w78484w8484d84f8d4
	
		//liberando resposta para porta diferente (no mesmo servidor) do projeto angular
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		//add token como resposta no corpo do http
		response.getWriter().write("{\"Authorization\": \""+token+"\"}");
	}
	
	
	//retorna o usuário validado com token ou caso não seja valido retorna null
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		
		//pega o token enviado no cabecalho http
		String token = request.getHeader(HEADER_STRING);
		
		if (token != null) {
			
			//faz a validacao do token do usuario na requisicao
			String user = Jwts.parser() 
								.setSigningKey(SECRET) //Bearer 8785as4874w8487w548w74848w78484w8484d84f8d4 
								.parseClaimsJws(token.replace(TOKEN_PREFIX, "")) //8785as4874w8487w548w74848w78484w8484d84f8d4
								.getBody().getSubject(); //retorna o usuario João Silva
			
			if (user != null) {
				Usuario usuario = ApplicationContextLoad.getApplicationContext()
									.getBean(UsuarioRepository.class).findUserByLogin(user);
				
				if (usuario != null) {
					//retorna o usuario logado
					return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(), usuario.getAuthorities());
				}				
			}			
		}
		
		//liberando resposta para porta diferente (no mesmo servidor) do projeto angular
		response.addHeader("Access-Control-Allow-Origin", "*");
		return null; //nao autorizado
						
	}
}



