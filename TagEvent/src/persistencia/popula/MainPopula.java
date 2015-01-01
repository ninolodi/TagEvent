package persistencia.popula;

import persistencia.popula.evento.PopulaEvento;
import persistencia.popula.palavras.PopulaPalavras;
import persistencia.popula.usuario.PopulaUsuario;
import utils.Constantes;
import aplicacao.MainTagEvent;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;

public class MainPopula {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		System.out.println(" ***** POPULA DADOS ***** ");
		
		//Inicializando constantes
		Constantes.inicializar();
		
		//Carrega as configuracoes do sistema
		Configuracoes.carregar(MainTagEvent.class.getResourceAsStream("configuracoes/configuracoes.properties"), MainTagEvent.class.getResourceAsStream("configuracoes/log4j.properties"));
		
		//Inicia a conexao com a base de dados
		BaseJDBC conexaoBD = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
			
		try
		{
			//Popula
//			System.out.println(" -- Popula Usuarios...");
//			new PopulaUsuario(conexaoBD);
//
//			System.out.println(" -- Popula Eventos...");
//			new PopulaEvento(conexaoBD);

			System.out.println(" -- Popula Palavras...");
			new PopulaPalavras(conexaoBD);
			
			conexaoBD.liberarConexao(true);
		}
		catch (Exception e) 
		{
			conexaoBD.liberarConexao(false);
		}
		System.out.println(" ***** FIM POPULA DADOS ***** ");
	}

}
