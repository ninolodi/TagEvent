package persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
public class Contato implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkContato;
	private String contato;
	private String responsavel;
	private boolean padrao;

	@ManyToOne @JoinColumn(name="fkTipoContato")
	private TipoContato tipoContato;

	@ManyToOne @JoinColumn(name="fkPessoaFisica")
	private PessoaFisica pessoaFisica;

	@ManyToOne @JoinColumn(name="fkPessoaJuridica")
	private PessoaJuridica pessoaJuridica;


	/*-*-*-* Construtores *-*-*-*/
	public Contato() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkContato() { return pkContato; }
	public void setPkContato(long pkContato) { this.pkContato = pkContato; }

	public String getContato() { return contato; }
	public void setContato(String contato) { this.contato = contato; }

	public String getResponsavel() { return responsavel; }
	public void setResponsavel(String responsavel) { this.responsavel = responsavel; }

	public boolean getPadrao() { return padrao; }
	public void setPadrao(boolean padrao) { this.padrao = padrao; }

	public TipoContato getTipoContato() { return tipoContato; }
	public void setTipoContato(TipoContato tipoContato) { this.tipoContato = tipoContato; }

	public PessoaFisica getPessoaFisica() { return pessoaFisica; }
	public void setPessoaFisica(PessoaFisica pessoaFisica) { this.pessoaFisica = pessoaFisica; }

	public PessoaJuridica getPessoaJuridica() { return pessoaJuridica; }
	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) { this.pessoaJuridica = pessoaJuridica; }
}