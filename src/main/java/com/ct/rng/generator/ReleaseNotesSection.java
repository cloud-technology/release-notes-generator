package com.ct.rng.generator;

import java.util.Arrays;
import java.util.List;

import com.ct.rng.properties.gitlab.issues.Issue;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ReleaseNotesSection {

    private final String title;
	private final String emoji;
	private final List<String> labels;
    
    ReleaseNotesSection(String title, String emoji, String... labels) {
		this(title, emoji, Arrays.asList(labels));
	}
	
	ReleaseNotesSection(String title, String emoji, List<String> labels) {
		Assert.hasText(title, "Title must not be empty");
		Assert.hasText(emoji, "Emoji must not be empty");
		Assert.isTrue(!CollectionUtils.isEmpty(labels), "Labels must not be empty");
		this.title = title;
		this.emoji = emoji;
		this.labels = labels;
	}

	boolean isMatchFor(Issue issue) {
		for (String candidate : this.labels) {
			for (String label : issue.getLabels()) {
				if (label.equals(candidate)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return this.emoji + " " + this.title;
	}
}