# AI Agent Instructions — XA Bank Time Deposit

## 1. Project Context

This project is a Kotlin/Spring Boot take-home assignment involving refactoring an existing time deposit calculator and implementing a REST API with PostgreSQL persistence.

The goal is to deliver a correct, maintainable, well-tested solution using lightweight Hexagonal Architecture.

Prioritize correctness, simplicity, and meaningful tests over unnecessary abstractions.

## 2. Technology Stack

- Kotlin
- Java 17+
- Spring Boot
- Maven
- PostgreSQL
- Spring Data JPA
- Flyway
- JUnit 5 and AssertJ
- Testcontainers
- OpenAPI / Swagger
- Docker Compose

Use existing dependencies and conventions wherever possible.

## 3. Mandatory Constraints

- Preserve compatibility with the existing `TimeDeposit` class.
- Do not change the `TimeDepositCalculator.updateBalance(xs: List<TimeDeposit>)` method signature.
- Preserve the existing calculator's observable behavior, including interest eligibility, rounding, and balance mutation.
- Implement exactly two application REST endpoints:
    - GET all time deposits, including withdrawals.
    - Update and persist balances of all time deposits.
- Do not add extra business endpoints.
- Do not implement unnecessary exception handling or input validation.
- Do not modify the original upstream repository or create pull requests to it.

## 4. Architecture Guidelines

Use lightweight Hexagonal Architecture:

- **Domain:** Business rules, interest calculation, and policies.
- **Application:** Use cases, orchestration, and repository ports.
- **Inbound adapter:** REST controller and API DTOs.
- **Outbound adapter:** JPA entities, Spring Data repositories, and persistence mapping.

Keep domain logic independent of Spring, HTTP, and persistence frameworks.

Dependencies should point toward the application/domain layers.

Avoid unnecessary interfaces, factories, inheritance hierarchies, and generic abstractions.

Preserve existing package compatibility where required.

## 5. Business Rules

The existing calculator is the reference implementation.

- Basic: 1% annual interest.
- Student: 3% annual interest, eligible on days 31–365.
- Premium: 5% annual interest, eligible after day 45.
- No plan earns interest during the first 30 days.
- Apply one month's interest per calculator invocation.
- Round calculated interest to two decimal places using `HALF_UP`.
- Preserve the original numeric behavior unless a change is explicitly approved.

Do not invent withdrawal deductions, scheduling, or other financial rules that are not specified.

Document reasonable assumptions when requirements are ambiguous.

## 6. Testing Requirements

Follow a test-first approach for existing behavior.

Before refactoring:

1. Analyze the existing implementation.
2. Add characterization tests.
3. Verify the tests pass against the original implementation.
4. Refactor incrementally.
5. Run tests after meaningful changes.

Cover:

- Basic plan: days 30 and 31.
- Student plan: days 30, 31, 365, and 366.
- Premium plan: days 45 and 46.
- Interest calculation and rounding.
- Multiple deposits.
- Unknown plan types and unchanged balances.
- Persistence and API behavior using Testcontainers.

Use meaningful assertions and descriptive test names.

Avoid redundant tests, excessive mocking, and assertions unrelated to actual behavior.

## 7. AI Development Workflow

When working on a task:

1. Read the relevant existing code and requirements.
2. Explain the proposed changes briefly before implementation.
3. Prefer small, focused changes.
4. Preserve existing public contracts.
5. Add or update tests where appropriate.
6. Run relevant tests and report results.
7. Review generated code for correctness, dead code, and unnecessary complexity.
8. Summarize what changed and why.

Do not generate large amounts of boilerplate without a clear purpose.

Do not claim tests have passed unless they were executed successfully.

Do not silently change business rules or introduce dependencies.

## 8. Git Practices

Follow atomic commit principles.

- Keep commits focused on one logical change.
- Use descriptive commit messages.
- Do not mix unrelated refactoring, infrastructure, and documentation changes.
- Do not commit credentials, environment secrets, or generated build artifacts.
- Do not create commits automatically without explicit approval.

Example commit messages:

- `test: characterize existing interest calculation`
- `refactor: extract interest calculation policies`
- `feat: add time deposit persistence`
- `feat: implement time deposit REST endpoints`
- `test: add PostgreSQL integration tests`
- `docs: document AI workflow and API usage`

## 9. Documentation

Keep the README concise and reproducible.

Include:

- Requirements and prerequisites.
- How to start the application using Docker Compose.
- How to run tests.
- How to access Swagger UI.
- API usage examples.
- Architectural decisions and assumptions.
- AI tools, models, configuration, and workflow actually used.
- Summary of AI-assisted implementation and testing.

Do not claim tools, models, or workflows were used unless they actually were.

## 10. Definition of Done

A task is complete when:

- The implementation satisfies its requirements.
- Existing calculator behavior remains compatible.
- Relevant tests pass.
- The code follows the project's architecture.
- No unnecessary complexity or dead code remains.
- Documentation is updated where needed.
- The changes can be explained and justified.