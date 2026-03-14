# GitHub Actions 배포 환경 변수 (Secrets) 가이드

이 문서는 `.github/workflows/make-release.yml` 워크플로우에서 사용되는 환경 변수 및 GitHub Secrets에 대한 설명입니다. 
성공적인 빌드, 릴리즈 생성 및 원격 서버 배포를 위해 저장소의 **Settings > Secrets and variables > Actions** 에 아래의 Secret 값들을 등록해야 합니다.

## 1. 서버 배포용 Secrets (SSH & SCP)
원격 서버에 빌드된 `.jar` 파일을 전송하고 스크립트를 통해 봇을 재시작하는 데 필요한 정보입니다.

| 시크릿 이름 | 설명 | 필수 여부 | 기본값 |
|---|---|---|---|
| `REMOTE_HOST` | 배포할 원격 서버의 IP 주소 또는 도메인 | 필수 | - |
| `REMOTE_USER` | 원격 서버에 SSH로 접속할 사용자 이름 (예: `ubuntu`, `root` 등) | 필수 | - |
| `REMOTE_PASSWORD` | 원격 서버 접속에 사용할 사용자 비밀번호 | 필수 | - |
| `REMOTE_PORT` | SSH 접속 포트 번호 | 선택 | `22` |
| `REMOTE_TARGET_DIR` | 서버 내에서 봇 파일이 위치하고 실행될 디렉토리 절대 경로 (예: `/home/ubuntu/musicbot`) | 필수 | - |

> **참고:** 서버 배포 후 실행되는 `./restart.sh` 스크립트는 `REMOTE_TARGET_DIR` 경로 안에 미리 생성되어 있어야 합니다.

## 2. GitHub 제공 Secrets
| 시크릿 이름 | 설명 | 필수 여부 |
|---|---|---|
| `GITHUB_TOKEN` | GitHub Release를 생성하기 위해 사용되는 권한 토큰입니다. GitHub Actions에서 실행 시 자동으로 발급하므로 사용자가 직접 등록할 필요는 없습니다. | 자동 발급 |

## 3. 워크플로우 수동 실행 시 입력값 (Inputs)
해당 워크플로우를 수동으로 실행(Workflow Dispatch)할 때 사용자에게 입력받는 항목입니다.

| 변수명 | 설명 | 타입 | 필수 여부 |
|---|---|---|---|
| `info` | 이번 릴리즈에 대한 설명(Description)입니다. 이 내용은 GitHub 릴리즈 노트의 본문으로 사용됩니다. | String | 필수 |