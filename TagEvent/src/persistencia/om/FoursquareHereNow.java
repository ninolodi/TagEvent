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
public class FoursquareHereNow implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkFoursquareHereNow;
	@Type(type="timestamp")
	private Data data;
	private Integer tipo;
	private Integer quatidadeNova;
	private Integer quantidadeAntiga;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkFoursquare")
	private Foursquare foursquare;


	/*-*-*-* Construtores *-*-*-*/
	public FoursquareHereNow() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkFoursquareHereNow() { return pkFoursquareHereNow; }
	public void setPkFoursquareHereNow(long pkFoursquareHereNow) { this.pkFoursquareHereNow = pkFoursquareHereNow; }

	public Data getData() { return data; }
	public void setData(Data data) { this.data = data; }

	public Integer getTipo() { return tipo; }
	public void setTipo(Integer tipo) { this.tipo = tipo; }

	public Integer getQuatidadeNova() { return quatidadeNova; }
	public void setQuatidadeNova(Integer quatidadeNova) { this.quatidadeNova = quatidadeNova; }

	public Integer getQuantidadeAntiga() { return quantidadeAntiga; }
	public void setQuantidadeAntiga(Integer quantidadeAntiga) { this.quantidadeAntiga = quantidadeAntiga; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Foursquare getFoursquare() { return foursquare; }
	public void setFoursquare(Foursquare foursquare) { this.foursquare = foursquare; }
}