package persistencia.popula.usuario;

import logs.Logs;
import persistencia.dao.UsuarioDAO;
import persistencia.om.Usuario;
import utils.BDConstantes;
import br.com.mresolucoes.atta.criptografia.AttaCriptografia;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;

public class PopulaUsuario 
{
	public PopulaUsuario(BaseJDBC baseJDBC)
	{
		add(baseJDBC, "ninolodi@gmail.com","123", BDConstantes.TIPO_USUARIO_ROOT, BDConstantesAtta.STATUS_ATIVO);
	}
	
	public boolean add(BaseJDBC baseJDBC, String login, String senha, int tipo, int status)
	{
		try
		{
			Usuario usuario = new Usuario();
			usuario.setLogin(login);
			usuario.setSenha(AttaCriptografia.criptografar(senha));
			usuario.setTipo(tipo);
			usuario.setStatus(status);
			
			new UsuarioDAO().inserir(baseJDBC, usuario);
			
			return true;
		}
		catch(Exception e)
		{
			Logs.addError(e);
			return false;
		}
	}
}
