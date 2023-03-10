
CREATE TABLE TASK_EXECUTION  (
	TASK_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
	START_TIME TIMESTAMP(9) DEFAULT NULL ,
	END_TIME TIMESTAMP(9) DEFAULT NULL ,
	TASK_NAME  VARCHAR(100) ,
	EXIT_CODE INTEGER ,
	EXIT_MESSAGE VARCHAR(2500) ,
	ERROR_MESSAGE VARCHAR(2500) ,
	LAST_UPDATED TIMESTAMP(9),
	EXTERNAL_EXECUTION_ID VARCHAR(255),
	PARENT_EXECUTION_ID BIGINT
);

CREATE TABLE TASK_EXECUTION_PARAMS  (
	TASK_EXECUTION_ID BIGINT NOT NULL ,
	TASK_PARAM VARCHAR(2500) ,
	constraint TASK_EXEC_PARAMS_FK foreign key (TASK_EXECUTION_ID)
	references TASK_EXECUTION(TASK_EXECUTION_ID)
) ;

CREATE TABLE TASK_TASK_BATCH (
  TASK_EXECUTION_ID BIGINT NOT NULL ,
  JOB_EXECUTION_ID BIGINT NOT NULL ,
	constraint TASK_EXEC_BATCH_FK foreign key (TASK_EXECUTION_ID)
	references TASK_EXECUTION(TASK_EXECUTION_ID)
) ;

CREATE SEQUENCE TASK_SEQ AS BIGINT START WITH 0 MINVALUE 0 MAXVALUE 9223372036854775807 NOCACHE NOCYCLE;

CREATE TABLE TASK_LOCK  (
	LOCK_KEY CHAR(36) NOT NULL,
	REGION VARCHAR(100) NOT NULL,
	CLIENT_ID CHAR(36),
	CREATED_DATE TIMESTAMP(9) NOT NULL,
	constraint LOCK_PK primary key (LOCK_KEY, REGION)
);
