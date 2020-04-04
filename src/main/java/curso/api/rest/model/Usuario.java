package curso.api.rest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true)
	private String login;
	private String senha;
	private String nome;
	
	//OneToMany -> um usuário pode ter muitos telefones
	@OneToMany(mappedBy = "usuario", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Telefone> telefones = new ArrayList<Telefone>();
	
	//OneToMany -> um usuário pode ter muitas regras (roles)
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuarios_role", //cria tabela de acesso do usuário - muitos para muitos (uma regra pode ter muitos usuários)
				uniqueConstraints = @UniqueConstraint( //adicionando uma constraint
														columnNames = {"usuario_id", "role_id"},
														name = "unique_role_user"
													),
				//tabela Usuario
				joinColumns = @JoinColumn( //unindo tabelas
											name = "usuario_id", //da nova tabela = usuarios_role
											referencedColumnName = "id", //da tabela usuario
											table = "usuario",
											unique = false, //pode se repetir usuario
											foreignKey = @ForeignKey(name = "usuario_fk", value = ConstraintMode.CONSTRAINT)
										),
				//tabela Role
				inverseJoinColumns = @JoinColumn( //unindo tabelas
										name = "role_id", //da nova tabela = usuarios_role
										referencedColumnName = "id", //da tabela role 
										table = "role",
										unique = false, //pose se repetir role
										updatable = false,
										foreignKey = @ForeignKey(name = "role_fk", value = ConstraintMode.CONSTRAINT))
			)
	private List<Role> roles = new ArrayList<Role>();
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
	
	public List<Telefone> getTelefones() {
		return telefones;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	//sao os acessos do usuario ROLE_ADMIN...
	@Override
	public Collection<Role> getAuthorities() {				
		return this.roles;
	}

	@JsonIgnore //não é retornado no json para o cliente
	@Override
	public String getPassword() {
		return this.senha;
	}

	@JsonIgnore //não é retornado no json para o cliente
	@Override
	public String getUsername() {
		return this.login;
	}

	@JsonIgnore //não é retornado no json para o cliente
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore //não é retornado no json para o cliente
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore //não é retornado no json para o cliente
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore //não é retornado no json para o cliente
	@Override
	public boolean isEnabled() {
		return true;
	}

	
	
	
}
