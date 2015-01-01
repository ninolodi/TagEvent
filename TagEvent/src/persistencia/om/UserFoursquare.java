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
public class UserFoursquare implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkUserFoursquare;
	@Type(type="timestamp")
	private Data lancamento;
	private String nome;
	private String idFoursquare;
	private Integer genero;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuario;

	@OneToMany(mappedBy="userFoursquare")
	private List<FoursquareCheckin> foursquareCheckins;


	/*-*-*-* Construtores *-*-*-*/
	public UserFoursquare() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkUserFoursquare() { return pkUserFoursquare; }
	public void setPkUserFoursquare(long pkUserFoursquare) { this.pkUserFoursquare = pkUserFoursquare; }

	public Data getLancamento() { return lancamento; }
	public void setLancamento(Data lancamento) { this.lancamento = lancamento; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getIdFoursquare() { return idFoursquare; }
	public void setIdFoursquare(String idFoursquare) { this.idFoursquare = idFoursquare; }

	public Integer getGenero() { return genero; }
	public void setGenero(Integer genero) { this.genero = genero; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }

	public List<FoursquareCheckin> getFoursquareCheckins() { if(foursquareCheckins==null) { foursquareCheckins = new ArrayList<FoursquareCheckin>(); } return foursquareCheckins; }
	public void setFoursquareCheckins(List<FoursquareCheckin> foursquareCheckins) { this.foursquareCheckins = foursquareCheckins; }
}