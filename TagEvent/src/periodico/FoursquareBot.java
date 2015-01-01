package periodico;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import logs.Logs;
import persistencia.dao.EventoDAO;
import persistencia.dao.FoursquareDAO;
import persistencia.om.Evento;
import persistencia.om.Foursquare;
import persistencia.om.FoursquareCheckin;
import persistencia.om.FoursquareHereNow;
import persistencia.om.UserFoursquare;
import utils.BDConstantes;
import utils.PostUtil;
import utils.data.Data;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.periodico.baseTarefa.TarefaPeriodica;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FoursquareBot extends TarefaPeriodica
{
	private String accessToken = "0WSHBT0FHCC3Q5VRR0DKQLBZITCMEIFFFLS0WTEVMTBNSMIJ";
//	private String clientID = "RKXOS5OAAPDQJU3BVAC3TAQAOZEXKIA3GZKLVIYIPWPMM0XL";
//	private String clientSecret = "1IZP442UO4CTPFPCYIRNFLKXJCQOXLAR54CF0VUIE04FQDTD";
//	private String callbackURL = "http://www.tagevent.com.br/notificacoes/foursquare";
	
	private BaseJDBC baseJDBC = null;
	
	/*-*-*-* Construtores *-*-*-*/
	/**
	 * Construtor
	 * {@link TarefaPeriodica#TarefaPeriodica(int, int, int, int, int, String, boolean)}
	 * @param pkSerieIndicador indica em qual serie os lancamentos dever ser inseridos (necessario cadastrar previamente o indicador e a serie)
	 */
	public FoursquareBot(int intervaloTempo)
	{
		super(TEMPORAL, intervaloTempo, true);
		
		//Inicia a conexao com a base de dados
		baseJDBC = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
	}
	
	/*-*-*-* Metodos Publicos *-*-*-*/
	@Override
	public void executarTarefa()
	{
		System.out.println("/***** FoursquareBot " + new Data().toString("dd/MM/yyyy HH:mm"));
		try 
		{
	    	long fkEvento;
	    	String idVenue;
	    	String getCheckinsURL;
	    	URL url;
	    	InputStreamReader inputReader;
	    	BufferedReader bufferedReader;
	    	String linha;
	    	StringBuffer sb;
	    	JsonParser parser;
	        JsonElement element;
	        JsonObject result;
	        JsonArray itens;
	        JsonObject item;
	        JsonObject user;
	        FoursquareDAO foursquareDAO = new FoursquareDAO();
	    	
			List<Evento> eventos = new EventoDAO().getTodosEventos(baseJDBC, BDConstantesAtta.STATUS_ATIVO, 0);
			
			Data data = new Data();
			String id = null;
			int genero = 0;
			String post = null;
			String idCheckinFoursquare = null;
			int classificacao = BDConstantes.TIPO_POST_INDEFINIDO;
			Evento evento = null;
	    	Foursquare foursquare;
	    	UserFoursquare userFoursquare = null;
	    	FoursquareHereNow checkinHereNow;
	    	FoursquareCheckin checkin;
	    	List<Foursquare> foursquares = null;
	    	int quantidadeAntiga = 0;
	    	int quantidadeNova = 0;
	    	for(int i = 0; i<eventos.size(); i++)
	    	{
	    		evento = eventos.get(i);

	    		//Informacoes do evento
	    		fkEvento = evento.getPkEvento();
	    		
	    		//Pega todos os foursquares de um determinado evento
	    		foursquares = foursquareDAO.getFoursquaresEvento(baseJDBC, fkEvento, BDConstantesAtta.STATUS_ATIVO, -1);
	    		
		    	for(int j = 0; j<foursquares.size(); j++)
		    	{
		    		foursquare = foursquares.get(j);
		    		
		    		idVenue = foursquare.getIdVenue();
		    		//URL de imagens do Instagram
		    		getCheckinsURL = "https://api.foursquare.com/v2/venues/"+idVenue+"/herenow?oauth_token="+accessToken+"&v="+data.toString("YYYYMMDD");
		    		
		    		url = new URL(getCheckinsURL);
					//Cria um stream de entrada do conteúdo
					inputReader = new InputStreamReader( url.openStream() );
					bufferedReader = new BufferedReader( inputReader );
		 
					linha = "";
					sb = new StringBuffer();
					while((linha = bufferedReader.readLine()) != null)
					{ sb.append(linha); }
					
					parser = new JsonParser();
			        element = parser.parse(sb.toString());
			        if (element.isJsonObject()) 
			        {
			        	 result = element.getAsJsonObject();
				         quantidadeNova = result.get("response").getAsJsonObject().get("hereNow").getAsJsonObject().get("count").getAsInt();
				         
				         checkinHereNow = foursquareDAO.getUltimoCheckin(baseJDBC, foursquare.getPkFoursquare(), BDConstantesAtta.STATUS_ATIVO, -1);
				         
				         if(checkinHereNow!=null)
				         {
				        	 quantidadeAntiga = checkinHereNow.getQuatidadeNova();
				        	 if(quantidadeNova!=quantidadeAntiga)
					         {  
				        		 if(foursquareDAO.addCheckinHereNow(baseJDBC, data, quantidadeAntiga, quantidadeNova, BDConstantesAtta.STATUS_ATIVO, BDConstantes.TIPO_CHECKIN_HERE_NOW, foursquare)==false)
			        			 {
				        			 throw new Exception("FoursquareBot - Falha inserindo checkin de atualizacao do Foursquare");
			        			 }
				        	 }
				         }
				         else
				         {
				        	 if(foursquareDAO.addCheckinHereNow(baseJDBC, data, 0, quantidadeNova, BDConstantesAtta.STATUS_ATIVO, BDConstantes.TIPO_CHECKIN_HERE_NOW, foursquare)==false)
		        			 {
			        			 throw new Exception("FoursquareBot - Falha inserindo checkin inicial do Foursquare");
		        			 }
				         }
				         
				         if(result.get("response").getAsJsonObject().get("hereNow").getAsJsonObject().get("items").isJsonArray() && result.get("response").getAsJsonObject().get("hereNow").getAsJsonObject().get("items").getAsJsonArray().isJsonNull()==false)
				         {
				        	itens = result.get("response").getAsJsonObject().get("hereNow").getAsJsonObject().get("items").getAsJsonArray();
					         
					        for (int k = 0; k < itens.size(); k++) 
					        {
					        	item = itens.get(k).getAsJsonObject();

				    			//ID do checkin
				    			idCheckinFoursquare = item.get("id").getAsString();
				    			
				    			//Busca o checkin no sistema TagEvent
				    			int[] status = new int[] {BDConstantesAtta.STATUS_ATIVO, BDConstantes.STATUS_ATIVO_LIBERADO, BDConstantes.STATUS_BLOQUEADO, BDConstantesAtta.STATUS_REMOVIDO}; 
				    			checkin = foursquareDAO.getCheckinIdFoursquare(baseJDBC, idCheckinFoursquare, foursquare.getPkFoursquare(), status, -1);
					        	
					        	if(checkin==null)
					        	{
					        		userFoursquare = null;
					        		user = null;
					        		if(item.has("user") && item.get("user").isJsonNull()==false)
					        		{ 
					        			user = item.get("user").getAsJsonObject();
					        			id = (user.has("id") && user.get("id").isJsonNull()==false ? user.get("id").getAsString() : null);
					        			genero = (user.has("gender") && user.get("gender").isJsonNull()==false ? (user.get("gender").getAsString().equals("male") ? BDConstantes.TIPO_GENERO_MASCULINO : BDConstantes.TIPO_GENERO_FEMININO) : BDConstantes.TIPO_GENERO_INDEFINIDO);
					        			
					        			if(id!=null)
					        			{
						        			userFoursquare = foursquareDAO.getUserIdFoursquare(baseJDBC, id, BDConstantesAtta.STATUS_ATIVO, -1);
						        			if(userFoursquare==null)
						        			{ userFoursquare = foursquareDAO.addUserFousquare(baseJDBC, data, id, user.get("firstName").getAsString() + " " + user.get("lastName").getAsString(), genero, BDConstantesAtta.STATUS_ATIVO, null); }
					        			}
					        		}
					        		
					        		post = (item.has("shout") && item.get("shout").isJsonNull()==false ? item.get("shout").getAsString() : "-");
					        		classificacao = PostUtil.analisador(post);
					        		
					        		if(foursquareDAO.addCheckin(baseJDBC, data, new Data(item.get("createdAt").getAsLong()), item.get("id").getAsString(), post, classificacao, BDConstantesAtta.STATUS_ATIVO, foursquare, userFoursquare)==false)
					        		{
						        		throw new Exception("FoursquareBot - Falha inserindo checkin de atualizacao do Foursquare");
					        		}
					        	}
					        }
				        }
				        else
				        { itens = null; }
			    	}
		    	}
	    	}
	    	
	    	baseJDBC.liberarConexao(true);
		}
		catch(UnknownHostException e) 
		{ 
			baseJDBC.liberarConexao(false);
			System.out.println("/***** [SEM CONEXÃO COM A INTERNET] FoursquareBot " + new Data().toString("dd/MM/yyyy HH:mm"));
		}
		catch(IOException e) 
		{ 
			baseJDBC.liberarConexao(false);
			System.out.println("/***** [SEM CONEXÃO SERVIDOR] FoursquareBot " + new Data().toString("dd/MM/yyyy HH:mm"));
		}
		catch(Exception e) 
		{ 
			baseJDBC.liberarConexao(false);
			Logs.addFatal("FoursquareBot - Falha atualizando checkins do Foursquare", e); 
		}
	}
}