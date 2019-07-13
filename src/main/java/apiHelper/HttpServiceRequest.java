package apiHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import apiHelper.HttpServiceConfigurations.HttpServiceConfiguration;
import apiHelper.HttpServiceConfigurations.NameValuePair;
import apiHelper.MethodEnum.Method;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.cookie.Cookie;

public class HttpServiceRequest {

	private ThreadLocal<HttpServiceConfiguration> _httpServiceConfiguration = new ThreadLocal();
	private ThreadLocal<HttpRequestHandler> _httpRequestHandler = new ThreadLocal();
	private ThreadLocal<String> _requestBody = new ThreadLocal();

	public HttpServiceRequest(HttpRequestHandler httpRequestHandler, HttpServiceConfiguration httpServiceConfiguration,
			String proxyAddress, int proxyPort) {
		setHttpRequestHandler(httpRequestHandler);
		setHttpServiceConfiguration(httpServiceConfiguration);
		initHttpRequest(proxyAddress, proxyPort);
	}

	public HttpServiceRequest setProxy(String proxyAddress, int proxyPort) {
		getHttpRequestHandler().setProxy(proxyAddress, proxyPort);
		return this;
	}

	public HttpServiceResponse execute() {
		ThreadLocal<CloseableHttpResponse> httpResponse = new ThreadLocal<>();
		try {
			System.out.print("\n**Start executing service, [Service Name]: "
					+ getHttpServiceConfiguration().getName() + "\n[Service URL]: "
					+ getHttpServiceConfiguration().getHost() + getHttpServiceConfiguration().getUrl()
					+ "\n[Service Content-Type]: " + getHttpServiceConfiguration().getConentType()
					+ "\n[Service HTTP-Method]: " + getHttpServiceConfiguration().getHttpMethod()
					+ "\n[Service Request-Body]: " + getServiceRequestBody()
					+ "\n");

			httpResponse
					.set(getHttpRequestHandler().executeSessionRequest(getHttpServiceConfiguration().getMethodName()));
		} catch (URISyntaxException | IOException e) {
			System.out.print("Error while executing service, [Service Name]: "
					+ getHttpServiceConfiguration().getName());
		}
		return getHttpServiceResponse(httpResponse.get());
	}

	public HttpServiceRequest resolveServiceRequestBody(Hashtable<String, String> PlaceholderKeyValueHashtable) {
		if (PlaceholderKeyValueHashtable != null) {
			for (Entry<String, String> placeholder : PlaceholderKeyValueHashtable.entrySet()) {
				setServiceRequestBody(getServiceRequestBody().replace(placeholder.getKey(), placeholder.getValue()));
			}
		}
		setHttpRequestHandlerRequestBody();
		return this;
	}

	public HttpServiceRequest resolveServiceRequestBody(String placeholderName, String placeholderValue) {
		setServiceRequestBody(getServiceRequestBody().replace(placeholderName, placeholderValue));
		setHttpRequestHandlerRequestBody();
		return this;
	}

	public HttpServiceRequest resolveAndAppendBodyTemplate(String templateName) {
		return resolveBodyTemplate(templateName, true);
	}

	public HttpServiceRequest resolveBodyTemplate(String templateName) {
		return resolveBodyTemplate(templateName, false);
	}

	public HttpServiceRequest resolveServiceRequestHeader(String headerName, String placeholderName,
			String placeholderValue) {
		for (Header header : getHttpRequestHandler().getRequestHeaders(headerName)) {
			if (header.getValue() != null) {
				getHttpRequestHandler().setRequestHeader(headerName,
						header.getValue().replace(placeholderName, placeholderValue));
			}
		}
		return this;
	}

	public HttpServiceRequest resolveServiceRequestUrl(String placeholderName, String placeholderValue,
			boolean encodeUrl) {
		String url = getHttpServiceConfiguration().getUrl();
		if (url != null && url != "") {
			placeholderValue = getEncodedValue(placeholderValue, encodeUrl);
			url = url.replace(placeholderName, placeholderValue);
			getHttpServiceConfiguration().setUrl(url);
			setRequestUrl();
		}
		return this;
	}

	public HttpServiceRequest resolveServiceRequestUrl(String placeholderName, String placeholderValue) {
		return resolveServiceRequestUrl(placeholderName, placeholderValue, false);
	}

	public HttpServiceRequest addCookie(String name, String value, String domain, String path) {
		getHttpRequestHandler().setSessionCookie(name, value, domain, path);
		return this;
	}

	public HttpServiceRequest addCookie(Cookie cookie) {
		getHttpRequestHandler().setSessionCookie(cookie);
		return this;
	}

	public HttpServiceRequest setRequestBodyToStepStore(String storeKey) {
		StateHelper.setScenarioState(storeKey, getServiceRequestBody());
		return this;
	}

	public HttpServiceRequest setRequestHeader(String name, String value) {
		getHttpRequestHandler().setRequestHeader(name, value);
		return this;
	}

	void setRequestUrl() {
		try {
			String url = getHttpServiceConfiguration().getHost() + getHttpServiceConfiguration().getUrl();
			getHttpRequestHandler().setRequestUrl(url);
		} catch (URISyntaxException e) {
e.printStackTrace();
		}
	}

