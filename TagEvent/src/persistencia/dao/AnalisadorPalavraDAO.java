package persistencia.dao;

import java.util.List;

import persistencia.om.AnalisadorPalavras;
import persistencia.om.Evento;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.persistencia.dao.base.BaseDAOAtta;
import br.com.mresolucoes.atta.persistencia.utils.QuerySQL;


public class AnalisadorPalavraDAO extends BaseDAOAtta
{
	public int apagarBasePalavras(BaseJDBC baseJDBC, Evento evento) throws Exception
	{
		QuerySQL query = new QuerySQL();

		query.add("DELETE FROM analisadorpalavras");
		if(evento!=null) { query.add(" WHERE fkEvento = ?", evento.getPkEvento()); }
		
		return baseJDBC.executaDelete(query);
	}
	
	/**
	 * Obtem todas as palavras para analisar de uma determinada classificacao
	 * @param baseJDBC
	 * @param classificacao
	 * @param status
	 * @param profundidade
	 * @return
	 * @throws Exception
	 */
	public List<AnalisadorPalavras> getPalavrasClassificacao(BaseJDBC baseJDBC, int classificacao, int status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM AnalisadorPalavras");
		query.add(" WHERE pkAnalisadorPalavras>0");
		query.add(" AND classificacao = ?", classificacao);
		query.add(" AND status = ?", status);
		
		return obtem(baseJDBC, AnalisadorPalavras.class, query, profundidade);
	}
}
