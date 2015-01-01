package aplicacao.execucoes;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import utils.Constantes;
import aplicacao.MainTagEvent;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;

public class TesteTwitter {
	private static String consumerKey	= "BaBtWNVmVSR1n91vmBJeQ";
	private static String consumerSecret = "gkTNimg4YLT5pRu2PcpZDfQKFV81zchoyEfcjGDiXA";
	private static String accessToken	= "49614349-2UNpqoXQ9KnXGGt6WaztlvovhtyJFSOLBAfkM1RSk";
	private static String accessTokenSecret = "4axpp1hxCjzbzirh0N70saMCluJVMdBbkW1lvJEzSBVyw";
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try 
		{
			Locale.setDefault(new Locale("pt", "BR"));

			//Inicializando constantes
			Constantes.inicializar();
			
			//Carrega as configuracoes do sistema
			Configuracoes.carregar(MainTagEvent.class.getResourceAsStream("configuracoes/configuracoes.properties"), MainTagEvent.class.getResourceAsStream("configuracoes/log4j.properties"));
		
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey(consumerKey)
			  .setOAuthConsumerSecret(consumerSecret)
			  .setOAuthAccessToken(accessToken)
			  .setOAuthAccessTokenSecret(accessTokenSecret);

			TwitterFactory twitterFactory = new TwitterFactory(cb.build());
			Twitter appTwitter = twitterFactory.getInstance();
			Status tweet;
			
			Query query = new Query("SuperBowlXLVIII");
//			query.setSince(new Data().toString("YYYY-MM-DD"));
			query.setCount(1000);
						
			QueryResult result = appTwitter.search(query);
			List<Status> tweets = result.getTweets();
			
			Iterator<Status> itTweet = tweets.iterator();
			
			while(itTweet.hasNext())
			{
				tweet = itTweet.next();
				System.out.println(tweet.getUser().getName() + " - " + tweet.getUser().getScreenName() + " - " + tweet.getUser().getProfileImageURL() + " - " + tweet.getUser().getBiggerProfileImageURL() + " - " + tweet.getText());
			}
		
		} catch (Exception e) 
		{
			e.printStackTrace();
			System.out.println("ERRO");
		}
		System.exit(0);
	}

}
