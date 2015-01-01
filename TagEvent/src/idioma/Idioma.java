package idioma;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import logs.Logs;

public class Idioma 
{
	/*-*-*-* Constantes Publicas *-*-*-*/
	public static final int INGLES = 3030;
	public static final Locale INGLES_LOCALE = Locale.US;
	
	public static final int PORTUGUES_BR = 3031;
	public static final Locale PORTUGUES_BR_LOCALE = new Locale("pt", "BR");
	
	/*-*-*-* Variaveis e Objetos Privados *-*-*-*/
	private static List<Object[]> idiomas = new ArrayList<Object[]>();
	

	/*-*-*-* Metodos Publicos *-*-*-*/
	/**
	 * Carrega os maps de idiomas atendidos e seus properties
	 */
	public static void inicializar()
	{
		try
		{
			Properties properties = new Properties();
			properties.load(Idioma.class.getResourceAsStream("idioma_en_US.properties"));
			idiomas.add(new Object[]{INGLES, "English", INGLES_LOCALE, properties});
			
			properties = new Properties();
			properties.load(Idioma.class.getResourceAsStream("idioma_pt_BR.properties"));
			idiomas.add(new Object[]{PORTUGUES_BR, "Português (BR)", PORTUGUES_BR_LOCALE, properties});
		}
		catch(Exception e)
		{ Logs.addFatal("Problema durante a inicializacao dos idiomas", e); }
	}
	
	/**
	 * Metodo que obtem o properties de traducao referente ao locale
	 * @param locale
	 * @return
	 */
	public static Properties getProperties(Locale locale)
	{
		Object[] idioma;
		for(int i=0; i<idiomas.size(); i++)
		{
			idioma = idiomas.get(i);
			if(idioma[2].equals(locale))
			{
				return (Properties)idioma[3];
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param locale
	 * @return
	 */
	public static String getNome(int idiomaID)
	{
		Object[] idioma;
		for(int i=0; i<idiomas.size(); i++)
		{
			idioma = idiomas.get(i);
			if((int)idioma[0]==idiomaID)
			{
				return (String)idioma[1];
			}
		}
		return "";
	}
	
	/**
	 * 
	 * @param locale
	 * @return
	 */
	public static int getID(String nome)
	{
		Object[] idioma;
		for(int i=0; i<idiomas.size(); i++)
		{
			idioma = idiomas.get(i);
			if(((String)idioma[1]).equalsIgnoreCase(nome))
			{
				return (int)idioma[0];
			}
		}
		return 0;
	}
	
	/**
	 * 
	 * @param locale
	 * @return
	 */
	public static Locale getLocale(String idiomaNome)
	{
		Object[] idioma;
		for(int i=0; i<idiomas.size(); i++)
		{
			idioma = idiomas.get(i);
			if(((String)idioma[1]).equalsIgnoreCase(idiomaNome))
			{
				return (Locale)idioma[2];
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param locale
	 * @return
	 */
	public static Locale getLocale(int idiomaID)
	{
		Object[] idioma;
		for(int i=0; i<idiomas.size(); i++)
		{
			idioma = idiomas.get(i);
			if((int)idioma[0]==idiomaID)
			{
				return (Locale)idioma[2];
			}
		}
		return null;
	}
}