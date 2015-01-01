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
public class AdministradorEvento implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkAdministradorEvento;
	private Integer tipo;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkEvento")
	private Evento evento;

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuario;


	/*-*-*-* Construtores *-*-*-*/
	public AdministradorEvento() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkAdministradorEvento() { return pkAdministradorEvento; }
	public void setPkAdministradorEvento(long pkAdministradorEvento) { this.pkAdministradorEvento = pkAdministradorEvento; }

	public Integer getTipo() { return tipo; }
	public void setTipo(Integer tipo) { this.tipo = tipo; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Evento getEvento() { return evento; }
	public void setEvento(Evento evento) { this.evento = evento; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}