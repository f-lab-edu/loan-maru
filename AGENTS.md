# Loan Maru Agent Guide

## 공통 원칙

- 관련 코드와 기존 구현을 먼저 확인하고, 요청 범위만 변경한다. 새 기능·의존성·계층을 임의로 추가하지 않는다.
- 사용자와 다른 작업자의 변경을 보존한다. 위임 시 담당 파일을 지정하고 수정 범위가 겹치지 않게 한다.
- 오류는 각 모듈 `common.exception`의 `ErrorCode`와 `ApplicationException`으로 표현하고, 응답은 `GlobalExceptionHandler`가 `ErrorResponse`로 만든다.
- Entity·테이블을 바꾸면 ERD 문서도 함께 맞춘다.
- 기존 동작과 테스트 의도를 보존하고 관련 테스트를 실행한다. 미실행·실패·검증 한계는 보고한다. 로컬 검증과 외부 CI 결과를 구분한다.

## 모듈과 패키지

- 서버 모듈은 `server/<모듈 이름>`에 둔다. Gradle 경로는 `:server:<모듈 이름>`이다.
- 공통 빌드 설정은 루트 `build.gradle`에 두고, 모듈 `build.gradle`에는 그 모듈만 쓰는 의존성만 둔다.
- 모든 모듈은 기본 패키지 `me.ngyu.loanmaru`와 같은 패키지 구조를 쓴다.

```text
me.ngyu.loanmaru
├── <Server>Application
├── application
│   └── <도메인>
│       ├── api            # Controller
│       │   └── dto        # 요청·응답
│       ├── service        # Service
│       └── domain         # 도메인 모델, Repository·외부 조회 인터페이스
├── infrastructure
│   ├── persistence        # domain Repository 구현(JPA, 임시 고정 응답)
│   └── client             # 외부 서버 호출 구현
└── common
    ├── exception          # 공통 오류 응답
    └── config
```

- 기능은 `application.<도메인>` 아래 `api`, `service`, `domain`으로 나눈다. 여러 도메인이 쓰는 기능은 `common.<공용기능>`에 둔다.
- 의존 방향은 `api → service → domain`이다. `infrastructure`는 `domain`의 인터페이스를 구현한다.
- `domain`은 `api`, `service`, `infrastructure`를 import하지 않는다. `api`는 `infrastructure`를 직접 쓰지 않는다.
- 허용하는 예외는 하나다. `service`는 `api.dto`를 파라미터나 반환 타입으로 쓸 수 있다. 기본은 Controller가 dto를 domain으로 바꿔 넘기는 것이고, 변환이 오히려 복잡할 때만 쓴다.
- 헥사고날 구조를 기본으로 하되 `port`, `adapter` 패키지와 `UseCase`, `Port`, `Adapter` 이름은 쓰지 않는다.
- 데이터 모델이 정해지면 JPA Entity를 domain 모델로 쓴다.

## 코드 스타일

- Java 포맷은 루트 `GoogleStyle.xml`을 따른다. 다른 포맷터의 기본값으로 대체하지 않는다.
- DI는 `@RequiredArgsConstructor`와 `private final` 필드를 사용한다. 검증·초기화 본문이 필요한 생성자만 직접 작성한다.
- 관련 코드는 가까이 두고, 상위 흐름을 먼저, 보조 메서드는 호출자 아래에 둔다.

## 테스트

- 전체 테스트는 `./gradlew test`, 모듈 하나는 `./gradlew :server:<모듈 이름>:test`로 실행한다.
- DB를 쓰는 `loan-screening-server`는 H2(PostgreSQL 모드)로 테스트한다.
- `@DisplayName`은 검증하는 동작을 한국어 문장으로 쓴다.
- jsonb, 부분 유일 인덱스, advisory lock처럼 H2가 흉내 내지 못하는 동작은 테스트 한계로 보고한다.

## 브랜치와 커밋

- 브랜치·커밋·push 규칙은 `.claude/skills/git-commit/SKILL.md`를 따른다.
- 요청이 금지하지 않았다면 작업 완료와 관련 검증 성공 후 로컬에 커밋한다. push와 PR은 사용자 승인 후에만 한다.
