package apiHelper;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RequestHandler {

	public static HttpResponse postRequest(String url, HashMap<String, String> headers, String entity) {
		// received parameter to be hit
		System.out.println("prepare the post request");
		HttpResponse response = null;
		HttpPost httpPost = null;
		org.apache.http.HttpResponse httpResponse = null;

		try {
			CloseableHttpClient client = HttpClients.custom().build();
			System.out.println("URL: " + url);
			httpPost = new HttpPost(url);
			for (String key : headers.keySet()) {
				System.out.println("Header: " + key + " = " + headers.get(key));
				httpPost.setHeader(key, headers.get(key));
			}
			System.out.println("Entity: " + entity.toString());
			httpPost.setEntity(new StringEntity(entity.toString(), "UTF-8"));
			System.out.println("execute the post request");
			httpResponse = client.execute(httpPost);
			response = readResponse(httpResponse);
		} catch (MalformedURLException ex) {
			System.out.println("Error while try to post request");
			ex.printStackTrace();
			response = null;
		} catch (IOException ioe) {
			System.out.println("Error while try to post request");
			ioe.printStackTrace();
			response = null;
		} finally {
			System.out.println("release Connection");
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
		}
		return response;
	}

	public static HttpResponse putRequest(String url, HashMap<String, String> headers, String entity) {
		// received parameter to be hit
		System.out.println("prepare the post request");
		HttpResponse response = null;
		HttpPut httpput = null;
		org.apache.http.HttpResponse httpResponse = null;

		try {
			CloseableHttpClient client = HttpClients.custom().build();
			System.out.println("URL: " + url);
			httpput = new HttpPut(url);
			for (String key : headers.keySet()) {
				System.out.println("Header: " + key + " = " + headers.get(key));
				httpput.setHeader(key, headers.get(key));
			}
			System.out.println("Entity: " + entity.toString());
			httpput.setEntity(new StringEntity(entity.toString(), "UTF-8"));
			System.out.println("execute the post request");
			httpResponse = client.execute(httpput);
			response = readResponse(httpResponse);
		} catch (MalformedURLException ex) {
			System.out.println("Error while try to post request");
			ex.printStackTrace();
			response = null;
		} catch (IOException ioe) {
			System.out.println("Error while try to post request");
			ioe.printStackTrace();
			response = null;
		} finally {
			System.out.println("release Connection");
			if (httpput != null) {
				httpput.releaseConnection();
			}
		}
		return response;
	}

	public static HttpResponse getRequest(String url, Map<String, String> params) {
		System.out.println("prepare the get request");
		HttpResponse response = null;

		HttpGet httpGet = null;
		org.apache.http.HttpResponse httpResponse = null;
		try {
			CloseableHttpClient client = HttpClients.custom().build();
			String getURL = prepareGetURL(url, params);
			System.out.println("URL: " + getURL);
			httpGet = new HttpGet(getURL);
			httpResponse = client.execute(httpGet);
			response = readResponse(httpResponse);
		} catch (MalformedURLException ex) {
			System.out.println("Error while try to post request");
			ex.printStackTrace();
			response = null;
		} catch (IOException ioe) {
			System.out.println("Error while try to post request");
			ioe.printStackTrace();
			response = null;
		} catch (URISyntaxException e) {
			System.out.println("Error while try to post request");
			e.printStackTrace();
			response = null;
		} finally {
			if (httpGet != null) {
				httpGet.releaseConnection();
			}

		}
		return response;
	}

	private static String prepareGetURL(String url, Map<String, String> params) throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(url);
		if (params != null && !params.isEmpty()) {
			Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
				uriBuilder.addParameter(entry.getKey(), entry.getValue());
			}
		}
		return uriBuilder.build().toString();

	}

	private static HttpResponse readResponse(org.apache.http.HttpResponse httpResponse) throws IOException {
		HttpResponse responseObject = new HttpResponse();
		StatusLine statusLine = httpResponse.getStatusLine();
		System.out.println("Status Code: " + statusLine.getStatusCode());
		responseObject.setResponseCode(statusLine.getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String value = "";
		while ((value = rd.readLine()) != null) {
			result.append(value);
		}
		System.out.println("Response-Contetn: " + result.toString());
		responseObject.setResponseContent(result.toString());
		return responseObject;
	}

}
