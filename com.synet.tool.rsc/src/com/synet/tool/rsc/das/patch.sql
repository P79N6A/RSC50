
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

