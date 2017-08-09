package libcore.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {
	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	public static int TimeOut = 60;
	public static int _connTimeOut = 30000;// 连接时间10s
	public static int _soTimeOut = 60000;// 数据传输时间30s

	static final int TYPICAL_LENGTH = 202400;
	static final int maxPageSize = 1024;// KB
	static final int connectNum = 1;

	static String defaultCharset = "UTF-8";

	public static String _ua = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0";

	/**
	 * 读取网页
	 * 
	 * @param url
	 *                = url地址
	 * @param encode
	 *                = 编码
	 * 
	 * @return 网页内容
	 */
	public static String get(String url) {
		return get(url, defaultCharset);
	}

	public static String get(String url, String encode) {
		return get(url, encode, _ua);
	}
	public static String get(String url, String encode, String ua) {
		return get(url, encode, ua, null);
	}

	public static String get(String url, String encode, String ua, String refer) {
		logger.debug("get=" + url);
		CloseableHttpClient client = HttpClients.createDefault();

		HttpGet httpget = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.BEST_MATCH)
				.setExpectContinueEnabled(true)
				.setStaleConnectionCheckEnabled(true)
				.setSocketTimeout(_connTimeOut)
				.setConnectTimeout(_connTimeOut)
				.setConnectionRequestTimeout(_connTimeOut)
				.build();
		httpget.setConfig(requestConfig);

		httpget.setHeader("User-Agent", ua);

		httpget.setHeader("Referer", refer != null ? refer : url);
		httpget.setHeader("Accept-Language", "zh-cn");
		// httpget.setHeader("Connection","Keep-Alive");
		httpget.setHeader("Http-Accept",
				"text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
		httpget.setHeader("Http-Accept-Encoding", "gzip,deflate");
		httpget.setHeader("Http-Accept-Charset", "gb2312,utf-8;q=0.7,*;q=0.7");
		// httpget.setHeader("Http-keep-Alive","300");
		// httpget.setHeader("Http-Cache-Control","max-age=0");

		try {
			CloseableHttpResponse httpResponse = client.execute(httpget);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				logger.error(url + ", failed: " + statusCode);

				httpget.abort();
				return null;
			} else {
				HttpEntity entity = httpResponse.getEntity();
				byte[] bytes = EntityUtils.toByteArray(entity);

				String charSet = entity.getContentType().getValue();
				if (charSet != null) {
					String arr[] = charSet.split("charset=");
					charSet = arr.length == 2 ? arr[1] : "";
				}
				charSet = charSet != null && charSet.length() > 0 ? charSet : encode;

				httpget.abort();
				return new String(bytes, charSet);
			}
		} catch (Exception e) {
			logger.error("get Error(" + url + "):", e);
			return null;
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				logger.error("get Error(" + url + "):", e);
			}
		}
	}

	/**
	 * 读取网页
	 * 
	 * @param url
	 *                = url地址
	 * @param encode
	 *                = 编码
	 * 
	 * @return 网页内容
	 */
	public static String post(String url) {
		return post(url, null, defaultCharset);
	}

	public static String post(String url, ArrayList<BasicNameValuePair> list) {
		return post(url, list, defaultCharset, _ua);
	}

	public static String post(String url, ArrayList<BasicNameValuePair> list, String encode) {
		return post(url, list, encode, _ua);
	}
	public static String post(String url, ArrayList<BasicNameValuePair> list, String encode, String ua) {
		return post(url, list, encode, _ua, null);
	}
	public static String post(String url, ArrayList<BasicNameValuePair> list, String encode, String ua, String refer) {
		logger.debug("post=" + url);
		CloseableHttpClient client = HttpClients.createDefault();

		HttpPost httppost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.BEST_MATCH)
				.setExpectContinueEnabled(true)
				.setStaleConnectionCheckEnabled(true)
				.setSocketTimeout(_connTimeOut)
				.setConnectTimeout(_connTimeOut)
				.setConnectionRequestTimeout(_connTimeOut)
				.build();
		httppost.setConfig(requestConfig);

		httppost.setHeader("User-Agent", ua);
		
		httppost.setHeader("Referer", refer != null ? refer : url);
		httppost.setHeader("Accept-Language", "zh-cn");
		// httpget.setHeader("Connection","Keep-Alive");
		httppost.setHeader("Http-Accept",
				"text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
		httppost.setHeader("Http-Accept-Encoding", "gzip,deflate");
		httppost.setHeader("Http-Accept-Charset", "gb2312,utf-8;q=0.7,*;q=0.7");
		// httpget.setHeader("Http-keep-Alive","300");
		// httpget.setHeader("Http-Cache-Control","max-age=0");

		try {
			httppost.setEntity(new UrlEncodedFormEntity(list, encode));
			CloseableHttpResponse httpResponse = client.execute(httppost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				logger.error(url + ", failed: " + statusCode);

				httppost.abort();
				return null;
			} else {
				HttpEntity entity = httpResponse.getEntity();
				byte[] bytes = EntityUtils.toByteArray(entity);

				String charSet = entity.getContentType().getValue();
				if (charSet != null) {
					String arr[] = charSet.split("charset=");
					charSet = arr.length == 2 ? arr[1] : "";
				}
				charSet = charSet != null && charSet.length() > 0 ? charSet : encode;

				httppost.abort();
				return new String(bytes, charSet);
			}
		} catch (Exception e) {
			logger.error("get Error(" + url + "):", e);
			return null;
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				logger.error("get Error(" + url + "):", e);
			}
		}
	}

	/**
	 * post 流数据
	 * 
	 * @param url
	 *                = url地址
	 * @param encode
	 *                = 编码
	 * 
	 * @return 网页内容
	 */
	public static String postStream(String url) {
		return postStream(url, null, defaultCharset);
	}

	public static String postStream(String url, String data) {
		return postStream(url, data, defaultCharset, _ua);
	}

	public static String postStream(String url, String data, String encode) {
		return postStream(url, data, encode, _ua);
	}
	public static String postStream(String url, String data, String encode, String ua) {
		return postStream(url, data, encode, _ua, null);
	}
	public static String postStream(String url, String data, String encode, String ua, String refer) {
		logger.debug("postStream=" + url);
		CloseableHttpClient client = HttpClients.createDefault();

		
		HttpPost httppost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom()
				.setCookieSpec(CookieSpecs.BEST_MATCH)
				.setExpectContinueEnabled(true)
				.setStaleConnectionCheckEnabled(true)
				.setSocketTimeout(_connTimeOut)
				.setConnectTimeout(_connTimeOut)
				.setConnectionRequestTimeout(_connTimeOut)
				.build();
		httppost.setConfig(requestConfig);

		httppost.setHeader("User-Agent", ua);
		
		httppost.setHeader("Referer", refer != null ? refer : url);
		httppost.setHeader("Accept-Language", "zh-cn");
		// httpget.setHeader("Connection","Keep-Alive");
		httppost.setHeader("Http-Accept",
				"text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
		httppost.setHeader("Http-Accept-Encoding", "gzip,deflate");
		httppost.setHeader("Http-Accept-Charset", "gb2312,utf-8;q=0.7,*;q=0.7");
		// httpget.setHeader("Http-keep-Alive","300");
		// httpget.setHeader("Http-Cache-Control","max-age=0");

		try {
//			InputStreamEntity sEntity = new InputStreamEntity(new ByteArrayInputStream(data.getBytes(encode)), -1);
//			sEntity.setContentType("binary/octet-stream");  
//			sEntity.setChunked(true);  
			
			StringEntity sEntity = new StringEntity(data, encode);
			sEntity.setChunked(true); 
			httppost.setEntity(sEntity);
			CloseableHttpResponse httpResponse = client.execute(httppost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				logger.error(url + ", failed: " + statusCode);

				httppost.abort();
				return null;
			} else {
				HttpEntity entity = httpResponse.getEntity();
				byte[] bytes = EntityUtils.toByteArray(entity);

				String charSet = entity.getContentType().getValue();
				if (charSet != null) {
					String arr[] = charSet.split("charset=");
					charSet = arr.length == 2 ? arr[1] : "";
				}
				charSet = charSet != null && charSet.length() > 0 ? charSet : encode;

				httppost.abort();
				return new String(bytes, charSet);
			}
		} catch (Exception e) {
			logger.error("get Error(" + url + "):", e);
			return null;
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				logger.error("get Error(" + url + "):", e);
			}
		}
		
	}

	/**
	 * 读取网页
	 * 
	 * @param urlStr
	 *                url地址
	 * @return 网页内容
	 */
	public static String getPage(String urlStr) {
		return getPage(urlStr, null);
	}

	public static String getPage(String urlStr, String ua) {
		byte[] content = getPageByte(urlStr, ua);
		String returnStr = null;
		if (content != null) {
			returnStr = new String(content);
		} else {
			logger.error("getPage error  " + urlStr);
		}
		return returnStr;
	}

	public static byte[] getPageByte(String urlStr) {
		return getPageByte(urlStr, null);
	}

	/**
	 * 读取网页内容
	 * 
	 * @param urlStr
	 *                url地址
	 * @return 网页字节内容
	 */
	public static byte[] getPageByte(String urlStr, String ua) {
		logger.debug("getPageByte=" + urlStr);
		byte[] buf = null;
		if (urlStr.indexOf("http://") != 0 && urlStr.indexOf("https://") != 0) {
			urlStr = "http://" + urlStr;
		}
		HttpURLConnection conn = null;
		try {
			System.setProperty("sun.net.client.defaultConnectTimeout", "15000");
			System.setProperty("sun.net.client.defaultReadTimeout", "15000");
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			if (ua != null) {
				conn.setRequestProperty("User-Agent", ua);
			}

			// conn.setRequestProperty("User-Agent",
			// "Mozilla/4.0 (compatible; MSIE 6.0;Windows 2000)");
			// conn.setRequestProperty("Expect", "100-continue");
			conn.setRequestProperty("Cookie", "");
			// conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setUseCaches(false);
			conn.setReadTimeout(15000);

			// conn.connect();

			// conn.setRequestMethod("GET");
			// conn.setDoOutput(true);
			// conn.setRequestProperty("Content-Type",
			// "application/x-www-form-urlencoded");
			// conn.connect();

			// PrintWriter out = new PrintWriter(new
			// OutputStreamWriter(conn.getOutputStream(), "utf-8"));
			// out.println("");
			// out.close();

			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				// 从数据流中读取数据并处理
				conn.getContentLength();
				int maxBytes = maxPageSize * 1024;
				int expectedLength = conn.getContentLength();
				if (expectedLength == -1)
					expectedLength = TYPICAL_LENGTH;
				buf = new byte[expectedLength];
				int n;
				int total = 0;
				while ((n = in.read(buf, total, buf.length - total)) != -1) {
					total += n;
					if (total == buf.length) {
						// try to read one more
						// character
						int c = in.read();
						if (c == -1)
							break; // EOF, we're
								// done
						else {
							byte[] newbuf = new byte[Math.min(buf.length * 2, maxBytes)];
							System.arraycopy(buf, 0, newbuf, 0, buf.length);
							buf = newbuf;
							buf[total++] = (byte) c;
						}
					}
				}
				in.close();
				if (total != buf.length) {
					// resize the array to be precisely
					// total bytes long
					byte[] newbuf = new byte[total];
					System.arraycopy(buf, 0, newbuf, 0, total);
					buf = newbuf;
				}
			}

		} catch (Exception e) {
			logger.error("getPageByte Error(" + urlStr + "):", e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return buf;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("abcd", "中国123"));

		//System.out.println(post("http://localhost/print_headers.php?id=111", list));
		System.out.println(postStream("http://localhost/print_headers.php?id=111", "333333中国33333333aasdfasfdafsd"));
		
		// System.out.println(HttpUtil.get("http://www.google.com.hk/",""));
		// String urlStr = "http://www.huacai.com";
		// String resp = NetUtil.getPage(urlStr);
		// logger.error("resp=" + resp);

	}

}
