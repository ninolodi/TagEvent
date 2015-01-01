package persistencia.administracao;

import logs.Logs;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.persistencia.conexao.pool.PoolJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.persistencia.utils.QuerySQL;

/**
 * Classe responsavel conter o pool de conexoes com o banco de dados
 */
public class BancoDeDados
{
	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	private static PoolJDBC poolJDBC=null;
	
	/**
	 * Cria o pool de conexoes e inicializa as conexoes
	 */
	public static void conectar()
	{
		try
		{
 			BaseJDBC conexaoBD = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
			poolJDBC = new PoolJDBC(conexaoBD, Configuracoes.propriedades.getInt("tamanhoMinPool"), Configuracoes.propriedades.getInt("tamanhoMaxPool"), Configuracoes.propriedades.getInt("poolgc"), Configuracoes.propriedades.getInt("conexaoTimeOut"));
			poolJDBC.conectar();
		} 
		catch (Exception e)
		{
			Logs.addFatal("Criando pool de conexoes com o banco de dados", e);
		}
	}
	
	/**
	 * Cria o pool de conexoes e inicializa as conexoes
	 */
	public static void conectar(String baseUrl, String baseBase, int basePorta, String baseLogin, String baseSenha, int minPool, int maxPool, int poolgc, int timeOut)
	{
		try
		{
			BaseJDBC conexaoBD = new PostgresJDBC(baseUrl, baseBase, basePorta, baseLogin, baseSenha, null);
			poolJDBC = new PoolJDBC(conexaoBD, minPool, maxPool, poolgc, timeOut);
			poolJDBC.conectar();
		} 
		catch (Exception e)
		{
			Logs.addFatal("Criando pool de conexoes com o banco de dados", e);
		}
	}
	
	/**
	 * Metodo que retorna uma conexao para ser utilizada
	 * <BR><B>A conexao deve ser liberada apos o uso</B>
	 * @return conexao com o banco de dados
	 */
	public static PostgresJDBC getConexao()
	{
		return (poolJDBC!=null) ? (PostgresJDBC)poolJDBC.getConexao(null) : null;
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
		{
			Logs.addFatal("Fechando as conexoes com a base de dados", e);
		}
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
