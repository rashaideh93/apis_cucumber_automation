package apiHelper;

import java.io.Serializable;

public class HttpResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int responseCode;
	public String responseContent;

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}

	@Override
	public String toString() {
		return "ResponseObject [responseCode=" + responseCode + ", responseContent=" + responseContent + "]";
	}

}
