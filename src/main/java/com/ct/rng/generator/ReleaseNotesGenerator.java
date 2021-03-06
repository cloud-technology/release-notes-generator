package com.ct.rng.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.ct.rng.clients.GitlabClient;
import com.ct.rng.clients.MilestonesQueryParams;
import com.ct.rng.properties.ApplicationProperties;
import com.ct.rng.properties.gitlab.Gitlab;
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

    public void generate(String milestone, String path) throws Exception {
        this.sections = new ReleaseNotesSections(applicationProperties);

        Gitlab gitlab = applicationProperties.getGitlab();
        Optional<Integer> milestoneNumberOptional = this.getMilestoneNumber(gitlab.getMilestoneTitle());
        if (milestoneNumberOptional.isPresent()) {
            List<Issue> issues = this.getIssuesForMilestone(milestoneNumberOptional.get(), null, null);
            String content = generateContent(issues);
            writeContentToFile(content, path);
            Pattern pattern = Pattern.compile(applicationProperties.getRegexExpression());
            Matcher matcher = pattern.matcher(milestone);
            if(matcher.matches()){
                this.createRelease(content);
            }else{
                throw new IllegalAccessException(String.format("Tag Name %s 不符合 %s", milestone, applicationProperties.getRegexExpression()));
            }
        } else {
            throw new IllegalAccessException(String.format("無法找到對應 Milestone name=%s", milestone));
        }
    }

    private Optional<Integer> getMilestoneNumber(String milestoneTitle) {
        Gitlab gitlab = applicationProperties.getGitlab();
        Milestone milestone = null;
        MilestonesQueryParams params = new MilestonesQueryParams();
        params.setTitle(milestoneTitle);
        List<Milestone> milestones = gitlabClient.getMilestones(String.format("Bearer %s", gitlab.getAccessToken()),
                gitlab.getProjectId(), params);
        if (milestones.size() == 1) {
            milestone = milestones.get(0);
        }
        return Optional.of(milestone.getId());
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
        return "- " + title + " " + getLinkToIssue(issue) + " " + this.getFlowInfo(issue) + "\n";
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

    private void createRelease(String content) {
        Gitlab gitlab = applicationProperties.getGitlab();
        ReleaseCreateDto releaseCreateDto = new ReleaseCreateDto();
        releaseCreateDto.setName(gitlab.getMilestoneTitle());
        releaseCreateDto.setTagName(gitlab.getMilestoneTitle());
        releaseCreateDto.setDescription(content);
        releaseCreateDto.setRef(gitlab.getMilestoneTitle());
        releaseCreateDto.setMilestones(Arrays.asList(gitlab.getMilestoneTitle()));
        log.debug("releaseCreateDto={}", releaseCreateDto);
        String rs = gitlabClient.createRelease(String.format("Bearer %s", gitlab.getAccessToken()),
                gitlab.getProjectId(), releaseCreateDto);
        log.debug("createRelease={}", rs);
    }

}