	String getEncodedValue(String placeholderValue, boolean encodeUrl) {
		if (encodeUrl) {
			try {
				placeholderValue = URLEncoder.encode(placeholderValue, "UTF-8");
			} catch (UnsupportedEncodingException e) {

e.printStackTrace();			}
		}
		return placeholderValue;
	}

	protected HttpServiceRequest initHttpRequest(String proxyAddress, int proxyPort) {
		getHttpRequestHandler().createNewRequest(getRequestHttpMethod(), getHttpServiceConfiguration().getMethodName());
		getHttpRequestHandler().setRequestHeader("Content-Type", getHttpServiceConfiguration().getConentType());
		setRequestUrl();
		initServiceRequestBody(getHttpServiceConfiguration().getRequestBodyParams());
		initServiceRequestHeaders(getHttpServiceConfiguration().getRequestHeaders());
		initProxy(proxyAddress, proxyPort);
		return this;
	}

	protected void initServiceRequestBody(List<NameValuePair> httpServiceRequestBodyConfigParam) {
		if (getHttpServiceConfiguration().getConentType().toLowerCase()
				.contains("application/x-www-form-urlencoded".toLowerCase())) {
			initServiceRequestBodyFrom(httpServiceRequestBodyConfigParam);
		} else // application/Json
		{
			initServiceRequestBodyJson(httpServiceRequestBodyConfigParam);
		}
	}

	protected void initServiceRequestHeaders(List<NameValuePair> httpServiceRequestHeaders) {
		if (httpServiceRequestHeaders != null) {
			for (NameValuePair header : httpServiceRequestHeaders) {
				if (header != null) {
					getHttpRequestHandler().setRequestHeader(header.name, header.value);
				}
			}
		}
	}

	public void setHttpServiceConfiguration(HttpServiceConfiguration serviceConfig) {
		this._httpServiceConfiguration.set(serviceConfig);
	}

	public HttpServiceConfiguration getHttpServiceConfiguration() {
		return this._httpServiceConfiguration.get();
	}

	protected HttpRequestHandler getHttpRequestHandler() {
		return _httpRequestHandler.get();
	}

	protected void setHttpRequestHandler(final HttpRequestHandler httpRequestHandler) {
		this._httpRequestHandler.set(httpRequestHandler);
	}

	protected void setHttpRequestHandlerRequestBody() {
		try {
			if (getServiceRequestBody() != null) {
				getHttpRequestHandler().setRequestBody(getServiceRequestBody());
			}
			else{//set request body to empty string to avoid getting HTTP/1.1 411 Length Required when post with empty body.
				getHttpRequestHandler().setRequestBody("");
			}
		} catch (UnsupportedEncodingException e) {
e.printStackTrace();		}
	}

	protected void setServiceRequestBody(final String requestBody) {
		_requestBody.set(requestBody);
	}

	protected String getServiceRequestBody() {
		return _requestBody.get();
	}

	protected Method getRequestHttpMethod() {
		switch (getHttpServiceConfiguration().getHttpMethod()) {
		case "POST": {
			return Method.POST;
		}
		case "GET": {
			return Method.GET;
		}
		case "DELETE": {
			return Method.DELETE;
		}
		case "PUT": {
			return Method.PUT;
		}
		
		case "PATCH": {
			return Method.PATCH;
		}
		
		
		}
		return Method.POST;
	}

	private HttpServiceResponse getHttpServiceResponse(CloseableHttpResponse httpResponse) {
		ThreadLocal<HttpServiceResponse> httpServiceResponse = new ThreadLocal<>();

		httpServiceResponse.set(new HttpServiceResponse(this, httpResponse, getHttpServiceConfiguration(),
				getHttpServiceConfiguration().getName()));

		return httpServiceResponse.get();
	}

	private HttpServiceRequest resolveBodyTemplate(String templateName, boolean append) {
		String body = getServiceRequestBody();
		if (append) {
			body = body.replace(templateName,
					getHttpServiceConfiguration().getRequestBodyTemplateValue(templateName) + "," + templateName);
		} else {
			body = body.replace(templateName, getHttpServiceConfiguration().getRequestBodyTemplateValue(templateName));
		}
		setServiceRequestBody(body);
		setHttpRequestHandlerRequestBody();
		return this;
	}

	private void initServiceRequestBodyFrom(List<NameValuePair> httpServiceRequestBodyConfigParam) {
		setServiceRequestBody("method=" + getHttpServiceConfiguration().getMethodName());
		if (httpServiceRequestBodyConfigParam != null) {
			String requestBody = getServiceRequestBody();
			for (NameValuePair param : httpServiceRequestBodyConfigParam) {
				if (param != null) {
					requestBody += "&" + param.name + "=" + param.value;
					setServiceRequestBody(requestBody);
				}
			}
		}
		setHttpRequestHandlerRequestBody();
	}

	private void initServiceRequestBodyJson(List<NameValuePair> httpServiceRequestBodyConfigParam) {
		if (httpServiceRequestBodyConfigParam != null) {
			for (NameValuePair param : httpServiceRequestBodyConfigParam) {
				if (param != null) {
					setServiceRequestBody(param.value);
					break;
				}
			}
		}
		setHttpRequestHandlerRequestBody();
	}

	private void initProxy(String proxyAddress, int proxyPort) {
		if (proxyAddress != null && !proxyAddress.equals("") && proxyPort != 0) {
			this.setProxy(proxyAddress, proxyPort);
		}
	}
}