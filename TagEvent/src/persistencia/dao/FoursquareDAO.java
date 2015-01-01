package persistencia.dao;

import java.util.List;

import persistencia.om.Foursquare;
import persistencia.om.FoursquareCheckin;
import persistencia.om.FoursquareHereNow;
import persistencia.om.UserFoursquare;
import persistencia.om.Usuario;
import utils.data.Data;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.persistencia.dao.base.BaseDAOAtta;
import br.com.mresolucoes.atta.persistencia.utils.QuerySQL;


public class FoursquareDAO extends BaseDAOAtta
{
	/**
	 * Obtem lista de todos os foursquares ativos de um evento
	 * @param baseJDBC
	 * @param status
	 * @param profundidade
	 * @return eventos
	 * @throws Exception
	 */
	public List<Foursquare> getFoursquaresEvento(BaseJDBC baseJDBC, long fkEvento, int status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM Foursquare");
		query.add(" WHERE fkEvento=?", fkEvento);
		query.add(" AND status=?", status);
		
		return obtem(baseJDBC, Foursquare.class, query, profundidade);
	}

	/**
	 * Obtem o checkin mais atual ativo de um determinado foursquare de um evento
	 * @param baseJDBC
	 * @param status
	 * @param profundidade
	 * @return eventos
	 * @throws Exception
	 */
	public FoursquareHereNow getUltimoCheckin(BaseJDBC baseJDBC, long fkFoursquare, int status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM FoursquareHereNow");
		query.add(" WHERE fkFoursquare=?", fkFoursquare);
		query.add(" AND status=?", status);
		query.add(" ORDER BY data DESC");
		query.add(" LIMIT 1");
		
		return obtemUnico(baseJDBC, FoursquareHereNow.class, query, profundidade);
	}
	
	public boolean addCheckinHereNow(BaseJDBC baseJDBC, Data data, int quantidadeAntiga, int quantidadeNova, int status, int tipo, Foursquare foursquare) throws Exception
	{
		FoursquareHereNow checkin = new FoursquareHereNow();
		checkin.setData(data);
		checkin.setQuantidadeAntiga(quantidadeAntiga);
		checkin.setQuatidadeNova(quantidadeNova);
		checkin.setStatus(status);
		checkin.setTipo(tipo);
		checkin.setFoursquare(foursquare);
		
		return (salvar(baseJDBC, checkin)!=null ? true : false);
	}
	
	/**
	 * Obtem o checkin pelo ID CHECKIN do Foursquare
	 * @param baseJDBC
	 * @param status
	 * @param profundidade
	 * @return eventos
	 * @throws Exception
	 */
	public FoursquareCheckin getCheckinIdFoursquare(BaseJDBC baseJDBC, String idCheckin, long fkFoursquare, int[] status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM FoursquareCheckin");
		query.add(" WHERE pkFoursquareCheckin>0");
		query.add(" AND idCheckin= ?", idCheckin);
		query.add(" AND fkFoursquare = ?", fkFoursquare);
		query.add(" AND status IN (?)", status);
		
		return obtemUnico(baseJDBC, FoursquareCheckin.class, query, profundidade);
	}
	
	
	public boolean addCheckin(BaseJDBC baseJDBC, Data lancamento, Data data, String idCheckin, String post, int classificacao, int status, Foursquare foursquare, UserFoursquare userFoursquare) throws Exception
	{
		FoursquareCheckin checkin = new FoursquareCheckin();
		checkin.setLancamento(lancamento);
		checkin.setData(data);
		checkin.setIdCheckin(idCheckin);
		checkin.setPost(post);
		checkin.setClassificacao(classificacao);
		checkin.setStatus(status);
		checkin.setFoursquare(foursquare);
		checkin.setUserFoursquare(userFoursquare);
		
		return (salvar(baseJDBC, checkin)!=null ? true : false);
	}

	/**
	 * Obtem o user pelo ID do Foursquare
	 * @param baseJDBC
	 * @param status
	 * @param profundidade
	 * @return eventos
	 * @throws Exception
	 */
	public UserFoursquare getUserIdFoursquare(BaseJDBC baseJDBC, String IdFoursquare, int status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM UserFoursquare");
		query.add(" WHERE pkUserFoursquare>0");
		query.add(" AND idFoursquare= ?", IdFoursquare);
		query.add(" AND status = ?", status);
		
		return obtemUnico(baseJDBC, UserFoursquare.class, query, profundidade);
	}
	
	public UserFoursquare addUserFousquare(BaseJDBC baseJDBC, Data lancamento, String idFoursquare, String nome, int genero, int status, Usuario usuario) throws Exception
	{
		UserFoursquare user = new UserFoursquare();
		user.setLancamento(lancamento);
		user.setIdFoursquare(idFoursquare);
		user.setNome(nome);
		user.setGenero(genero);
		user.setUsuario(usuario);
		user.setStatus(status);
		
		salvar(baseJDBC, user);
		return user;
	}
}
