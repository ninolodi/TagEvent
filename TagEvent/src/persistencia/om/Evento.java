package persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.Type;
import utils.data.Data;
import javax.persistence.Column;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;

@Entity
public class Evento implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkEvento;
	private String nome;
	private String senha;
	@Type(type="timestamp")
	private Data lancamento;
	@Column(length=1000)
	private String descricao;
	@Column(length=15000)
	private String informacoes;
	private Integer tipo;
	private Boolean verificado;
	private int status = BDConstantesAtta.STATUS_ATIVO;
	private Boolean enviaEmail;
	private Boolean privado;
	private int ativo;

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuario;

	@ManyToOne @JoinColumn(name="fkCategoria")
	private Categoria categoria;

	@OneToMany(mappedBy="evento")
	private List<Foursquare> foursquares;

	@OneToMany(mappedBy="evento")
	private List<Agenda> agendas;

	@OneToMany(mappedBy="evento")
	private List<Twitter> twitters;

	@OneToMany(mappedBy="evento")
	private List<Instagram> instagrams;

	@OneToMany(mappedBy="evento")
	private List<AdministradorEvento> administradorEventos;

	@OneToMany(mappedBy="evento")
	private List<InstagramConfiguracao> instagramConfiguracaos;

	@OneToMany(mappedBy="evento")
	private List<FoursquareConfiguracao> foursquareConfiguracaos;

	@OneToMany(mappedBy="evento")
	private List<TwitterConfiguracao> twitterConfiguracaos;


	/*-*-*-* Construtores *-*-*-*/
	public Evento() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkEvento() { return pkEvento; }
	public void setPkEvento(long pkEvento) { this.pkEvento = pkEvento; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getSenha() { return senha; }
	public void setSenha(String senha) { this.senha = senha; }

	public Data getLancamento() { return lancamento; }
	public void setLancamento(Data lancamento) { this.lancamento = lancamento; }

	public String getDescricao() { return descricao; }
	public void setDescricao(String descricao) { this.descricao = descricao; }

	public String getInformacoes() { return informacoes; }
	public void setInformacoes(String informacoes) { this.informacoes = informacoes; }

	public Integer getTipo() { return tipo; }
	public void setTipo(Integer tipo) { this.tipo = tipo; }

	public Boolean getVerificado() { return verificado; }
	public void setVerificado(Boolean verificado) { this.verificado = verificado; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Boolean getEnviaEmail() { return enviaEmail; }
	public void setEnviaEmail(Boolean enviaEmail) { this.enviaEmail = enviaEmail; }

	public Boolean getPrivado() { return privado; }
	public void setPrivado(Boolean privado) { this.privado = privado; }

	public int getAtivo() { return ativo; }
	public void setAtivo(int ativo) { this.ativo = ativo; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

	public Categoria getCategoria() { return categoria; }
	public void setCategoria(Categoria categoria) { this.categoria = categoria; }

	public List<Foursquare> getFoursquares() { if(foursquares==null) { foursquares = new ArrayList<Foursquare>(); } return foursquares; }
	public void setFoursquares(List<Foursquare> foursquares) { this.foursquares = foursquares; }

	public List<Agenda> getAgendas() { if(agendas==null) { agendas = new ArrayList<Agenda>(); } return agendas; }
	public void setAgendas(List<Agenda> agendas) { this.agendas = agendas; }

	public List<Twitter> getTwitters() { if(twitters==null) { twitters = new ArrayList<Twitter>(); } return twitters; }
	public void setTwitters(List<Twitter> twitters) { this.twitters = twitters; }

	public List<Instagram> getInstagrams() { if(instagrams==null) { instagrams = new ArrayList<Instagram>(); } return instagrams; }
	public void setInstagrams(List<Instagram> instagrams) { this.instagrams = instagrams; }

	public List<AdministradorEvento> getAdministradorEventos() { if(administradorEventos==null) { administradorEventos = new ArrayList<AdministradorEvento>(); } return administradorEventos; }
	public void setAdministradorEventos(List<AdministradorEvento> administradorEventos) { this.administradorEventos = administradorEventos; }

	public List<InstagramConfiguracao> getInstagramConfiguracaos() { if(instagramConfiguracaos==null) { instagramConfiguracaos = new ArrayList<InstagramConfiguracao>(); } return instagramConfiguracaos; }
	public void setInstagramConfiguracaos(List<InstagramConfiguracao> instagramConfiguracaos) { this.instagramConfiguracaos = instagramConfiguracaos; }

	public List<FoursquareConfiguracao> getFoursquareConfiguracaos() { if(foursquareConfiguracaos==null) { foursquareConfiguracaos = new ArrayList<FoursquareConfiguracao>(); } return foursquareConfiguracaos; }
	public void setFoursquareConfiguracaos(List<FoursquareConfiguracao> foursquareConfiguracaos) { this.foursquareConfiguracaos = foursquareConfiguracaos; }

	public List<TwitterConfiguracao> getTwitterConfiguracaos() { if(twitterConfiguracaos==null) { twitterConfiguracaos = new ArrayList<TwitterConfiguracao>(); } return twitterConfiguracaos; }
	public void setTwitterConfiguracaos(List<TwitterConfiguracao> twitterConfiguracaos) { this.twitterConfiguracaos = twitterConfiguracaos; }
}