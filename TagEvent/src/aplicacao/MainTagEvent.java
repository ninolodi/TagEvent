package aplicacao;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import periodico.FoursquareBot;
import periodico.GestorEvento;
import periodico.InstagramBot;
import periodico.TwitterBot;
import persistencia.administracao.Pool;
import propriedades.Propriedades;
import logs.Logs;
import utils.Constantes;
import utils.PostUtil;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.periodico.Periodico;
import br.com.mresolucoes.atta.persistencia.conexao.BancoDeDados;


/**
 * Servlet implementation class MainTagEvent
 */
public class MainTagEvent implements Servlet
{
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException
	{
		try
		{

			Locale.setDefault(new Locale("pt", "BR"));

			//Inicializando constantes
			Constantes.inicializar();
			
			//Carrega as configuracoes do sistema
			Configuracoes.carregar(MainTagEvent.class.getResourceAsStream("configuracoes/configuracoes.properties"), MainTagEvent.class.getResourceAsStream("configuracoes/log4j.properties"));
//			Logs.inicializar(MainTagEvent.class.getResourceAsStream("configuracoes/log4j.properties"));
//				
//			Propriedades propriedades = new Propriedades("configuracoes.properties", MainTagEvent.class.getResourceAsStream("configuracoes/configuracoes.properties"));
//			Pool.conectar(propriedades.get("baseUrl"), propriedades.get("baseBase"), propriedades.getInt("basePorta"), propriedades.get("baseLogin"),  propriedades.get("baseSenha"), propriedades.getInt("tamanhoMinPool"), propriedades.getInt("tamanhoMaxPool"), propriedades.getInt("poolgc"), propriedades.getInt("conexaoTimeOut"));
					
			//Inicia a conexao com a base de dados
			BancoDeDados.conectar();

			//Carrega listas de palavras para analises
			PostUtil.inicializar();
			
//			Periodico.getInstance().addTarefa(new PeriodicoUsuarioPortal("05:00"));
			Periodico.getInstance().addTarefa(new InstagramBot(1));
//			Periodico.getInstance().addTarefa(new FoursquareBot(1));
//			Periodico.getInstance().addTarefa(new TwitterBot(1));
//			Periodico.getInstance().addTarefa(new GestorEvento(5));
			
			System.out.println("Sistema TagEvent iniciado (" + Configuracoes.propriedades.get("baseUrl") + ", " + Configuracoes.propriedades.get("baseBase") + ")");
			Logs.addInfo("Sistema TagEvent iniciado (" + Configuracoes.propriedades.get("baseUrl") + ", " + Configuracoes.propriedades.get("baseBase") + ")");
		
		}
		catch(Exception e)
		{ e.printStackTrace(); }
	}

	/**
	 * Metodo que desliga as inicializacoes do metodo iniciarConstantesPropriedadesBancoDados
	 * @throws Exception
	 */
	public static void desligar() throws Exception
	{
		BancoDeDados.desconectar();
		Pool.desconectar();
	}
	
	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() 
	{
		try
		{
			desligar();
		}
		catch(Exception e)
		{
			Logs.addFatal("Falha desativando o sistema", e);
		}
		System.out.println("------- SISTEMA TAGEVENT FINALIZADO COM SUCESSO! ------- ");
	}

	@Override
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}


}
