package com.ct.rng.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.ct.rng.properties.ApplicationProperties;
import com.ct.rng.properties.ApplicationProperties.Section;
import com.ct.rng.properties.gitlab.issues.Issue;

import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReleaseNotesSections {

	private static final List<ReleaseNotesSection> DEFAULT_SECTIONS;

	static {
		List<ReleaseNotesSection> sections = new ArrayList<>();
		add(sections, "New Features", ":star:", "enhancement");
		add(sections, "Bug Fixes", ":beetle:", "bug", "regression");
		add(sections, "Documentation", ":notebook_with_decorative_cover:", "documentation");
		add(sections, "Dependency Upgrades", ":hammer:", "dependency-upgrade");
		DEFAULT_SECTIONS = Collections.unmodifiableList(sections);
	}

	private static void add(List<ReleaseNotesSection> sections, String title, String emoji, String... labels) {
		sections.add(new ReleaseNotesSection(title, emoji, labels));
	}

	private final List<ReleaseNotesSection> sections;

	ReleaseNotesSections(ApplicationProperties properties) {
		this.sections = adapt(properties.getSections());
	}

	private List<ReleaseNotesSection> adapt(List<Section> propertySections) {
		if (CollectionUtils.isEmpty(propertySections)) {
			return DEFAULT_SECTIONS;
		}
		return propertySections.stream().map(this::adapt).collect(Collectors.toList());
	}

	private ReleaseNotesSection adapt(Section propertySection) {
		return new ReleaseNotesSection(propertySection.getTitle(), propertySection.getEmoji(),
				propertySection.getLabels());
	}

	Map<ReleaseNotesSection, List<Issue>> collate(List<Issue> issues) {
		SortedMap<ReleaseNotesSection, List<Issue>> collated = new TreeMap<>(
				Comparator.comparing(this.sections::indexOf));
		for (Issue issue : issues) {
			ReleaseNotesSection section = getSection(issue);
			if (section != null) {
				collated.computeIfAbsent(section, (key) -> new ArrayList<>());
				collated.get(section).add(issue);
			}
		}
		// log.debug("collate={}", collated);
		return collated;
	}

	private ReleaseNotesSection getSection(Issue issue) {
		for (ReleaseNotesSection section : this.sections) {
			if (section.isMatchFor(issue)) {
				return section;
			}
		}
		return null;
	}

}