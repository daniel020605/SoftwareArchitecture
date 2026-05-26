package com.example.hotelpricingadd.util;

public final class JsonBlockExtractor {

    private JsonBlockExtractor() {
    }

    public static String extract(String rawText) {
        if (rawText == null || rawText.isBlank()) {
            throw new IllegalArgumentException("Cannot extract JSON from an empty response.");
        }

        String trimmed = rawText.trim();
        if (trimmed.startsWith("```")) {
            int firstLineBreak = trimmed.indexOf('\n');
            if (firstLineBreak > -1) {
                trimmed = trimmed.substring(firstLineBreak + 1);
            }
            if (trimmed.endsWith("```")) {
                trimmed = trimmed.substring(0, trimmed.length() - 3).trim();
            }
        }

        int start = trimmed.indexOf('{');
        int end = trimmed.lastIndexOf('}');
        if (start < 0 || end < 0 || end <= start) {
            throw new IllegalArgumentException("No JSON object found in model response.");
        }
        return trimmed.substring(start, end + 1);
    }
}
