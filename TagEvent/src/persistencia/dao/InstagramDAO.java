package persistencia.dao;

import java.util.List;

import persistencia.om.Instagram;
import persistencia.om.InstagramConfiguracao;
import persistencia.om.InstagramFoto;
import persistencia.om.UserInstagram;
import persistencia.om.Usuario;
import utils.BDConstantes;
import utils.data.Data;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.persistencia.dao.base.BaseDAOAtta;
import br.com.mresolucoes.atta.persistencia.utils.QuerySQL;
import br.com.mresolucoes.atta.persistencia.utils.ResultSQL;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class InstagramDAO extends BaseDAOAtta
{
	/**
	 * Obtem a configuracao instagram de um evento
	 * @param baseJDBC
	 * @param status
	 * @param profundidade
	 * @return evento
	 * @throws Exception
	 */
	public InstagramConfiguracao getInstagramConfiguracaoEvento(BaseJDBC baseJDBC, long fkEvento, int status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM InstagramConfiguracao");
		query.add(" WHERE fkEvento=?", fkEvento);
		query.add(" AND status=?", status);
		
		return obtemUnico(baseJDBC, InstagramConfiguracao.class, query, profundidade);
	}

	/**
	 * Obtem lista de todos os foursquares ativos de um evento
	 * @param baseJDBC
	 * @param status
	 * @param profundidade
	 * @return eventos
	 * @throws Exception
	 */
	public List<Instagram> getInstagramsEvento(BaseJDBC baseJDBC, long fkEvento, int status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM Instagram");
		query.add(" WHERE fkEvento=?", fkEvento);
		query.add(" AND status=?", status);
		
		return obtem(baseJDBC, Instagram.class, query, profundidade);
	}

	/**
	 * Obtem todas as fotos ativas de um evento
	 * @param baseJDBC
	 * @param status
	 * @param profundidade
	 * @return eventos
	 * @throws Exception
	 */
	public List<InstagramFoto> getFotosInstagram(BaseJDBC baseJDBC, long fkInstagram, int status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM InstagramFoto");
		query.add(" WHERE fkInstagram=?", fkInstagram);
		query.add(" AND status=?", status);
		
		return obtem(baseJDBC, InstagramFoto.class, query, profundidade);
	}

	/**
	 * Obtem todas as fotos ativas de um evento a partir de um determinada data
	 * @param baseJDBC
	 * @param status
	 * @param profundidade
	 * @return eventos
	 * @throws Exception
	 */
	public List<InstagramFoto> getFotosInstagramEventoData(BaseJDBC baseJDBC, long pkEvento, Data data, int[] status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT f.*");
		query.add(" FROM InstagramFoto AS f");
		query.add(" INNER JOIN Instagram AS i ON (f.fkInstagram = i.pkInstagram)");
		query.add(" WHERE f.pkInstagramFoto > 0");
		query.add(" AND f.habilitado >= ?", data);
		query.add(" AND f.status IN (?)", status);
		query.add(" AND i.fkevento = ?", pkEvento);
		query.add(" AND i.status = ?", BDConstantesAtta.STATUS_ATIVO);
		query.add(" ORDER BY f.habilitado DESC");
		
		System.out.println(query.toString(baseJDBC));
		return obtem(baseJDBC, InstagramFoto.class, query, profundidade);
	}
	
	/**
	 * Obtem todas as fotos ativas de um evento
	 * @param baseJDBC
	 * @param status
	 * @param profundidade
	 * @return eventos
	 * @throws Exception
	 */
	public List<InstagramFoto> getFotosInstagramEvento(BaseJDBC baseJDBC, long pkEvento, int[] status, int profundidade, Integer limit, Integer offset) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT f.*");
		query.add(" FROM InstagramFoto AS f");
		query.add(" INNER JOIN Instagram AS i ON (f.fkInstagram = i.pkInstagram)");
		query.add(" WHERE f.pkInstagramFoto > 0");
		query.add(" AND f.status IN (?)", status);
		query.add(" AND i.fkevento = ?", pkEvento);
		query.add(" AND i.status = ?", BDConstantesAtta.STATUS_ATIVO);
		query.add(" ORDER BY f.habilitado DESC");
		if(limit!=null && limit > 0) { query.add(" LIMIT ?", limit); }
		if(offset!=null && offset > 0) { query.add(" OFFSET ?", offset); }
		
		System.out.println(query.toString(baseJDBC));
		return obtem(baseJDBC, InstagramFoto.class, query, profundidade);
	}
	
	/**
	 * Obtem todas as fotos ativas de um evento
	 * @param baseJDBC
	 * @param status
	 * @param profundidade
	 * @return eventos
	 * @throws Exception
	 */
	public List<InstagramFoto> getFotosInstagramHashtag(BaseJDBC baseJDBC, String hashtag, int[] status, int profundidade, Integer limit, Integer offset) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT f.*");
		query.add(" FROM InstagramFoto AS f");
		query.add(" INNER JOIN Instagram AS i ON (f.fkInstagram = i.pkInstagram)");
		query.add(" WHERE f.pkInstagramFoto > 0");
		query.add(" AND f.status IN (?)", status);
		query.add(" AND upper(i.hashtag) = ?", hashtag.toUpperCase());
		query.add(" AND i.status = ?", BDConstantesAtta.STATUS_ATIVO);
		query.add(" ORDER BY f.habilitado DESC");
		if(limit!=null && limit > 0) { query.add(" LIMIT ?", limit); }
		if(offset!=null && offset > 0) { query.add(" OFFSET ?", offset); }
		
		System.out.println(query.toString(baseJDBC));
		return obtem(baseJDBC, InstagramFoto.class, query, profundidade);
	}
	
	/**
	 * Obtem a foto ativa com o id do instagram
	 * @param status
	 * @param profundidad
	 * @return eventos
	 * @throws Exception
	 */
	public InstagramFoto getFotoIdInstagram(BaseJDBC baseJDBC, String idFotoInstagram, long fkInstagram, int[] status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM InstagramFoto");
		query.add(" WHERE pkInstagramFoto>0");
		query.add(" AND idFotoInstagram = ?", idFotoInstagram);
		query.add(" AND fkInstagram = ?", fkInstagram);
		query.add(" AND status IN (?)", status);
		
		return obtemUnico(baseJDBC, InstagramFoto.class, query, profundidade);
	}

	public boolean addInstagramFoto(BaseJDBC baseJDBC, JsonObject fotoInstagram, UserInstagram userInstagram, int classificacao, int statusFoto, Instagram instagram) throws Exception
    {
    	if(fotoInstagram!=null)
    	{
    		InstagramFoto foto = new InstagramFoto();
    		foto.setIdFotoInstagram(fotoInstagram.get("id").getAsString());
    		foto.setUrlImagemPequena(fotoInstagram.get("images").getAsJsonObject().get("thumbnail").getAsJsonObject().get("url").getAsString());
    		foto.setUrlImagemMedia(fotoInstagram.get("images").getAsJsonObject().get("low_resolution").getAsJsonObject().get("url").getAsString());
    		foto.setUrlImagemGrande(fotoInstagram.get("images").getAsJsonObject().get("standard_resolution").getAsJsonObject().get("url").getAsString());
    		
    		if(fotoInstagram.has("videos") && fotoInstagram.get("videos").equals(null))
    		{    			
    			JsonObject video = fotoInstagram.get("videos").getAsJsonObject();
    			foto.setTipo(BDConstantes.TIPO_VIDEO);
    			foto.setUrlVideoPequeno(video.get("low_resolution").getAsJsonObject().get("url").getAsString());
    			foto.setUrlVideoPequeno(video.get("standard_resolution").getAsJsonObject().get("url").getAsString());
    		}
    		else
    		{ foto.setTipo(BDConstantes.TIPO_FOTO); }

    		foto.setLink(fotoInstagram.get("link").getAsString());
    		if(fotoInstagram.has("caption") && fotoInstagram.get("caption").isJsonNull()==false
    			&& fotoInstagram.get("caption").getAsJsonObject().has("text") && fotoInstagram.get("caption").getAsJsonObject().get("text").isJsonNull()==false)
    	    { foto.setTexto(fotoInstagram.get("caption").getAsJsonObject().get("text").getAsString()); }
    		    		
    		long timestamp = fotoInstagram.get("created_time").getAsLong();	// Get the timestamp
    		foto.setData(new Data(timestamp));	// Convert the timestamp
    		foto.setLancamento(new Data());
    		foto.setTempo(timestamp);
    		
    		if(fotoInstagram.has("location") && fotoInstagram.get("location").isJsonNull()==false)
    	    {
    			JsonObject location = fotoInstagram.get("location").getAsJsonObject();
    			if(location!=null)
    			{
    				if(location.has("latitude"))
        			{ foto.setLatitude(location.get("latitude").getAsDouble()); }
        			
        			if(location.has("longitude"))
        			{ foto.setLongitude(location.get("longitude").getAsDouble()); }
        			
        			if(location.has("name") && location.get("name").equals(null))
        			{ foto.setLocalizacao(location.get("name").getAsString()); }
    			}
    		}
    		
    		foto.setStatus(statusFoto);
    		if(statusFoto==BDConstantes.STATUS_ATIVO_LIBERADO)
    		{ foto.setHabilitado(new Data()); }
    		
    		foto.setInstagram(instagram);
    		
    		JsonArray tags = fotoInstagram.get("tags").getAsJsonArray();
    		String tagsText = new String();
    		for(int h=0; h<tags.size(); h++)
    		{ tagsText += tags.get(h) + " | "; }
    		foto.setTags(tagsText);
    		
    		foto.setClassificacao(classificacao);
    		
    		if(userInstagram!=null) { foto.setUserInstagram(userInstagram); }
    		
    		return (new InstagramDAO().salvar(baseJDBC, foto)!=null ? true : false);
    	}
    	return false;
    }
	
	/**
	 * Obtem o user pelo ID do Instagram
	 * @param baseJDBC
	 * @param status
	 * @param profundidade
	 * @return eventos
	 * @throws Exception
	 */
	public UserInstagram getUserIdInstagram(BaseJDBC baseJDBC, String IdInstagram, int status, int profundidade) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT *");
		query.add(" FROM UserInstagram");
		query.add(" WHERE pkUserInstagram>0");
		query.add(" AND idInstagram= ?", IdInstagram);
		query.add(" AND status = ?", status);
		
		return obtemUnico(baseJDBC, UserInstagram.class, query, profundidade);
	}
	
	public UserInstagram addUserInstagram(BaseJDBC baseJDBC, Data lancamento, String idInstagram, String nome, String username, String urlImagemMedia, int genero, int status, Usuario usuario) throws Exception
	{
		UserInstagram user = new UserInstagram();
		user.setLancamento(lancamento);
		user.setIdInstagram(idInstagram);
		user.setNome(nome);
		user.setUsername(username);
		user.setUrlImagemMedia(urlImagemMedia);
		user.setGenero(genero);
		user.setUsuario(usuario);
		user.setStatus(status);
		
		salvar(baseJDBC, user);
		return user;
	}
	
	public ResultSQL getHistogramaEvento(BaseJDBC baseJDBC, long pkEvento, String formatoData) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT to_char(f.habilitado, ?) AS data, COUNT( f.pkInstagramFoto ) AS nrFotos, i.hashtag", formatoData);
		query.add(" FROM InstagramFoto AS f");
		query.add(" INNER JOIN Instagram AS i ON (f.fkInstagram = i.pkInstagram)");
		query.add(" WHERE f.pkInstagramFoto > 0");
		query.add(" AND f.status IN (?)", new int[] {BDConstantesAtta.STATUS_ATIVO, BDConstantes.STATUS_ATIVO_LIBERADO});
		query.add(" AND i.fkevento = ?", pkEvento);
		query.add(" AND i.status = ?", BDConstantesAtta.STATUS_ATIVO);
		query.add(" GROUP BY to_char(f.habilitado, ?), i.hashtag", formatoData);
		query.add(" ORDER BY data ASC, nrFotos DESC, i.hashtag");
		System.out.println(query.toString(baseJDBC));
		return baseJDBC.executaSelect(query);
	}
	
	public ResultSQL getHistogramaInstagram(BaseJDBC baseJDBC, long pkInstagram, String formatoData) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add("SELECT i.hashtag, to_char(f.habilitado, ?) AS data, COUNT( f.pkInstagramFoto ) AS nrFotos", formatoData);
		query.add(" FROM InstagramFoto AS f");
		query.add(" INNER JOIN Instagram AS i ON (f.fkInstagram = i.pkInstagram)");
		query.add(" WHERE f.pkInstagramFoto > 0");
		query.add(" AND f.status IN (?)", new int[] {BDConstantesAtta.STATUS_ATIVO, BDConstantes.STATUS_ATIVO_LIBERADO});
		query.add(" AND i.pkInstagram = ?", pkInstagram);
		query.add(" AND i.status = ?", BDConstantesAtta.STATUS_ATIVO);
		query.add(" GROUP BY i.hashtag, to_char(f.habilitado, ?)", formatoData);
		query.add(" ORDER BY i.hashtag, data ASC, nrFotos DESC");
		System.out.println(query.toString(baseJDBC));
		return baseJDBC.executaSelect(query);
	}
	
	public ResultSQL getDistribuicaoMidiaEvento(BaseJDBC baseJDBC, Long pkEvento) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add(" SELECT i.hashtag, f.tipo, COUNT( f.pkInstagramFoto ) AS nrFotos");
		query.add(" FROM InstagramFoto AS f");
		query.add(" INNER JOIN Instagram AS i ON (f.fkInstagram = i.pkInstagram)"); 
		query.add(" WHERE f.pkInstagramFoto > 0");
		query.add(" AND f.status IN (?)", new int[] {BDConstantesAtta.STATUS_ATIVO, BDConstantes.STATUS_ATIVO_LIBERADO});
		if(pkEvento!=null && pkEvento>0) 	{ query.add(" AND i.fkevento = ?", pkEvento); }
		query.add(" AND i.status = ?", BDConstantesAtta.STATUS_ATIVO);
		query.add(" GROUP BY i.hashtag, f.tipo");
		query.add(" ORDER BY nrFotos DESC, i.hashtag, f.tipo");
		
		System.out.println(query.toString(baseJDBC));
		return baseJDBC.executaSelect(query);
	}
	
	public ResultSQL getSaudeMarcaEvento(BaseJDBC baseJDBC, Long pkEvento, Integer classificacao) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add(" SELECT f.classificacao, COUNT( f.pkInstagramFoto ) AS nrFotos");
		query.add(" FROM InstagramFoto AS f");
		query.add(" INNER JOIN Instagram AS i ON (f.fkInstagram = i.pkInstagram)"); 
		query.add(" WHERE f.pkInstagramFoto > 0");
		query.add(" AND f.status IN (?)", new int[] {BDConstantesAtta.STATUS_ATIVO, BDConstantes.STATUS_ATIVO_LIBERADO});
		if(pkEvento!=null && pkEvento>0) 	{ query.add(" AND i.fkevento = ?", pkEvento); }
		if(classificacao!=null && classificacao>0) 	{ query.add(" AND f.classificacao = ?", classificacao); }
		query.add(" AND i.status = ?", BDConstantesAtta.STATUS_ATIVO);
		query.add(" GROUP BY f.classificacao");
		query.add(" ORDER BY f.classificacao, nrFotos DESC");
		
		System.out.println(query.toString(baseJDBC));
		return baseJDBC.executaSelect(query);
	}
	
	public Long getNrPostTipoEvento(BaseJDBC baseJDBC, Long pkEvento, Integer tipo) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add(" SELECT COUNT( f.pkInstagramFoto ) AS nrPost");
		query.add(" FROM InstagramFoto AS f");
		query.add(" INNER JOIN Instagram AS i ON (f.fkInstagram = i.pkInstagram)"); 
		query.add(" WHERE f.pkInstagramFoto > 0");
		query.add(" AND f.status IN (?)", new int[] {BDConstantesAtta.STATUS_ATIVO, BDConstantes.STATUS_ATIVO_LIBERADO});
		if(pkEvento!=null && pkEvento>0) 	{ query.add(" AND i.fkevento = ?", pkEvento); }
		query.add(" AND i.status = ?", BDConstantesAtta.STATUS_ATIVO);
		if(tipo!=null && tipo>0) { query.add(" AND f.tipo = ?", tipo); }
		
		System.out.println(query.toString(baseJDBC));
		return (Long) (baseJDBC.executaSelect(query)!=null && baseJDBC.executaSelect(query).size()>0 ? baseJDBC.executaSelect(query).get(0).get(0) : 0);
	}
	
