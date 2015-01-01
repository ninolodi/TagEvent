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

@Entity
public class InstagramFoto implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkInstagramFoto;
	private String urlImagemPequena;
	private String urlImagemMedia;
	private String urlImagemGrande;
	private String urlVideoPequeno;
	private String urlVideoMedio;
	private Integer tipo;
	private String link;
	private String idFotoInstagram;
	private String tags;
	private String texto;
	@Type(type="timestamp")
	private Data data;
	private Double latitude;
	private Double longitude;
	private String localizacao;
	private Long tempo;
	@Type(type="timestamp")
	private Data lancamento;
	@Type(type="timestamp")
	private Data habilitado;
	private Integer classificacao;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkInstagram")
	private Instagram instagram;

	@ManyToOne @JoinColumn(name="fkUserInstagram")
	private UserInstagram userInstagram;


	/*-*-*-* Construtores *-*-*-*/
	public InstagramFoto() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkInstagramFoto() { return pkInstagramFoto; }
	public void setPkInstagramFoto(long pkInstagramFoto) { this.pkInstagramFoto = pkInstagramFoto; }

	public String getUrlImagemPequena() { return urlImagemPequena; }
	public void setUrlImagemPequena(String urlImagemPequena) { this.urlImagemPequena = urlImagemPequena; }

	public String getUrlImagemMedia() { return urlImagemMedia; }
	public void setUrlImagemMedia(String urlImagemMedia) { this.urlImagemMedia = urlImagemMedia; }

	public String getUrlImagemGrande() { return urlImagemGrande; }
	public void setUrlImagemGrande(String urlImagemGrande) { this.urlImagemGrande = urlImagemGrande; }

	public String getUrlVideoPequeno() { return urlVideoPequeno; }
	public void setUrlVideoPequeno(String urlVideoPequeno) { this.urlVideoPequeno = urlVideoPequeno; }

	public String getUrlVideoMedio() { return urlVideoMedio; }
	public void setUrlVideoMedio(String urlVideoMedio) { this.urlVideoMedio = urlVideoMedio; }

	public Integer getTipo() { return tipo; }
	public void setTipo(Integer tipo) { this.tipo = tipo; }

	public String getLink() { return link; }
	public void setLink(String link) { this.link = link; }

	public String getIdFotoInstagram() { return idFotoInstagram; }
	public void setIdFotoInstagram(String idFotoInstagram) { this.idFotoInstagram = idFotoInstagram; }

	public String getTags() { return tags; }
	public void setTags(String tags) { this.tags = tags; }

	public String getTexto() { return texto; }
	public void setTexto(String texto) { this.texto = texto; }

	public Data getData() { return data; }
	public void setData(Data data) { this.data = data; }

	public Double getLatitude() { return latitude; }
	public void setLatitude(Double latitude) { this.latitude = latitude; }

	public Double getLongitude() { return longitude; }
	public void setLongitude(Double longitude) { this.longitude = longitude; }

	public String getLocalizacao() { return localizacao; }
	public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

	public Long getTempo() { return tempo; }
	public void setTempo(Long tempo) { this.tempo = tempo; }

	public Data getLancamento() { return lancamento; }
	public void setLancamento(Data lancamento) { this.lancamento = lancamento; }

	public Data getHabilitado() { return habilitado; }
	public void setHabilitado(Data habilitado) { this.habilitado = habilitado; }

	public Integer getClassificacao() { return classificacao; }
	public void setClassificacao(Integer classificacao) { this.classificacao = classificacao; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Instagram getInstagram() { return instagram; }
	public void setInstagram(Instagram instagram) { this.instagram = instagram; }

	public UserInstagram getUserInstagram() { return userInstagram; }
	public void setUserInstagram(UserInstagram userInstagram) { this.userInstagram = userInstagram; }
}