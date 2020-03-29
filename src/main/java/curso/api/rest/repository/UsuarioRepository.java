package curso.api.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import curso.api.rest.model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	@Query("select u from Usuario u where u.login = ?1")
	Usuario findUserByLogin(String login);
	
	@Query("select u from Usuario u where u.nome like %?1%")
	List<Usuario> findUserByNome(String nome);
	
	@Query(value = "select constraint_name "
					+ "from information_schema.constraint_column_usage "
					+ "where table_name = 'usuarios_role' "
						+ "and column_name = 'role_id' "
						+ "and constraint_name <> 'unique_role_user';", 
					nativeQuery = true)
	String consultaConstraintRole();
		
	@Modifying //quando tiver alteracao no banco de dados deve-se usar essa anotacao
	@Query(value = "alter table usuarios_role drop constraint ?1;", nativeQuery = true)
	void removerConstratintRole(String constraint);
	
	@Modifying
	@Query(value = "insert into usuarios_role (usuario_id, role_id) "
					+ "values (?1, select id from role where nome_role = 'ROLE_USER');")
	void insereAcessoRolePadrao(Long idUser);
}
