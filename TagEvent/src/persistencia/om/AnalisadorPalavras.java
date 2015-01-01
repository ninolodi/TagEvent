package persistencia.om;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;

@Entity
public class AnalisadorPalavras implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	@Transient
	private static final long serialVersionUID = 1L;

	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkAnalisadorPalavras;
	private String palavra;
	private Integer classificacao;
	private Integer status = BDConstantesAtta.STATUS_ATIVO;


	/*-*-*-* Construtores *-*-*-*/
	public AnalisadorPalavras() { }

	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public long getPkAnalisadorPalavras() { return pkAnalisadorPalavras; }
	public void setPkAnalisadorPalavras(long pkAnalisadorPalavras) { this.pkAnalisadorPalavras = pkAnalisadorPalavras; }

	public String getPalavra() { return palavra; }
	public void setPalavra(String palavra) { this.palavra = palavra; }

	public Integer getClassificacao() { return classificacao; }
	public void setClassificacao(Integer classificacao) { this.classificacao = classificacao; }

	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }
}