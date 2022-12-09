package snippets;

import java.net.URI;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class API {

	public static Logger logger = LogManager.getLogger(API.class);

//	public static HttpRequestInterceptor requestInterceptor = new HttpRequestInterceptor() {
//		@Override
//		public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
//			// TODO Auto-generated method stub
//
//		}
//	};

	public static HttpRequestInterceptor requestInterceptor = (request, context) -> {

	};

//	public static HttpResponseInterceptor responseInterceptor = new HttpResponseInterceptor() {
//		@Override
//		public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
//			// TODO Auto-generated method stub
//
//		}
//	};

	public static HttpResponseInterceptor responseInterceptor = (response, context) -> {

	};

	public static int timeout = 3;
	public static RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
			.setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();

	public static HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config)
			.addInterceptorFirst(requestInterceptor).addInterceptorFirst(responseInterceptor).build();

	/**
	 * @method HTTP POST
	 * @param uri     target URI
	 * @param body    request body
	 * @param headers request headers
	 * @return JSON response body
	 */

	public static JSONObject post(String uri, JSONObject body, JSONObject headers) {
		logger.info("POST request received");

		JSONObject response = new JSONObject();
		try {
			HttpPost post = new HttpPost(uri);

			Iterator<String> head_keys = headers.keys();
			while (head_keys.hasNext()) {
				String head_key = head_keys.next();
				String head_value = (String) headers.get(head_key);
				post.addHeader(head_key, head_value);
			}

			StringEntity entity = new StringEntity(body.toString(), "UTF-8");
			post.setEntity(entity);

			HttpResponse httpResponse = client.execute(post);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity2 = httpResponse.getEntity();
				String content = EntityUtils.toString(entity2, "UTF-8");
				response.put("response", new JSONObject(content));
				response.put("api_success", true);
				logger.info("POST requst successful, " + content);

			} else {
				String error = "Error via API with status code " + httpResponse.getStatusLine().getStatusCode();
				response.put("error", error);
				response.put("api_success", false);
				logger.error(error);
				logger.fatal("POST requst failed");
			}
		} catch (Exception e) {
			logger.fatal("Error while performing POST request, " + e);
		}

		return response;
	}

	/***
	 * @method HTTP GET
	 * @param uri     target URI
	 * @param headers request headers
	 * @return JSON response body
	 */

	public static JSONObject httpGet(String uri, JSONObject headers) {
		logger.info("GET request received");

		JSONObject response = new JSONObject();
		try {
			HttpGet get = new HttpGet();
			get.setURI(new URI(uri));

			Iterator<String> head_keys = headers.keys();
			while (head_keys.hasNext()) {
				String head_key = head_keys.next();
				String head_val = (String) headers.get(head_key);
				get.setHeader(head_key, head_val);
			}

			HttpResponse rsp = client.execute(get);

			if (rsp.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = rsp.getEntity();
				String content = EntityUtils.toString(entity, "UTF-8");
				response.put("response", new JSONObject(content));
				response.put("api_success", true);
				logger.info("GET request successfull, " + content);

			} else {
				String error = "Error via API with status code " + rsp.getStatusLine().getStatusCode();
				response.put("error", error);
				response.put("api_success", false);
				logger.error(error);
				logger.fatal("GET request failed");
			}

		} catch (Exception e) {
			logger.fatal("Error while performing GET request, " + e);
//			e.printStackTrace();
		}

		return response;
	}

	public static boolean isJSON(String json) {
		try {
			new JSONObject(json);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
