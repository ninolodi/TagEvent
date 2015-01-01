package periodico;

import java.util.Iterator;
import java.util.List;

import logs.Logs;
import persistencia.dao.EventoDAO;
import persistencia.dao.MonitoramentoAutomaticoDAO;
import persistencia.dao.TwitterDAO;
import persistencia.om.Evento;
import persistencia.om.MonitoramentoAutomatico;
import persistencia.om.Twitter;
import persistencia.om.TwitterTweet;
import persistencia.om.UserTwitter;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import utils.BDConstantes;
import utils.PostUtil;
import utils.data.Data;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.periodico.baseTarefa.TarefaPeriodica;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;

public class TwitterBot extends TarefaPeriodica
{
	private String consumerKey	= "BaBtWNVmVSR1n91vmBJeQ";
	private String consumerSecret = "gkTNimg4YLT5pRu2PcpZDfQKFV81zchoyEfcjGDiXA";
	private String accessToken	= "49614349-2UNpqoXQ9KnXGGt6WaztlvovhtyJFSOLBAfkM1RSk";
	private String accessTokenSecret = "4axpp1hxCjzbzirh0N70saMCluJVMdBbkW1lvJEzSBVyw";
	
	private BaseJDBC baseJDBC = null;
	
	/*-*-*-* Construtores *-*-*-*/
	/**
	 * Construtor
	 * {@link TarefaPeriodica#TarefaPeriodica(int, int, int, int, int, String, boolean)}
	 * @param pkSerieIndicador indica em qual serie os lancamentos dever ser inseridos (necessario cadastrar previamente o indicador e a serie)
	 */
	public TwitterBot(int intervaloTempo)
	{
		super(TEMPORAL, intervaloTempo, true);
		
		//Inicia a conexao com a base de dados
		baseJDBC = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
	}
	
