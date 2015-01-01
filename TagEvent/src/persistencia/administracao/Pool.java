package persistencia.administracao;

import logs.Logs;
import br.com.mresolucoes.atta.persistencia.conexao.pool.PoolJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.persistencia.utils.QuerySQL;

/**
 * Classe responsavel conter o pool de conexoes com o banco de dados
 */
public class Pool
{
	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	private static PoolJDBC poolJDBC=null;
	
	/**
	 * Cria o pool de conexoes e inicializa as conexoes
	 */
	public static void conectar(String url, String base, int porta, String login, String senha, int minPool, int maxPoll, int poolgc, int conexaoTimeOut)
	{
		try
		{
			BaseJDBC conexaoBD = new PostgresJDBC(url, base, porta, login, senha, null);
			poolJDBC = new PoolJDBC(conexaoBD, minPool, maxPoll, poolgc, conexaoTimeOut);
			poolJDBC.conectar();
		} 
		catch (Exception e)
		{
			Logs.addFatal("Criando pool de conexoes com o banco de dados", e);
		}
	}
	
	/**
	 * Este metodo ja nasceu errado, ele foi criado apenas para o processo de migracao do atta4 para o atta5
	 * <BR><B>A conexao deve ser liberada apos o uso</B>
	 * @return conexao com o banco de dados
	 */
	public synchronized static BaseJDBC getConexao()
	{
		return (poolJDBC!=null) ? poolJDBC.getConexao(null) : null;
	}
	
	/**
	 * Desconecta todas as conexoes com a base de dados
	 */
	public static void desconectar()
	{
		try
		{
			if(poolJDBC!=null) { poolJDBC.desconectar(); }
		} 
		catch(Exception e)
		{ Logs.addFatal("Fechando as conexoes com a base de dados", e); }
	}
	
	/**
	 * Metodo auxiliar que imprime uma querySQL utilizando uma conexao disponivel
	 * @param querySQL
	 * @return
	 */
	public static String queryToString(QuerySQL querySQL)
	{
		BaseJDBC baseJDBC = getConexao();
		String query = querySQL.toString(baseJDBC);
		baseJDBC.liberarConexao(false);
		return query;
	}
	
	/*-*-*-* Metodos Gets e Sets *-*-*-*/
	public static PoolJDBC getPoolJDBC() { return poolJDBC; }
}