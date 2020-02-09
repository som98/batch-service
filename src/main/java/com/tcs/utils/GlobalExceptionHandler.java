package com.tcs.utils;

import com.tcs.models.ResponseModel;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    public ResponseModel sizeExceedHandler(MultipartException e, RedirectAttributes redirectAttributes){
        return new ResponseModel(false,e.getMessage());
    }
}
