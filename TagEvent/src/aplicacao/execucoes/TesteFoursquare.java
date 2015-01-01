package aplicacao.execucoes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logs.Logs;
import utils.data.Data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.entities.Checkin;

public class TesteFoursquare {
	
	private static String code = "3I5OUQ2CEDO23O05ZB51QPHUL3CVKDZENN1VTKAVKE4CRZWH#_=_";
	private static String accessToken = "0WSHBT0FHCC3Q5VRR0DKQLBZITCMEIFFFLS0WTEVMTBNSMIJ";
	private static String clientID = "RKXOS5OAAPDQJU3BVAC3TAQAOZEXKIA3GZKLVIYIPWPMM0XL";
	private static String clientSecret = "1IZP442UO4CTPFPCYIRNFLKXJCQOXLAR54CF0VUIE04FQDTD";
	private static String callbackURL = "http://www.tagevent.com.br/notificacoes/foursquare";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try
		{

//			https://foursquare.com/oauth2/authenticate?client_id=RKXOS5OAAPDQJU3BVAC3TAQAOZEXKIA3GZKLVIYIPWPMM0XL&response_type=code&redirect_uri=http://www.tagevent.com.br/notificacoes/foursquare
	//
//			https://foursquare.com/oauth2/access_token?client_id=RKXOS5OAAPDQJU3BVAC3TAQAOZEXKIA3GZKLVIYIPWPMM0XL&client_secret=1IZP442UO4CTPFPCYIRNFLKXJCQOXLAR54CF0VUIE04FQDTD&grant_type=authorization_code&redirect_uri=http://www.tagevent.com.br/notificacoes/foursquare&code=H2B45JXVQ2JQ2HGU23RDNDEEGGVMWGTRV04CSC2EP5M0AMBF#_=_
	//
//			{"access_token":"0WSHBT0FHCC3Q5VRR0DKQLBZITCMEIFFFLS0WTEVMTBNSMIJ"}

//			String idVenue = "503a6669e4b0bbba982a08d9"; //Arena Amil
			String idVenue = "4b437e02f964a5209fe125e3"; //Shopp D. Pedro
//			String idVenue = "52f02c7d11d25e220d547bf1"; //Creative Mind			
//			String idVenue = "mlstadium";
			
			Data data = new Data();
//			
			FoursquareApi foursquareApi = new FoursquareApi(clientID, clientSecret, callbackURL);

//			foursquareApi.authenticateCode(code);
			foursquareApi.venuesHereNow(idVenue, 500, 0, data.getTimeInMillis());
			fi.foyt.foursquare.api.Result<Checkin> resultados = foursquareApi.checkinsAdd("4b437e02f964a5209fe125e3", "Parque Dom Pedro Shopping", "Testando Checkin Integração", "private", "-22.84762436429311,-47.06326246261597", 10.0, 10.0, 10.0);

			foursquareApi.venuesHereNow(idVenue, 500, 0, data.getTimeInMillis());
			
			String accessToken = "0WSHBT0FHCC3Q5VRR0DKQLBZITCMEIFFFLS0WTEVMTBNSMIJ";
			
			String pagina = "https://api.foursquare.com/v2/venues/"+idVenue+"/herenow?oauth_token="+accessToken+"&v="+data.toString("YYYYMMDD");
			System.out.println(pagina);
						
			URL url = new URL(pagina);
			// cria um stream de entrada do conteúdo
			InputStreamReader inputReader = new InputStreamReader( url.openStream() );
			BufferedReader bufferedReader = new BufferedReader( inputReader );
 
			System.out.println( "\n** Conteúdo do recurso web **" );
			String linha = "";
			StringBuffer sb = new StringBuffer();
			while((linha = bufferedReader.readLine()) != null)
			{
				System.out.println(linha);
				sb.append(linha);
			}
		
			JsonParser parser = new JsonParser();
	        // The JsonElement is the root node. It can be an object, array, null or
	        // java primitive.
	        JsonElement element = parser.parse(sb.toString());
	        // use the isxxx methods to find out the type of jsonelement. In our
	        // example we know that the root object is the Albums object and
	        // contains an array of dataset objects
	        if (element.isJsonObject()) 
	        {
	            JsonObject albums = element.getAsJsonObject();
	            System.out.println(albums.get("response").getAsJsonObject().get("hereNow").getAsJsonObject().get("count").getAsInt());
	        }
		}
		catch (Exception e) 
		{
			Logs.addError(e);
		}
		System.exit(0);
	}
	
	
//	items: [
//	{
//	id: "52f02e2f498ecea68bfb34c4"
//	createdAt: 1391472175
//	type: "checkin"
//	shout: "TagEventiando mais um pouco pra variar!!"
//	timeZoneOffset: -120
//	user: {
//	id: "74062337"
//	firstName: "Welington"
//	lastName: "Lodi"
//	gender: "male"
//	relationship: "self"
//	photo: {
//	prefix: "https://irs3.4sqi.net/img/user/"
//	suffix: "/74062337-YL1BYLOAU0VWI55C.jpg"
//	}
//	}
//	likes: {}
//	like: false
//	}
//	]
	
	public void authenticationRequest(HttpServletRequest request, HttpServletResponse response) {
	    FoursquareApi foursquareApi = new FoursquareApi("Client ID", "Client Secret", "Callback URL");
	    try {
	      // First we need to redirect our user to authentication page. 
	      response.sendRedirect(foursquareApi.getAuthenticationUrl());
	    } catch (IOException e) {
	      // TODO: Error handling
	    }
	  }
	  
	  public void handleCallback(HttpServletRequest request, HttpServletResponse response) {
	    // After user has logged in and confirmed that our program may access user's Foursquare account
	    // Foursquare redirects user back to callback url. 
	    FoursquareApi foursquareApi = new FoursquareApi("Client ID", "Client Secret", "Callback URL");
	    // Callback url contains authorization code 
	    String code = request.getParameter("code");
	    try {
	      // finally we need to authenticate that authorization code 
	      foursquareApi.authenticateCode(code);
	      // ... and voilà we have a authenticated Foursquare client
	    } catch (FoursquareApiException e) {
	     // TODO: Error handling
	    }
	  }
}
