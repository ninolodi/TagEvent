package persistencia.dao;

import java.util.List;

import persistencia.om.Usuario;
import utils.BDConstantes;
import beans.estrututra.him.UserHIM;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.persistencia.dao.base.BaseDAOAtta;
import br.com.mresolucoes.atta.persistencia.utils.QuerySQL;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;
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
	
	/**
	 * Obtem uma lista com todos os usuarios cadastrados na base de dados
	 * @param login
	 * @param profundidade
	 * @return primeiro usuario que possui o login
	 * @throws Exception
	 */
	public List<UserHIM> getUsuariosCadastrados(BaseJDBC baseJDBC) throws Exception
	{		
		QuerySQL query = new QuerySQL();
		query.add("SELECT"); 
		query.add(" (CASE WHEN u.status=? THEN 'Ativo' WHEN u.status=? THEN 'Removido' ELSE 'Indefinido' END) AS status,", BDConstantesAtta.STATUS_ATIVO, BDConstantesAtta.STATUS_REMOVIDO);
		query.add(" u.login AS login,");
		query.add(" (CASE WHEN u.tipo=? THEN 'Root' WHEN u.tipo=? THEN 'Admin' ELSE 'Normal' END) AS tipo,", BDConstantes.TIPO_USUARIO_ROOT, BDConstantes.TIPO_USUARIO_ADMIN);
		query.add(" COALESCE(pf.nome, pj.nomefantasia, '-') AS nome,");
		query.add(" COALESCE(CAST(pf.nascimento AS varchar), '-') AS nascimento,");
		query.add(" COALESCE(pf.telefone, '-') AS telefone, ");
		query.add(" COALESCE(CAST(u.lancamento AS varchar), '-') AS cadastrado,");
		query.add(" COALESCE(CAST(u.ultimoacesso AS varchar), '-') AS ultimoAcesso,"); 
		query.add(" COALESCE(tabAcesso.qtd, 0) AS nrAcessos");
		query.add(" FROM Usuario AS u");
		query.add(" LEFT OUTER JOIN PessoaFisica AS pf ON (pf.fkUsuario = u.pkUsuario)");
		query.add(" LEFT OUTER JOIN PessoaJuridica AS pj ON (pj.fkUsuario = u.pkUsuario)");
		query.add(" LEFT OUTER JOIN (SELECT fkUsuario AS pk, COUNT(pkAcesso) AS qtd FROM Acesso GROUP BY fkUsuario) AS tabAcesso ON (tabAcesso.pk = u.pkUsuario)");
		query.add(" ORDER BY nome, login, nascimento");
		return obtem(baseJDBC, UserHIM.class, query, -1);
	}
}
