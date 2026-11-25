---
name: git-commit
description: loan-maru의 브랜치·커밋 규칙. 작업 브랜치를 만들거나, 커밋 메시지를 쓰거나, 커밋할 파일을 고르거나, push·PR을 준비할 때 쓴다.
---

# git-commit

git flow를 참고하되, 지금은 배포 단계가 없어 `main`과 작업 브랜치만 쓴다.

## 브랜치

| 브랜치 | 용도 | 규칙 |
|---|---|---|
| `main` | 리뷰를 마친 코드 | 직접 커밋하지 않는다. PR 머지로만 바뀐다. |
| `<type>/<이슈 번호>-<영문 설명>` | 이슈 하나의 작업 | 최신 `origin/main`에서 만든다. 예: `feat/3-loan-product-query` |

- 영문 설명은 소문자와 하이픈으로 쓴다.
- 다른 작업 브랜치 위에 브랜치를 쌓지 않는다. 앞 작업이 필요하면 그 PR이 머지된 뒤 `origin/main`에서 다시 만든다.
- `develop`, `release/*`, `hotfix/*`는 배포 환경이 생기면 도입한다.

## 흐름

1. 이슈를 만든다. 템플릿을 쓰고 type 라벨을 단다.
2. `origin/main`에서 작업 브랜치를 만든다.
3. 작업하고 검증한 뒤 로컬에 커밋한다.
4. 브랜치, 커밋 목록, PR 제목·본문 초안을 보여 주고 승인을 받는다.
   - 제목은 이슈 제목과 같게 쓴다.
   - 본문은 `.github/pull_request_template.md`의 절을 그대로 채운다. 맨 위 `> issue : #<번호>`, 맨 끝 `Closes #<번호>`를 둔다. Claude Code 생성 표시 줄(`🤖 Generated with ...`)은 넣지 않는다.
5. 승인받은 뒤에 push하고 `gh pr create --body-file`로 PR을 만든다.
6. 머지는 사용자가 한다.

## 커밋 메시지

```text
<type>: <무엇을 바꿨는지 한글로>

<필요할 때만: 왜 바꿨는지, 리뷰어가 알아야 할 점>

Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>
```

| type | 쓰는 때 |
|---|---|
| `feat` | 새 기능, API |
| `fix` | 버그 수정 |
| `refactor` | 동작을 바꾸지 않는 구조 개선 |
| `test` | 테스트만 추가·수정 |
| `docs` | 문서만 수정 |
| `chore` | 빌드, 설정, 저장소 관리 |

- 제목은 50자 안쪽으로, 마침표 없이 "~ 추가", "~ 수정"처럼 끝낸다.
- type은 이슈 라벨과 같은 이름을 쓴다.

## 커밋 단위

- 커밋 하나에는 논리적인 변경 하나만 담는다. 예: 빌드 구성과 API 구현은 나눈다.
- 각 커밋에서 빌드가 깨지지 않게 한다. 검증이 실패한 상태로 커밋하지 않는다.
- 커밋 전에 `git status`와 `git diff --staged`로 담긴 파일을 확인한다.
- 파일은 경로를 지정해 `git add`한다. `git add -A`, `git add .`은 쓰지 않는다.
- `build/`, `.gradle/`, `.claude/settings.local.json`, 비밀값이 든 파일은 커밋하지 않는다.

## 하지 않는 것

- 승인 없는 push, PR 생성, 머지, PR 닫기, 브랜치 삭제
- `git push --force`, 이미 push한 커밋의 amend·rebase
- `--no-verify`로 훅 건너뛰기
- `gh pr close/merge --delete-branch`. 로컬 브랜치까지 지운다.
