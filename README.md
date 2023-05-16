# WeatherToday
 명지대학교 모바일컴퓨팅 2023년 1학기
 조원 : 이진서, 윤상필, 황준하

# 내용

전체적인 디자인과 레이아웃, 동작과정은 iPhone의 날씨 app 과 유사
화면은 총 "Main", "AddLocation" 그리고 "SearchLocation" 으로 구성

## Main
: 추가한 위치의 날씨 정보를 알려주는 화면이야. 해당 화면에는 해당 정보가 나와야함. 추가한 위치는 하나 이상이 가능.

- 추가된 위치의 현재 기온(섭씨)
- 추가된 위치의 최저/최고 기온
- 추가된 위치
- 추가된 위치의 현재 습도
- 추가된 위치의 현재 시간과 날짜
- 추가된 위치의 현재 시간 이후의 시간별 기온 및 날씨
- 추가된 위치의 일출/일몰 시간

* 위의 정보들은 초단기실황조회, 초단기예보조회, 단기예보조회 api 를 사용해 가져오도록 

* Main 화면을 왼쪽으로 swipe 하면 AddLocation 화면이 나와야 함. 그렇기에 Main 화면 하단에 총 몇 개의 Main 화면이 있는지 알려주는 . 을 표시해줘야하며, 마지막에 AddLocation 화면이 있다는 것을 알려주는 + 모양을 표기

* 또한 빈 공간에 작게 새로고침 버튼이 존재하여, 해당 버튼을 클릭 시 현재 시간을 기반으로 추가된 위치의 현재 기온을 갱신하고 가져올 수 있어야함

* 또한 빈 공간에 해당 Main 화면에 해당된 주소를 DB에서 지울 수 있는 삭제 버튼 존재


## AddLocation
: 추가된 위치가 없거나, Main 화면에서 왼쪽으로 swipe 하면 뜨는 화면으로 화면 중간 상단에 큰 원 안에 + 모양이 있는 버튼이 있으며 그 하단에 버튼보다 작게 "위치를 추가해주세요."라는 text 가 떠 있음. 큰 원 안에 +가 있는 버튼을 클릭하면 SearchLocation 화면으로 전환

## SearchLocation
: 상단에 주소를 입력하고 검색할 수 있는 검색 창이 있고, 그 옆에 검색 버튼이 있어.  검색 창에 주소를 입력하고 검색 버튼을 클릭하면 검색 창 아래로 이에 해당하는 주소들이 row 별로 하나 씩 전부 나오게 됨. 이 중 하나를 선택하면 해당 주소가 데이터베이스에 추가 되며 해당 주소를 기반으로 날씨가 Main에 나오게 됨

* 주소 검색은 Kakao 주소 검색 api를 통해 진행

--------------------------

Weather Today Application을 사용하는 일련의 과정
1. Weather Today 최초 실행 시 추가된 주소가 없으므로 AddLocation 화면이 나오게 됨
2. + 버튼을 클릭 시 SearchLocation 화면으로 전환
3. 검색 창에 내가 원하는 주소를 입력하고 검색
4. 나오는 주소 중 내가 원하는 주소와 가장 일치하는 주소 클릭(클릭한 주소가 DB에 저장)
5. 주소 클릭 시 Main 으로 전환되며 Main에는 클릭한 주소의 날씨 정보가 나오게 됨
6. 또다른 주소를 추가하고 싶을 경우, Main 에서 왼쪽으로 swipe 하면 AddLocation 화면이 나오게 됨.
7. 2~5의 과정을 진행하면 1에서 추가한 주소 외에 다른 주소가 추가됨, 고로 Main 화면은 두개이며 첫 Main 화면에서 한번 왼쪽으로 swipe 하면 가장 최근에 추가한 주소의 날씨를 보여주는 Main 화면이 보이고 다시 한번 왼쪽으로 swipe 하면 AddLocation 화면이 보임.

----------------------------

# 필수 구현 목록
- 2개 이상의 activity / 2개 이상의 fragment 사용
- database를 사용하고 insert / delete 기능이 앱에서 작동 가능해야 함 
: 위의 조건에 맞는 database를 설계하고 주소를 insert/delete 할 수 있게 구현
- DB위한 table 설계가 있어야하며 DB의 data를 동적으로 view를 통해 보여주는 기능이 있어야함
- 개발한 기능내에서는 이상 없이 정상 작동 해야함


# 프로젝트 규칙  
- 코드, 폴더 구조 변경 금지  
- Commit 규칙 준수하기  
- Commit은 따로 Branch 생성 / Fork 후 PR하기  
- 커밋 메시지는 "[Type] message" 형식으로 작성하기  
  | **Type** | **Meaning** |  
  | -------- | ------|  
  | Feat    | 새로운 기능 |  
  | Fix     | 버그 수정 |  
  | Resolve | 충돌 해결 |  
  | Build   | 빌드 관련 파일 수정 |  
  | Chore   | 그 외 자잘한 수정 |  
  | Style   | 스타일 변경 |  
  | Docs    | 문서 수정 |  
  | Refactor| 코드 리팩토링 |  
  | Test    | 테스트 코드 수정 |  
  | Add     | 새로운 라이브러리 / 파일 생성 |
