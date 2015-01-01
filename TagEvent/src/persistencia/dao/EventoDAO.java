package persistencia.dao;

import java.util.List;

import persistencia.om.Evento;
import utils.BDConstantes;
import utils.data.Data;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.persistencia.dao.base.BaseDAOAtta;
import br.com.mresolucoes.atta.persistencia.utils.QuerySQL;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;


public class EventoDAO extends BaseDAOAtta
{
	/**
	 * Obtem lista de todos os eventos ativos
	 * @param status
	 * @param profundidad
	 * @return eventos
	 * @throws Exception
	 */
	public List<Evento> getTodosEventos(BaseJDBC baseJDBC, int status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM evento");
		query.add(" WHERE status=?", status);
		
		return obtem(baseJDBC, Evento.class, query, profundidade);
	}

	/**
	 * Obtem um determinado evento
	 * @param pkEvento
	 * @param status
	 * @param profundidade
	 * @return evento
	 * @throws Exception
	 */
	public Evento getEvento(BaseJDBC baseJDBC, long pkEvento, int status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM evento");
		query.add(" WHERE status=?", status);
		query.add(" AND pkEvento=?", pkEvento);
		
		return obtemUnico(baseJDBC, Evento.class, query, profundidade);
	}

	/**
	 * Obtem lista de todos os eventos ativos de um usuario
	 * @param fkUser
	 * @param status
	 * @param profundidad
	 * @return eventos
	 * @throws Exception
	 */
	public List<Evento> getTodosEventosUsuario(BaseJDBC baseJDBC, long fkUser, int status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM evento");
		query.add(" WHERE status=?", status);
		query.add(" AND fkusuario=?", fkUser);
		query.add(" ORDER BY lancamento DESC");
		
		return obtem(baseJDBC, Evento.class, query, profundidade);
	}

	/**
	 * Obtem lista de todos os eventos ativos de um usuario
	 * @param fkUser
	 * @param status
	 * @param profundidad
	 * @return eventos
	 * @throws Exception
	 */
	public Evento getEventoUsuario(BaseJDBC baseJDBC, long pkEvento, Long fkUser, int status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM evento");
		query.add(" WHERE status=?", status);
		query.add(" AND pkEvento=?", pkEvento);
		if(fkUser!=null) { query.add(" AND fkusuario=?", fkUser); }
		
		return obtemUnico(baseJDBC, Evento.class, query, profundidade);
	}
	
	/**
	 * Obtem lista de todos os eventos que devem ser liberados nos proximos instantes
	 * @param baseJDBC
	 * @param dataInicial
	 * @param tempoInicial
	 * @param profundidade
	 * @return
	 * @throws Exception
	 */
	public List<Evento> getTodosEventosLiberar(BaseJDBC baseJDBC, Data dataInicial, int tempoInicial, int profundidade) throws Exception
	{
		int[] periodicidades = new int[] {BDConstantesAtta.PERIODICIDADE_SEMANAL, BDConstantesAtta.PERIODICIDADE_MENSAL, BDConstantesAtta.PERIODICIDADE_TRIMESTRAL, BDConstantesAtta.PERIODICIDADE_SEMESTRAL, BDConstantesAtta.PERIODICIDADE_ANUAL};
		
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT DISTINCT ev.* FROM Evento AS ev");
		query.add(" INNER JOIN Agenda AS ag ON (ag.fkEvento = ev.pkEvento)");
		query.add(" WHERE ev.pkEvento > 0");
		query.add(" AND ev.status = ?", BDConstantes.STATUS_EVENTO_BLOQUEADO);
		query.add(" AND ag.status = ?", BDConstantesAtta.STATUS_ATIVO);
		query.add(" AND (");
		query.add(" 	(ag.periodicidade IS NOT NULL AND ag.periodicidade = ? AND (? BETWEEN ag.datainicio AND ag.datatermino))", BDConstantesAtta.PERIODICIDADE_DIARIO, dataInicial);
		query.add(" 	OR");
		query.add(" 	(ag.periodicidade IS NOT NULL AND ag.periodicidade IN (?) AND (? BETWEEN ag.horainicio AND ag.horafim))", periodicidades, tempoInicial);
		query.add(" )");
		
		return obtem(baseJDBC, Evento.class, query, profundidade);
	}
	
	
	/**
	 * Obtem lista de todos os eventos que devem ser bloqueados nos proximos instantes
	 * @param baseJDBC
	 * @param dataFinal
	 * @param tempoFinal
	 * @param profundidade
	 * @return
	 * @throws Exception
	 */
	public List<Evento> getTodosEventosBloquear(BaseJDBC baseJDBC, Data dataFinal, int tempoFinal, int profundidade) throws Exception
	{
		int[] periodicidades = new int[] {BDConstantesAtta.PERIODICIDADE_SEMANAL, BDConstantesAtta.PERIODICIDADE_MENSAL, BDConstantesAtta.PERIODICIDADE_TRIMESTRAL, BDConstantesAtta.PERIODICIDADE_SEMESTRAL, BDConstantesAtta.PERIODICIDADE_ANUAL};
		
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT DISTINCT ev.* FROM Evento AS ev");
		query.add(" INNER JOIN Agenda AS ag ON (ag.fkEvento = ev.pkEvento)");
		query.add(" WHERE ev.pkEvento > 0");
		query.add(" AND ev.status = ?", BDConstantes.STATUS_EVENTO_LIBERADO);
		query.add(" AND ag.status = ?", BDConstantesAtta.STATUS_ATIVO);
		query.add(" AND (");
		query.add(" 	(ag.periodicidade IS NOT NULL AND ag.periodicidade = ? AND (? >= ag.datatermino))", BDConstantesAtta.PERIODICIDADE_DIARIO, dataFinal);
		query.add(" 	OR");
		query.add(" 	(ag.periodicidade IS NOT NULL AND ag.periodicidade IN (?) AND (? >= ag.horafim))", periodicidades, tempoFinal);
		query.add(" )");
		
		return obtem(baseJDBC, Evento.class, query, profundidade);
	}	
}
