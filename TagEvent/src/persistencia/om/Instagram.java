package persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;

@Entity
public class Instagram implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkInstagram;
	private String hashtag;
	private int janela;
	@Column(length=25000)
	private String layoutSlideshow;
	private Boolean monitoramento;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkEvento")
	private Evento evento;

	@OneToMany(mappedBy="instagram")
	private List<MonitoramentoAutomatico> monitoramentoAutomaticos;

	@OneToMany(mappedBy="instagram")
	private List<InstagramFoto> instagramFotos;


	/*-*-*-* Construtores *-*-*-*/
	public Instagram() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkInstagram() { return pkInstagram; }
	public void setPkInstagram(long pkInstagram) { this.pkInstagram = pkInstagram; }

	public String getHashtag() { return hashtag; }
	public void setHashtag(String hashtag) { this.hashtag = hashtag; }

	public int getJanela() { return janela; }
	public void setJanela(int janela) { this.janela = janela; }

	public String getLayoutSlideshow() { return layoutSlideshow; }
	public void setLayoutSlideshow(String layoutSlideshow) { this.layoutSlideshow = layoutSlideshow; }

	public Boolean getMonitoramento() { return monitoramento; }
	public void setMonitoramento(Boolean monitoramento) { this.monitoramento = monitoramento; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Evento getEvento() { return evento; }
	public void setEvento(Evento evento) { this.evento = evento; }

	public List<MonitoramentoAutomatico> getMonitoramentoAutomaticos() { if(monitoramentoAutomaticos==null) { monitoramentoAutomaticos = new ArrayList<MonitoramentoAutomatico>(); } return monitoramentoAutomaticos; }
	public void setMonitoramentoAutomaticos(List<MonitoramentoAutomatico> monitoramentoAutomaticos) { this.monitoramentoAutomaticos = monitoramentoAutomaticos; }

	public List<InstagramFoto> getInstagramFotos() { if(instagramFotos==null) { instagramFotos = new ArrayList<InstagramFoto>(); } return instagramFotos; }
	public void setInstagramFotos(List<InstagramFoto> instagramFotos) { this.instagramFotos = instagramFotos; }
}