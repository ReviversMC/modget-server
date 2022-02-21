package com.github.reviversmc.modget.restservice.api;

import com.github.reviversmc.modget.restservice.beans.RestPathNotFound;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CustomErrorController implements ErrorController {


    public CustomErrorController() {
    }

    @RequestMapping("/error")
    public RestPathNotFound getDefaultError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        return new RestPathNotFound(statusCode);
    }
}
