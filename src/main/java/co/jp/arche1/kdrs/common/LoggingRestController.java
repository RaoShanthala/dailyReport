package co.jp.arche1.kdrs.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;

@Component
public class LoggingRestController extends OncePerRequestFilter {
    private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
                    MediaType.valueOf("text/*"),
                    MediaType.APPLICATION_FORM_URLENCODED,
                    MediaType.APPLICATION_JSON,
                    MediaType.APPLICATION_XML,
                    MediaType.valueOf("application/*+json"),
                    MediaType.valueOf("application/*+xml"),
                    MediaType.MULTIPART_FORM_DATA
                );
    private static final List<MediaType> FORM_TYPES = Arrays.asList(
                    MediaType.APPLICATION_FORM_URLENCODED,
                    MediaType.MULTIPART_FORM_DATA
                    );
    private final ThreadLocal<Map<String, Object>> localParams = ThreadLocal.withInitial(() -> new LinkedHashMap<>());

    @Autowired
    private ObjectMapper mapper;

    @Override
    protected void initFilterBean() throws ServletException {
        // アプリ起動時に呼び出される。
    	//logger.info("start initFilterBean()");
    }
    @Override
    public void destroy() {
        // アプリ終了時に呼び出される
    	//logger.info("start destroy()");
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
    	//logger.info("start doFilterInternal()");

    	// プロキシを介してアクセスしたクライアントのIPアドレス
    	//String xForwardedFor = request.getHeader("X-Forwarded-For");
    	//String remoteIpAddress;
    	//if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
        //    remoteIpAddress = xForwardedFor.split(",")[0].trim();
        //} else {
        //    remoteIpAddress = request.getRemoteAddr();
        //}

    	//String remoteIpAddress = request.getRemoteAddr();
        //System.out.println(remoteIpAddress);
        //String remoteHost = request.getRemoteHost();
        //System.out.println(remoteHost);

        //System.out.println(formatIpAddress(remoteIpAddress));

        // スレッドローカルから取り出す
        //SecurityContext ctx = SecurityContextHolder.getContext();
        //if (null != ctx) {
        //    System.out.println(ctx.getAuthentication().getName());
        //}

        if (isAsyncDispatch(request)) {
        	// 後続処理(Servlet 等)、コントローラの呼び出し
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);

//2020.12.23
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
//            response.setHeader("Access-Control-Max-Age", "12000");
//            response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
//            response.setHeader("Access-Control-Expose-Headers", "*");

        }
    }

    //private String formatIpAddress(String ipAddress) {
    //    String[] nums = ipAddress.split("\\.");
    //    StringBuilder stringBuilder = new StringBuilder();
    //    for (String num : nums) {
    //        if (stringBuilder.length() > 0) {
    //            stringBuilder.append('.');
    //        }
    //        stringBuilder.append(String.format("%03d", Integer.parseInt(num)));
    //    }
    //    return stringBuilder.toString();
    //}

    protected void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {
        try {
        	// request時の前処理、後続処理を行いたくない場合、ここでreturn
            beforeRequest(request, response);
            outputParams();			// ログ出力用データの出力
            localParams.remove();	// ログ出力用データの削除

            // 後続処理(Servlet 等)、コントローラの呼び出し
            filterChain.doFilter(request, response);

        } finally {
        	// request時に対する処理後の後処理
            afterRequest(request, response);
            outputParams();			// ログ出力用データの出力
            localParams.remove();	// ログ出力用データの削除
            // 通常、レスポンスボディはストリームに書き込まれてメモリから消えてしまうため、メモリに一時的に保持する必要がある。
            // Springで用意されている ContentCachingResponseWrapper というクラスを使うと、レスポンスボディをバイト配列で取り出すことができまる。
            // 次の後処理でレスポンスボディがストリームに書き込まれるようにする。次の処理をしないとTomcatの場合は待ち状態になってしまう。
            response.copyBodyToResponse();
        }
    }

    protected void beforeRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        if (logger.isDebugEnabled()) {
            throwable("logRequestHeader", () -> logRequestHeader(request));
            throwable("logRequestBody", () -> logRequestBody(request));
        }
    }

    protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        if (logger.isDebugEnabled()) {
            //throwable("logRequestBody", () -> logRequestBody(request));
            throwable("logResponse", () -> logResponse(response));
        }
    }

    private void logRequestHeader(ContentCachingRequestWrapper request) {
        localParams.get().put("client", request.getRemoteAddr());
        String queryString = request.getQueryString();
        if (queryString == null) {
            localParams.get().put("request", request.getMethod() + " " + request.getRequestURI());
        }
        else {
            localParams.get().put("request", request.getMethod() + " " + request.getRequestURI() + "?" + queryString);
        }
        Map<String, List<String>> headers = new LinkedHashMap<>();
        Collections.list(request.getHeaderNames()).forEach(headerName ->
            Collections.list(request.getHeaders(headerName)).forEach(headerValue -> {
                if (headers.containsKey(headerName)) {
                    headers.get(headerName).add(headerValue);
                }
                else {
                    headers.put(headerName, new ArrayList<>());
                    headers.get(headerName).add(headerValue);
                }
            }));
        localParams.get().put("requestHeaders", headers);
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (null != ctx) {
            localParams.get().put("authentication", convertTo(ctx.getAuthentication()));
        }
     }

    private void logRequestBody(ContentCachingRequestWrapper request) throws ServletException, IOException {
        Collection<Part> parts = getRequestParts(request);
        if (parts.isEmpty()) {
            byte[] content = request.getContentAsByteArray();
            if (content.length > 0) {
                logContent(content, request.getContentType(), request.getCharacterEncoding(), "requestBody");
            }
            else {
                localParams.get().put("requestBody", "[empty body]");
            }
        }
        else {
            Map<String, Object> contents = new LinkedHashMap<>();
            parts.stream().forEach(p -> {
                String name = p.getName();
                try {
                    byte[] content = FileCopyUtils.copyToByteArray(p.getInputStream());
                    contents.put(name, logContent(content, p.getContentType(), "UTF-8"));
                }
                catch (IOException e) {
                    String fileName = p.getSubmittedFileName();
                    if (StringUtils.hasText(fileName)) {
                        contents.put(name, String.format("fileName[%s]", fileName));
                    }
                    else {
                        contents.put(name, String.format("IOException[%s]", e.getMessage()));
                    }
                }
            });
            localParams.get().put("requestBody", contents);
        }
    }

    private Collection<Part> getRequestParts(ContentCachingRequestWrapper request) throws ServletException, IOException {
        MediaType mediaType = getMediaType(request.getContentType());
        boolean isPost = FORM_TYPES.stream().anyMatch(formType -> formType.includes(mediaType));
        return isPost ? request.getParts() : Collections.emptyList();
    }

    private void logResponse(ContentCachingResponseWrapper response) {
        int status = response.getStatus();
        localParams.get().put("response", status + " " + HttpStatus.valueOf(status).getReasonPhrase());
        Map<String, List<String>> headers = new LinkedHashMap<>();
        response.getHeaderNames().forEach(headerName ->
            response.getHeaders(headerName).forEach(headerValue -> {
                if (headers.containsKey(headerName)) {
                    headers.get(headerName).add(headerValue);
                }
                else {
                    headers.put(headerName, new ArrayList<>());
                    headers.get(headerName).add(headerValue);
                }
            }));
        localParams.get().put("responseHeaders", headers);
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            //logContent(content, response.getContentType(), response.getCharacterEncoding(), "responseBody");
            logContent(content, response.getContentType(), "UTF-8", "responseBody");
        }
    }

    private void logContent(byte[] content, String contentType, String contentEncoding, String key) {
        localParams.get().put(key, logContent(content, contentType, contentEncoding));
    }

    private Object logContent(byte[] content, String contentType, String contentEncoding) {
        MediaType mediaType = getMediaType(contentType);
        boolean visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
        boolean json = mediaType.getSubtype().contains("json");
        if (visible) {
            try {
                String contentString = new String(content, contentEncoding);
                if (json) {
                    return mapper.readTree(contentString);
                }
                else {
                    return contentString;
                }
            }
            catch (IOException e) {
                return String.format("[%d bytes content]", content.length);
            }
        }
        else {
            return String.format("[%d bytes content]", content.length);
        }
    }

    private MediaType getMediaType(String contentType) {
        try {
            return MediaType.valueOf(contentType);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }

    private void outputParams() {
        logger.debug(unparse(mapper, localParams.get()));
    }

    private JsonNode convertTo(Object obj) {
        NullNode node = mapper.getDeserializationConfig().getNodeFactory().nullNode();
        if (null == obj) {
            return node;
        }
        try {
            return mapper.readTree(mapper.writeValueAsString(obj));
        }
        catch (IOException e) {
            return node;
        }
    }

    private void throwable(String key, ThrowableExecutor method) {
        try {
            method.apply();
        }
        catch (Exception e) {
            localParams.get().put(key, String.format("%s[%s]", e.getClass().getSimpleName(), e.getMessage()));
        }
    }

    public static String unparse(ObjectMapper mapper, Object obj) {
        if (null == obj) {
            return "null";
        }
        if (null == mapper) {
            return String.valueOf(obj);
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }

    public static interface ThrowableExecutor {
        void apply() throws Exception;
    }
}
