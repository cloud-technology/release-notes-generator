package com.ct.rng;

import java.io.IOException;

import com.ct.rng.generator.ReleaseNotesGenerator;
import com.ct.rng.properties.ApplicationProperties;
import com.ct.rng.properties.Gitlab;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationLoader {
    @NonNull
    private final ApplicationProperties applicationProperties;
    @NonNull
    private final ReleaseNotesGenerator releaseNotesGenerator;

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReady() {
        Gitlab gitlab = applicationProperties.getGitlab();
        try {
            releaseNotesGenerator.generate(gitlab.getMilestoneTitle(), gitlab.getPathToGenerateFile());
        } catch (IOException e) {
            log.error("執行發生錯誤", e);
        }

    }
}
