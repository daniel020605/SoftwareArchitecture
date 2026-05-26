package com.example.hotelpricingadd.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class JsonBlockExtractorTests {

    @Test
    void extractsJsonFromMarkdownFence() {
        String raw = """
                ```json
                {"hello":"world"}
                ```
                """;

        assertThat(JsonBlockExtractor.extract(raw)).isEqualTo("{\"hello\":\"world\"}");
    }
}
