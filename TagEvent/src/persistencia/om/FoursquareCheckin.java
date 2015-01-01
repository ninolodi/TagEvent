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
public class FoursquareCheckin implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkFoursquareCheckin;
	@Type(type="timestamp")
	private Data lancamento;
	@Type(type="timestamp")
	private Data data;
	private String idCheckin;
	private String post;
	private Integer classificacao;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkFoursquare")
	private Foursquare foursquare;

	@ManyToOne @JoinColumn(name="fkUserFoursquare")
	private UserFoursquare userFoursquare;


	/*-*-*-* Construtores *-*-*-*/
	public FoursquareCheckin() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkFoursquareCheckin() { return pkFoursquareCheckin; }
	public void setPkFoursquareCheckin(long pkFoursquareCheckin) { this.pkFoursquareCheckin = pkFoursquareCheckin; }

	public Data getLancamento() { return lancamento; }
	public void setLancamento(Data lancamento) { this.lancamento = lancamento; }

	public Data getData() { return data; }
	public void setData(Data data) { this.data = data; }

	public String getIdCheckin() { return idCheckin; }
	public void setIdCheckin(String idCheckin) { this.idCheckin = idCheckin; }

	public String getPost() { return post; }
	public void setPost(String post) { this.post = post; }

	public Integer getClassificacao() { return classificacao; }
	public void setClassificacao(Integer classificacao) { this.classificacao = classificacao; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Foursquare getFoursquare() { return foursquare; }
	public void setFoursquare(Foursquare foursquare) { this.foursquare = foursquare; }

	public UserFoursquare getUserFoursquare() { return userFoursquare; }
	public void setUserFoursquare(UserFoursquare userFoursquare) { this.userFoursquare = userFoursquare; }
}