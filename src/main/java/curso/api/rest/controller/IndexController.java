package curso.api.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/usuario")
public class IndexController {

//	@GetMapping(value = "/", produces = "application/json")
//	public ResponseEntity init() {
//		return new ResponseEntity("Olá Usuário REST Spring Boot", HttpStatus.OK);
//	}
	
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity init2(@RequestParam (value = "nome", defaultValue = "nome nao informado", required = true) String nome, 
								@RequestParam (value = "salario", defaultValue = "salario nao informado", required = true) String salario) {		
		//para testar:
		//http://localhost:8080/usuario/?nome=thiago&salario=102
		return new ResponseEntity("Olá Usuário REST Spring Boot. Nome: " + nome + " => salario: " + salario, HttpStatus.OK);
	}
	
}
