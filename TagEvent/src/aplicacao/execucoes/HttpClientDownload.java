package aplicacao.execucoes;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import persistencia.dao.InstagramDAO;
import persistencia.om.Instagram;
import persistencia.om.InstagramFoto;
import utils.BDConstantes;
import utils.Constantes;
import aplicacao.MainTagEvent;
import br.com.mresolucoes.atta.configuracoes.Configuracoes;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.PostgresJDBC;
import br.com.mresolucoes.atta.persistencia.conexao.servidores.base.BaseJDBC;
import br.com.mresolucoes.atta.utils.BDConstantesAtta;
 
public class HttpClientDownload {

//	  private static String url1 = "http://distilleryimage8.s3.amazonaws.com/1008d380a54f11e3b716127154f16c08_8.jpg";
//	  private static String url2 = "http://distilleryimage2.s3.amazonaws.com/e4e2ffd2b11011e3bd03128992bcf25a_8.jpg";
  private static String pathLocal = "D://garbage";
 
  public static void main(String[] args) 
  {
		//Inicializando constantes
		Constantes.inicializar();
				
		//Carrega as configuracoes do sistema
		Configuracoes.carregar(MainTagEvent.class.getResourceAsStream("configuracoes/configuracoes.properties"), MainTagEvent.class.getResourceAsStream("configuracoes/log4j.properties"));
				
		//Inicia a conexao com a base de dados
		BaseJDBC baseJDBC = new PostgresJDBC(Configuracoes.propriedades.get("baseUrl"), Configuracoes.propriedades.get("baseBase"), Configuracoes.propriedades.getInt("basePorta"), Configuracoes.propriedades.get("baseLogin"), Configuracoes.propriedades.get("baseSenha"), null);
		
	  try
	  {		
		long fkEvento = 9;

        List<String> filesListInDir = new ArrayList<String>();
		
//		Evento evento = new EventoDAO().obtemUnico(baseJDBC, Evento.class, "pkEvento = " + fkEvento, 0);  
		
		List<Instagram> instagrams = new InstagramDAO().getInstagramsEvento(baseJDBC, fkEvento, BDConstantesAtta.STATUS_ATIVO, 0);
		Instagram instagram;
		for(int j = 0; j<instagrams.size(); j++)
    	{
    		instagram = instagrams.get(j);
    		List<InstagramFoto> fotosInstagram = new InstagramDAO().getFotosInstagram(baseJDBC, instagram.getPkInstagram(), BDConstantes.STATUS_ATIVO_LIBERADO, -1);
    		
    		for(int k = 0; k<fotosInstagram.size(); k++)
        	{
    	        filesListInDir.add(fotosInstagram.get(k).getUrlImagemGrande());
        	}
    	}
		
//        filesListInDir.add(url1);
//        filesListInDir.add(url2);
        
        FileOutputStream fosZip = new FileOutputStream(pathLocal+"/fotos.zip");
        ZipOutputStream zos = new ZipOutputStream(fosZip);
        for(String filePath : filesListInDir){
            System.out.println("Zipping "+filePath);
            //for ZipEntry we need to keep only relative file path, so we used substring on absolute path
            URL url = new URL(filePath); 
            ZipEntry ze = new ZipEntry(url.getPath().substring(1, url.getPath().length()));
            zos.putNextEntry(ze);

            //Cria streams de leitura (este metodo ja faz a conexao)...  
            InputStream is = url.openStream();  
            FileOutputStream fos = new FileOutputStream(pathLocal+url.getPath());   
            int umByte = 0;  
            while ((umByte = is.read()) != -1){  
                fos.write(umByte);  
            }  
            is.close();  
            fos.close();  
            
            
            //read the file and write to ZipOutputStream
            FileInputStream fis = new FileInputStream(pathLocal+url.getPath());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            zos.closeEntry();
            fis.close();
        }
        zos.close();
        fosZip.close();
          
    } catch (Exception e) {  
        //Lembre-se de tratar bem suas excecoes, ou elas tambem lhe tratarão mal!  
        //Aqui so vamos mostrar o stack no stderr.  
        e.printStackTrace();  
    }  
         
//    return null;  
  }
}