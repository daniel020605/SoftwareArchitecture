# Hotel Pricing ADD Multi-Agent

This project implements the `C: Multi-agent + Qwen3-Max + Spring AI Alibaba` route for `Software Architecture (2026) Assignment 2`.

It provides a Spring Boot application that:

- runs the fixed four-iteration ADD 3.0 workflow for the Hotel Pricing System case,
- uses a five-role multi-agent collaboration pattern,
- archives complete timestamped conversation logs,
- exports structured iteration snapshots,
- and generates an English report draft that matches the assignment appendix.

## 1. What is included

- `src/main/resources/knowledge/`: the prior knowledge bundle used in prompts.
- `src/main/java/.../service/PromptFactory.java`: role prompts, dialogue rules, and workflow wiring.
- `src/main/java/.../service/AddMultiAgentWorkflowService.java`: the multi-agent orchestration engine.
- `src/main/java/.../service/ArtifactService.java`: report, log, and JSON artifact generation.
- `docs/assignment-analysis.md`: assignment-to-implementation mapping.
- `docs/multi-agent-workflow.md`: detailed agent design and prompt strategy.
- `docs/submission-report-draft.md`: a polished English report draft for submission.
- `docs/request-examples.md`: a quick guide to real run requests.
- `examples/real-run-requests.http`: ready-to-send HTTP requests.

## 2. Agent roles

The workflow uses five specialized roles:

1. `Requirements Analyst`: selects drivers and iteration scope.
2. `Solution Architect`: proposes and revises the architecture.
3. `Quality Reviewer`: performs evidence-based verification.
4. `Diagram Curator`: generates Mermaid or PlantUML view code.
5. `Iteration Moderator`: consolidates the final JSON result for each iteration.

The orchestration sequence for every iteration is:

1. Analyst brief
2. Architect draft
3. Reviewer verification
4. Architect revision
5. Diagram generation
6. Moderator consolidation

## 3. Prerequisites

- Java 21
- Internet access for Maven dependency resolution
- DashScope API key if you want to call the real model

## 4. Run with real Qwen3-Max

Set these environment variables before starting the app:

```bash
export SPRING_AI_DASHSCOPE_ENABLED=true
export AI_DASHSCOPE_API_KEY=your_dashscope_key
export APP_QWEN_MODEL=qwen3-max
```

Then start the application:

```bash
./mvnw spring-boot:run
```

## 5. Run in local documentation mode

Without a DashScope key, the app still starts, but the workflow endpoint will return a clear error telling you which environment variables are missing.

```bash
./mvnw spring-boot:run
```

## 6. API

### `GET /api/add/brief`

Returns the assignment metadata, prior knowledge bundle, and the fixed four-iteration plan.

### `POST /api/add/runs`

Runs the full multi-agent ADD workflow.

Example request:

```bash
curl -X POST http://localhost:8080/api/add/runs \
  -H "Content-Type: application/json" \
  -d '{
    "teamName": "Team Alpha",
    "teamMembers": ["Alice", "Bob", "Carol"],
    "diagramType": "mermaid",
    "humanInteractionTurns": 1,
    "reviewerRounds": 1,
    "notes": "Keep the wording concise and assignment-oriented."
  }'
```

Additional request packs:

- [docs/request-examples.md](docs/request-examples.md)
- [examples/real-run-requests.http](examples/real-run-requests.http)
- [examples/request-bodies/submission-mermaid.json](examples/request-bodies/submission-mermaid.json)
- [examples/request-bodies/submission-plantuml.json](examples/request-bodies/submission-plantuml.json)

## 7. Output artifacts

Every execution creates a folder under `artifacts/<run-id>/`:

```text
artifacts/
  run-20260518-084500/
    report.md
    summary.json
    conversations/
      all-turns.json
      iteration-01.md
      iteration-02.md
      iteration-03.md
      iteration-04.md
    iterations/
      iteration-01.json
      iteration-02.json
      iteration-03.json
      iteration-04.json
```

This directly supports the assignment deliverables:

- source code,
- full conversation log with timestamps,
- report draft in English.

## 8. Tests

```bash
./mvnw test
```

The test suite verifies:

- the Spring context can boot without a DashScope key,
- JSON extraction from model responses,
- and full artifact generation with a stubbed multi-agent gateway.

## 9. Submission helpers

- Report draft: [docs/submission-report-draft.md](docs/submission-report-draft.md)
- Real request guide: [docs/request-examples.md](docs/request-examples.md)
