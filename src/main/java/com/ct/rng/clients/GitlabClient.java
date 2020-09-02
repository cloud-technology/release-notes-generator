package com.ct.rng.clients;

import java.util.List;

import com.ct.rng.properties.gitlab.Milestone;
import com.ct.rng.properties.gitlab.ReleaseCreateDto;
import com.ct.rng.properties.gitlab.issues.Issue;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "gitlab", url = "https://gitlab.com/api/v4")
public interface GitlabClient {

        @RequestMapping(method = RequestMethod.GET, path = "/projects/{projectid}/milestones")
        List<Milestone> getMilestones(@RequestHeader("Authorization") String authorization,
                        @PathVariable("projectid") Integer projectid,
                        @SpringQueryMap MilestonesQueryParams milestonesQueryParams);

        @RequestMapping(method = RequestMethod.GET, path = "/projects/{projectid}/milestones/{milestoneNumber}/issues")
        List<Issue> getIssuesForMilestone(@RequestHeader("Authorization") String authorization,
                        @PathVariable("projectid") Integer projectid,
                        @PathVariable("milestoneNumber") Integer milestoneNumber);

        @RequestMapping(method = RequestMethod.POST, path = "/projects/{projectid}/releases")
        String createRelease(@RequestHeader("Authorization") String authorization,
                        @PathVariable("projectid") Integer projectid, ReleaseCreateDto releaseCreateDto);
}