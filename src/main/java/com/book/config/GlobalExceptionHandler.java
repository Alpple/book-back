package com.book.config;


import com.book.beans.Result;
import com.book.config.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 返回自定义HTTP错误
     *
     * @param request         请求
     * @param errorMsg        自定义错误信息（Result.msg)
     * @param resultErrorCode 自定义错误信息的错误码
     * @param httpStatus      HTTP状态码
     * @return ResponseEntity<Result < String>>
     */
    private ResponseEntity<Result<String>> handleHttpException(
            HttpServletRequest request, String errorMsg, int resultErrorCode, HttpStatus httpStatus
    ) {
        String method = request.getMethod();
        String requestUrl = request.getRequestURI();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        log.info("[请求接口] - {} : {} : {} : {} : {} ", method, requestUrl, httpStatus, resultErrorCode, errorMsg);
        return new ResponseEntity<Result<String>>(Result.<String>error(null, resultErrorCode, errorMsg), headers, httpStatus);
    }

    private ResponseEntity<Result<Object>> handleHttpSuccess(
            HttpServletRequest request, Result<Object> result
    ) {
        String method = request.getMethod();
        String requestUrl = request.getRequestURI();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        log.info("[请求接口] - {} : {} : {} ", method, requestUrl, result);
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Result<Object>> generalException(HttpServletRequest request, GeneralException e) {
        e.printStackTrace();
        return handleHttpSuccess(request, Result.error(null, Result.ERROR, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<String>> missingServletRequestParameterException(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        return handleHttpException(request, "服务器未知错误", Result.UNK_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
