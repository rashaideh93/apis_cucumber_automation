package apiHelper;

import apiHelper.HttpServiceConfigurations.HttpServiceConfiguration;


public class HttpServicesBuilder {

	private ThreadLocal<HttpServicesConfigurationManager> _httpServicesConfigurationManager = new ThreadLocal<>();
	private ThreadLocal<HttpRequestHandler> _httpRequestHandler = new ThreadLocal<>();

	public HttpServicesBuilder(String servicesConfiguratinFilePath) {
		try {
			setHttpRequestHandler();
			setHttpServicesConfigurationManager(servicesConfiguratinFilePath);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HttpServiceRequest build(String serviceName) {
		ThreadLocal<HttpServiceRequest> serviceRequest = new ThreadLocal<>();
		try {
			HttpServiceConfiguration httpServiceConfiguration = getHttpServicesConfigurationManager()
					.getHttpServiceConfiguration(serviceName);
			serviceRequest.set(new HttpServiceRequest(getHttpRequestHandler(), httpServiceConfiguration,
					getHttpServicesConfigurationManager().getHttpServicesConfigurationCollection().getProxyAddress(),
					getHttpServicesConfigurationManager().getHttpServicesConfigurationCollection().getProxyPort()));
			// throw exception in case service object is null
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serviceRequest.get();
	}

	protected void setHttpRequestHandler() {
		_httpRequestHandler.set(HttpRequestHandler.getInstance());
	}

	protected HttpRequestHandler getHttpRequestHandler() {
		return _httpRequestHandler.get();
	}

	protected HttpServicesConfigurationManager getHttpServicesConfigurationManager() {
		return _httpServicesConfigurationManager.get();
	}

	protected void setHttpServicesConfigurationManager(final String servicesConfiguratinFilePath) throws Exception {

		if (_httpServicesConfigurationManager.get() == null) {
			if (servicesConfiguratinFilePath == null || servicesConfiguratinFilePath.trim() == "") {

				throw new Exception(
						"Invalid configuration file (null reference), http services configuration object has not been initiated.");
			}
			try {
				_httpServicesConfigurationManager
						.set(new HttpServicesConfigurationManager(servicesConfiguratinFilePath));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}