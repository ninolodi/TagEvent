package beans.aplicacao;

import idioma.Idioma;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.util.Locale;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logs.Logs;
import utils.SConstantes;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

public class Contexto
{
	/*-*-*-* Constantes de Privadas *-*-*-*/
	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
	
	public void addMensagem(String form, Severity severidade, String... mensagens)
	{
		if(mensagens!=null && mensagens.length>0)
		{
			FacesContext facesContext = FacesContext.getCurrentInstance();

			StringBuffer sb = new StringBuffer();
			for(int i=0; i<mensagens.length; i++)
			{
				if(!mensagens[i].contains(" ")) //Se nao tem ' ' sera considerado properties
				{
					sb.append(getMsg(mensagens[i]));
				}
				else
				{
					sb.append(mensagens[i]);
				}
			}
			
			if(sb.length()>0)
			{
				facesContext.addMessage(form, new FacesMessage(severidade, sb.toString(), sb.toString()));
			}
		}
	}
	
	public void addMensagem(String form, FacesMessage facesMessage)
	{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(form, facesMessage);
	}

	public Object getSessionObject(String chave)
	{
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(chave);
	}
	
	public Object getSessionObject(HttpServletRequest req, String chave)
	{
		return req.getSession().getAttribute(chave);
	}
	
	public void putSessionObject(String chave, Object object)
	{
//		if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey(chave))
//		{
//			FacesContext.getCurrentInstance().getExternalContext().getSessionMap()put(chave, object);
//		}
//		else
//		{			
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(chave, object);
//		}
			System.out.println("SIZE SESSION ::: " + FacesContext.getCurrentInstance().getExternalContext().getSessionMap().size());
	}
	
	/**
	 * Obtem uma mensagm de um properties de idioma
	 * @param chave
	 * @return
	 */
	public String getMsg(String chave)
	{
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if(locale==null) { locale = Idioma.PORTUGUES_BR_LOCALE; } //Padrao em caso de erros
		
		if(chave!=null && chave.length()>0)
		{
			Properties properties = Idioma.getProperties(locale);
			if(properties!=null)
			{
				return properties.getProperty(chave, "-");
			}
		}
		return "--";
	}
	
	public void enviarArquivo(byte[] bytes, String nomeArquivo) throws Exception
	{
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse)facesContext.getExternalContext().getResponse();

        BufferedInputStream input = null;
        BufferedOutputStream output = null;
        try 
        {
            input = new BufferedInputStream(new ByteInputStream(bytes, bytes.length), DEFAULT_BUFFER_SIZE);

            response.reset();
            response.setHeader("Content-Type", "application/pdf");
            response.setHeader("Content-Length", String.valueOf(bytes.length));
            response.setHeader("Content-Disposition", "inline; filename=\"" + nomeArquivo + "\"");
            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) 
            {
                output.write(buffer, 0, length);
            }
            output.flush();
        } 
        finally 
        {
        	fechar(output);
        	fechar(input);
        }
        facesContext.responseComplete(); 
	}
	
    private void fechar(Closeable resource) 
    {
        if(resource!=null) 
        {
            try 				{  resource.close(); } 
            catch (Exception e) { Logs.addError("Falha fechando resource", e); }
        }
    }
}