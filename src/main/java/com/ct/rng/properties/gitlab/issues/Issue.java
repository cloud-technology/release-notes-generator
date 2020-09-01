package com.ct.rng.properties.gitlab.issues;

import java.time.ZonedDateTime;
import java.util.List;

import com.ct.rng.properties.gitlab.Milestone;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "iid", "project_id", "title", "description", "state", "created_at", "updated_at",
        "closed_at", "closed_by", "labels", "milestone", "assignees", "author", "assignee", "user_notes_count",
        "merge_requests_count", "upvotes", "downvotes", "due_date", "confidential", "discussion_locked", "web_url",
        "time_stats", "task_completion_status", "blocking_issues_count" })
public class Issue {
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
    public ZonedDateTime createdAt;
    @JsonProperty("updated_at")
    public ZonedDateTime updatedAt;
    @JsonProperty("closed_at")
    public ZonedDateTime closedAt;
    @JsonProperty("closed_by")
    public User closedBy;
    @JsonProperty("labels")
    public List<String> labels = null;
    @JsonProperty("milestone")
    public Milestone milestone;
    @JsonProperty("assignees")
    public List<User> assignees = null;
    @JsonProperty("author")
    public User author;
    @JsonProperty("assignee")
    public User assignee;
    @JsonProperty("user_notes_count")
    public Integer userNotesCount;
    @JsonProperty("merge_requests_count")
    public Integer mergeRequestsCount;
    @JsonProperty("upvotes")
    public Integer upvotes;
    @JsonProperty("downvotes")
    public Integer downvotes;
    @JsonProperty("due_date")
    public Object dueDate;
    @JsonProperty("confidential")
    public Boolean confidential;
    @JsonProperty("discussion_locked")
    public Object discussionLocked;
    @JsonProperty("web_url")
    public String webUrl;
    @JsonProperty("time_stats")
    public TimeStats timeStats;
    @JsonProperty("task_completion_status")
    public TaskCompletionStatus taskCompletionStatus;
    @JsonProperty("blocking_issues_count")
    public Object blockingIssuesCount;

}