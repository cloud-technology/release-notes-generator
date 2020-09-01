package com.ct.rng.properties.gitlab.issues;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "time_estimate", "total_time_spent", "human_time_estimate", "human_total_time_spent" })
public class TimeStats {

    @JsonProperty("time_estimate")
    public Integer timeEstimate;
    @JsonProperty("total_time_spent")
    public Integer totalTimeSpent;
    @JsonProperty("human_time_estimate")
    public Object humanTimeEstimate;
    @JsonProperty("human_total_time_spent")
    public Object humanTotalTimeSpent;

}