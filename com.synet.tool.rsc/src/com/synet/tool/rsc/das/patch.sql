
/*==============================================================*/
/* Table: IM110_LINK_WARN                                       */
/*==============================================================*/
create table IM110_LINK_WARN
(
   IM110_CODE           varchar(48) not null,
   IM100_CODE           varchar(48) not null,
   DEV_NAME           	varchar(96),
   DEV_DESC           	varchar(96),
   MMS_DESC	            varchar(96),
   MMS_REF_ADDR     	varchar(96),
   SEND_DEV_NAME     	varchar(96),
   CB_REF		     	varchar(96),
   MATCHED           	INT,
   primary key (IM110_CODE)
);

/*==============================================================*/
/* Table: IM111_Fibre_List                                      */
/*==============================================================*/
create table IM111_Fibre_List
(
   IM111_CODE           varchar(48) not null,
   IM100_CODE           varchar(48) not null,
   CABLE_CODE           varchar(48),
   CORE_CODE           	varchar(96),
   CONN_TYPE            varchar(48),
   DEV_NAMEA           	varchar(96),
   DEV_DESCA           	varchar(96),
   BOARD_CODEA          varchar(96),
   PORT_CODEA           varchar(96),
   DEV_NAMEB          	varchar(96),
   DEV_DESCB           	varchar(96),
   BOARD_CODEB          varchar(96),
   PORT_CODEB           varchar(96),
   MATCHED           	INT,
   primary key (IM111_CODE)
);

/*==============================================================*/
/* Table: TB1084_FuncClass                                      */
/*==============================================================*/
create table TB1084_FuncClass
(
   F1084_CODE           varchar(48) not null,
   F1084_DESC           varchar(96) not null,
   primary key (F1084_CODE)
);

/*==============================================================*/
/* Table: TB1085_ProtFunc                                       */
/*==============================================================*/
create table TB1085_ProtFunc
(
   F1085_CODE           varchar(48) not null,
   F1046_CODE           varchar(48) not null,
   F1084_CODE           varchar(48) not null,
   F1085_LEVEL          INT,
   primary key (F1085_CODE)
);

/*==============================================================*/
/* Table: TB1086_DefectFuncR                                    */
/*==============================================================*/
create table TB1086_DefectFuncR
(
   	F1086_CODE           varchar(48) not null,
  	F1086_ST_CODE        varchar(48),
  	F1086_MX_CODE        varchar(48),
	F1086_DefectType     INT,
	F1086_SubType        INT,
	F1086_DefectLevel    INT,
	F1085_CODE       	 varchar(48) not null,
   	primary key (F1086_CODE)
);

CREATE INDEX TB1006F1006ADDREFIndex ON TB1006_AnalogData(F1006_ADDREF);
CREATE INDEX TB1006F1046CODEIndex ON TB1006_AnalogData(F1046_CODE);

CREATE INDEX TB1016F1016ADDREFIndex ON TB1016_StateData(F1016_ADDREF);
CREATE INDEX TB1016F1046CODEIndex ON TB1016_StateData(F1046_CODE);

CREATE INDEX TB1058F1058ADDREFIndex ON TB1058_MMSFCDA(F1058_RefAddr);
CREATE INDEX TB1058F1046CODEIndex ON TB1058_MMSFCDA(F1046_CODE);

CREATE INDEX TB1061F1061ADDREFIndex ON TB1061_POUT(F1061_RefAddr);
CREATE INDEX TB1061F1046CODEIndex ON TB1061_POUT(F1046_CODE);

CREATE INDEX TB1062F1062ADDREFIndex ON TB1062_PIN(F1062_RefAddr);
CREATE INDEX TB1062F1046CODEIndex ON TB1062_PIN(F1046_CODE);

