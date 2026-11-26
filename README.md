# loan-maru
> 대출 신청을 받아 상품별 규칙으로 한도와 금리를 심사하고, 판정 근거를 남기는 대출 한도 심사 서버

### Project Spec

`Java 21` `Spring Boot 4.x` `PostgreSQL` `Gradle 멀티모듈`

### 선택 이유

- **Java 21**: 외부 호환성 제약이 없는 새 프로젝트이고, 현재 LTS 버전이다.
- **Spring Boot 4.x**: 3.x를 유지해야 할 기존 시스템이 없다. 나중에 4.x로 옮기는 비용을 미리 없앤다.
- **PostgreSQL**: 심사 정합성을 DB 기능으로 지키기 쉽다.
  - 규칙별 판정 근거(대외계 기준 시각 등)를 `jsonb`로 저장하고 조회할 수 있다.
  - 부분 유일 인덱스(`WHERE` 조건이 붙은 `UNIQUE`)로 진행 중인 신청만 골라 재신청을 식별할 수 있다.
  - advisory lock으로 같은 차주(`cust_ci`)의 심사 확정을 직렬화해, 동시에 들어온 신청이 한도를 이중으로 쓰지 않게 한다.
- **멀티모듈**: 심사 서버가 부르는 외부 기관 4곳을 Mock 서버로 함께 띄워, 로컬에서 전체 흐름을 확인한다.

### 모듈 구성

| 모듈 | 역할 | 포트 | 임시 응답 API |
|---|---|---|---|
| `server/loan-screening-server` | 신청 접수와 심사 | 8080 | `GET /api/v1/loan-products` |
| `server/account-server` | 계정계 Mock (자사 여신 계좌) | 8081 | `POST /if/loan/inqLoanAcctList` |
| `server/credit-info-server` | 대외계 Mock (타 금융기관 대출) | 8082 | `POST /credit/loan/inquiry` |
| `server/delinquency-server` | 여신사후관리 Mock (연체 이력) | 8083 | `GET /delinquencies?acctNo=` |
| `server/notification-server` | 발송 Mock (알림톡) | 8084 | `POST /notifications/alimtalk` |

지금은 모든 API가 고정 응답을 준다. DB는 `loan-screening-server`만 쓴다.

### 패키지 구조

5개 모듈이 같은 구조를 쓴다. 헥사고날 구조를 기본으로 하되 `port`/`adapter` 계층은 두지 않는다.

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

| 모듈 | 도메인 패키지 |
|---|---|
| `loan-screening-server` | `application.product` |
| `account-server` | `application.loanaccount` |
| `credit-info-server` | `application.externalloan` |
| `delinquency-server` | `application.delinquency` |
| `notification-server` | `application.alimtalk` |

- 의존 방향: `api → service → domain ← infrastructure`
- 예외: `service`는 `api.dto`를 파라미터나 반환 타입으로 쓸 수 있다.
- 테이블 구조: [ERD](docs/erd/README.md)

### 로컬 실행

Docker와 JDK가 필요하다. 로컬에 JDK 21이 없으면 Gradle이 툴체인을 내려받는다.

```bash
docker compose up -d                                   # PostgreSQL(localhost:5432/loan_maru)
./gradlew :server:loan-screening-server:bootRun        # 심사 서버
./gradlew :server:account-server:bootRun               # Mock 서버는 각각 띄운다
```

DB 접속 정보는 `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, 포트는 `SERVER_PORT` 환경변수로 바꿀 수 있다.
5432 포트를 이미 쓰고 있다면 `DB_PORT=55432 docker compose up -d`로 띄우고 `DB_URL=jdbc:postgresql://localhost:55432/loan_maru`로 실행한다.

테스트는 DB 없이 실행된다. 심사 서버는 H2 인메모리 DB(PostgreSQL 모드)를 쓴다.

```bash
./gradlew test
```
