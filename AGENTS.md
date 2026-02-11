# AGENT CONVENTIONS & GUIDELINES

이 문서는 이 프로젝트에 참여하는 모든 AI 에이전트 및 개발자가 준수해야 할 규칙과 소양을 정의합니다.

## 1. Core Values (기본 소양)

*   **Proactive Analysis (선제적 분석)**: 요청받은 작업만 수행하지 않고, 잠재적인 문제점이나 더 나은 설계 방향을 먼저 제안한다.
*   **Context Awareness (맥락 인지)**: 현재 수정 중인 파일뿐만 아니라, 프로젝트 전체 구조와 의존성을 고려하여 코드를 작성한다.
*   **Precision (정확성)**: 불확실한 가정에 의존하지 않고, 명확한 근거(공식 문서, 코드베이스 분석)를 바탕으로 답한다.
*   **Minimalism (간결함)**: 불필요한 주석이나 장황한 설명은 지양하고, 핵심 변경 사항과 이유에 집중한다.

## 2. Project Architecture Standards

### Module Structure
*   **Root**: `:app` (Application Entry Point)
*   **Core Layer** (`:core:*`):
    *   `:core:model`: 순수 도메인 모델 및 데이터 클래스.
    *   `:core:common`: 공통 유틸리티, 확장 함수.
    *   `:core:domain`: UseCase, Repository Interface.
    *   `:core:data`: Repository Implementation, DataSource.
    *   `:core:network`: API 호출 로직 (Retrofit/OkHttp).
    *   `:core:database`: 로컬 데이터베이스 (Room).
    *   `:core:ui`: 공통 UI 컴포넌트, 테마, 디자인 시스템.
    *   `:core:testing`: 테스트 유틸리티 및 공통 Test Rule.
*   **Feature Layer** (`:feature:*`):
    *   각 기능별로 모듈을 분리하며, `:core:domain`, `:core:ui`, `:core:model` 등을 참조한다.

### Technology Stack
*   **Language**: Kotlin (Strict Mode)
*   **UI**: Jetpack Compose (Material3)
*   **DI**: Hilt
*   **Async**: Coroutines & Flow
*   **Network**: Retrofit2 & OkHttp3
*   **Database**: Room
*   **Build**: Gradle Kotlin DSL + Version Catalog (`libs.versions.toml`)

## 3. Coding Conventions

### General
*   **KDoc**: 모든 public 함수와 클래스에는 KDoc을 작성한다.
*   **Testing**:
    *   Business Logic: JUnit4/5 + MockK + Truth
    *   Coroutines: `runTest` + `StandardTestDispatcher`
    *   UI: Compose Test Rule
*   **String Resources**: 모든 UI 텍스트는 `strings.xml`에 정의하여 사용한다 (하드코딩 금지).

### Git & Version Control
*   **Commit Message**: `type: description` (예: `feat: Add user login screen`, `fix: Resolve memory leak in HomeFragment`)

## 4. Communication Style

*   **Language**: 한국어 (기술 용어는 영어 원문 사용).
*   **Format**:
    *   중요 키워드는 '작은따옴표'로 강조.
    *   단계별 전략(Step-by-Step Strategy)을 먼저 제시하고 승인을 득한다.
    *   코드 변경 시 전체 파일을 출력하지 않고, `write_file` 등을 통해 효율적으로 적용한다.

---
**Note**: 이 문서는 프로젝트가 발전함에 따라 지속적으로 업데이트되어야 한다.
