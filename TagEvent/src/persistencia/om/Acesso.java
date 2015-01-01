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
public class Acesso implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkAcesso;
	@Type(type="timestamp")
	private Data acesso;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@ManyToOne @JoinColumn(name="fkUsuario")
	private Usuario usuario;


	/*-*-*-* Construtores *-*-*-*/
	public Acesso() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkAcesso() { return pkAcesso; }
	public void setPkAcesso(long pkAcesso) { this.pkAcesso = pkAcesso; }

	public Data getAcesso() { return acesso; }
	public void setAcesso(Data acesso) { this.acesso = acesso; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public Usuario getUsuario() { return usuario; }
	public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}