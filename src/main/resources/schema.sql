
DROP TABLE IF EXISTS       User_Detail;

CREATE TABLE User_Detail
(
    id        VARCHAR (50) PRIMARY KEY,
    login     VARCHAR (50) NOT NULL,
    name      NVARCHAR (50) NOT NULL, --NVarchar to support unicode 
    salary    NUMERIC (13,3)  NOT NULL default 0,
    start_date      DATE  NOT NULL default now()
);

ALTER TABLE User_Detail ADD (
  CONSTRAINT UC_User_Detail_Login
  UNIQUE (login));