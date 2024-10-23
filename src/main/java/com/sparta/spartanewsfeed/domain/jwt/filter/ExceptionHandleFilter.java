package com.sparta.spartanewsfeed.domain.jwt.filter;

import com.sparta.spartanewsfeed.exception.customException.HasNotPermissionException;
import com.sparta.spartanewsfeed.exception.customException.NotFoundEntityException;
import com.sparta.spartanewsfeed.exception.customException.NotValidCookieException;
import com.sparta.spartanewsfeed.exception.customException.NotValidTokenException;
import com.sparta.spartanewsfeed.exception.dto.ResponseExceptionCode;
import com.sparta.spartanewsfeed.exception.enums.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j(topic = "FilterException")
@Component
@Order(2)
public class ExceptionHandleFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            chain.doFilter(request, response);

        } catch (NotValidTokenException e) {
            setExceptionToResponse(httpResponse, e.getExceptionCode());

        } catch (NotValidCookieException e) {
            setExceptionToResponse(httpResponse, e.getExceptionCode());

        } catch (HasNotPermissionException e) {
            setExceptionToResponse(httpResponse, e.getExceptionCode());

        } catch (NotFoundEntityException e) {
            setExceptionToResponse(httpResponse, e.getExceptionCode());
        }
    }

    private void setExceptionToResponse(HttpServletResponse httpServletResponse, ExceptionCode exceptionCode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        httpServletResponse.setStatus(exceptionCode.getHttpStatus().value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ResponseExceptionCode responseExceptionCode = ResponseExceptionCode.builder()
                .code(exceptionCode.name())
                .message(exceptionCode.getMessage())
                .build();

        log.error("{}: {}", exceptionCode, exceptionCode.getMessage());

        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(responseExceptionCode));
    }
}
