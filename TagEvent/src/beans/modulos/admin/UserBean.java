package beans.modulos.admin;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import logs.Logs;
import persistencia.dao.UsuarioDAO;
import beans.estrututra.him.UserHIM;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;

@ManagedBean(name="UserBean")
@RequestScoped
public class UserBean 
{	
	private BaseJDBC baseJDBC = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
	private List<UserHIM> users = new ArrayList<UserHIM>();
	
	public UserBean() 
	{
		atualizar();
	}

	public void atualizar()
	{
		try
		{
			users = new UsuarioDAO().getUsuariosCadastrados(baseJDBC);
		}
		catch (Exception e)
		{
			Logs.addError("UserBean - Atualizar", e);
		}
	}
	
	/*-*-* Getters and Setters *-*-*/
	public List<UserHIM> getUsers() {
		return users;
	}

	public void setUsers(List<UserHIM> users) {
		this.users = users;
	}
	
}
