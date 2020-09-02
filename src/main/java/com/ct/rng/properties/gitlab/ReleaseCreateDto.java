package com.ct.rng.properties.gitlab;

import lombok.Data;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"name",
"tag_name",
"description",
"ref",
"milestones"
})
public class ReleaseCreateDto {
    @JsonProperty("name")
    private String name;
    @JsonProperty("tag_name")
    private String tagName;
    @JsonProperty("description")
    private String description;
    @JsonProperty("ref")
    private String ref;
    @JsonProperty("milestones")
    private List<String> milestones = null;
}
