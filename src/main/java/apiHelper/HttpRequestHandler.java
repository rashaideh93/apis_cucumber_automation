package apiHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.text.Document;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * handler to handle get,post,put, and delete methods all httpclient
 * configurations manage headers, body, authentication, cookies depends on
 * httpcomponent-client 4.5.1 you must call createNewRequest for each new
 * request
 *
 * 
 * About HttpClient
 * 
 * https://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/
 * http://hc.apache.org/httpcomponents-client-ga/F
 * 
 */
public class HttpRequestHandler {
	private static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
	private static RequestConfig globalConfig;
	private RequestConfig localConfig;
	private static HttpClientBuilder httpclientBuilder;
	private RequestBuilder requestBuilder;
	private HttpClientContext localContext;
	private URIBuilder requestURI;
	private HttpHost proxy = null;
	private static BasicCookieStore sessionCookieStore;
	private static BasicCookieStore requestCookieStore;
	private static HttpRequestHandler instance = null;
	public Map<String, CloseableHttpResponse> myResponses = new HashMap<String, CloseableHttpResponse>();

	private HttpRequestHandler() {
		// Prevent direct instantiation.
	}

	public static HttpRequestHandler getInstance() {
		if (instance == null) {
			httpclientBuilder = HttpClientBuilder.create();

			globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();

			poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
			// Increase max total connection to 200
			poolingHttpClientConnectionManager.setMaxTotal(200);
			// Increase default max connection per route to 20
			poolingHttpClientConnectionManager.setDefaultMaxPerRoute(20);
			// Increase max connections for localhost:80 to 50
			// HttpHost localhost = new HttpHost("localhost", 80);
			// poolingHttpClientConnectionManager.setMaxPerRoute(new
			// HttpRoute(localhost), 50);

			httpclientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
			httpclientBuilder.setDefaultRequestConfig(globalConfig);

			instance = new HttpRequestHandler();
			requestCookieStore = new BasicCookieStore();
			sessionCookieStore = new BasicCookieStore();
		}
		return instance;
	}

	/**
	 * 
	 * Method used to Create new HTTP Request
	 * 
	 * @param method
	 * 
	 *            method type from (MethodEnum.java) MethodEnum located in the
	 *            same package Allowed values (POST, PUT , Delete, Get )
	 * 
	 * @param responseUniqueID
	 * 
	 *            Unique Id set inside myResponses map as key
	 * 
	 * 
	 */
	public void createNewRequest(MethodEnum.Method method, String responseUniqueID) {
		requestBuilder = RequestBuilder.create(method.name());
		requestURI = new URIBuilder();
		localContext = HttpClientContext.create();
		localConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();

		myResponses.put(responseUniqueID, null);
	}

	/**
	 * Set Request URL Method used to set request URL values inside URIBuilder
	 * object
	 * 
	 * @param scheme
	 *            scheme used eg (http , https)
	 * @param host
	 *            URI host
	 * @param path
	 *            URI path
	 * @throws URISyntaxException
	 *             Checked exception thrown to indicate that a string could not
	 *             be parsed as a URI reference.
	 */
	public void setRequestUrl(String scheme, String host, String path) throws URISyntaxException {
		requestURI.setScheme(scheme).setHost(host).setPath(path);
	}

	/**
	 * Set Request URL Method used to set request URL values inside URIBuilder
	 * object
	 * 
	 * @param url:
	 *            URL as String.
	 * @throws URISyntaxException
	 *             Checked exception thrown to indicate that a string could not
	 *             be parsed as a URI reference.
	 */
	public void setRequestUrl(String url) throws URISyntaxException {
		requestURI = new URIBuilder(url);
	}

	/**
	 * Set request URL by pass object of type URI
	 * 
	 * @param uri
	 * 
	 *            URI object
	 * 
	 * @throws URISyntaxException
	 *             Checked exception thrown to indicate that a string could not
	 *             be parsed as a URI reference.
	 */
	public void setRequestUrl(URI uri) throws URISyntaxException {
		requestURI = new URIBuilder(uri);
	}

	/**
	 * Method used to Adds parameter to URI query. The parameter name and value
	 * are expected to be unescaped and may contain non ASCII characters. Please
	 * note query parameters are mutually exclusive. This method will remove
	 * custom query if present.
	 * 
	 * @param key
	 * 
	 *            parameter name
	 * 
	 * @param value
	 *            parameter value
	 * 
	 */
	public void setRequestQueryString(String key, String value) {
		requestURI.addParameter(key, value);
	}

