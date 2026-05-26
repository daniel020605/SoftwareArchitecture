# Multi-Agent Workflow Design

## 1. Why this is a multi-agent solution

The system does not rely on a single monolithic prompt. Instead, it decomposes the ADD work into specialized roles with explicit handoff boundaries and collaborative verification.

That is the core difference required by the assignment's multi-agent track.

## 2. Role definitions

### Requirements Analyst

- input: iteration focus, prior knowledge bundle, previous iteration summaries
- output: selected drivers, iteration goal, elements to refine, candidate design concepts, ambiguities

### Solution Architect

- input: analyst brief, previous iterations, review feedback
- output: ADD Step 4 to Step 6 architectural proposal and later revision

### Quality Reviewer

- input: analyst brief and architect draft
- output: accept-or-revise verdict with driver-based evidence

### Diagram Curator

- input: revised architecture
- output: Mermaid or PlantUML code

### Iteration Moderator

- input: all previous agent outputs in the current iteration
- output: final structured JSON for ADD Step 2 to Step 7

## 3. Dialogue rules

Every role follows the same hard rules:

1. Only use the assignment's prior knowledge.
2. Do not bring in external domain knowledge or handcrafted few-shot examples.
3. Keep reasoning traceable to driver IDs, concerns, or constraints.
4. Preserve the fixed iteration plan.
5. Produce English output because the report must be submitted in English.

## 4. Workflow details

For each of the four iterations:

1. The analyst narrows the design scope.
2. The architect drafts the design.
3. The reviewer checks compliance and identifies gaps.
4. The architect revises the design.
5. The diagram curator captures the resulting view.
6. The moderator consolidates the iteration into structured output.

The workflow can repeat the review-revision loop through `reviewerRounds`.

## 5. Artifact strategy

The system emits four types of artifacts:

- human-readable conversation logs,
- JSON snapshots for each iteration,
- a full run summary JSON,
- and a report draft in English.

This keeps the implementation directly aligned with the grading requirements.

## 6. Operational notes

- By default, DashScope is disabled so the application can boot without secrets.
- To run the real model, enable DashScope and provide the API key.
- If usage metadata is returned by the provider, token counts are written into the logs and the summary report.
