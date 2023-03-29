package com.example.handler;

import com.example.enums.CustomExceptionType;
import com.example.exception.CustomException;
import com.example.util.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/***
 * @description: 全局异常处理器
 * @author: yk
 **/
@Slf4j
@ControllerAdvice
public class WebExceptionHandler {
    //处理程序员主动抛出的自定义异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public AjaxResponse handleException(CustomException e){
        if (e.getCode() == CustomExceptionType.SYSTEM_ERROR.getCode()) {
            log.info(CustomExceptionType.SYSTEM_ERROR.getDesc());
            //400异常不需要持久化，将异常信息以友好的方式告知用户就可以
            //将500异常信息持久化处理，方便运维人员处理
        }
        return AjaxResponse.error(e);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public AjaxResponse handleBindException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        assert fieldError != null;
        return AjaxResponse.error(new CustomException(CustomExceptionType.PARAM_ERROR, fieldError.getDefaultMessage()));
    }
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public AjaxResponse handleBindException(BindException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        assert fieldError != null;
        return AjaxResponse.error(new CustomException(CustomExceptionType.PARAM_ERROR, fieldError.getDefaultMessage()));
    }
}