	/**
	 * Method used to Adds parameter to URI query. The parameter is name,value
	 * pair stored in List
	 * 
	 * @param queryList
	 * 
	 *            Parameter Name,value pair as
	 */

	public void setRequestQueryString(List<NameValuePair> queryList) {
		requestURI.addParameters(queryList);
	}

	/**
	 * Method used to set HTTP request header
	 * 
	 * @param header
	 * 
	 *            Header Object
	 */

	public void setRequestHeader(Header header) {
		requestBuilder.setHeader(header);

	}

	/**
	 * @param name
	 *            is any variable from class org.apache.http.HttpHeaders
	 * 
	 * @param value
	 *            header value
	 */
	public void setRequestHeader(String name, String value) {
		requestBuilder.setHeader(name, value);
	}

	/**
	 * Return request headers for specific header name
	 * 
	 * @param name
	 *            header name
	 * 
	 * @return
	 * 
	 * 		Array of headers for request name
	 * 
	 */
	public Header[] getRequestHeaders(String name) {

		return requestBuilder.getHeaders(name);
	}

	/**
	 * 
	 * Creates HttpHost instance(Proxy) with the given scheme, hostname (Proxy
	 * address) and port. Method used to set - register new Proxy with default
	 * scheme (HTTP)
	 * 
	 * 
	 * 
	 * 
	 * @param proxyAddress
	 * 
	 *            proxyAddress IP or DNS name
	 * 
	 * @param proxyPort
	 * 
	 * 
	 *            port number indicates the scheme default port
	 * 
	 */

	public void setProxy(String proxyAddress, int proxyPort) {
		if (proxyAddress == null) {
			proxy = null;
		} else {
			proxy = new HttpHost(proxyAddress, proxyPort, HttpHost.DEFAULT_SCHEME_NAME);
		}
	}

	/**
	 * This method used to add Cookie to the request
	 * 
	 * @param name
	 * 
	 *            Cookie name
	 * 
	 * @param value
	 *            Cookie value
	 * 
	 * @param domain
	 *            The value of the domain attribute
	 * @param path
	 *            The value of the path attribute
	 */

	public void setRequestCookie(String name, String value, String domain, String path) {
		BasicClientCookie cookie = new BasicClientCookie(name, value);
		cookie.setDomain(domain);
		cookie.setPath(path);

		this.setRequestCookie(cookie);
	}

	/**
	 * This method used to add Cookie to the request by passing full Cookie
	 * object
	 * 
	 * @param cookie
	 *            Cookie object how to use cookie = new
	 *            BasicClientCookie(name,value); cookie.setDomain(domain);
	 *            cookie.setPath(path); setRequestCookie(cookie);
	 * 
	 *
	 */

	public void setRequestCookie(Cookie cookie) {
		requestCookieStore = new BasicCookieStore();
		requestCookieStore.addCookie(cookie);
	}

	/**
	 * This method used to add Cookies to the request by passing array of
	 * Cookies
	 * 
	 * 
	 * @param cookies
	 *            array of cookies need to added to the request
	 * 
	 *
	 */
	public void setRequestCookies(Cookie[] cookies) {
		requestCookieStore = new BasicCookieStore();
		requestCookieStore.addCookies(cookies);
	}

	/**
	 * Method used to get request Cookies
	 * 
	 * @return List of Cookie.
	 */
	public List<Cookie> getRequestCookies() {

		return requestCookieStore.getCookies();
	}

