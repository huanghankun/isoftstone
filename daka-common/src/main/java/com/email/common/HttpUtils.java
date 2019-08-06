package com.email.common;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @Description: HTTP工具类
 * 例子:
 *  CloseableHttpResponse userNamePost = HttpUtils.doHttpsPost("http://i.isoftstone.com/apigateway/UserInfoService/GetForMobile", "{\"UserName\":\"" + userName + "\"}", headersMap);
 *  String respJson = EntityUtils.toString(userNamePost.getEntity());
 *  EmployeeInfo employeeInfo = JSON.parseObject(respJson, EmployeeInfo.class);
 * @author ex_huanghk5
 * @date 2018-07-12 13:49
 * @version V1.0
 */
public class HttpUtils {
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String DEFAULT_ENCODING = "UTF-8";
    private static int socketTimeout = 2 * 1000;  //获取数据超时参数
    private static int connectTimeout = 5 * 1000; //连接超时参数
    private static SSLConnectionSocketFactory socketFactory;
    private static TrustManager manager = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };


    public HttpUtils() {
    }

    public static CloseableHttpResponse doGet(String url, Map<String, String> headers, String refer) throws ClientProtocolException, IOException {
        HttpGet get = new HttpGet(url);
        setRequestHeader(get, headers, refer);
        CloseableHttpResponse response = buildHttpClient().execute(get);
        return response;
    }

    public static CloseableHttpResponse doGet(String url, Map<String, String> headers) throws ClientProtocolException, IOException {
        return doGet(url, headers, null);
    }

    public static CloseableHttpResponse doPost(String url, List<NameValuePair> values, Map<String, String> headers, String refer) throws ClientProtocolException, IOException {
        return doPost(url, values, headers, refer, null);
    }

    public static CloseableHttpResponse doPost(String url, List<NameValuePair> values, Map<String, String> headers) throws ClientProtocolException, IOException {
        return doPost(url, values, headers, null, null);
    }

    public static CloseableHttpResponse doPost(String url, List<NameValuePair> values, Map<String, String> headers, String refer, String contentType) throws ClientProtocolException, IOException {
        HttpPost post = new HttpPost(url);
        setRequestHeader(post, headers, refer);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(values, Consts.UTF_8);
        if (contentType != null && !"".equals(contentType.trim())) {
            entity.setContentType(contentType);
        }
        post.setEntity(entity);
        CloseableHttpResponse response = buildHttpClient().execute(post);
        return response;
    }

    public static CloseableHttpResponse doPost(String url, String contentType, List<NameValuePair> values, Map<String, String> headers) throws ClientProtocolException, IOException {
        return doPost(url, values, headers, null, contentType);
    }

    public static CloseableHttpResponse doPost(String url, String json, Map<String, String> headers, String refer) throws ClientProtocolException, IOException {
        HttpPost post = new HttpPost(url);
        setRequestHeader(post, headers, refer);
        StringEntity entity = new StringEntity(json, DEFAULT_ENCODING);
        entity.setContentEncoding(DEFAULT_ENCODING);
        entity.setContentType(CONTENT_TYPE_JSON);
        post.setEntity(entity);
        CloseableHttpResponse response = buildHttpClient().execute(post);
        return response;
    }

    public static CloseableHttpResponse doHttpsGet(String url, Map<String, String> headers, String refer) throws ClientProtocolException, IOException {
        HttpGet get = new HttpGet(url);
        setRequestHeader(get, headers, refer);
        CloseableHttpResponse response = buildHttpsGetClient().execute(get);
        return response;
    }

    public static CloseableHttpResponse doHttpsPost(String url, List<NameValuePair> values, Map<String, String> headers, String refer) throws ClientProtocolException, IOException {
        return doHttpsPost(url, values, headers, refer, DEFAULT_ENCODING);
    }

    public static CloseableHttpResponse doHttpsPost(String url, List<NameValuePair> values, Map<String, String> headers, String refer, String contentType) throws ClientProtocolException, IOException {
        HttpPost post = new HttpPost(url);
        setRequestHeader(post, headers, refer);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(values, contentType);
        post.setEntity(entity);
        CloseableHttpResponse response = buildHttpsPostClient().execute(post);
        return response;
    }

    public static CloseableHttpResponse doHttpsPost(String url, String json, Map<String, String> headers, String refer, String encoding, int socketTimeout, int connectTimeout) throws ClientProtocolException, IOException {
        HttpPost post = new HttpPost(url);
        setRequestHeader(post, headers, refer);
        if (json != null && !"".equals(json)) {
            StringEntity entity = new StringEntity(json, encoding);
            entity.setContentEncoding(encoding);
            entity.setContentType(CONTENT_TYPE_JSON);
            post.setEntity(entity);
        }
        CloseableHttpResponse response = buildHttpsClient(socketTimeout, connectTimeout).execute(post);
        return response;
    }

    public static CloseableHttpResponse doHttpsPost(String url, String json, Map<String, String> headers) throws IOException {
        return doHttpsPost(url, json, headers, null, DEFAULT_ENCODING, socketTimeout, connectTimeout);
    }

    public static CloseableHttpResponse doHttpsPost(String url, String json, Map<String, String> headers, int socketTimeout, int connectTimeout) throws ClientProtocolException, IOException {
        return doHttpsPost(url, json, headers, DEFAULT_ENCODING, socketTimeout, connectTimeout);
    }

    public static CloseableHttpResponse doHttpsPost(String url, String json, Map<String, String> headers, String encoding, int socketTimeout, int connectTimeout) throws ClientProtocolException, IOException {
        return doHttpsPost(url, json, headers, null, encoding, socketTimeout, connectTimeout);
    }

    public static CloseableHttpClient buildHttpClient() {
        RequestConfig config = RequestConfig.custom().setCookieSpec("standard-strict").build();
        return HttpClients.custom().setDefaultRequestConfig(config).build();
    }

    public static CloseableHttpClient buildHttpsGetClient() {
        return buildHttpsClient(socketTimeout, connectTimeout);
    }

    public static CloseableHttpClient buildHttpsPostClient() {
        return buildHttpsClient(socketTimeout, connectTimeout);
    }

    public static CloseableHttpClient buildHttpsClient(int socketTimeout, int connectTimeout) {
        enableSSL();
        RequestConfig.Builder builder = RequestConfig.custom()
                .setCookieSpec("standard-strict")
                .setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(new String[]{"NTLM", "Digest"}))
                .setProxyPreferredAuthSchemes(Arrays.asList(new String[]{"Basic"}));
        if (socketTimeout > 0)
            builder.setSocketTimeout(socketTimeout);    //设置请求超时时间
        if (connectTimeout > 0)
            builder.setConnectTimeout(connectTimeout);  //设置传输超时时间
        RequestConfig defaultRequestConfig = builder.build();
        Registry socketFactoryRegistry = RegistryBuilder.create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", socketFactory).build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        return HttpClients.custom().setConnectionManager(connectionManager)
                .setDefaultRequestConfig(defaultRequestConfig).build();
    }

    private static void enableSSL() {
        try {
            SSLContext e = SSLContext.getInstance("TLS");
            e.init((KeyManager[]) null, new TrustManager[]{manager}, (SecureRandom) null);
            socketFactory = new SSLConnectionSocketFactory(e, NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException var1) {
            var1.printStackTrace();
        } catch (KeyManagementException var2) {
            var2.printStackTrace();
        }

    }

    private static void setRequestHeader(HttpRequestBase request, Map<String, String> headers, String refer) {
        setRequestHeader(request, headers);
        if (refer != null) {
            request.setHeader("Referer", refer);
        }

    }

    private static void setRequestHeader(HttpRequestBase request, Map<String, String> headers) {
        if (headers != null && headers.size() > 0) {
            Iterator var3 = headers.entrySet().iterator();

            while (var3.hasNext()) {
                Entry entry = (Entry) var3.next();
                request.setHeader((String) entry.getKey(), (String) entry.getValue());
            }
        }

    }
}