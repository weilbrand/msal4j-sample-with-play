package com.microsoft.foobar;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Set;

import com.microsoft.aad.msal4j.AuthorizationCodeParameters;
import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.IClientCredential;
import com.microsoft.aad.msal4j.MsalException;
import com.microsoft.aad.msal4j.PublicClientApplication;
import com.microsoft.aad.msal4j.SilentParameters;

public class TelemetrySender {

	private final static String CLIENT_ID = "<InsertClientIdHere>";
	private final static String AUTHORITY = "<InsertAuthorityHere>";
	private final static String CLIENT_SECRET = "<InsertClientSecretHere>";
	private final static Set<String> SCOPE = Collections.singleton("<InsertScopesHere>");
	private final static String API_URL = "<InsertUrlToAPIManagementHere>";
	private final static String API_PATH = "token";
	

	public static void main(String[] args) throws Exception {
		System.out.println("Starting telemetry sender");

		IClientCredential credential = ClientCredentialFactory.createFromSecret(CLIENT_SECRET);
		ConfidentialClientApplication app = ConfidentialClientApplication.builder(CLIENT_ID, credential)
				.authority(AUTHORITY).build();
	
		
		IAuthenticationResult result;
		
		System.out.println("Getting access token");
		try {
			SilentParameters silentParameters = SilentParameters.builder(SCOPE).build();

			// try to acquire token silently from cache. This call will fail since the token cache does not
			// have a token for the application you are requesting an access token for
			result = app.acquireTokenSilently(silentParameters).join();

		} catch (Exception ex) {
			if (ex.getCause() instanceof MsalException) {

				ClientCredentialParameters parameters = ClientCredentialParameters.builder(SCOPE).build();
				
	             // Try to acquire a token. If successful, you should see
	             // the token information printed out to console
	             result = app.acquireToken(parameters).join();
	          
	         } else {
	             // Handle other exceptions accordingly
	             throw ex;
	         }
		}
		
		String resultOfCall = "";
		try {
			
			resultOfCall = CallApi(API_PATH, result);
			System.out.println("Result: " + resultOfCall);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Stop Telemetry Sender");
	}
	
	private static String CallApi(String path, IAuthenticationResult authResult) throws Exception {
		URL url = new URL(API_URL + path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		System.out.println("Calling API: " + url.toString());
		
		System.out.println("Token: " + authResult.accessToken());
		
		// Set the appropriate header fields in the request header.
		conn.setRequestProperty("Authorization", "Bearer " + authResult.accessToken());
		conn.setRequestProperty("Accept", "application/json");

		String response = HttpClientHelper.getResponseStringFromConn(conn);

		int responseCode = conn.getResponseCode();
		if(responseCode != HttpURLConnection.HTTP_OK) {
		    throw new IOException(response);
		}
		
		conn.disconnect();

		return response;
	}

}
