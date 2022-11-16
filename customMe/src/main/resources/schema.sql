--
-- 원료 테이블
--
DROP TABLE IF EXISTS MATERIAL;

CREATE TABLE MATERIAL COMMENT '원료' (
    NAME        	VARCHAR(10) NOT NULL                     COMMENT '원료명'
  , AMOUNT    	INT              NOT NULL DEFAULT 0      COMMENT '잔량'
  , STATUS      	VARCHAR(1)  NOT NULL DEFAULT 1      COMMENT '원료 상태(1: 정상, 2:보충중)'
  , SUPPLY_DATE	DATETIME			   COMMENT '원료 보충 시작 시간'
  , PRIMARY KEY (NAME)
);


--
-- 주문 테이블
--
DROP TABLE IF EXISTS ORDER_INFO;

CREATE TABLE ORDER_INFO COMMENT '주문' (
    ORDER_NUMBER	VARCHAR(20)       NOT NULL   COMMENT '주문번호'
  , ORDER_CODE        	VARCHAR(5) 	NOT NULL   COMMENT '주문코드'
  , REG_DATE		DATETIME		    COMMENT '등록일자'
  , ORDER_DATE    		DATE	                            COMMENT '주문일자'
  , SEND_DATE 		DATE                                 COMMENT '발송예정일'
  , ORDER_STATUS		VARCHAR(1)		    COMMENT '주문 상태(0: 주문취소, 1: 주문접수, 2:제품 생산 중, 3: 제품 생산 완료, 4: 발송 준비 중, 5: 발송 완료)'
  , ERROR		VARCHAR(100)		    COMMENT 'ERROR 내용'
  , PRIMARY KEY (ORDER_NUMBER)
);