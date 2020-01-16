package curso.api.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Usuario;

@RestController
@RequestMapping(value = "/usuario")
public class IndexController {
	
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> init() {		
		
		Usuario usuario = new Usuario();
		usuario.setId(39L);
		usuario.setLogin("login@login");
		usuario.setSenha("123");
		usuario.setNome("thiago");
		
		Usuario usuario2 = new Usuario();
		usuario2.setId(23L);
		usuario2.setLogin("maria@maria");
		usuario2.setSenha("454654");
		usuario2.setNome("maria");
		
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios.add(usuario);
		usuarios.add(usuario2);
		
		//para testar:
		//http://localhost:8080/usuario/?nome=thiago&salario=102		
		return new ResponseEntity(usuarios, HttpStatus.OK);
	}
	
}
