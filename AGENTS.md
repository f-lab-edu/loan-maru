# Loan Maru Agent Guide

## 공통 원칙

- 관련 코드와 기존 구현을 먼저 확인하고, 요청 범위만 변경한다. 새 기능·의존성·계층을 임의로 추가하지 않는다.
- 사용자와 다른 작업자의 변경을 보존한다. 위임 시 담당 파일을 지정하고 수정 범위가 겹치지 않게 한다.
- 기능은 `application/{screening,product,policy,notification}/{api,domain,service}`, DTO는 `api/dto`, Spring Data Repository는 `infrastructure/persistence`, 외부 서버 Client는 `infrastructure/client`에 둔다. JPA Entity를 도메인 모델로 사용한다.
- 오류는 `common.exception`의 `ErrorCode`와 `ApplicationException`으로 표현하고, 응답은 `GlobalExceptionHandler`가 `ErrorResponse`로 만든다.
- Entity·테이블을 바꾸면 `docs/erd`의 ERD도 함께 맞춘다.
- 기존 동작과 테스트 의도를 보존하고 관련 테스트를 실행한다. 미실행·실패·검증 한계는 보고한다. 로컬 검증과 외부 CI 결과를 구분한다.

## 코드 스타일

- Java 포맷은 루트 `GoogleStyle.xml`을 따른다. 다른 포맷터의 기본값으로 대체하지 않는다.
- DI는 `@RequiredArgsConstructor`와 `private final` 필드를 사용한다. 검증·초기화 본문이 필요한 생성자만 직접 작성한다.
- 관련 코드는 가까이 두고, 상위 흐름을 먼저, 보조 메서드는 호출자 아래에 둔다.

## 테스트

- 테스트는 H2(PostgreSQL 모드)로 실행한다: `./gradlew test`
- `@DisplayName`은 검증하는 동작을 한국어 문장으로 쓴다.
- jsonb, 부분 유일 인덱스, advisory lock처럼 H2가 흉내 내지 못하는 동작은 테스트 한계로 보고한다.

## 브랜치

- 브랜치 이름은 `<type>/<이슈 번호>-<영문 설명>` 형식을 사용한다. 영문 설명은 소문자와 하이픈으로 작성한다. 예: `feat/3-loan-product-query`.
- type은 `feat`, `fix`, `refactor`, `test`, `docs`, `chore` 중 선택한다.
- 새 이슈의 작업 브랜치는 최신 `origin/main`에서 분기한다. 지정한 브랜치에서만 수정한다.

## 커밋

- 요청이 금지하지 않았다면 작업 완료와 관련 검증 성공 후 커밋한다. 검증 실패 상태에서는 커밋하지 않는다.
- 하나의 커밋은 하나의 논리적인 변경만 포함한다.
- 메시지는 한글로 `<type>: <변경 내용>` 형식을 사용한다.
