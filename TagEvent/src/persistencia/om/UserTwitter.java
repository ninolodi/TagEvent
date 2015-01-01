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
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;

@Entity
public class UserTwitter implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkUserTwitter;
	@Type(type="timestamp")
	private Data lancamento;
	private Long idTwitter;
	private String nome;
	private String username;
	private String urlImagemPequena;
	private String urlImagemMedia;
	private String urlImagemGrande;
	private Integer genero;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuario;

	@OneToMany(mappedBy="userTwitter")
	private List<TwitterTweet> twitterTweets;


	/*-*-*-* Construtores *-*-*-*/
	public UserTwitter() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkUserTwitter() { return pkUserTwitter; }
	public void setPkUserTwitter(long pkUserTwitter) { this.pkUserTwitter = pkUserTwitter; }

	public Data getLancamento() { return lancamento; }
	public void setLancamento(Data lancamento) { this.lancamento = lancamento; }

	public Long getIdTwitter() { return idTwitter; }
	public void setIdTwitter(Long idTwitter) { this.idTwitter = idTwitter; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getUrlImagemPequena() { return urlImagemPequena; }
	public void setUrlImagemPequena(String urlImagemPequena) { this.urlImagemPequena = urlImagemPequena; }

	public String getUrlImagemMedia() { return urlImagemMedia; }
	public void setUrlImagemMedia(String urlImagemMedia) { this.urlImagemMedia = urlImagemMedia; }

	public String getUrlImagemGrande() { return urlImagemGrande; }
	public void setUrlImagemGrande(String urlImagemGrande) { this.urlImagemGrande = urlImagemGrande; }

	public Integer getGenero() { return genero; }
	public void setGenero(Integer genero) { this.genero = genero; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

	public List<TwitterTweet> getTwitterTweets() { if(twitterTweets==null) { twitterTweets = new ArrayList<TwitterTweet>(); } return twitterTweets; }
	public void setTwitterTweets(List<TwitterTweet> twitterTweets) { this.twitterTweets = twitterTweets; }
}