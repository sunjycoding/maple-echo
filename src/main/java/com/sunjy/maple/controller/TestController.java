package com.sunjy.maple.controller;

import com.sunjy.maple.model.HttpResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author created by sunjy on 12/1/23
 */
@RequestMapping("test")
@RequiredArgsConstructor
@RestController
public class TestController {

    @GetMapping
    public HttpResult<Object> test() {
        return HttpResult.success();
    }
}
