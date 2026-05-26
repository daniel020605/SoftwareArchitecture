package com.example.hotelpricingadd.model;

import java.nio.file.Path;

public record RunDirectories(
        Path runDirectory,
        Path reportFile,
        Path summaryFile,
        Path conversationsDirectory,
        Path iterationsDirectory) {
}