	/**
	 * Set AUTHORIZATION in the request header Auth value will be combination
	 * between uses and password
	 * 
	 * 
	 * @param user
	 *            user value
	 * @param password
	 * 
	 *            password value
	 */
	public void setRequestBasicAuth(String user, String password) {
		String auth = user + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("ISO-8859-1")));
		String authHeader = "Basic " + new String(encodedAuth);

		this.setRequestHeader(HttpHeaders.AUTHORIZATION, authHeader);
	}

	/**
	 * Add header to the session
	 * 
	 * @param header
	 * 
	 *            Header object
	 */
	public void setSessionHeader(Header header) {
		List<Header> headersList = new ArrayList<Header>();
		headersList.add(header);

		this.setSessionHeader(headersList);
	}

	/**
	 * Add header to the session with specific name and value
	 * 
	 * @param name
	 * 
	 *            header name
	 * 
	 * @param value
	 * 
	 *            header value
	 */
	public void setSessionHeader(String name, String value) {
		List<Header> headersList = new ArrayList<Header>();
		headersList.add(new BasicHeader(name, value));

		this.setSessionHeader(headersList);
	}

	/**
	 * Add headers to the session
	 * 
	 * @param defaultHeaders
	 *            List of headers to be added
	 * 
	 */
	public void setSessionHeader(List<Header> defaultHeaders) {
		httpclientBuilder.setDefaultHeaders(defaultHeaders);
	}

	/**
	 * This method used to add session cookie
	 * 
	 * @param name
	 * 
	 *            Cookie name
	 * 
	 * @param value
	 *            Cookie value
	 * 
	 * @param domain
	 *            The value of the domain attribute
	 * @param path
	 *            The value of the path attribute
	 */

	public void setSessionCookie(String name, String value, String domain, String path) {
		BasicClientCookie cookie = new BasicClientCookie(name, value);
		cookie.setDomain(domain);
		cookie.setPath(path);

		this.setSessionCookie(cookie);
	}

	/**
	 * This method used to add session Cookie as object
	 * 
	 * @param cookie
	 *            cookie object
	 */

	public void setSessionCookie(Cookie cookie) {
		sessionCookieStore.addCookie(cookie);
	}

	/**
	 * This method used set session Cookies passing array of Cookies
	 * 
	 * 
	 * @param cookies
	 *            array of cookies need to added to the request
	 * 
	 *
	 */

	public void setSessionCookies(Cookie[] cookies) {
		sessionCookieStore.addCookies(cookies);
	}

	/**
	 * Method used to get session Cookies
	 * 
	 * @return List of Cookie.
	 * 
	 */
	public List<Cookie> getSessionCookies() {
		return sessionCookieStore.getCookies();
	}

	/**
	 * Method used to remove session cookie
	 * 
	 * @param cookie
	 * 
	 *            cookie object to be removed
	 */
	public void removeSessionCookie(Cookie cookie) {
		sessionCookieStore.getCookies().remove(cookie);
	}

	/**
	 * 
	 * Clear session Cookies List
	 * 
	 */

	public void clearSessionCookies() {
		sessionCookieStore.getCookies().clear();
	}

	/**
	 * Set AUTHORIZATION in the Session Auth value will be combination between
	 * uses and password
	 * 
	 * 
	 * @param user
	 *            user value
	 * @param password
	 * 
	 *            password value
	 */
	public void setSessionBasicAuth(String user, String password) {
		String auth = user + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("ISO-8859-1")));
		String authHeader = "Basic " + new String(encodedAuth);

		this.setSessionHeader(HttpHeaders.AUTHORIZATION, authHeader);
	}

	/**
	 * Method used to set request body with HttpEntity object
	 * 
	 * @param entity
	 * 
	 *            HttpEntity object need to be added to the request body
	 * 
	 * 
	 * 
	 *            About HTTP entity
	 * 
	 *            http://hc.apache.org/httpcomponents-core-ga/httpcore/apidocs/org/apache/http/HttpEntity.html?is-external=true
	 * 
	 */

	private void setRequestBody(HttpEntity entity) {
		requestBuilder.setEntity(entity);
	}

	/**
	 * Method used to set request body with String value
	 * 
	 * @param entity
	 * 
	 *            string added to the request body
	 * 
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 *             when the The Character Encoding is not supported.
	 * 
	 * 
	 */

	public void setRequestBody(String entity) throws UnsupportedEncodingException {
		StringEntity newEntity = new StringEntity(entity, "utf-8");
		this.setRequestBody(newEntity);
	}

	/**
	 * Method used to set request body with name value pair list
	 * 
	 * @param nameValuePair
	 * 
	 *            name value pair List need to be added to request body
	 * 
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 *             when the The Character Encoding is not supported.
	 * 
	 * 
	 */

	public void setRequestBody(List<NameValuePair> nameValuePair) throws UnsupportedEncodingException {
		this.setRequestBody(new UrlEncodedFormEntity(nameValuePair));
	}

	/**
	 * Method used to set request body with JsonElement
	 * 
	 * @param entity
	 * 
	 *            JsonElement value
	 * 
	 * @throws UnsupportedEncodingException
	 *             when the The Character Encoding is not supported.
	 */

	public void setRequestBody(JsonElement entity) throws UnsupportedEncodingException {
		String jsonEntity = new Gson().toJson(entity);
		this.setRequestBody(jsonEntity);
	}

	/**
	 * Method used to set request body with Document object
	 * 
	 * @param entity
	 *            Document object
	 * 
	 * @throws UnsupportedEncodingException
	 *             when the The Character Encoding is not supported.
	 */

	public void setRequestBody(Document entity) throws UnsupportedEncodingException {
		this.setRequestBody(entity.toString());
	}

	/**
	 * Method used to set request body with XML body represented as String
	 * object
	 * 
	 * @param entity
	 *            XML body represented String object
	 * 
	 * @throws UnsupportedEncodingException
	 *             when the The Character Encoding is not supported.
	 */

	public void setRequestBodyByXMLString(String entity) throws UnsupportedEncodingException {
		HttpEntity newEntity = new ByteArrayEntity(entity.getBytes("UTF-8"));
		this.setRequestBody(newEntity);
	}

	/**
	 * return response by httpclient request using CloseableHttpResponse request
	 * created using CloseableHttpClient
	 * http://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/org/apache/http/impl/client/CloseableHttpClient.html
	 * https://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/org/apache/http/client/methods/CloseableHttpResponse.html
	 * 
	 * 
	 * @return CloseableHttpResponse response object
	 * @param responseUniqueID
	 *            response id
	 * @throws URISyntaxException
	 *             Checked exception thrown to indicate that a string could not
	 *             be parsed as a URI reference.
	 * @throws ClientProtocolException
	 *             Signals an error in the HTTP protocol.
	 * @throws IOException
	 *             Signals that an I/O exception of some sort has occurred. This
	 *             class is the general class of exceptions produced by failed
	 *             or interrupted I/O operations.
	 */
	public CloseableHttpResponse execute(String responseUniqueID)
			throws URISyntaxException, ClientProtocolException, IOException

	{

		CloseableHttpClient httpclient;

		if (proxy != null) {
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
			httpclient = httpclientBuilder.setDefaultCookieStore(requestCookieStore).setRoutePlanner(routePlanner)
					.build();

		} else {
			httpclient = httpclientBuilder.setDefaultCookieStore(requestCookieStore).build();
		}

		HttpUriRequest requestHead = requestBuilder.setUri(requestURI.build()).setConfig(localConfig).build();

		CloseableHttpResponse response = httpclient.execute(requestHead, localContext);

		myResponses.remove(responseUniqueID);
		myResponses.put(responseUniqueID, response);

		return response;
	}

	/**
	 * return response by httpclient request request using CloseableHttpResponse
	 * request created using CloseableHttpClient
	 * http://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/org/apache/http/impl/client/CloseableHttpClient.html
	 * https://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/org/apache/http/client/methods/CloseableHttpResponse.html
	 * 
	 * 
	 * @return CloseableHttpResponse response object
	 * @param responseUniqueID
	 *            response id
	 * @throws URISyntaxException
	 *             Checked exception thrown to indicate that a string could not
	 *             be parsed as a URI reference.
	 * @throws ClientProtocolException
	 *             Signals an error in the HTTP protocol.
	 * @throws IOException
	 *             Signals that an I/O exception of some sort has occurred. This
	 *             class is the general class of exceptions produced by failed
	 *             or interrupted I/O operations.
	 */
	public CloseableHttpResponse executeSessionRequest(String responseUniqueID)
			throws URISyntaxException, ClientProtocolException, IOException {

		CloseableHttpClient httpclient;

		if (proxy != null) {
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
			httpclient = httpclientBuilder.setDefaultCookieStore(sessionCookieStore).setRoutePlanner(routePlanner)
					.build();

		} else {
			httpclient = httpclientBuilder.setDefaultCookieStore(sessionCookieStore).build();
		}


		HttpUriRequest requestHead = requestBuilder.setUri(requestURI.build()).setConfig(localConfig).build();
		CloseableHttpResponse response = httpclient.execute(requestHead, localContext);
		myResponses.remove(responseUniqueID);
		myResponses.put(responseUniqueID, response);
		return response;

	}
}
