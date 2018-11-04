
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