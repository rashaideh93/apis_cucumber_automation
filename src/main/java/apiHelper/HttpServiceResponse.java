package apiHelper;

import apiHelper.HttpServiceConfigurations.HttpServiceConfiguration;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

public class HttpServiceResponse {

	private ThreadLocal<CloseableHttpResponse> _httpResponse = new ThreadLocal();
	private ThreadLocal<String> _httpResponseResultAsString = new ThreadLocal();
	private ThreadLocal<String> _serviceName = new ThreadLocal();
	private ThreadLocal<HttpServiceConfiguration> _httpServiceConfiguration = new ThreadLocal();
	private ThreadLocal<ResponseResult> _responseResult = new ThreadLocal();
	private ThreadLocal<HttpServiceRequest> _httpServiceRequest = new ThreadLocal();

	public HttpServiceResponse(HttpServiceRequest httpServiceRequest, final CloseableHttpResponse httpResponse,
			final HttpServiceConfiguration httpServiceConfiguration, final String serviceName) {
		setHttpServiceRequest(httpServiceRequest);
		setHttpServiceConfiguration(httpServiceConfiguration);
		setServiceName(serviceName);
		setHttpResponse(httpResponse);
		setResultAsString(parseResultAsString());
		setResponseResult();

	}

	public HttpServiceResponse getHttpServiceResponse() {
		return this;
	}

	public HttpServiceResponse setResultAsStringToStoryStore(String sotreKey) {
		StateHelper.setStoryState(sotreKey, getResultAsString());
		return this;
	}

	public String getResultAsStringFromStoryStore(String sotreKey) {
		return StateHelper.getStoryState(sotreKey).toString();
	}

	public HttpServiceResponse setResultAsStringToStepStore(String sotreKey) {
		StateHelper.setScenarioState(sotreKey, getResultAsString());
		return this;
	}

	public String getResultAsStringFromStepStore(String sotreKey) {
		return StateHelper.getScenarioState(sotreKey).toString();
	}

	public String getResultAsString() {
		//System.err.println("[Service Response]:"+_httpResponseResultAsString.get());
		return _httpResponseResultAsString.get();
	}

	protected HttpServiceResponse setResultAsString(final String httpResponseResultAsString) {
		_httpResponseResultAsString.set(httpResponseResultAsString);
		System.out.println(this.getResultAsString());
		return this;
	}

	private String getValueToCompare() {
		return getHttpServiceConfiguration().getValueToCompare();
	}

	private void setHttpResponse(final CloseableHttpResponse httpResponse) {
		_httpResponse.set(httpResponse);
	}

	private CloseableHttpResponse getHttpResponse() {
		return _httpResponse.get();
	}

	private void setServiceName(String serviceName) {
		_serviceName.set(serviceName);
	}

	private String getServiceName() {
		return _serviceName.get();
	}

	private void setHttpServiceRequest(HttpServiceRequest httpServiceRequest) {
		_httpServiceRequest.set(httpServiceRequest);
	}

	private HttpServiceRequest getHttpServiceRequest() {
		return _httpServiceRequest.get();
	}

	private void setHttpServiceConfiguration(HttpServiceConfiguration httpServiceConfiguration) {
		_httpServiceConfiguration.set(httpServiceConfiguration);
	}

	private HttpServiceConfiguration getHttpServiceConfiguration() {
		return _httpServiceConfiguration.get();
	}

	private String parseResultAsString() {
		Parsers parser = new Parsers();
		String value = null;
		try {
			if(getHttpResponse()==null)
				value = "";
			else 
			value = parser.asString(getHttpResponse());
		} catch (ParseException | IOException e) {
		}
		return value;
	}

	public ResponseResult getResponseResult() {
		return _responseResult.get();
	}

	private void setResponseResult() {
		this._responseResult.set(new ResponseResult(getHttpServiceRequest(), getServiceName(), getResultAsString(),
				getValueToCompare(), getHttpServiceConfiguration().getJSONFilePath(),
				getHttpServiceConfiguration().getJSONCheckRules(),
				getHttpResponse() != null ? getHttpResponse().getAllHeaders() : null,
				getHttpResponse() != null && getHttpResponse().getStatusLine() != null
						? getHttpResponse().getStatusLine().getStatusCode() : 0));
	}
}