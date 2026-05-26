# Real Run Request Examples

This project exposes `POST /api/add/runs` for the real multi-agent execution.

Before sending any real request, make sure the application is started with a valid DashScope API key:

```bash
export SPRING_AI_DASHSCOPE_ENABLED=true
export AI_DASHSCOPE_API_KEY=your_dashscope_key
export APP_QWEN_MODEL=qwen3-max
./mvnw spring-boot:run
```

The request examples below are also available as ready-to-send files in `examples/`.

## Example 1: Quick verification run

Use this when you want to confirm that the real model can complete all four iterations with the default Mermaid output.

- File: `examples/request-bodies/quick-verification.json`
- Main trait: minimal and fast

## Example 2: Submission-oriented Mermaid run

Use this when you want a clean report draft for the final submission and prefer Mermaid diagrams.

- File: `examples/request-bodies/submission-mermaid.json`
- Main trait: concise report-oriented wording

## Example 3: Submission-oriented PlantUML run

Use this when your group prefers PlantUML for the architectural views.

- File: `examples/request-bodies/submission-plantuml.json`
- Main trait: same workflow, different view notation

## Example 4: Retrieve assignment brief before running

Use `GET /api/add/brief` when you want to verify the fixed iteration plan and the prior knowledge bundle before launching a real run.
