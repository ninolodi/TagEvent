package beans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import logs.Logs;
import persistencia.administracao.Pool;
import persistencia.dao.UsuarioDAO;
import persistencia.om.Usuario;
import utils.BDConstantes;
import utils.Constantes;
import utils.Permissoes;
import utils.SConstantes;
import beans.aplicacao.Contexto;
import beans.aplicacao.PermissaoBean;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;
import criptografia.CriptografiaRC4;

@ManagedBean(name="LoginBeanVnoc")
@SessionScoped
public class LoginBeanVnoc implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String login = "";
	private String senha = "";
	private String loginRecuperarSenha = "";

	/**
	 * Metodo que verifica o login e senha para logar o usuario
	 * @return
	 */
	public String logar()
	{
		try
		{
			BaseJDBC baseJDBC = Pool.getConexao();
			if(verificarConsistencia())
			{
				try
				{
//					Usuario usuario = new UsuarioDAO().getUsuario(baseJDBC, login, CriptografiaRC4.criptografar(senha, Constantes.CRIP_KEY_ACESS));
//
//					if(usuario!=null && usuario.getStatus()!=BDConstantesAtta.STATUS_REMOVIDO)
//					{
//						if(PermissaoBean.possuiPermissao(usuario, Permissoes.PERMISSAO_LOGAR))
//						{
//							new Contexto().putSessionObject(SConstantes.USUARIO_LOGADO, usuario);
//							return "pages/principal/calendario?faces-redirect=true";
//						}
//						else
//						{
//							new Contexto().addMensagem(null, FacesMessage.SEVERITY_ERROR, "login.invalido");
//						}
//					}
//					else
//					{
//						new Contexto().addMensagem(null, FacesMessage.SEVERITY_ERROR, "login.invalido");
//					}
				}
				catch(Exception e1)
				{
					Logs.addError("Falha tentando obter usuario para login, usuario: " + login, e1);
					new Contexto().addMensagem(null, FacesMessage.SEVERITY_ERROR, "login.usuario.erro");
				}
			}
			baseJDBC.liberarConexao(false);
		}
		catch (Exception e) 
		{ 
			Logs.addError("Erro salvando registro", e);
			new Contexto().addMensagem(null, FacesMessage.SEVERITY_ERROR, "sistema.erro.operacao");
		}
		return null;
	}
	
	/**
	 * Verifica a consistencia dos campos
	 * @param baseJDBC
	 * @return
	 * @throws Exception
	 */
	public boolean verificarConsistencia() throws Exception
	{
		boolean sucesso = true;
		if(login==null || login.trim().length()<=0)
		{
			new Contexto().addMensagem(null, new FacesMessage(FacesMessage.SEVERITY_WARN, new Contexto().getMsg("cadastro.msg.campo.obrigatorio") + " " + new Contexto().getMsg("login.login"), new Contexto().getMsg("cadastro.msg.campo.obrigatorio")));
			sucesso=false;
		}
		if(senha==null || senha.trim().length()<=0)
		{
			new Contexto().addMensagem(null, new FacesMessage(FacesMessage.SEVERITY_WARN, new Contexto().getMsg("cadastro.msg.campo.obrigatorio") + " " + new Contexto().getMsg("login.senha"), new Contexto().getMsg("cadastro.msg.campo.obrigatorio")));
			sucesso=false;
		}
		return sucesso;
	}
	
	/**
	 * Metodo que encerra a cessao e desloga o usuario
	 * @return
	 */
	public String logout()
	{
		try
		{
			new Contexto().putSessionObject(SConstantes.USUARIO_LOGADO, null);
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession)facesContext.getExternalContext().getSession(false);  
			session.invalidate();
			FacesContext.getCurrentInstance().getExternalContext().redirect("/TagEvent/login.xhtml");
		}
		catch (Exception e) 
		{ Logs.addError("Falha fazendo o logout, usuario: " + login, e); }
		return null;
	}
	
	
	//TODO Nao implementado
	public String recuperarSenha()
	{
		try
		{
			System.out.println("Aqui no java !!! REC222");
			
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.addMessage("recuperarSenhaForm", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conjunto login/senha incorretos", "descricao"));
			return null;
		}
		catch (Exception e) 
		{ Logs.addError("Falha recuperando senha, usuario: " + login, e); }
		return null;
	}
	
	public String getLoginRecuperarSenha() { return loginRecuperarSenha; }
	public void setLoginRecuperarSenha(String loginRecuperarSenha) { this.loginRecuperarSenha = loginRecuperarSenha; }
	
	public String getLogin() { return login; }
	public void setLogin(String login) { this.login = login; }

	public String getSenha() { return senha; }
	public void setSenha(String senha) { this.senha = senha; }
}