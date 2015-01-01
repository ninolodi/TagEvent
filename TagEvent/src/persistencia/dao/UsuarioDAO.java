package persistencia.dao;

import persistencia.om.Usuario;
import br.com.mresolucoes.atta.persistencia.dao.base.BaseDAOAtta;
import br.com.mresolucoes.atta.persistencia.utils.QuerySQL;
import br.com.mresolucoes.atta.utils.ConstantesAtta;
import criptografia.CriptografiaSimples;


public class UsuarioDAO extends BaseDAOAtta
{
	/**
	 * Obtem usuario e sua lista de permissoes (na lista de permissoes a variavel Usuario e ignorada)
	 * @author Flacker
	 * @param login do usuario
	 * @param senha do usuario nao criptografada
	 * @param status
	 * @param profundidadeUsuario para obtencao do usuario
	 * @param profundidadePermissao para a obtencao das permissoes do usuario
	 * @return usuario com sua lista de permissoes
	 * @throws Exception
	 */
	public Object getUsuarioLogin(String login, String senha, int status) throws Exception
	{
		QuerySQL query = new QuerySQL();
		String senhaCript = CriptografiaSimples.criptografar(senha, ConstantesAtta.CRIPTOGRAFIA_MISTURA);

		query.add("SELECT *");
		query.add(" FROM usuario");
		query.add(" WHERE UPPER(login)=UPPER(?)", login.trim());
		query.add(" AND senha=?", senhaCript.trim());
		query.add(" AND status=?", status);
		
		Usuario usuario = (Usuario)obtemUnico(Usuario.class, query, 0);
		
		return usuario;
	}
		
	/**
	 * Obtem um usuario atraves de seu login, caso existam mais de um login apenas o primeiro encontrado sera retornado
	 * @param login
	 * @param profundidade
	 * @return primeiro usuario que possui o login
	 * @throws Exception
	 */
	public Usuario getUsuario(String login, int profundidade) throws Exception
	{		
		QuerySQL query = new QuerySQL();
		query.add("SELECT *");
		query.add(" FROM usuario");
		query.add(" WHERE login=?", login);
		return (Usuario)obtemUnico(Usuario.class, query, profundidade);
	}
	
	/**
	 * Altera a senha do usuario atraves do seu login, caso existam mais de 1 login igual nada sera efetuado
 	 * @author Flacker
 	 * @param login do usuario
 	 * @param senha do usuario nao criptografada
	 * @return numero de linhas afetadas independente do resultado da operacao, que sera sucesso apenas caso o nuemro de linhas seja 1
	 */
	public int alterarSenhaLogin(String login, String senha) throws Exception
	{
		try
		{
			String senhaCript = CriptografiaSimples.criptografar(senha, ConstantesAtta.CRIPTOGRAFIA_MISTURA);
			
			QuerySQL query = new QuerySQL();
			query.add("UPDATE usuario");
			query.add(" SET senha=?", senhaCript);
			query.add(" WHERE login=?", login);
			int linhasAfetadas = alterarSQL(query);
		
			return linhasAfetadas;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
}
