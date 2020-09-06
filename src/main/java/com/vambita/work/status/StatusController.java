package com.vambita.work.status;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/status")
public class StatusController {

    @GetMapping()
    public HostResponse status() {
        return HostResponse.builder().build();
    }

}
