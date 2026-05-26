# Assignment Compliance Check

Checked against `../2026SoftArch-assignment2.pdf`.

## Verdict

The project is broadly compliant with the selected route:

- AI paradigm: `C: Multi-agent`
- LLM: `Qwen3-Max`
- Framework: Spring AI Alibaba
- Method: ADD 3.0
- Case study: Hotel Pricing System

The strongest submission artifact is:

`artifacts/run-20260525-055906/report-submission-ready.md`

The original generated model artifacts are preserved under:

`artifacts/run-20260525-055906/`

## Requirement Checklist

| PDF Requirement | Current Status | Evidence |
| --- | --- | --- |
| Select one AI paradigm and one LLM | Pass | `C: Multi-agent`, `Qwen3-Max` in `application.yml`, `README.md`, and report metadata |
| Use ADD 3.0 | Pass | `src/main/resources/knowledge/add-3.0.md`; report contains ADD Step 1 and Steps 2-7 for each iteration |
| Use Hotel Pricing System prior knowledge | Pass | `src/main/resources/knowledge/hotel-pricing-system.md` |
| Use fixed four-iteration plan | Pass | `src/main/resources/knowledge/iteration-plan.md`; `AssignmentCatalog.java`; four iteration JSON files |
| Multi-agent should include multiple role prompts, dialogue rules, workflow | Pass | `PromptFactory.java`; `AddMultiAgentWorkflowService.java`; `docs/multi-agent-workflow.md` |
| Views generated using Mermaid or PlantUML | Pass for submission-ready report | `report-submission-ready.md` contains four Mermaid `flowchart` diagrams |
| No few-shot examples or handcrafted demonstration outputs in prompt | Pass | Prompts are role/task instructions and prior knowledge only |
| No requirement augmentation beyond unified prior knowledge | Mostly pass | Submission-ready report was conservative; original raw logs contain some model-introduced specific implementation terms |
| All decision rules derived from system instructions | Mostly pass | Prompt rules enforce traceability; submission-ready report avoids unsupported named mechanisms |
| Source code deliverable | Pass | Spring Boot project with tests |
| Complete conversation log with timestamps for four iterations | Pass | `conversations/all-turns.json` and `iteration-01.md` through `iteration-04.md` |
| English report | Pass | `report-submission-ready.md` is in English |
| Report follows appendix structure | Pass | Output results of ADD, interaction cost analysis, individual reflection |
| Interaction cost includes way, LLM, human turns, tokens, time | Pass | 2 human turns, 24 agent turns, 123.37K tokens, 16.00 min |
| Individual contribution table | Pass but needs manual completion | Left as `To be filled`, as requested |
| Report length below 30 A4 pages | Likely pass | Concise Markdown report, approximately 347 lines |

## Important Risk

The raw real-run conversation logs were generated before the later prompt tightening. They contain some model-introduced named implementation terms. This is acceptable as an audit trail of the actual run, but the submitted report should use `report-submission-ready.md`, which was edited to be more conservative and aligned with the prior knowledge bundle.

If maximum strictness is required, rerun the workflow once with the updated prompts so the raw conversation logs also avoid those terms. This will cost another real model run.

## Recommended Submission Package

Include:

- Source code: the entire `hotel-pricing-add` project
- Report: `artifacts/run-20260525-055906/report-submission-ready.md`, exported to PDF or DOCX as required by the course
- Conversation logs:
  - `artifacts/run-20260525-055906/conversations/all-turns.json`
  - `artifacts/run-20260525-055906/conversations/iteration-01.md`
  - `artifacts/run-20260525-055906/conversations/iteration-02.md`
  - `artifacts/run-20260525-055906/conversations/iteration-03.md`
  - `artifacts/run-20260525-055906/conversations/iteration-04.md`
- Structured run evidence:
  - `artifacts/run-20260525-055906/summary.json`
  - `docs/real-run-record.md`

Before final submission:

1. Fill in the real team name if required.
2. Fill in Chinese names and contribution descriptions.
3. Export the report to the required final format.
