package persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;

@Entity
public class Categoria implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkCategoria;
	private String nome;
	private int status = BDConstantesAtta.STATUS_ATIVO;

	@OneToMany(mappedBy="categoria")
	private List<Evento> eventos;


	/*-*-*-* Construtores *-*-*-*/
	public Categoria() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkCategoria() { return pkCategoria; }
	public void setPkCategoria(long pkCategoria) { this.pkCategoria = pkCategoria; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }

	public List<Evento> getEventos() { if(eventos==null) { eventos = new ArrayList<Evento>(); } return eventos; }
	public void setEventos(List<Evento> eventos) { this.eventos = eventos; }
}