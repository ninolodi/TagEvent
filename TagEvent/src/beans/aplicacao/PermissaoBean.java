package beans.aplicacao;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import persistencia.om.Usuario;
import utils.BDConstantes;
import utils.SConstantes;

@ManagedBean(name="PermissaoBean")
@SessionScoped
public class PermissaoBean implements Serializable
{
	/*-*-*-* Constante de Serializacao *-*-*-*/
	private static final long serialVersionUID = 1L;
	
	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
		

	/*-*-*-* Metodos Publicos *-*-*-*/
	/**
	 * tipoRetorno:
	 * <BR>0- "" ou "none"
	 * @param tipoRetorno
	 * @param permissao
	 * @return
	 */
	public String permissao(int tipoRetorno, String permissao)
	{
		Usuario usuario = (Usuario)new Contexto().getSessionObject(SConstantes.USUARIO_LOGADO);
		if(usuario!=null)
		{
//			if(permissao.equals("menu.cadastro"))
//			{
//				if(possuiPermissao(usuario, BDConstantes.PERMISSAO_CADASTROS) || possuiPermissao(usuario, BDConstantes.PERMISSAO_CADASTRO_USUARIO))
//				{
//					return "";
//				}
//			}
//			else if(permissao.equals("menu.cadastro.usuario"))
//			{
//				if(possuiPermissao(usuario, BDConstantes.PERMISSAO_CADASTRO_USUARIO))
//				{
//					return "";
//				}
//			}
//			else if(permissao.equals("menu.conferencia.agendamento"))
//			{
//				if(possuiPermissao(usuario, BDConstantes.PERMISSAO_AGENDAMENTO_CONFERENCIA))
//				{
//					return "";
//				}
//			}
//			else if(permissao.equals("menu.conferencia.acompanhamento"))
//			{
//				if(possuiPermissao(usuario, BDConstantes.PERMISSAO_ACOMPANHAMENTO_CONFERENCIA))
//				{
//					return "";
//				}
//			}
//			else if(permissao.equals("menu.relatorios"))
//			{
//				if(possuiPermissao(usuario, BDConstantes.PERMISSAO_RELATORIOS))
//				{
//					return "";
//				}
//			}
//			else if(permissao.equals("todas.empresas"))
//			{
//				if(possuiPermissao(usuario, BDConstantes.PERMISSAO_TODAS_EMPRESAS))
//				{
//					return "";
//				}
//			}
		}
		return "none";
	}
	
	public boolean permissaoJava(int permissao)
	{
		Usuario usuario = (Usuario)new Contexto().getSessionObject(SConstantes.USUARIO_LOGADO);
		if(usuario!=null)
		{
			return possuiPermissao(usuario, permissao);
		}
		return false;
	}
	
	public static synchronized boolean possuiPermissao(Usuario usuario, int permissao)
	{
//		for(int i=0; i<usuario.getPermissaoUsuarios().size(); i++)
//		{
//			if(usuario.getPermissaoUsuarios().get(i).getPermissao()==permissao || usuario.getPermissaoUsuarios().get(i).getPermissao()==BDConstantes.PERMISSAO_ROOT)
//			{
//				return true;
//			}
//		}
		return false;
	}
}