//	public Long getTopUsersTipo(BaseJDBC baseJDBC, Long pkEvento, Integer tipo) throws Exception
//	{
//		QuerySQL query = new QuerySQL();
//		
//		query.add(" SELECT COUNT(ui.pkUserInstagram) AS nrUsers");
//		query.add(" FROM InstagramFoto  AS f");
//		query.add(" INNER JOIN Instagram AS i ON (i.pkInstagram = f.fkInstagram)");
//		query.add(" INNER JOIN UserInstagram AS ui ON (f.fkUserInstagram = ui.pkUserInstagram)");
//		query.add(" WHERE f.pkInstagramFoto > 0");
//		query.add(" AND f.status IN (?)", new int[] {BDConstantesAtta.STATUS_ATIVO, BDConstantes.STATUS_ATIVO_LIBERADO});
//		if(pkEvento!=null && pkEvento>0) 	{ query.add(" AND i.fkevento = ?", pkEvento); }
//		query.add(" AND i.status = ?", BDConstantesAtta.STATUS_ATIVO);
//		if(tipo!=null && tipo>0) 			{ query.add(" AND f.tipo = ?", tipo); }
//		query.add(" GROUP BY f.fkUserInstagram");
//		query.add(" ORDER BY nrUsers DESC");
//		
//		System.out.println(query.toString(baseJDBC));
//		return (Long) (baseJDBC.executaSelect(query)!=null && baseJDBC.executaSelect(query).size()>0 ? baseJDBC.executaSelect(query).get(0).get(0) : 0);
//	}
	
	public ResultSQL getTopUsersTipoHashtag(BaseJDBC baseJDBC, Long pkEvento, Integer tipo, Integer limit) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add(" SELECT i.hashtag, COUNT(ui.pkUserInstagram) AS nrUsers");
		query.add(" FROM InstagramFoto  AS f");
		query.add(" INNER JOIN Instagram AS i ON (i.pkInstagram = f.fkInstagram)");
		query.add(" INNER JOIN UserInstagram AS ui ON (f.fkUserInstagram = ui.pkUserInstagram)");
		query.add(" WHERE f.pkInstagramFoto > 0");
		query.add(" AND f.status IN (?)", new int[] {BDConstantesAtta.STATUS_ATIVO, BDConstantes.STATUS_ATIVO_LIBERADO});
		if(pkEvento!=null && pkEvento>0) 	{ query.add(" AND i.fkevento = ?", pkEvento); }
		query.add(" AND i.status = ?", BDConstantesAtta.STATUS_ATIVO);
		if(tipo!=null && tipo>0) 	{ query.add(" AND f.tipo = ?", tipo); }
		query.add(" GROUP BY i.hashtag");
		query.add(" ORDER BY nrUsers DESC");
		if(limit!=null && limit>0) { query.add(" LIMIT ?", limit); }
		
		System.out.println(query.toString(baseJDBC));
		return baseJDBC.executaSelect(query);
	}
	
	
	public ResultSQL getTopPostUserTipoEvento(BaseJDBC baseJDBC, Long pkEvento, Integer tipo, Integer limit) throws Exception
	{
		QuerySQL query = new QuerySQL();
		
		query.add(" SELECT ui.nome, ui.username, ui.urlimagemmedia, COUNT(f.pkInstagramFoto) AS nrPost");
		query.add(" FROM InstagramFoto  AS f");
		query.add(" INNER JOIN Instagram AS i ON (i.pkInstagram = f.fkInstagram)");
		query.add(" INNER JOIN UserInstagram AS ui ON (f.fkUserInstagram = ui.pkUserInstagram)");
		query.add(" WHERE f.pkInstagramFoto > 0");
		query.add(" AND f.status IN (?)", new int[] {BDConstantesAtta.STATUS_ATIVO, BDConstantes.STATUS_ATIVO_LIBERADO});
		if(pkEvento!=null && pkEvento>0) 	{ query.add(" AND i.fkevento = ?", pkEvento); }
		query.add(" AND i.status = ?", BDConstantesAtta.STATUS_ATIVO);
		if(tipo!=null && tipo>0) { query.add(" AND f.tipo = ?", tipo); }
		query.add(" GROUP BY ui.nome, ui.username, ui.urlimagemmedia");
		query.add(" ORDER BY nrPost DESC, ui.nome, ui.username");
		if(limit!=null && limit>0) { query.add(" LIMIT ?", limit); }
		
		System.out.println(query.toString(baseJDBC));
		return baseJDBC.executaSelect(query);
	}
}
