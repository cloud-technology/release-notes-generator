package com.ct.rng.properties;

import java.util.ArrayList;
import java.util.List;

import com.ct.rng.properties.gitlab.Gitlab;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "releasenotes")
public class ApplicationProperties {
	private String regexExpression;
	
    private Gitlab gitlab;

    private List<Section> sections = new ArrayList<>();

    public static class Section {

		/**
		 * The title of the section.
		 */
		private String title;

		/**
		 * The emoji character to use, for example ":star:".
		 */
		private String emoji;

		/**
		 * The labels used to identify if an issue is for the section.
		 */
		private List<String> labels = new ArrayList<>();

		public String getTitle() {
			return this.title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getEmoji() {
			return this.emoji;
		}

		public void setEmoji(String emoji) {
			this.emoji = emoji;
		}

		public List<String> getLabels() {
			return this.labels;
		}

		public void setLabels(List<String> labels) {
			this.labels = labels;
		}

	}
}
