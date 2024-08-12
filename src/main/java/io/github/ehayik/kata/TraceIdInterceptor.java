package io.github.ehayik.kata;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

import static java.util.UUID.randomUUID;

@Component
class TraceIdInterceptor implements HandlerInterceptor {

    private static final String TRACE_ID_KEY = "traceId";
    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        var traceId = getOrGenerateTraceId(request);
        MDC.put(TRACE_ID_KEY, traceId);
        return true;
    }

    private String getOrGenerateTraceId(HttpServletRequest request) {
        String traceId = request.getHeader(TRACE_ID_HEADER);
        if (traceId == null || traceId.isBlank()) {
            traceId = randomUUID().toString();
        }
        return traceId;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        response.setHeader(TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY));
        MDC.remove(TRACE_ID_KEY);
    }
}