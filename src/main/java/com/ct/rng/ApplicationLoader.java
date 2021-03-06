package com.ct.rng;

import com.ct.rng.generator.ReleaseNotesGenerator;
import com.ct.rng.properties.ApplicationProperties;
import com.ct.rng.properties.gitlab.Gitlab;

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
            if(gitlab == null){
                throw new IllegalAccessException("Gitlab 設定檔不存在");
            }
            releaseNotesGenerator.generate(gitlab.getMilestoneTitle(), gitlab.getPathToGenerateFile());
        } catch (Exception e) {
            log.error("執行發生錯誤", e);
        }
    }
}