	/*-*-*-* Metodos Publicos *-*-*-*/
	@Override
	public void executarTarefa()
	{	
    	System.out.println("/***** TwitterBot " + new Data().toString("dd/MM/yyyy HH:mm"));
		try 
		{
	    	long fkEvento;
	    	String hashtag;
	        List<Twitter> twitters = null;
	        TwitterDAO twitterDAO = new TwitterDAO();
	        
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey(consumerKey)
			  .setOAuthConsumerSecret(consumerSecret)
			  .setOAuthAccessToken(accessToken)
			  .setOAuthAccessTokenSecret(accessTokenSecret);
	        
	        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
	        twitter4j.Twitter appTwitter = twitterFactory.getInstance();
	        Query query = null;
	        QueryResult result = null;
	        Status tweet = null;
	      	        
	        boolean monitoramentoEvento;
	        int statusMonitoramento = 0;
	        int statusTweet;
	        String username = null;
			
			MonitoramentoAutomatico monitoramentoLiberado = null;
			MonitoramentoAutomaticoDAO monitoramentoDAO = new MonitoramentoAutomaticoDAO();
			
			List<Evento> eventos = new EventoDAO().getTodosEventos(baseJDBC, BDConstantesAtta.STATUS_ATIVO, 0);
			
			Data data = new Data();	
			int classificacao = BDConstantes.TIPO_POST_INDEFINIDO;
	    	Evento evento = null;
	    	UserTwitter userTwitter = null;
	    	Twitter twitter = null;
	    	TwitterTweet twitterTweet = null;
	    	for(int i = 0; i<eventos.size(); i++)
	    	{
	    		evento = eventos.get(i);

	    		//Informacoes do evento
	    		fkEvento = evento.getPkEvento();
	    		
	    		//Pega todos os Twitters de um determinado evento
	    		twitters = twitterDAO.getTwittersEvento(baseJDBC, fkEvento, BDConstantesAtta.STATUS_ATIVO, -1);
	    		
		    	for(int j = 0; j<twitters.size(); j++)
		    	{
		    		twitter = twitters.get(j);
		    		
		    		hashtag = twitter.getHashtag();
		    		monitoramentoEvento = twitter.getMonitoramento();
		    		statusMonitoramento = (monitoramentoEvento==true ? BDConstantes.STATUS_MONITORAMENTO : BDConstantes.STATUS_ATIVO_LIBERADO);
		    				    		
		    		query = new Query(hashtag);
//		    		query.setSince(data.toString("yyyy-MM-dd"));
					query.setCount(100);
		    		
		    		result = appTwitter.search(query);
		    		List<Status> tweets = result.getTweets();
		    		
		    		Iterator<Status> itTweet = tweets.iterator();
		    		
		    		while(itTweet.hasNext())
		    		{
		    			tweet = itTweet.next();
		    			statusTweet = statusMonitoramento;
		    			monitoramentoLiberado = null;
		    			userTwitter = null;
//				    			System.out.println(tweet.getUser().getName() + " - " + tweet.getText());
		    			
		    			//ID do tweet
		    			long idTweetTwitter = tweet.getId();

		    			//Busca o tweet no sistema TagEvent
		    			int[] status = new int[] {BDConstantesAtta.STATUS_ATIVO, BDConstantes.STATUS_ATIVO_LIBERADO, BDConstantes.STATUS_BLOQUEADO, BDConstantesAtta.STATUS_REMOVIDO}; 
		    			if(monitoramentoEvento==true)
		    			{ status = new int[] {BDConstantesAtta.STATUS_ATIVO, BDConstantes.STATUS_ATIVO_LIBERADO, BDConstantes.STATUS_MONITORAMENTO, BDConstantes.STATUS_BLOQUEADO, BDConstantesAtta.STATUS_REMOVIDO}; }
		    			twitterTweet = twitterDAO.getTweetIdTwitter(baseJDBC, idTweetTwitter, twitter.getPkTwitter(), status, -1);
				         
				         if(twitterTweet==null)
				         {
		    				username = tweet.getUser().getScreenName();
		    				monitoramentoLiberado = monitoramentoDAO.getUserMonitoramento(baseJDBC, username, twitter, BDConstantesAtta.STATUS_ATIVO, -1);
		    				if(statusMonitoramento == BDConstantes.STATUS_MONITORAMENTO && monitoramentoLiberado!=null)
		    				{ statusTweet = BDConstantes.STATUS_ATIVO_LIBERADO; } 
		    				
		    				userTwitter = twitterDAO.getUserIdTwitter(baseJDBC, tweet.getUser().getId(), BDConstantesAtta.STATUS_ATIVO, -1);
		    				if(userTwitter==null)
		        			{ userTwitter = twitterDAO.addUserTwitter(baseJDBC, data, tweet.getUser().getId(), tweet.getUser().getName(), tweet.getUser().getScreenName(), tweet.getUser().getMiniProfileImageURL(), tweet.getUser().getOriginalProfileImageURL(), tweet.getUser().getBiggerProfileImageURL(), BDConstantes.TIPO_GENERO_INDEFINIDO, BDConstantesAtta.STATUS_ATIVO, null); }
		    				
		    				classificacao = PostUtil.analisador(tweet.getText());
			        				    				
				        	if(twitterDAO.addTwitterTweet(baseJDBC, data, new Data(tweet.getCreatedAt()), tweet.getId(), tweet.getText(), classificacao, statusTweet, twitter, userTwitter)==false)
			        		{
				        		 throw new Exception("TwitterBot - Falha inserindo um tweet do Twitter");
			        		}
				         }
		    		}
		    	}
	    	}
	    	
	    	baseJDBC.liberarConexao(true);
		}
		catch (TwitterException e) 
		{
			baseJDBC.liberarConexao(false);
			System.out.println("/***** [SEM CONEXÃO COM A INTERNET] TwitterBot " + new Data().toString("dd/MM/yyyy HH:mm"));
		}
		catch(Exception e) 
		{ 
			baseJDBC.liberarConexao(false);
			Logs.addFatal("TwitterBot - Falha atualizando tweets do Twitter", e); 
		}
	}
}