# Software Architecture (2026) Assignment 2

## Report Draft for Submission

This file is a polished draft that can be merged with the generated artifact at `artifacts/<run-id>/report.md`.

Recommended workflow:

1. Run the system with the real model.
2. Open the generated `report.md`.
3. Replace the bracketed placeholders in this draft with the latest run-specific content.
4. Keep the reflection section personal and truthful to your team.

## Project Metadata

- Team name: `[Your team name]`
- Team members: `[Member 1]`, `[Member 2]`, `[Member 3]`
- AI paradigm: `C: Multi-agent`
- LLM used: `Qwen3-Max`
- Framework: `Spring AI Alibaba`

## 1. Output results of ADD

### ADD Step 1

Before starting the iterations, the group reviewed the complete set of architectural drivers provided in the assignment. These inputs included the six primary use cases, the quality attribute scenarios, the architectural concerns, and the system constraints. Because the case study describes the Hotel Pricing System as a greenfield replacement, the design activity focused on defining an initial architecture that could support both the short MVP schedule and the longer-term full release objective.

### Iteration 1: Establishing an Overall System Structure

The first iteration focused on establishing the overall system structure for the Hotel Pricing System. The main objective was to define a stable architectural decomposition that could satisfy browser-based access, cloud hosting constraints, identity integration, protocol extensibility, and the six-month delivery plan while still leaving room for later refinement.

#### ADD Step 2

[Insert the generated Iteration 1 ADD Step 2 content from `artifacts/<run-id>/report.md`.]

#### ADD Step 3

[Insert the generated Iteration 1 ADD Step 3 content.]

#### ADD Step 4

[Insert the generated Iteration 1 ADD Step 4 content.]

#### ADD Step 5

[Insert the generated Iteration 1 ADD Step 5 content.]

#### ADD Step 6

[Insert the generated Iteration 1 ADD Step 6 content, including the main design decisions and rationale.]

#### ADD Step 7

[Insert the generated Iteration 1 ADD Step 7 content.]

### Iteration 2: Identifying Structures to Support Primary Functionality

The second iteration refined the structure to support the system's primary functionality, especially login, price changes, price queries, hotel management, rate management, and user management. At this stage, the design moved from high-level decomposition toward responsibility allocation, interface definition, and runtime collaboration for the most important business capabilities.

#### ADD Step 2

[Insert the generated Iteration 2 ADD Step 2 content.]

#### ADD Step 3

[Insert the generated Iteration 2 ADD Step 3 content.]

#### ADD Step 4

[Insert the generated Iteration 2 ADD Step 4 content.]

#### ADD Step 5

[Insert the generated Iteration 2 ADD Step 5 content.]

#### ADD Step 6

[Insert the generated Iteration 2 ADD Step 6 content.]

#### ADD Step 7

[Insert the generated Iteration 2 ADD Step 7 content.]

### Iteration 3: Addressing Reliability and Availability Quality Attributes

The third iteration concentrated on quality attributes that are critical to the Hotel Pricing System, especially reliability, availability, scalability, monitorability, and testability. The goal of this iteration was to strengthen the architecture without violating the structure established in the earlier rounds.

#### ADD Step 2

[Insert the generated Iteration 3 ADD Step 2 content.]

#### ADD Step 3

[Insert the generated Iteration 3 ADD Step 3 content.]

#### ADD Step 4

[Insert the generated Iteration 3 ADD Step 4 content.]

#### ADD Step 5

[Insert the generated Iteration 3 ADD Step 5 content.]

#### ADD Step 6

[Insert the generated Iteration 3 ADD Step 6 content.]

#### ADD Step 7

[Insert the generated Iteration 3 ADD Step 7 content.]

### Iteration 4: Addressing Development and Operations

The fourth iteration focused on development and operations concerns, including deployment, continuous delivery, team allocation, environment portability, and operational support. This round translated the architectural structure into a practical plan that aligns with the assignment's cloud-native direction and delivery constraints.

#### ADD Step 2

[Insert the generated Iteration 4 ADD Step 2 content.]

#### ADD Step 3

[Insert the generated Iteration 4 ADD Step 3 content.]

#### ADD Step 4

[Insert the generated Iteration 4 ADD Step 4 content.]

#### ADD Step 5

[Insert the generated Iteration 4 ADD Step 5 content.]

#### ADD Step 6

[Insert the generated Iteration 4 ADD Step 6 content.]

#### ADD Step 7

[Insert the generated Iteration 4 ADD Step 7 content.]

## 2. Interaction cost analysis

The assignment was completed using the multi-agent paradigm with Qwen3-Max as the base model. Instead of relying on a single prompt, the workflow used role specialization so that requirement framing, architectural design, review, diagram preservation, and final consolidation could be handled in separate steps. This structure improved traceability and made it easier to review whether each design decision remained grounded in the prior knowledge bundle.

- The way of completing the assignment: `C: Multi-agent`
- The LLM used: `Qwen3-Max`
- Number of Human Interactions (turns): `[Copy from summary.json]`
- Agent Turns: `[Copy from summary.json]`
- Token Consumption (K tokens): `[Copy from summary.json]`
- Time Cost (min): `[Copy from summary.json]`

Optional sentence for submission:

The recorded interaction cost shows that the multi-agent approach introduced more internal coordination than a single-prompt solution, but it also provided explicit review checkpoints and cleaner traceability across the four ADD iterations.

## 3. Individual Reflection

### 3.1 Problems encountered and solutions adopted

- One problem was keeping the model outputs strictly within the prior knowledge provided by the assignment while still producing concrete design decisions. We addressed this by embedding explicit dialogue rules into each role prompt and by using a reviewer agent to challenge unsupported assumptions.
- Another problem was maintaining consistency across four iterations. We addressed this by carrying the summarized outcomes of previous iterations into the next round, which reduced contradictions and preserved architectural continuity.
- A further problem was converting raw model output into submission-ready material. We addressed this by generating structured logs, iteration snapshots, and a report draft that could be checked and polished by the group before submission.

Replace the points above with your actual team experience if your final run revealed more specific issues.

### 3.2 Personal contributions to the group work

| Name (Chinese) | Contributions |
| --- | --- |
| `[Member 1]` | `[Describe concrete contribution]` |
| `[Member 2]` | `[Describe concrete contribution]` |
| `[Member 3]` | `[Describe concrete contribution]` |

## Submission Checklist

- Confirm that all ADD sections use the final run content.
- Confirm that the diagrams match the generated Mermaid or PlantUML output.
- Confirm that the interaction cost values match `summary.json`.
- Confirm that the reflection section is written by the team, not left as a generic template.
- Confirm that the final report stays within the page limit.
