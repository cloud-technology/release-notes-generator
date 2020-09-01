package com.ct.rng.properties;

import java.util.List;

import lombok.Data;

@Data
public class Gitlab {
    private String accessToken;
    private String name;
    private Integer projectId;
    private String milestoneTitle;
    private Integer milestoneIid;
    private String pathToGenerateFile;
}