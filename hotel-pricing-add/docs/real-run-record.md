# Real Run Record

This file records the successful real execution used for the submission-ready draft.

## Runtime

- Date/time: 2026-05-25, Asia/Shanghai local time
- Project directory: `.`
- Environment source: `../env.sh`
- Model: `qwen3-max`
- Framework/client: Spring AI Alibaba DashScope native chat client
- Native DashScope base URL: `https://dashscope.aliyuncs.com`
- Native chat path used by the dependency: `/api/v1/services/aigc/text-generation/generation`

Note: `https://dashscope.aliyuncs.com/compatible-mode/v1` is the OpenAI-compatible endpoint. This project currently uses the Spring AI Alibaba DashScope native client, so the compatible-mode base URL should not be used here unless the implementation is changed to an OpenAI-compatible client.

## Start Command

```bash
cd .
set -a
source ../env.sh
set +a
./mvnw spring-boot:run
```

## Request Used

```bash
curl -s -w '\nHTTP_STATUS:%{http_code}\n' \
  -X POST http://localhost:8080/api/add/runs \
  -H 'Content-Type: application/json' \
  -d '{
    "teamName": "",
    "teamMembers": [],
    "diagramType": "mermaid",
    "humanInteractionTurns": 2,
    "reviewerRounds": 1,
    "notes": "Produce concise submission-ready English. Leave team member names and personal contributions blank or marked as To be filled. Keep every design decision traceable only to the provided drivers, concerns, and constraints."
  }'
```

## Result

- HTTP status: `200`
- Run ID: `run-20260525-055906`
- Agent turns: `24`
- Human interaction turns: `2`
- Prompt tokens: `92119`
- Completion tokens: `31250`
- Total tokens: `123369`
- Token consumption in K tokens: `123.37`
- Elapsed time: `960` seconds, or `16.00` minutes

## Generated Artifacts

- Summary JSON: `artifacts/run-20260525-055906/summary.json`
- Original generated report: `artifacts/run-20260525-055906/report.md`
- Submission-ready report: `artifacts/run-20260525-055906/report-submission-ready.md`
- All turns JSON: `artifacts/run-20260525-055906/conversations/all-turns.json`
- Per-iteration conversation logs: `artifacts/run-20260525-055906/conversations/iteration-01.md` through `iteration-04.md`
- Structured iteration snapshots: `artifacts/run-20260525-055906/iterations/iteration-01.json` through `iteration-04.json`

## Verification

```bash
./mvnw test
```

Result:

```text
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```
