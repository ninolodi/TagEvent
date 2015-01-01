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
public class UserInstagram implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkUserInstagram;
	@Type(type="timestamp")
	private Data lancamento;
	private String idInstagram;
	private String nome;
	private String username;
	private String urlImagemMedia;
	private Integer genero;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuario;

	@OneToMany(mappedBy="userInstagram")
	private List<InstagramFoto> instagramFotos;


	/*-*-*-* Construtores *-*-*-*/
	public UserInstagram() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkUserInstagram() { return pkUserInstagram; }
	public void setPkUserInstagram(long pkUserInstagram) { this.pkUserInstagram = pkUserInstagram; }

	public Data getLancamento() { return lancamento; }
	public void setLancamento(Data lancamento) { this.lancamento = lancamento; }

	public String getIdInstagram() { return idInstagram; }
	public void setIdInstagram(String idInstagram) { this.idInstagram = idInstagram; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getUrlImagemMedia() { return urlImagemMedia; }
	public void setUrlImagemMedia(String urlImagemMedia) { this.urlImagemMedia = urlImagemMedia; }

	public Integer getGenero() { return genero; }
	public void setGenero(Integer genero) { this.genero = genero; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

	public List<InstagramFoto> getInstagramFotos() { if(instagramFotos==null) { instagramFotos = new ArrayList<InstagramFoto>(); } return instagramFotos; }
	public void setInstagramFotos(List<InstagramFoto> instagramFotos) { this.instagramFotos = instagramFotos; }
}