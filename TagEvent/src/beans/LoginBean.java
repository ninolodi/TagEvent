package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import logs.Logs;
import persistencia.dao.UsuarioDAO;
import persistencia.om.Usuario;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.criptografia.AttaCriptografia;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.persistencia.dao.base.BaseDAOAtta;
import br.com.mresolucoes.atta.persistencia.utils.QuerySQL;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;

@ManagedBean(name="LoginBean")
@SessionScoped
public class LoginBean
{
	private String login = "";
	private String senha = "";
	private boolean falhaLogar = false;
	
	public LoginBean() { }

	public String login()
	{
		try
		{
			BaseJDBC baseJDBC = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
//			BaseJDBC baseJDBC = BancoDeDados.getConexao();
			
			String senhaCript = AttaCriptografia.criptografar(senha.trim());
			
			QuerySQL query = new QuerySQL();
			
			query.add("SELECT *");
			query.add(" FROM usuario");
			query.add(" WHERE UPPER(login)=UPPER(?)", login.trim());
			query.add(" AND senha=?", senhaCript);
			query.add(" AND status=?", BDConstantesAtta.STATUS_ATIVO);
			Usuario usuario = new UsuarioDAO().obtemUnico(baseJDBC, Usuario.class, query, 2, null, BaseDAOAtta.CONSIDERAR_CLASSES); 
			
			login = "";
			senha = "";
			
			if(usuario==null)
			{ 
				falhaLogar = true;
				return "login";
			}
			
		}
		catch (Exception e)
		{
			falhaLogar = true;
			Logs.addError("LoginBean - Construtor", e);
//			redirecionarErro(e);
		}
		
		return "welcome?faces-redirect=true";
	}
	
	public void logout()
	{
		try
		{
//			encerrarSessao();
		}
		catch (Exception e)
		{
			falhaLogar = true;
			Logs.addError("LoginBean - Logout", e);
//			redirecionarErro(e);
		}
	}
	
	public boolean isFalhaLogar() {
		return falhaLogar;
	}

	/*-*-* Getters and Setters *-*-*/
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
