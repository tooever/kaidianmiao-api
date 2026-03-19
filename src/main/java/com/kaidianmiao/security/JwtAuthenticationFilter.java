package com.kaidianmiao.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaidianmiao.common.ErrorCode;
import com.kaidianmiao.common.Result;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * JWT 认证过滤器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    
    /**
     * 不需要 Token 验证的路径
     */
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/api/auth/**",
            "/api/admin/login",
            "/error",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );
    
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestPath = request.getRequestURI();
        
        // 检查是否是需要验证的路径
        if (isExcludedPath(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // 获取 Authorization header
        String token = resolveToken(request);
        
        if (!StringUtils.hasText(token)) {
            sendUnauthorizedResponse(response, ErrorCode.UNAUTHORIZED, "缺少认证令牌", "请先登录");
            return;
        }
        
        // 检查 Token 是否过期
        if (jwtTokenProvider.isTokenExpired(token)) {
            sendUnauthorizedResponse(response, ErrorCode.TOKEN_EXPIRED, "Token expired", "登录已过期，请重新登录");
            return;
        }
        
        // 验证 Token
        if (!jwtTokenProvider.validateToken(token)) {
            sendUnauthorizedResponse(response, ErrorCode.TOKEN_INVALID, "Token invalid", "无效的认证令牌");
            return;
        }
        
        // 将用户信息放入请求属性
        Long userId = jwtTokenProvider.getUserId(token);
        String role = jwtTokenProvider.getRole(token);
        
        request.setAttribute("userId", userId);
        request.setAttribute("role", role);
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * 检查路径是否不需要验证
     */
    private boolean isExcludedPath(String path) {
        return EXCLUDED_PATHS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
    
    /**
     * 从请求中解析 Token
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    /**
     * 发送未授权响应
     */
    private void sendUnauthorizedResponse(HttpServletResponse response, int code, String message, String userMessage) 
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        Result<Void> result = Result.error(code, message, userMessage);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}