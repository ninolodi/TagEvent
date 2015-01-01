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
public class Endereco implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkEndereco;
	private String apelido;
	private String logradouro;
	private String numero;
	private String CEP;
	private String complemento;
	private String bairro;
	private boolean padrao;
	private String pontoReferencia;

	@ManyToOne @JoinColumn(name="fkCidade")
	private Cidade cidade;

	@ManyToOne @JoinColumn(name="fkTipoEndereco")
	private TipoEndereco tipoEndereco;


	/*-*-*-* Construtores *-*-*-*/
	public Endereco() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkEndereco() { return pkEndereco; }
	public void setPkEndereco(long pkEndereco) { this.pkEndereco = pkEndereco; }

	public String getApelido() { return apelido; }
	public void setApelido(String apelido) { this.apelido = apelido; }

	public String getLogradouro() { return logradouro; }
	public void setLogradouro(String logradouro) { this.logradouro = logradouro; }

	public String getNumero() { return numero; }
	public void setNumero(String numero) { this.numero = numero; }

	public String getCEP() { return CEP; }
	public void setCEP(String CEP) { this.CEP = CEP; }

	public String getComplemento() { return complemento; }
	public void setComplemento(String complemento) { this.complemento = complemento; }

	public String getBairro() { return bairro; }
	public void setBairro(String bairro) { this.bairro = bairro; }

	public boolean getPadrao() { return padrao; }
	public void setPadrao(boolean padrao) { this.padrao = padrao; }

	public String getPontoReferencia() { return pontoReferencia; }
	public void setPontoReferencia(String pontoReferencia) { this.pontoReferencia = pontoReferencia; }

	public Cidade getCidade() { return cidade; }
	public void setCidade(Cidade cidade) { this.cidade = cidade; }

	public TipoEndereco getTipoEndereco() { return tipoEndereco; }
	public void setTipoEndereco(TipoEndereco tipoEndereco) { this.tipoEndereco = tipoEndereco; }
}