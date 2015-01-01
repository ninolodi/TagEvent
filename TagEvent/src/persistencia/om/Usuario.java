package persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.Type;
import utils.data.Data;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;

@Entity
public class Usuario implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkUsuario;
	private String login;
	private String senha;
	private int tipo; //TIPO_USUARIO_ROOT, TIPO_USUARIO_REPRESENTANTE_SISTEMA, TIPO_USUARIO_NORMAL
	@Type(type="timestamp")
	private Data lancamento;
	@Type(type="timestamp")
	private Data ultimoacesso;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@OneToMany(mappedBy="usuario")
	private List<PessoaFisica> pessoaFisicas;

	@OneToMany(mappedBy="usuario")
	private List<PessoaJuridica> pessoaJuridicas;

	@OneToMany(mappedBy="usuario")
	private List<AdministradorEvento> administradorEventos;

	@OneToMany(mappedBy="usuario")
	private List<Evento> eventos;

	@OneToMany(mappedBy="usuario")
	private List<UserFoursquare> userFoursquares;

	@OneToMany(mappedBy="usuario")
	private List<UserInstagram> userInstagrams;

	@OneToMany(mappedBy="usuario")
	private List<UserTwitter> userTwitters;

	@OneToMany(mappedBy="usuario")
	private List<Acesso> acessos;


	/*-*-*-* Construtores *-*-*-*/
	public Usuario() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkUsuario() { return pkUsuario; }
	public void setPkUsuario(long pkUsuario) { this.pkUsuario = pkUsuario; }

	public String getLogin() { return login; }
	public void setLogin(String login) { this.login = login; }

	public String getSenha() { return senha; }
	public void setSenha(String senha) { this.senha = senha; }

	public int getTipo() { return tipo; }
	public void setTipo(int tipo) { this.tipo = tipo; }

	public Data getLancamento() { return lancamento; }
	public void setLancamento(Data lancamento) { this.lancamento = lancamento; }

	public Data getUltimoacesso() { return ultimoacesso; }
	public void setUltimoacesso(Data ultimoacesso) { this.ultimoacesso = ultimoacesso; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public List<PessoaFisica> getPessoaFisicas() { if(pessoaFisicas==null) { pessoaFisicas = new ArrayList<PessoaFisica>(); } return pessoaFisicas; }
	public void setPessoaFisicas(List<PessoaFisica> pessoaFisicas) { this.pessoaFisicas = pessoaFisicas; }

	public List<PessoaJuridica> getPessoaJuridicas() { if(pessoaJuridicas==null) { pessoaJuridicas = new ArrayList<PessoaJuridica>(); } return pessoaJuridicas; }
	public void setPessoaJuridicas(List<PessoaJuridica> pessoaJuridicas) { this.pessoaJuridicas = pessoaJuridicas; }

	public List<AdministradorEvento> getAdministradorEventos() { if(administradorEventos==null) { administradorEventos = new ArrayList<AdministradorEvento>(); } return administradorEventos; }
	public void setAdministradorEventos(List<AdministradorEvento> administradorEventos) { this.administradorEventos = administradorEventos; }

	public List<Evento> getEventos() { if(eventos==null) { eventos = new ArrayList<Evento>(); } return eventos; }
	public void setEventos(List<Evento> eventos) { this.eventos = eventos; }

	public List<UserFoursquare> getUserFoursquares() { if(userFoursquares==null) { userFoursquares = new ArrayList<UserFoursquare>(); } return userFoursquares; }
	public void setUserFoursquares(List<UserFoursquare> userFoursquares) { this.userFoursquares = userFoursquares; }

	public List<UserInstagram> getUserInstagrams() { if(userInstagrams==null) { userInstagrams = new ArrayList<UserInstagram>(); } return userInstagrams; }
	public void setUserInstagrams(List<UserInstagram> userInstagrams) { this.userInstagrams = userInstagrams; }

	public List<UserTwitter> getUserTwitters() { if(userTwitters==null) { userTwitters = new ArrayList<UserTwitter>(); } return userTwitters; }
	public void setUserTwitters(List<UserTwitter> userTwitters) { this.userTwitters = userTwitters; }

	public List<Acesso> getAcessos() { if(acessos==null) { acessos = new ArrayList<Acesso>(); } return acessos; }
	public void setAcessos(List<Acesso> acessos) { this.acessos = acessos; }
}