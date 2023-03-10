
CREATE TABLE TASK_EXECUTION  (
	TASK_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY ,
	START_TIME TIMESTAMP DEFAULT NULL ,
	END_TIME TIMESTAMP DEFAULT NULL ,
	TASK_NAME  VARCHAR(100) ,
	EXIT_CODE INTEGER ,
	EXIT_MESSAGE VARCHAR(2500) ,
	ERROR_MESSAGE VARCHAR(2500) ,
	LAST_UPDATED TIMESTAMP,
	EXTERNAL_EXECUTION_ID VARCHAR(255),
	PARENT_EXECUTION_ID BIGINT
);

CREATE TABLE TASK_EXECUTION_PARAMS  (
	ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	TASK_EXECUTION_ID BIGINT NOT NULL ,
	TASK_PARAM VARCHAR(2500) ,
	constraint TASK_EXEC_PARAMS_FK foreign key (TASK_EXECUTION_ID)
	references TASK_EXECUTION(TASK_EXECUTION_ID)
) ;

CREATE TABLE TASK_TASK_BATCH (
  ID BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  TASK_EXECUTION_ID BIGINT NOT NULL ,
  JOB_EXECUTION_ID BIGINT NOT NULL ,
	constraint TASK_EXEC_BATCH_FK foreign key (TASK_EXECUTION_ID)
	references TASK_EXECUTION(TASK_EXECUTION_ID)
) ;

CREATE SEQUENCE TASK_SEQ ;

CREATE TABLE TASK_LOCK  (
	LOCK_KEY CHAR(36) NOT NULL,
	REGION VARCHAR(100) NOT NULL,
	CLIENT_ID CHAR(36),
	CREATED_DATE TIMESTAMP NOT NULL,
	constraint LOCK_PK primary key (LOCK_KEY, REGION)
);
