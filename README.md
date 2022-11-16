# amore

# 개발환경
- Spring Tool Suite 4(Version: 4.15.3.RELEASE)
- java 17
- h2 db(내장 db)
- mybatis

# 빌드 및 실행
 1. https://github.com/dami9350/amore 에서 소스 zip 파일로 다운로드 후 압축 해제
 2. sts 또는 eclipse 에서 inport -> Maven -> Existing Maven Projects 클릭 후 압축 해제된 customMe 폴더 선택
 3. tomcat 실행(h2 db 스키마 및 데이터 자동 생성 됨)
 4. postman 등을 통해 http://localhost:8080/order 를 post로 json 형식으로 호출
    ex) 
    {
    "order_number" : "102347129883",
    "order_code" : "A7B3",
    "order_date" : "20221116"
    }
 
# 특이사항
- 주문 입력 url : http://localhost:8080/order
- 근무 시간외 처리(제품 생산 -> 발송, 주문 접수 -> 제품 생산), 원료 보충은 Spring Scheduled 로 처리
- 근무 시간외 처리 배치는 5초마다 실행, 생산 설비의 상태 로그는 10초마다 실행
- 하루 8시간 근무 시간을 480초 로 간주(근무 시간 1분을 1초로 간주)
- 근무시간 외 16시간은 30초로 간주
