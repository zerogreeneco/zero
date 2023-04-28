package com.zerogreen.zerogreeneco.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ProfileController {

    private final Environment env;

    @GetMapping("/profile")
    public String profile() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        log.info("yjyjtest"+profiles);
        List<String> realProfiles = Arrays.asList("real", "real1", "real2");
        String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);
        log.info("yjyjtest22222"+defaultProfile);

        log.info("yjyjtest3333"+profiles.stream().filter(realProfiles::contains).findAny().orElse(defaultProfile));
        return profiles.stream().filter(realProfiles::contains).findAny().orElse(defaultProfile);
    }

}
