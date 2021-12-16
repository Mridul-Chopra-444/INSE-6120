import java.net.*;
import java.util.*;
import java.io.*;

class HttpRequest{
	
	public static final String GET = "GET";
	public static final String POST = "POST";
	
	public void request( String requestUrl, String requestType, Map<String, String> parametres, Map<String,String> headers, String body ) throws IOException{
		
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(requestType);
		conn.setDoOutput(true);
		
		// if parameters are present
		if(parametres != null){
			// converting parameters map to string
			StringBuilder res = new StringBuilder();
			for( Map.Entry<String,String> entry : parametres.entrySet()){
				res.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
				res.append("=");
				res.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
				res.append("&");
			}
			
			String result = res.toString();
			result = result.length() > 0 
										? 	result.substring(0, result.length() -1)
										:	result;
										
			
			// Adding the parametres
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.writeBytes(result);
			out.flush();
			out.close();
		}
		
		// if headers are present
		if(headers != null){
			// Adding the headers
			for( Map.Entry<String,String> entry : headers.entrySet() ) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		
		// Adding body in case request is post
		if(requestType.equals(POST)){
			OutputStream os = conn.getOutputStream();
			byte[] input = body.getBytes("utf-8");
			os.write(input, 0, input.length);
		}
		
		int status = conn.getResponseCode();
		System.out.println(status);
		conn.disconnect();
	}
}

class RequestSender{
	
	public static void main(String[] args) throws Exception{
		
		HttpRequest sender = new HttpRequest();
		String url = "https://api.magic.link/v1/auth/user/login/phone/start";
		
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("X-Magic-API-Key","pk_test_3F8F2B46C789AB90");
		
		String body = "{\"phone_number\":\"+14389244654\",\"request_origin_message\":\"\"}";
		
		
		while(true)
		{
			sender.request(url, HttpRequest.POST, null, headers, body);
			Thread.sleep(32000);
		}
	}
}