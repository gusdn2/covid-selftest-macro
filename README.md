# 코로나19 자가검진 매크로 앱 (안드로이드 전용)
예약해두면 매일 특정 시간에 자동으로 자가진단을 합니다.  
**이 앱을 사용하여 생기는 모든 문제의 책임은 이 앱의 사용자인 여러분에게 있습니다.** 건강상태가 좋지 않다면 매크로 예약을 취소해두고 공식 사이트나 앱에서 자가진단을 하시길 바랍니다. 다만 버그가 생긴다면 최대한 빨리 고치도록 노력하겠습니다. 기존에 매크로가 **작동하다가 작동하지 않으면** [제 개인 이메일](mailto:lhwdev6@outlook.com)로 버그에 대한 자세한 정보를 보내주세요.

[**다운로드 링크**](https://github.com/lhwdev/covid-selftest-macro/releases/latest/download/app-release.apk)
(이 링크는 항상 최신버전 앱의 링크를 나타냅니다.)

오류가 생기면 실시간으로(보통 몇시간 안에) 패치합니다.  
제작자 자신이 쓸려고 만든 앱이고 제작자의 지인들도 쓰고 있답니다.  
참고로 가장 최근 버전 기준으로 몇 달째 잘 작동하고 있습니다. 다만 공식 사이트의 api 구조가 바뀌면
작동하지 않게 될 수 있습니다. 그런 경우 앱 화면을 띄우면 업데이트하라고 뜰... 겁니다.

참고: **새 학년이 시작될 때는 공식 앱이나 사이트에서 약관에 동의해야 합니다.**  
참고2: 구형 휴대폰(옛날 버전 안드로이드)에서는 잘 작동하지 않는 것 같습니다. 언젠간... 고치겠습니다.


## 기능:
- 버튼 한 개 클릭으로 자가진단을 할 수 있습니다.
- 매일 일정 시각에 자가진단을 자동으로 합니다. (배터리 최적화 꺼야 함)
- 자가진단 api v2를 지원합니다.


## 개발 중(아직 구현되지 않은 것):
- [ ] 범위 내 렌덤 시간 기능
- [ ] 주말, 공휴일에 자가진단 하지 않기 기능


## 자가진단 API 관련 PoC
[이 파일을 참고하세요.](PoC.md)
