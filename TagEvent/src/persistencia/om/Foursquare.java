package persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;

@Entity
public class Foursquare implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkFoursquare;
	private String idVenue;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkEvento")
	private Evento evento;

	@OneToMany(mappedBy="foursquare")
	private List<FoursquareHereNow> foursquareHereNows;

	@OneToMany(mappedBy="foursquare")
	private List<FoursquareCheckin> foursquareCheckins;


	/*-*-*-* Construtores *-*-*-*/
	public Foursquare() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkFoursquare() { return pkFoursquare; }
	public void setPkFoursquare(long pkFoursquare) { this.pkFoursquare = pkFoursquare; }

	public String getIdVenue() { return idVenue; }
	public void setIdVenue(String idVenue) { this.idVenue = idVenue; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Evento getEvento() { return evento; }
	public void setEvento(Evento evento) { this.evento = evento; }

	public List<FoursquareHereNow> getFoursquareHereNows() { if(foursquareHereNows==null) { foursquareHereNows = new ArrayList<FoursquareHereNow>(); } return foursquareHereNows; }
	public void setFoursquareHereNows(List<FoursquareHereNow> foursquareHereNows) { this.foursquareHereNows = foursquareHereNows; }

	public List<FoursquareCheckin> getFoursquareCheckins() { if(foursquareCheckins==null) { foursquareCheckins = new ArrayList<FoursquareCheckin>(); } return foursquareCheckins; }
	public void setFoursquareCheckins(List<FoursquareCheckin> foursquareCheckins) { this.foursquareCheckins = foursquareCheckins; }
}