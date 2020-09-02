package com.ct.rng.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ct.rng.clients.GitlabClient;
import com.ct.rng.clients.MilestonesQueryParams;
import com.ct.rng.properties.ApplicationProperties;
import com.ct.rng.properties.Gitlab;
import com.ct.rng.properties.gitlab.Milestone;
import com.ct.rng.properties.gitlab.ReleaseCreateDto;
import com.ct.rng.properties.gitlab.issues.Issue;
import com.ct.rng.properties.gitlab.issues.User;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReleaseNotesGenerator {
    private static final String THANK_YOU = "## :heart: Contributors\n\n"
            + "We'd like to thank all the contributors who worked on this release!";
    @NonNull
    private final GitlabClient gitlabClient;
    @NonNull
    private final ApplicationProperties applicationProperties;

    private ReleaseNotesSections sections;

    public void generate(String milestone, String path) throws IOException {
        this.sections = new ReleaseNotesSections(applicationProperties);

        Gitlab gitlab = applicationProperties.getGitlab();
        int milestoneNumber = this.getMilestoneNumber(gitlab.getMilestoneTitle());
        List<Issue> issues = this.getIssuesForMilestone(milestoneNumber, null, null);
        String content = generateContent(issues);
        // log.debug("content={}", content);
        writeContentToFile(content, path);
        this.createRelease(content);
    }

    private int getMilestoneNumber(String milestoneTitle) {
        Gitlab gitlab = applicationProperties.getGitlab();
        Milestone milestone = null;
        MilestonesQueryParams params = new MilestonesQueryParams();
        params.setTitle(milestoneTitle);
        List<Milestone> milestones = gitlabClient.getMilestones(String.format("Bearer %s", gitlab.getAccessToken()),
                gitlab.getProjectId(), params);
        if (milestones.size() == 1) {
            milestone = milestones.get(0);
        }
        return milestone.getId();
    }

    public List<Issue> getIssuesForMilestone(int milestoneNumber, String organization, String repository) {
        Gitlab gitlab = applicationProperties.getGitlab();
        return gitlabClient.getIssuesForMilestone(String.format("Bearer %s", gitlab.getAccessToken()),
                gitlab.getProjectId(), milestoneNumber);
    }

    private String generateContent(List<Issue> issues) {
        StringBuilder content = new StringBuilder();
        addSectionContent(content, this.sections.collate(issues));
        Set<User> contributors = getContributors(issues);
        if (!contributors.isEmpty()) {
            addContributorsContent(content, contributors);
        }
        return content.toString();
    }

    private void addSectionContent(StringBuilder content, Map<ReleaseNotesSection, List<Issue>> sectionIssues) {
        sectionIssues.forEach((section, issues) -> {
            content.append((content.length() != 0) ? "\n" : "");
            content.append("## ").append(section).append("\n\n");
            issues.stream().map(this::getFormattedIssue).forEach(content::append);
        });
    }

    private Set<User> getContributors(List<Issue> issues) {
        return issues.stream().filter((issue) -> issue.getClosedAt() != null).map(Issue::getClosedBy)
                .collect(Collectors.toSet());
    }

    private void addContributorsContent(StringBuilder content, Set<User> contributors) {
        content.append("\n" + THANK_YOU + "\n\n");
        contributors.stream().map(this::formatContributors).forEach(content::append);
    }

    private String formatContributors(User c) {
        return "- [@" + c.getName() + "]" + "(" + c.getWebUrl() + ")\n";
    }

    private String getFormattedIssue(Issue issue) {
        String title = issue.getTitle();
        // title = ghUserMentionPattern.matcher(title).replaceAll("$1`$2`");
        return "- " + this.getFlowInfo(issue) + title + " " + getLinkToIssue(issue) + " " + this.getFlowInfo(issue) + "\n";
    }

    private String getFlowInfo(Issue issue) {
        StringBuffer flow = new StringBuffer();
        for (String label : issue.getLabels()) {
            if (label.startsWith("flow")) {
                flow.append(flow.length() > 0 ? ", " : "" + label.substring(5));
            }
        }
        if (flow.length() > 0) {
            flow.insert(0, "Flow - ");
            flow.append(" ");
        }
        return flow.toString();
    }

    private String getLinkToIssue(Issue issue) {
        return "[#" + issue.getIid() + "]" + "(" + issue.getWebUrl() + ")";
    }

    private void writeContentToFile(String content, String path) throws IOException {
        FileCopyUtils.copy(content, new FileWriter(new File(path)));
    }

    private void createRelease(String content){
        Gitlab gitlab = applicationProperties.getGitlab();
        ReleaseCreateDto releaseCreateDto = new ReleaseCreateDto();
        releaseCreateDto.setName(gitlab.getMilestoneTitle());
        releaseCreateDto.setTagName(gitlab.getMilestoneTitle());
        releaseCreateDto.setDescription(content);
        releaseCreateDto.setRef("main");
        releaseCreateDto.setMilestones(Arrays.asList(gitlab.getMilestoneTitle()));
        String rs = gitlabClient.createRelease(String.format("Bearer %s", gitlab.getAccessToken()), releaseCreateDto);
        log.debug("createRelease={}", rs);
    }

}