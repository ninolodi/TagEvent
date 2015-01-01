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
public class TwitterTweet implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkTwitterTweet;
	@Type(type="timestamp")
	private Data lancamento;
	@Type(type="timestamp")
	private Data data;
	private Long idTweet;
	private String post;
	@Type(type="timestamp")
	private Data habilitado;
	private Integer classificacao;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkTwitter")
	private Twitter twitter;

	@ManyToOne @JoinColumn(name="fkUserTwitter")
	private UserTwitter userTwitter;


	/*-*-*-* Construtores *-*-*-*/
	public TwitterTweet() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkTwitterTweet() { return pkTwitterTweet; }
	public void setPkTwitterTweet(long pkTwitterTweet) { this.pkTwitterTweet = pkTwitterTweet; }

	public Data getLancamento() { return lancamento; }
	public void setLancamento(Data lancamento) { this.lancamento = lancamento; }

	public Data getData() { return data; }
	public void setData(Data data) { this.data = data; }

	public Long getIdTweet() { return idTweet; }
	public void setIdTweet(Long idTweet) { this.idTweet = idTweet; }

	public String getPost() { return post; }
	public void setPost(String post) { this.post = post; }

	public Data getHabilitado() { return habilitado; }
	public void setHabilitado(Data habilitado) { this.habilitado = habilitado; }

	public Integer getClassificacao() { return classificacao; }
	public void setClassificacao(Integer classificacao) { this.classificacao = classificacao; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Twitter getTwitter() { return twitter; }
	public void setTwitter(Twitter twitter) { this.twitter = twitter; }

	public UserTwitter getUserTwitter() { return userTwitter; }
	public void setUserTwitter(UserTwitter userTwitter) { this.userTwitter = userTwitter; }
}