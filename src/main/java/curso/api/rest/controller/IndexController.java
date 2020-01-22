package curso.api.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/usuario")
public class IndexController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
//	@GetMapping(value = "v1/{id}", produces = "application/json") //ou
//	@GetMapping(value = "{id}", produces = "application/json", headers = "X-API-Version=v1") //passar no cabecalho da requisicao
	@GetMapping(value = "/{id}", produces = "application/json") 
	public ResponseEntity<Usuario> initV1(@PathVariable (value = "id") Long id) {		
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);	
		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	
//	@GetMapping(value = "v2/{id}", produces = "application/json") //ou
	@GetMapping(value = "{id}", produces = "application/json", headers = "X-API-Version=v2") //passar no cabecalho da requisicao
	public ResponseEntity<Usuario> initV2(@PathVariable (value = "id") Long id) {		
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		System.out.println("Executando Versão 2");
		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	
	
	//vamos supor que o carregamento de usuários seja um processo lento e queremos controlar ele com cache para agilizar o processo
//	@Cacheable("cacheusuarios") //mantem em cache os dados, porém quando há atualização o cache permanece antigo
	@GetMapping(value = "/", produces = "application/json")	
	@CachePut("cacheusuarios") //mantem em cache os dados, e quando há atualização o cache é atualizado ?????? acho que não ta funcionando direito
	public ResponseEntity<List<Usuario>> init2() throws InterruptedException {		
		
		List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();
		
//		Thread.sleep(6000); //segura o codigo por 6 segundos, simulando um processo lento
		
		return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/usuarioPorNome/{nome}", produces = "application/json")	
	public ResponseEntity<List<Usuario>> usuarioPorNome(@PathVariable("nome") String nome) throws InterruptedException {		
		
		List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findUserByNome(nome);
		
		return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);
	}
	
	
	
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {	
		
		for (int i = 0; i < usuario.getTelefones().size(); i++) {
			usuario.getTelefones().get(i).setUsuario(usuario);
		}
		
		String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhacriptografada);
		
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}
	
	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario) {		
		
		for (int i = 0; i < usuario.getTelefones().size(); i++) {
			usuario.getTelefones().get(i).setUsuario(usuario);
		}
		
		Usuario userTemp = usuarioRepository.findUserByLogin(usuario.getLogin());
		
		if (!userTemp.getSenha().equals(usuario.getSenha())) { //senhas diferentes
			String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhacriptografada);
		}
		
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}", produces = "application/text")
	public ResponseEntity delete(@PathVariable("id") Long id) {		
				
		usuarioRepository.deleteById(id);
		return new ResponseEntity("deletado", HttpStatus.OK);
	}
	
}
