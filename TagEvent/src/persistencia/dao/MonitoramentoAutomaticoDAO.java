package persistencia.dao;

import persistencia.om.Instagram;
import persistencia.om.MonitoramentoAutomatico;
import persistencia.om.Twitter;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.persistencia.dao.base.BaseDAOAtta;
import br.com.mresolucoes.atta.persistencia.utils.QuerySQL;


public class MonitoramentoAutomaticoDAO extends BaseDAOAtta
{
	/**
	 * Obtem um user que tem monitoramento automatico
	 * @param baseJDBC
	 * @param instagram
	 * @param status
	 * @param profundidade
	 * @return
	 * @throws Exception
	 */
	public MonitoramentoAutomatico getUserMonitoramento(BaseJDBC baseJDBC, String username, Instagram instagram, int status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM MonitoramentoAutomatico");
		query.add(" WHERE pkMonitoramentoAutomatico>0");
		query.add(" AND username = ?", username);
		query.add(" AND fkInstagram = ?", instagram.getPkInstagram());
		query.add(" AND status = ?", status);
		
		return obtemUnico(baseJDBC, MonitoramentoAutomatico.class, query, profundidade);
	}
	
	/**
	 * Obtem um user que tem monitoramento automatico
	 * @param baseJDBC
	 * @param twitter
	 * @param status
	 * @param profundidade
	 * @return
	 * @throws Exception
	 */
	public MonitoramentoAutomatico getUserMonitoramento(BaseJDBC baseJDBC, String username, Twitter twitter, int status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM MonitoramentoAutomatico");
		query.add(" WHERE pkMonitoramentoAutomatico>0");
		query.add(" AND username = ?", username);
		query.add(" AND fkTwitter = ?", twitter.getPkTwitter());
		query.add(" AND status = ?", status);
		
		return obtemUnico(baseJDBC, MonitoramentoAutomatico.class, query, profundidade);
	}
}
