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

@Entity
public class MonitoramentoAutomatico implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkMonitoramentoAutomatico;
	private String username;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkInstagram")
	private Instagram instagram;

	@ManyToOne @JoinColumn(name="fkTwitter")
	private Twitter twitter;


	/*-*-*-* Construtores *-*-*-*/
	public MonitoramentoAutomatico() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkMonitoramentoAutomatico() { return pkMonitoramentoAutomatico; }
	public void setPkMonitoramentoAutomatico(long pkMonitoramentoAutomatico) { this.pkMonitoramentoAutomatico = pkMonitoramentoAutomatico; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Instagram getInstagram() { return instagram; }
	public void setInstagram(Instagram instagram) { this.instagram = instagram; }

	public Twitter getTwitter() { return twitter; }
	public void setTwitter(Twitter twitter) { this.twitter = twitter; }
}