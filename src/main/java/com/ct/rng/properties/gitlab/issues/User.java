package com.ct.rng.properties.gitlab.issues;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "name", "username", "state", "avatar_url", "web_url" })
public class User {
    @JsonProperty("id")
    public Integer id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("username")
    public String username;
    @JsonProperty("state")
    public String state;
    @JsonProperty("avatar_url")
    public String avatarUrl;
    @JsonProperty("web_url")
    public String webUrl;

}