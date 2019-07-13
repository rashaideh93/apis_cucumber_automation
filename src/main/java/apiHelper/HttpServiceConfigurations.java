package apiHelper;

import java.util.List;




public class HttpServiceConfigurations {

	public class HttpServiceConfiguration {
		private String name;
		private int statusCode ;
		private String host;
		private String httpMethod;
		private String contentType;
		private String url;
		private String method;
		private List<NameValuePair> requestBodyParams;
		private List<NameValuePair> requestHeaders;
		private List<NameValuePair> requestBodyTemplates;
		private String valueToCompare;
		private String jsonFilePath;
		private List<JSONCheckRule> jsonCheckRules;

		public String getName() {
			return name;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public String getHttpMethod() {
			return httpMethod;
		}

		public void setHttpMethod(String httpMethod) {
			this.httpMethod = httpMethod;
		}

		public String getConentType() {
			return contentType;
		}

		public void setConentType(String contentType) {
			this.contentType = contentType;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getMethodName() {
			return method;
		}

		public String getJSONFilePath() {
			return jsonFilePath;
		}

		public List<JSONCheckRule> getJSONCheckRules() {
			return jsonCheckRules;
		}

		public List<NameValuePair> getRequestBodyParams() {
			return requestBodyParams;
		}

		public List<NameValuePair> getRequestHeaders() {
			return requestHeaders;
		}

		public void setRequestHeaders(List<NameValuePair> headers) {
			requestHeaders = headers;
		}

		public String getRequestBodyTemplateValue(String templateName) {
			if (null != requestBodyTemplates) {
				for (NameValuePair template : requestBodyTemplates) {
					if (template.name.equalsIgnoreCase(templateName))
						return template.value;
				}
			}
			return "";
		}

		public String getValueToCompare() {
			return valueToCompare;
		}
	}

	class NameValuePair {

		public String name;
		public String value;
	}

	public class JSONCheckRule {
		public String node;
		public String check;
	}

	class HttpServicesConfigurationCollection {

		private String defaultHost;
		private String defaultHttpMethod;
		private String defaultContentType;
		private List<HttpServiceConfiguration> services;
		private List<NameValuePair> defaultRequestHeaders;
		private String proxyAddress;
		private String proxyPort;

		public String getDefaultHost() {
			if (defaultHost == null || defaultHost.trim() == "") {
				try {
					throw new Exception("Host cannot be empty, there is no value has been set for this configuration.");
				} catch (Exception e) {


					e.printStackTrace();
				}
			}
			return defaultHost;
		}

		public String getDefaultHttpMethod() {
			if (defaultHttpMethod == null || defaultHttpMethod.trim() == "") {
				try {
					throw new Exception(
							"Http method cannot be empty, there is no value has been set for this configuration.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return defaultHttpMethod;
		}

		public String getDefaultContentType() {
			if (defaultContentType == null || defaultContentType.trim() == "") {
				try {
					throw new Exception(
							"Content type cannot be empty, there is no value has been set for this configuration.");
				} catch (Exception e) {
e.printStackTrace();				}
			}
			return defaultContentType;
		}

		public List<HttpServiceConfiguration> getServices() {
			return services;
		};

		public List<NameValuePair> getDefaultRequestHeaders() {
			return defaultRequestHeaders;
		}

		public String getProxyAddress() {
			return proxyAddress;
		}

		public int getProxyPort() {
			try {
				if (proxyPort != null) {
					if (proxyPort.trim() == "")
						return 0;
					return Integer.parseInt(proxyPort.trim());
				}
			} catch (Exception ex) {
ex.printStackTrace();
			}
			return 0;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
