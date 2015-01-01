package persistencia.dao;

import java.util.List;

import persistencia.om.Twitter;
import persistencia.om.TwitterTweet;
import persistencia.om.UserTwitter;
import persistencia.om.Usuario;
import utils.data.Data;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.persistencia.dao.base.BaseDAOAtta;
import br.com.mresolucoes.atta.persistencia.utils.QuerySQL;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;


public class TwitterDAO extends BaseDAOAtta
{
	/**
	 * Obtem lista de todos os twitters ativos de um evento
	 * @param baseJDBC
	 * @param status
	 * @param profundidade
	 * @return eventos
	 * @throws Exception
	 */
	public List<Twitter> getTwittersEvento(BaseJDBC baseJDBC, long fkEvento, int status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM Twitter");
		query.add(" WHERE fkEvento=?", fkEvento);
		query.add(" AND status=?", status);
		
		return obtem(baseJDBC, Twitter.class, query, profundidade);
	}

	/**
	 * Obtem o Tweet ativo com o id do twitter
	 * @param status
	 * @param profundidad
	 * @return eventos
	 * @throws Exception
	 */
	public TwitterTweet getTweetIdTwitter(BaseJDBC baseJDBC, long idTweet, long fkTwitter, int[] status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM TwitterTweet");
		query.add(" WHERE pkTwitterTweet>0");
		query.add(" AND idTweet= ?", idTweet);
		query.add(" AND fkTwitter = ?", fkTwitter);
		query.add(" AND status IN (?)", status);
		
		return obtemUnico(baseJDBC, TwitterTweet.class, query, profundidade);
	}
	
	/**
	 * Obtem o Tweet ativo com o id do twitter
	 * @param status
	 * @param profundidad
	 * @return eventos
	 * @throws Exception
	 */
	public boolean addTwitterTweet(BaseJDBC baseJDBC, Data lancamento, Data data, long idTweet, String post, int classificacao, int status, Twitter twitter, UserTwitter userTwitter) throws Exception
	{
		TwitterTweet twitterTweet = new TwitterTweet();
		twitterTweet.setLancamento(lancamento);
		twitterTweet.setData(data);
		twitterTweet.setIdTweet(idTweet);
		twitterTweet.setPost(post);
		twitterTweet.setClassificacao(classificacao);
		twitterTweet.setStatus(status);
		twitterTweet.setTwitter(twitter);
		twitterTweet.setUserTwitter(userTwitter);

		if(status==BDConstantesAtta.STATUS_ATIVO)
		{ twitterTweet.setHabilitado(new Data()); }
		
		return (salvar(baseJDBC, twitterTweet)!=null ? true : false);
	}
	
	/**
	 * Obtem o user pelo ID do Twitter
	 * @param baseJDBC
	 * @param status
	 * @param profundidade
	 * @return eventos
	 * @throws Exception
	 */
	public UserTwitter getUserIdTwitter(BaseJDBC baseJDBC, long IdTwitter, int status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM UserTwitter");
		query.add(" WHERE pkUserTwitter>0");
		query.add(" AND idTwitter= ?", IdTwitter);
		query.add(" AND status = ?", status);
		
		return obtemUnico(baseJDBC, UserTwitter.class, query, profundidade);
	}
	
	public UserTwitter addUserTwitter(BaseJDBC baseJDBC, Data lancamento, long IdTwitter, String nome, String username, String urlImagemPequena, String urlImagemMedia, String urlImagemGrande, int genero, int status, Usuario usuario) throws Exception
	{
		UserTwitter user = new UserTwitter();
		user.setLancamento(lancamento);
		user.setIdTwitter(IdTwitter);
		user.setNome(nome);
		user.setUsername(username);
		user.setUrlImagemPequena(urlImagemPequena);
		user.setUrlImagemMedia(urlImagemMedia);
		user.setUrlImagemGrande(urlImagemGrande);
		user.setGenero(genero);
		user.setUsuario(usuario);
		user.setStatus(status);
		
		salvar(baseJDBC, user);
		return user; 
	}
}
