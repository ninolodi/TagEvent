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

@Entity
public class FoursquareConfiguracao implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkFoursquareConfiguracao;
	private Integer janela;
	@Column(length=25000)
	private String layoutSlideshow;
	private Boolean monitoramento;
	private Integer status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkEvento")
	private Evento evento;


	/*-*-*-* Construtores *-*-*-*/
	public FoursquareConfiguracao() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkFoursquareConfiguracao() { return pkFoursquareConfiguracao; }
	public void setPkFoursquareConfiguracao(long pkFoursquareConfiguracao) { this.pkFoursquareConfiguracao = pkFoursquareConfiguracao; }

	public Integer getJanela() { return janela; }
	public void setJanela(Integer janela) { this.janela = janela; }

	public String getLayoutSlideshow() { return layoutSlideshow; }
	public void setLayoutSlideshow(String layoutSlideshow) { this.layoutSlideshow = layoutSlideshow; }

	public Boolean getMonitoramento() { return monitoramento; }
	public void setMonitoramento(Boolean monitoramento) { this.monitoramento = monitoramento; }

	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }

	public Evento getEvento() { return evento; }
	public void setEvento(Evento evento) { this.evento = evento; }
}