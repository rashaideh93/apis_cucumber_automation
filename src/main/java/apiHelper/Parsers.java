package apiHelper;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Parsers {

	/**
	 * Method used o parse HTTP response to JSON element
	 *
	 *
	 * @param response
	 *
	 *            CloseableHttpResponse response to be parsed to JSON Element
	 *
	 * @return
	 *
	 * 		response as JSON element
	 *
	 * @throws ParseException
	 * 
	 *             Signals that an error has been reached unexpectedly while
	 *             parsing.
	 * 
	 * @throws IOException
	 * 
	 *             Signals that an I/O exception of some sort has occurred. This
	 *             class is the general class of exceptions produced by failed
	 *             or interrupted I/O operations.
	 * 
	 */
	public JsonElement asJson(HttpResponse response) throws ParseException, IOException {
		String output = asString(response);
		return this.asJson(output);
	}

	/**
	 * Method used o parse JSON String to JSON element
	 *
	 *
	 * @param json
	 *
	 *            String in JSON format to be parsed to JSON Element
	 *
	 * @return
	 *
	 * 		response as JSON element
	 *
	 * @throws ParseException
	 * 
	 *             Signals that an error has been reached unexpectedly while
	 *             parsing.
	 * 
	 * @throws IOException
	 * 
	 *             Signals that an I/O exception of some sort has occurred. This
	 *             class is the general class of exceptions produced by failed
	 *             or interrupted I/O operations.
	 * 
	 */
	public JsonElement asJson(String json) throws ParseException, IOException {
		JsonParser parser = new JsonParser();
		JsonElement jsonObject = parser.parse(json);
		return jsonObject;
	}

	/**
	 * Method used to parse HTTP response to String
	 *
	 * @param response
	 *
	 *            CloseableHttpResponse response as String object
	 *
	 * @return
	 *
	 * 		response as String
	 *
	 * @throws ParseException
	 * 
	 *             Signals that an error has been reached unexpectedly while
	 *             parsing.
	 * 
	 * @throws IOException
	 * 
	 *             Signals that an I/O exception of some sort has occurred. This
	 *             class is the general class of exceptions produced by failed
	 *             or interrupted I/O operations.
	 * 
	 */
	public String asString(HttpResponse response) throws IOException {

		if (response.getEntity() == null) {

			return "";

		}

		else {
			return EntityUtils.toString(response.getEntity(), "UTF-8");
		}
	}

	/**
	 * Method used to parse HTTP response to XML
	 *
	 * @param response
	 *
	 *            CloseableHttpResponse response
	 *
	 * @return Document
	 *
	 *         response as XML Document
	 *
	 * @throws ParseException
	 *             Signals that an error has been reached unexpectedly while
	 *             parsing.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception of some sort has occurred. This
	 *             class is the general class of exceptions produced by failed
	 *             or interrupted I/O operations.
	 * 
	 * @throws ParserConfigurationException
	 *             an Exception that may occurred when calling
	 *             newDocumentBuilder using an object from
	 *             DocumentBuilderFactory.
	 * @throws SAXException
	 *             http://www.saxproject.org/apidoc/org/xml/sax/SAXException.html.
	 * 
	 */
	public Document asXML(CloseableHttpResponse response)
			throws ParseException, IOException, ParserConfigurationException, SAXException {
		String output = asString(response);
		return this.asXML(output);
	}

	/**
	 * Method used to parse xml String value to XML
	 *
	 *
	 * @param xml
	 *            XML String value need to parsed to XML Document
	 * @return Document
	 *
	 *         Document (XML) object
	 *
	 * @throws ParseException
	 *             Signals that an error has been reached unexpectedly while
	 *             parsing.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception of some sort has occurred. This
	 *             class is the general class of exceptions produced by failed
	 *             or interrupted I/O operations.
	 * @throws ParserConfigurationException
	 *             an Exception that may occurred when calling
	 *             newDocumentBuilder using an object from
	 *             DocumentBuilderFactory.
	 * @throws SAXException
	 *             http://www.saxproject.org/apidoc/org/xml/sax/SAXException.html
	 */
	public Document asXML(String xml) throws IOException, ParserConfigurationException, SAXException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xml)));
		return doc;
	}

	
	
	

	
	
	
	
	
}
