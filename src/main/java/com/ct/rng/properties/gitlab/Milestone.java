package com.ct.rng.properties.gitlab;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "iid", "project_id", "title", "description", "state", "created_at", "updated_at", "due_date",
        "start_date", "expired", "web_url" })
public class Milestone {
    @JsonProperty("id")
    public Integer id;
    @JsonProperty("iid")
    public Integer iid;
    @JsonProperty("project_id")
    public Integer projectId;
    @JsonProperty("title")
    public String title;
    @JsonProperty("description")
    public String description;
    @JsonProperty("state")
    public String state;
    @JsonProperty("created_at")
    public String createdAt;
    @JsonProperty("updated_at")
    public String updatedAt;
    @JsonProperty("due_date")
    public Object dueDate;
    @JsonProperty("start_date")
    public Object startDate;
    @JsonProperty("expired")
    public Object expired;
    @JsonProperty("web_url")
    public String webUrl;
}