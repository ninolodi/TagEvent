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
public class Agenda implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkAgenda;
	@Type(type="timestamp")
	private Data dataInicio;
	@Type(type="timestamp")
	private Data dataTermino;
	private Integer horaInicio;
	private Integer horaFim;
	private int status = BDConstantesAtta.STATUS_ATIVO;
	private Integer duracao;
	private Integer periodicidade;
	private String dias;

	@ManyToOne @JoinColumn(name="fkEvento")
	private Evento evento;


	/*-*-*-* Construtores *-*-*-*/
	public Agenda() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkAgenda() { return pkAgenda; }
	public void setPkAgenda(long pkAgenda) { this.pkAgenda = pkAgenda; }

	public Data getDataInicio() { return dataInicio; }
	public void setDataInicio(Data dataInicio) { this.dataInicio = dataInicio; }

	public Data getDataTermino() { return dataTermino; }
	public void setDataTermino(Data dataTermino) { this.dataTermino = dataTermino; }

	public Integer getHoraInicio() { return horaInicio; }
	public void setHoraInicio(Integer horaInicio) { this.horaInicio = horaInicio; }

	public Integer getHoraFim() { return horaFim; }
	public void setHoraFim(Integer horaFim) { this.horaFim = horaFim; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Integer getDuracao() { return duracao; }
	public void setDuracao(Integer duracao) { this.duracao = duracao; }

	public Integer getPeriodicidade() { return periodicidade; }
	public void setPeriodicidade(Integer periodicidade) { this.periodicidade = periodicidade; }

	public String getDias() { return dias; }
	public void setDias(String dias) { this.dias = dias; }

	public Evento getEvento() { return evento; }
	public void setEvento(Evento evento) { this.evento = evento; }
}