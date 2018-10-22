--//////////////////////////////////////////////////////////////////
--
--修改记录
--
--日期          作者         原因
--2018/06/14     songx         create tabled
--			
--//////////////////////////////////////////////////////////////////

--=================================================================参数表=================================================================

--================创建设备类型表（SCD设备类型表）=====================
drop table TB1001_DEVTYPE;
create table TB1001_DEVTYPE
  (
  F1001_TYPE       number(5)   not null,   --类型    
  F1001_CODE       char(48)    not null,   --代码   
  F1001_DESC       char(96)    null,       --描述     
  F1001_PARATBL    char(48)    null,       --参数表
  F1001_PREFIX     char(48)    null        --前缀
  );
alter table TB1001_DEVTYPE add constraint TB1001_DEVTYPEindex1
primary key (F1001_CODE);

--===============创建计算量关系参数表（计算量关系参数表）================
drop table TB1005_EXPRESSMAP;
create table TB1005_EXPRESSMAP
  (  
  F1005_CODE       char(48)     not null,   --目标代码  
  F1005_TYPE       number(3)    not null,   --0=遥测量，1=遥信量，2=电度量
  F1005_EXPRESS    char(255)    not null ,  --计算表达式
  F1005_STARTMODE  number(3)    not null,   --启动方式，0=循环，1=定时，2=指定日，3=指定月，4=指定年
  F1005_PERIOD		 number(10)   null,       --循环周期，秒
  F1005_ATMINUTE   number(5)    null,       --定时分钟
  F1005_ATHOUR     number(5)    null,       --定时小时
  F1005_ATDAY      number(5)    null,       --指定日期
  F1005_ATMONTH    number(5)    null,       --指定月
  F1005_ATYEAR     number(5)    null        --指定年
  );
alter table TB1005_EXPRESSMAP add constraint TB1005_EXPRESSMAPindex1 primary key (F1005_CODE);


--==============创建量测类型表(SCD对象类型表)=============================
drop table TB1011_MEASTYPE;
create table TB1011_MEASTYPE
  (   
  F1011_NO    number(5) not null,    --类型值
  F1011_CODE  char(48)  null,        --类型代码     
  F1011_DESC  char(96)  not null,    --描述   
  F1011_TBL   char(48)  not null    --表名
  );
alter table TB1011_MEASTYPE add constraint TB1011_MEASTYPEindex1 primary key  (F1011_NO);


--==============创建模拟量表（参数表）====================
drop table TB1006_ANALOGDATA;
create table TB1006_ANALOGDATA
(
	F1006_CODE		char(48)	not null,	--代码（Analog000001~Analog999999）  -RSC-50
	F1006_DESC		char(96)	null,		--描述（缺省填SCD中的描述）     -RSC-50
	F1006_SAFELEVEL		number(10)	null,		--安全级（缺省填0）    -RSC-50
	PARENT_CODE		char(48)	null,		--设备代码                -RSC-50
	F1011_NO		number(5)	null,		--类型       -RSC-50
	F1006_BYNAME		char(24)	null,		--别名
	F0008_NAME		char(48)	null,		--事项处理方式
	F0009_NAME		char(48)	null,		--事项句代码
	F1006_CALCFLAG		number(3)	null,		--计算量标志
	F1006_PICNAME		char(96)	null,		--推图名
	F1006_PDRMODE		number(3)	null,		--追忆模式
	F1006_K			float		null,		--斜率
	F1006_B			float		null,		--截距
	F1006_ZERODBAND		float		null,		--零漂
	F1006_OVERFLOW		float		null,		--上溢值
	F1006_LOWFLOW		float		null,		--下溢值
	F1006_MAXINC		float		null,		--最大增量
	F1006_HIWARN		float		null,		--警告上限
	F1006_LOWARN		float		null,		--警告下限
	F1006_HIALARM		float		null,		--警报上限
	F1006_LOALARM		float		null,		--警报下限
	F1006_SAVEPERIOD	number(3)	null,		--存盘周期
	F1006_PLANTIME		number(3)	null,		--计划间隔
	F1006_DEADTIME		number(5)	null,		--死数时间限
	F1006_ALARMLEVEL	number(3)	null,		--处理越限级别
	F1006_SAVETYPE		number(3)	null,		--历史数据采样类型（瞬时值/平均值）
	F1006_MANSET		number(3)	null,       	--置入标志
	F1006_SAMPLECPMAXINC 	number(3)	null        	--同期比较最大增量
);
alter table TB1006_ANALOGDATA
add constraint   TB1006_ANALOGDATAindex1 primary key (F1006_CODE);
--create table index TB1006_ANALOGDATAindex4 on TB1006_ANALOGDATA (F1006_DESC);

--==============创建状态量表（参数表）====================
drop table TB1016_STATEDATA;
create table TB1016_STATEDATA
(
	F1016_CODE		char(48)	not null,	--代码（State000001~State999999）     -RSC-50
	F1016_DESC		char(96)	null,		--描述（缺省填SCD中的描述）  -RSC-50
	F1016_SAFELEVEL		number(10)	null,		--安全级（缺省填0）  -RSC-50
	PARENT_CODE		char(48)	not null,	--设备代码（上一级索引）       -RSC-50
	F1011_NO		number(5)	not null,	--类型（功能类型）  -RSC-50
	F1016_BYNAME		char(24)	null,		--别名
	F0008_NAME		char(48)	null,		--事项处理方式
	F0009_NAME		char(48)	null,		--事项句代码
	F1016_CALCFLAG		number(3)	null,		--计算量标志（熟数据flag）
	F1016_PICNAME		char(96)	null,		--推图名（推图至上方）
	F1016_ISPDR		number(3)	null,		--是否追忆（历史断面）
	F1016_PDRNO		number(5)	null,		--追忆组号
	F1016_DPSFLAG		number(3)	null,		--双遥信标志
	F1016_MAINSTFLAG	number(3)	null,		--主遥信标志
	F1016_DPSCALCFLAG	number(3)	null,		--双遥信计算方式
	F1016_SOE		char(48)	null,		--SOE句
	F1016_SGLIMITVAL	number(5)	null,		--事故限值
	F1016_ALARMPROCMODE	number(3)	null,		--判事故方式
	F1016_PROCBAND		number(3)	null,		--判事故死区
	F1016_SGPROCNAME	char(48)	null,		--事故过程名
	F1016_REVFLAG		number(3)	null,		--取反标志
	F1016_ISSTA		number(3)	null		--是否统计
	--F1016_MANSET		number(3)	null        --置入标志
);
alter table TB1016_STATEDATA
add constraint   TB1016_STATEDATAindex1 primary key (F1016_CODE);

--==============创建字符串表（参数表）====================
drop table TB1026_STRINGDATA;
create table TB1026_STRINGDATA
(
	F1026_CODE			char(48)	not null,	--代码（String000001~String999999） -RSC-50
	F1026_DESC			char(96)	null,		--描述   -RSC-50
	F1026_SAFELEVEL			number(10)	null,		--安全级  -RSC-50
	PARENT_CODE			char(48)	not null,	--设备代码  -RSC-50
	F1011_NO			number(5)	not null,	--类型  -RSC-50
	F1026_BYNAME			char(24)	null,		--别名
	F0008_NAME			char(48)	null,		--事项处理方式
	F0009_NAME			char(48)	null,		--事项句代码
	F1026_CALCFLAG			number(3)	null,		--计算量标志
	F1026_PICNAME			char(96)	null,		--推图名
	F1026_ISPDR			number(3)	null,		--是否追忆
	F1026_PDRNO			number(5)	null,		--追忆组号
	F1026_ISSTA			number(3)	null		--是否统计
);
alter table TB1026_STRINGDATA
add constraint   TB1026_STRINGDATAindex1 primary key (F1026_CODE);





--drop view TB1023_OBJECTVIEW;
--create table view TB1023_OBJECTVIEW
--(
--	OBJ_CODE,	--对象代码
--	OBJ_TYPE,	--对象类型
--	PARENT_CODE	--对象的父对象代码
--);
--as 
--select 
--	TB1002_DEVPARA.F1002_CODE,
--	TB1002_DEVPARA.F1002_TYPE, 
--	TB1002_DEVPARA.PARENT_CODE
--from  
--	TB1002_DEVPARA
--union all
--select 
--	TB1003_DEVPARA.F1003_CODE,
--	TB1003_DEVPARA.F1003_CODE, 
--	TB1003_DEVPARA.PARENT_CODE
--from  
--	TB1003_DEVPARA;
	

	
	
--==============创建告警类型对应的缺陷等级配置表（参数表）====================
drop table TB1022_FAULTCONFIG;
create table TB1022_FAULTCONFIG
(
	F1022_CODE			char(48)	not null,	--代码（FaultConfig001~FaultConfig999）
	F1011_NO			number(5)	not null,	--告警类型编码
	F1022_FAULTLEVEL		number(3)	not null,	--对应的缺陷等级
	F1022_T1			Number(5)	null,		--缺陷等级4的告警信号T1时间（单位秒）内不返回视为严重缺陷
	F1022_T2			Number(5)	null,		--缺陷等级4的告警信号是否频繁出现的检测时间窗宽度T2
	F1022_K				Number(5)	null		--缺陷等级4的告警信号是否频繁出现的检测次数K
);
alter table TB1022_FAULTCONFIG add constraint   TB1022_FAULTCONFIGindex1 primary key (F1022_CODE);

	


--==============创建变电站表（参数表）====================
drop table TB1041_SUBSTATION;
create table TB1041_SUBSTATION
(
	F1041_CODE			char(48)	not null,	--变电站代码（Substation001~ Substation999）  -RSC-50
	F1041_NAME			char(48)	null,		--变电站名称（英文），从SSD导入，允许手工配置  -RSC-50
	F1041_DESC			char(96)	null,		--变电站名称（中文），从SSD导入，允许手工配置  -RSC-50
	F1041_DQNAME			char(48)	null,		--变电站所属地区代码，从SSD导入，允许手工配置  -RSC-50
	F1041_DQDESC			char(96)	null,		--变电站所属地区名称，从SSD导入，允许手工配置  -RSC-50
        F1041_COMPANY           	char(96)	null,		--变电站所属单位名称， -RSC-50
        F1041_VOLTAGEH                  Number(5)   	null,		--变电站的电压等级， -RSC-50
        F1041_VOLTAGEM                  Number(5)   	null,		--变电站的电压等级， -RSC-50
        F1041_VOLTAGEL                  Number(5)   	null		--变电站的电压等级， -RSC-50
);
alter table TB1041_SUBSTATION
add constraint   TB1041_SUBSTATIONindex1 primary key (F1041_CODE);

--==============创建间隔表（参数表）====================
drop table TB1042_BAY;
create table TB1042_BAY
(
	F1042_CODE			char(48)	not null,	--间隔代码（Bay001~Bay999） -RSC-50
	F1041_CODE			char(48)	not null,	--所属变电站代码   -RSC-50
	F1042_NAME			char(48)	null,		--间隔名称    -RSC-50
	F1042_DESC			char(96)	null,		--间隔名称（中文）   -RSC-50
	F1042_VOLTAGE			number(5)	null,		--间隔的电压等级，10、35、66、110、220、330、500、750、1000等   -RSC-50
	F1042_CONNTYPE			number(3)       null,  		--间隔接线类型   -RSC-50
        F1042_DEVTYPE                   number(3) 	null,  		--间隔设备类型 -RSC-50
        F1042_IEDSOLUTION               number(3) 	null  		--间隔的IED配置方案 -RSC-50
);
alter table TB1042_BAY
add constraint   TB1042_BAYindex1 primary key (F1042_CODE);

--==============创建一次设备表（参数表）====================
drop table TB1043_EQUIPMENT;
create table TB1043_EQUIPMENT
(
	F1043_CODE			char(48)	not null,	--一次设备代码（Equipment0001~ Equipment9999） -RSC-50
	F1042_CODE			char(48)	not null,	--一次设备所属间隔代码 -RSC-50
	F1043_NAME			char(48)	null,		--一次设备名称（英文） -RSC-50
	F1043_DESC			char(96)	null,		--一次设备名称（中文） -RSC-50
	F1043_ISVIRTUAL			number(3)	null,		--是否虚拟设备（缺省填0） -RSC-50
	F1043_TYPE			number(3)	null		--一次设备类型   -RSC-50
);
alter table TB1043_EQUIPMENT
add constraint   TB1043_EQUIPMENTindex1 primary key (F1043_CODE);

--==============创建一次设备端点表（参数表）====================
drop table TB1044_TERMINAL;
create table TB1044_TERMINAL
(
	F1044_CODE			char(48)	not null,	--端点代码（Term0001~Term9999）-RSC-50
	F1043_CODE			char(48)	null,		--所属的一次设备代码 -RSC-50
	F1045_CODE			char(48)	null,		--所连接的连接点代码 -RSC-50
	F1044_NAME			char(48)	null,		--端点名称（英文）（来自SSD或手配）-RSC-50
	F1044_DESC			char(96)	null		--端点名称（中文）（来自SSD或手配）-RSC-50 
);
alter table TB1044_TERMINAL
add constraint   TB1044_TERMINALindex1 primary key (F1044_CODE);

--==============创建一次拓扑连接点表（参数表）====================
drop table TB1045_CONNECTIVITYNODE;
create table TB1045_CONNECTIVITYNODE
(
	F1045_CODE			char(48)	not null,	--连接点代码（CNode0001~CNode9999）-RSC-50
	F1045_NAME			char(48)	null,		--连接点名称（英文）（来自SSD或手配）-RSC-50
	F1045_DESC			char(96)	null		--连接点名称（中文）（来自SSD或手配）-RSC-50
);
alter table TB1045_CONNECTIVITYNODE
add constraint   TB1045_CONNECTIVITYNODEindex1 primary key (F1045_CODE);






--==============创建装置表（参数表）====================
drop table TB1046_IED;
create table TB1046_IED
(
	F1046_CODE			char(48)	not null,	--IED的代码（IED0001~IED9999）-RSC-50
	F1042_CODE			char(48)	not null,	--装置所属间隔 -RSC-50
	F1050_CODE			char(48)	null,		--IED安装的屏柜代码 -RSC-50
	F1046_NAME			char(48)	null,		--IED的名称（英文），SCD文件中IED的name属性 -RSC-50
	F1046_DESC			char(96)	null,		--IED的名称（中文），SCD文件中IED的desc属性 -RSC-50
	F1046_MANUFACTUROR		char(48)	null,		--IED生产厂家，SCD文件中IED的manufacturer属性 -RSC-50
	F1046_MODEL			char(48)	null,		--IED型号，SCD文件中IED的type属性 -RSC-50
	F1046_CONFIGVERSION		char(48)	null,		--IED配置版本，SCD文件中IED的configVersion属性 -RSC-50
	F1046_AORB			number(3)	null,		--IED是A或B套 -RSC-50
	F1046_ISVIRTUAL			number(3)	null,		--IED是否虚拟设备 -RSC-50
	F1046_TYPE			number(3)	null,		--IED类型 -RSC-50
        F1046_CRC                       char(24)	null,		--从SCD文件中提取的装置CRC码 -RSC-50
	F1200_CODE			char(48)	null		--采集组 
);
alter table TB1046_IED
add constraint   TB1046_IEDindex1 primary key (F1046_CODE);

--==============创建装置表（视图）====================



--==============创建装置板卡表（参数表）====================
drop table TB1047_BOARD;
create table TB1047_BOARD
(
	F1047_CODE			char(48)	not null,	--板卡代码（Board0001~Board9999） -RSC-50
	F1046_CODE			char(48)	not null,	--所属IED -RSC-50
	F1047_SLOT			char(24)	null, 		--板卡编号 -RSC-50
	F1047_DESC			char(96)	null,		--板卡描述 -RSC-50
	F1047_TYPE			char(48)	null		--板卡型号 -RSC-50
);
alter table TB1047_BOARD
add constraint   TB1047_BOARDindex1 primary key (F1047_CODE);

--==============创建装置端口表（参数表）====================
drop table TB1048_PORT;
create table TB1048_PORT
(
	F1048_CODE			char(48)	not null,	--端口代码（Port0001~Port9999） -RSC-50
	F1047_CODE			char(48)	not null,	--所属板卡 -RSC-50
	F1048_NO			char(24)	null, 		--端口序号 -RSC-50
	F1048_DESC			char(96)	null,		--端口描述 -RSC-50
	F1048_DIRECTION			number(3)	null,		--端口方向 -RSC-50
	F1048_PLUG			number(3)	null		--接口型式 -RSC-50
);
alter table TB1048_PORT
add constraint   TB1048_PORTindex1 primary key (F1048_CODE);


--==============创建区域表（参数表）====================
drop table TB1049_REGION;
create table TB1049_REGION
(
	F1049_CODE			char(48)	not null,	--区域代码（Region001~Region999） -RSC-50
	F1041_CODE			char(48)	not null,	--变电站代码 -RSC-50
	F1049_NAME			char(48)	null, 		--区域名称 -RSC-50
	F1049_DESC			char(96)	null,		--区域描述 -RSC-50
	F1049_AREA			number(3)	null		--户内/户外，其值为： -RSC-50
);
alter table TB1049_REGION
add constraint   TB1049_REGIONindex1 primary key (F1049_CODE);

--==============创建屏柜表（参数表）====================
drop table TB1050_CUBICLE;
create table TB1050_CUBICLE
(
	F1050_CODE			char(48)	not null,	--屏柜代码（Cubicle0001~Cubicle9999） -RSC-50
	F1049_CODE			char(48)	not null,	--所属区域代码 -RSC-50
	F1050_NAME			char(48)	null, 		--屏柜名称 -RSC-50
	F1050_DESC			char(96)	null		--屏柜描述 -RSC-50
);
alter table TB1050_CUBICLE
add constraint   TB1050_CUBICLEindex1 primary key (F1050_CODE);

--==============创建线缆表（参数表）====================
drop table TB1051_CABLE;
create table TB1051_CABLE
(
	F1051_CODE			char(48)	not null,	--线缆代码（Cable0001~Cable9999） -RSC-50
	F1041_CODE			char(48)	not null,	--变电站代码 -RSC-50
	F1051_NAME			char(48)	null, 		--线缆名称 -RSC-50
	F1051_DESC			char(96)	null,		--线缆描述 -RSC-50
	F1051_LENGTH			number(5)	null,		--线缆长度，单位是米 -RSC-50
	F1051_CORENUM			number(3)	null,		--线缆芯数 -RSC-50
	F1050_CODE_A			char(48)	null, 		--A端屏柜代码 -RSC-50
	F1050_CODE_B			char(48)	null, 		--B端屏柜代码 -RSC-50
	F1051_TYPE			number(3)	null 		--线缆类型 -RSC-50
);
alter table TB1051_CABLE
add constraint   TB1051_CABLEindex1 primary key (F1051_CODE);

--==============创建芯线表（参数表）====================
drop table TB1052_CORE;
create table TB1052_CORE
(
	F1052_CODE			char(48)	not null,	--芯线代码（CORE0001~CORE9999） -RSC-50
	F1052_PARENT_CODE		char(48)	not null,	--芯线的父对象可以是屏柜或光缆 -RSC-50
	F1052_TYPE			number(3)	null, 		--芯线类型 -RSC-50
	F1052_NO			number(3)	null,  		--芯线序号，当类型是芯线时有效，表示芯线在线缆中的编号 -RSC-50
	F1048_CODE_A			char(48)	null,		--A端连接的端口代码 -RSC-50
	F1048_CODE_B			char(48)	null		--B端连接的端口代码 -RSC-50
);
alter table TB1052_CORE
add constraint   TB1052_COREindex1 primary key (F1052_CODE);



--==============创建过程层物理网络回路表（参数表）====================
drop table TB1053_PHYSCONN;
create table TB1053_PHYSCONN
(
	F1053_CODE			char(48)	not null,	--物理网络回路代码（PHC0001~PHC9999） -RSC-50
	F1041_CODE			char(48)	not null,	--变电站代码 -RSC-50
	F1048_CODE_A			char(48)	null, 	--端口A代码 -RSC-50
	F1048_CODE_B			char(48)	null	--端口B代码 -RSC-50
);
alter table TB1053_PHYSCONN
add constraint   TB1053_PHYSCONNindex1 primary key (F1053_CODE);

drop view TB1053_PHYSCONNVIEW1;
create view TB1053_PHYSCONNVIEW1
(
	F1053_CODE,
	F1041_CODE,
	F1048_CODE_A,
	F1048_DESC_A,
	F1048_CODE_B,
	F1048_DESC_B
)
as 
select 
  TB1053_PHYSCONN.F1053_CODE,
  TB1053_PHYSCONN.F1041_CODE,
  TB1053_PHYSCONN.F1048_CODE_A,
  TB1048_PORT.F1048_DESC,
  TB1053_PHYSCONN.F1048_CODE_B,
  TB1048_PORT.F1048_DESC
from 
  TB1053_PHYSCONN,
	TB1048_PORT  
where 
 TB1053_PHYSCONN.F1048_CODE_A=TB1048_PORT.F1048_CODE or TB1053_PHYSCONN.F1048_CODE_B=TB1048_PORT.F1048_CODE;
 
drop view TB1053_PHYSCONNVIEW;
create view TB1053_PHYSCONNVIEW
(
	F1053_CODE,
	F1041_CODE,
	F1048_CODE_A,
	F1048_DESC_A,
	F1048_CODE_B,
	F1048_DESC_B
)
as 
select 
  TB1053_PHYSCONNVIEW1.F1053_CODE,
  TB1053_PHYSCONNVIEW1.F1041_CODE,
  TB1053_PHYSCONNVIEW1.F1048_CODE_A,
  TB1053_PHYSCONNVIEW1.F1048_DESC_A,
  TB1053_PHYSCONNVIEW1.F1048_CODE_B,
  TB1048_PORT.F1048_DESC
from 
  TB1053_PHYSCONNVIEW1,
	TB1048_PORT  
where 
 TB1053_PHYSCONNVIEW1.F1048_CODE_B=TB1048_PORT.F1048_CODE;

--==============创建MMS数据集和控制块表（参数表）====================
drop table TB1054_RCB;
create table TB1054_RCB
(
	F1054_CODE			char(48)	not null,	--报告控制块代码（RCB0001~RCB9999） -RSC-50
	F1046_CODE			char(48)	not null,	--所属IED代码 -RSC-50
	F1054_RPTID			char(48)	null, 		--报告控制块ID -RSC-50
	F1054_DATASET			char(48)	null,		--对应的数据集名称 -RSC-50
	F1054_DSDESC			char(96)	null,		--对应的数据集描述 -RSC-50
	F1054_ISBRCB			number(3)	null,		--是否缓存数据控制块 -RSC-50
	F1054_CBTYPE			number(3)	null		--报告控制块类型 
);
alter table TB1054_RCB
add constraint   TB1054_RCBindex1 primary key (F1054_CODE);

--==============创建MMS数据集和控制块表（视图）====================
drop view TB1054_RCBVIEW;
create view TB1054_RCBVIEW
(
	F1054_CODE,
	F1046_CODE,
	F1046_NAME,
	F1054_RPTID, 
	F1054_DATASET,
	F1054_DSDESC,	
	F1054_ISBRCB,
	F1054_CBTYPE
)
as 
select 
  TB1054_RCB.F1054_CODE,
  TB1046_IED.F1046_CODE,
  TB1046_IED.F1046_NAME,
  TB1054_RCB.F1054_RPTID,
  TB1054_RCB.F1054_DATASET,
  TB1054_RCB.F1054_DSDESC,
  TB1054_RCB.F1054_ISBRCB,
  TB1054_RCB.F1054_CBTYPE
from 
  TB1054_RCB,
  TB1046_IED 
where 
TB1054_RCB.F1046_CODE = TB1046_IED.F1046_CODE;

--==============创建GOOSE数据集和控制块表（参数表）====================
drop table TB1055_GCB;
create table TB1055_GCB
(
	F1055_CODE			char(48)	not null,	--GOOSE数据集代码（GCB0001~GCB9999） -RSC-50
	F1046_CODE			char(48)	not null,	--所属IED的代码 -RSC-50
	F1055_CBNAME			char(48)	null,		--GOOSE控制块名称 -RSC-50
	F1055_CBID			char(48)	null,		--GOOSE控制块ID -RSC-50
	F1055_MACADDR			char(24)	null,		--MAC地址 -RSC-50
	F1055_VLANID			char(24)	null,		--VLAN ID -RSC-50
	F1055_VLANPRIORITY		char(24)	null,		--VLAN优先级 -RSC-50
	F1055_APPID			char(24)	null,		--APPID -RSC-50
	F1055_DATASET			char(24)	null,		--对应的数据集名称 -RSC-50
	F1055_DESC			char(96)	null,		--对应的数据集描述 -RSC-50
	F1071_CODE			char(48)	null		--对应的采集器代码 -RSC-50
);
alter table TB1055_GCB
add constraint   TB1055_GCBindex1 primary key (F1055_CODE);

--==============创建SV数据集和控制块表（参数表）====================
drop table TB1056_SVCB;
create table TB1056_SVCB
(
	F1056_CODE			char(48)	not null,	--SV数据集代码（SVCB0001~SVCB9999） -RSC-50
	F1046_CODE			char(48)	not null,	--所属IED的代码 -RSC-50
	F1056_CBNAME			char(48)	null,		--SV控制块名称 -RSC-50
	F1056_CBID			char(48)	null,		--SV控制块ID -RSC-50
	F1056_MACADDR			char(24)	null,		--MAC地址 -RSC-50
	F1056_VLANID			char(24)	null,		--VLAN ID -RSC-50
	F1056_VLANPriority		char(24)	null,		--VLAN优先级 -RSC-50
	F1056_APPID			char(24)	null,		--APPID -RSC-50
	F1056_DATASET			char(24)	null,		--对应的数据集名称 -RSC-50
	F1056_DESC			char(96)	null,		--对应的数据集描述 -RSC-50
	F1071_CODE			char(48)	null		--对应的采集器代码 -RSC-50
);
alter table TB1056_SVCB
add constraint   TB1056_SVCBindex1 primary key (F1056_CODE);

--==============创建定值数据集和控制块表（参数表）====================
drop table TB1057_SGCB;
create table TB1057_SGCB
(
	F1057_CODE			char(48)	not null,	--定值数据集代码（SGCB0001~SGCB9999） -RSC-50
	F1046_CODE			char(48)	not null,	--所属IED的代码 -RSC-50
	F1057_CBNAME			char(48)	null,		--定值控制块名称 -RSC-50
	F1057_DATASET			char(48)	null,		--对应的数据集名称 -RSC-50
	F1057_DSDESC			char(96)	null		--对应的数据集描述 -RSC-50
);
alter table TB1057_SGCB
    add constraint   TB1057_SGCBindex1 primary key (F1057_CODE);


--==============创建创建定值数据集和控制块表（参数表)（视图）====================
drop view TB1057_SGCBVIEW;
create view TB1057_SGCBVIEW
(
    F1057_CODE,
    F1046_CODE,
    F1046_NAME,
    F1057_CBNAME,
    F1057_DATASET,
    F1057_DSDESC
)
as 
select 
  TB1057_SGCB.F1057_CODE, 
  TB1057_SGCB.F1046_CODE, 
  TB1046_IED.F1046_NAME,
  TB1057_SGCB.F1057_CBNAME, 
  TB1057_SGCB.F1057_DATASET,
  TB1057_SGCB.F1057_DSDESC
from 
  TB1057_SGCB,
	TB1046_IED  
where 
 TB1057_SGCB.F1046_CODE=TB1046_IED.F1046_CODE;


--==============创建MMSFCDA表（参数表）====================
drop table TB1058_MMSFCDA;
create table TB1058_MMSFCDA
(
	F1058_CODE			char(48)	not null,	--FCDA代码（FCDA000001~FCDA999999） -RSC-50
	F1046_CODE			char(48)	not null,	--所属IED代码 -RSC-50
	F1054_CODE			char(48)	null,		--对应的报告控制块代码 -RSC-50
	F1058_REFADDR			char(48)	null,		--FCDA参引地址的参引地址 -RSC-50
	F1058_INDEX			number(3)	null,		--FCDA在数据集中的序号 -RSC-50
	F1058_DESC			char(96)	null,		--数据点描述 -RSC-50
	F1058_DATATYPE			number(3)	null,		--数据值类型 -RSC-50
	DATA_CODE			char(48)	null		--FCDA对应的数据点代码，根据数据值类型分别对状态量表TB1016、模拟量表TB1006和字符串量表TB1026中对应点的代码 -RSC-50
);
alter table TB1058_MMSFCDA
add constraint   TB1058_MMSFCDAindex1 primary key (F1058_CODE);


--==============创建MMSFCDA表（视图）====================
drop view TB1058_MMSFCDAVIEW;
create view TB1058_MMSFCDAVIEW
(
	F1058_CODE,
	F1046_CODE,
	F1054_CODE,
	F1058_REFADDR,
	F1058_INDEX,
	F1058_DESC,
	F1058_DATATYPE,
	DATA_CODE
)
as 
select 
  trim(TB1046_IED.F1046_NAME)||trim(TB1058_MMSFCDA.F1058_REFADDR),
  TB1058_MMSFCDA.F1046_CODE,
  TB1058_MMSFCDA.F1054_CODE,
  TB1058_MMSFCDA.F1058_REFADDR,
  TB1058_MMSFCDA.F1058_INDEX,
  TB1058_MMSFCDA.F1058_DESC,
  TB1058_MMSFCDA.F1058_DATATYPE,
  TB1058_MMSFCDA.DATA_CODE
from 
  TB1058_MMSFCDA,
	TB1046_IED  
where 
 TB1058_MMSFCDA.F1046_CODE=TB1046_IED.F1046_CODE;


--==============创建定值FCDA表（参数表）====================
drop table TB1059_SGFCDA;
create table TB1059_SGFCDA
(
	F1059_CODE			char(48)	not null,	--定值FCDA代码（SG00001~SG99999） -RSC-50
	F1057_CODE			char(48)	null,		--定值控制块代码 -RSC-50
	F1059_REFADDR			char(48)	null,		--FCDA参引地址的参引地址 -RSC-50
	F1059_INDEX			number(3)	null,		--定值序号（在数据集中的序号） -RSC-50
	F1059_DATATYPE			number(3)	null,		--数据值类型 -RSC-50
	F1059_DESC			char(96)	null,		--定值名称（中文描述） -RSC-50
	F1059_UNIT			char(24)	null,		--定值单位 
	F1059_STEPSIZE			float		null,		--定值步长 
	F1059_VALUEMIN			float		null,		--定值最小值
	F1059_VALUEMAX			float		null,		--定值最大值
	F1059_BASEVALUE			float		null		--定值基准值
);
alter table TB1059_SGFCDA
    add constraint   TB1059_SGFCDAindex1 primary key (F1059_CODE);



--==============创建定值FCDA表（参数表)（视图）====================
drop view TB1059_SGFCDAVIEW;
create view TB1059_SGFCDAVIEW
(
F1059_CODE,
F1057_CODE,
F1046_CODE,
F1046_NAME,
F1059_REFADDR,
F1059_INDEX,
F1059_DATATYPE,
F1059_DESC,
F1059_UNIT,
F1059_STEPSIZE,
F1059_VALUEMIN,
F1059_VALUEMAX,
F1059_BASEVALUE
)
as 
select 
  TB1059_SGFCDA.F1059_CODE,
  TB1059_SGFCDA.F1057_CODE,
  TB1057_SGCBVIEW.F1046_CODE,
  TB1057_SGCBVIEW.F1046_NAME,
  trim(TB1057_SGCBVIEW.F1046_NAME)||trim(TB1059_SGFCDA.F1059_REFADDR),
  TB1059_SGFCDA.F1059_INDEX,
  TB1059_SGFCDA.F1059_DATATYPE,
  TB1059_SGFCDA.F1059_DESC,
  TB1059_SGFCDA.F1059_UNIT,
  TB1059_SGFCDA.F1059_STEPSIZE,
  TB1059_SGFCDA.F1059_VALUEMIN,
  TB1059_SGFCDA.F1059_VALUEMAX,
  TB1059_SGFCDA.F1059_BASEVALUE
from 
    TB1059_SGFCDA,
    TB1057_SGCBVIEW
where 
 TB1059_SGFCDA.F1057_CODE=TB1057_SGCBVIEW.F1057_CODE;


--==============创建参数FCDA表（参数表）====================
drop table TB1060_SPFCDA;
create table TB1060_SPFCDA
(
	F1060_CODE			char(48)	not null,	--参数FCDA代码（SP00001~SP99999） -RSC-50
	F1046_CODE			char(48)	not null,	--所属IED的代码 -RSC-50
	F1060_REFADDR			char(48)	null,		--FCDA参引地址的参引地址 -RSC-50
	F1060_INDEX			number(3)	null,		--参数在数据集中的序号 -RSC-50
	F1060_DATATYPE			number(3)	null,		--数据值类型 -RSC-50
	F1060_DESC			char(96)	null,		--参数名称（中文描述） -RSC-50
	F1060_UNIT			char(24)	null,		--参数单位
	F1060_STEPSIZE			float		null,		--参数步长
	F1060_VALUEMIN			float		null,		--参数最小值
	F1060_VALUEMAX			float		null		--参数最大值
);
alter table TB1060_SPFCDA
add constraint   TB1060_SPFCDAindex1 primary key (F1060_CODE);



--==============创建参数FCDA表（视图）====================
drop view TB1060_SPFCDAVIEW;
create view TB1060_SPFCDAVIEW
(
	F1060_CODE,			
	F1046_CODE,			
	F1046_NAME,			
	F1060_REFADDR		,
	F1060_INDEX		,
	F1060_DATATYPE		,
	F1060_DESC		,
	F1060_UNIT		,
	F1060_STEPSIZE		,
	F1060_VALUEMIN		,
	F1060_VALUEMAX		
)
as 
select 
        TB1060_SPFCDA.F1060_CODE		,
	TB1060_SPFCDA.F1046_CODE,
	TB1046_IED.F1046_NAME,
	trim(TB1046_IED.F1046_NAME)||trim(TB1060_SPFCDA.F1060_REFADDR),		
	TB1060_SPFCDA.F1060_INDEX		,
	TB1060_SPFCDA.F1060_DATATYPE		,
	TB1060_SPFCDA.F1060_DESC		,
	TB1060_SPFCDA.F1060_UNIT		,
	TB1060_SPFCDA.F1060_STEPSIZE		,
	TB1060_SPFCDA.F1060_VALUEMIN		,
	TB1060_SPFCDA.F1060_VALUEMAX	
from 
  TB1060_SPFCDA,
  TB1046_IED  
where 
 TB1060_SPFCDA.F1046_CODE=TB1046_IED.F1046_CODE;


--==============创建GOOSE/SV输出虚端子表（参数表）====================
drop table TB1061_POUT;
create table TB1061_POUT
(
	F1061_CODE			char(48)	not null,	--输出虚端子代码（POUT000001 ~ POUT999999） -RSC-50
	F1046_CODE			char(48)	not null,	--所属IED代码 -RSC-50
	CB_CODE				char(48)	null,		--数据集和控制块代码（GCB或SVCB的代码） -RSC-50
	F1061_REFADDR			char(48)	null,		--FCDA的参引地址 -RSC-50
	F1061_INDEX			number(3)	null,		--在数据集中等额序号 -RSC-50
	F1061_DESC			char(96)	null,		--虚端子描述 -RSC-50
	F1064_CODE			char(48)	null,		--对应的软压板代码 -RSC-50
	DATA_CODE			char(48)	null		--FCDA对应的数据点代码，根据数据值类型分别对状态量表TB1016、模拟量表TB1006和字符串量表TB1026中对应点的代码 -RSC-50
);
alter table TB1061_POUT
add constraint   TB1061_POUTindex1 primary key (F1061_CODE);

--==============创建GOOSE/SV接收虚端子表（参数表）====================
drop table TB1062_PIN;
create table TB1062_PIN
(
	F1062_CODE			char(48)	not null,	--虚端子代码（PIN00001~PIN99999） -RSC-50
	F1046_CODE			char(48)	not null,	--所属IED的代码  -RSC-50
	F1062_REFADDR			char(48)	null,		--虚端子的参引地址  -RSC-50
	F1011_NO			number(3)	null,		--端子类型  -RSC-50
	F1062_DESC			char(96)	null,		--虚端子描述  -RSC-50
	F1062_ISUSED			number(3)	null,		--虚端子是否被使用  -RSC-50
	F1064_CODE			char(48)	null		--对应的软压板代码 -RSC-50
);
alter table TB1062_PIN
add constraint   TB1062_PINindex1 primary key (F1062_CODE);

--==============创建GOOSE/SV虚回路表（参数表）====================
drop table TB1063_CIRCUIT;
create table TB1063_CIRCUIT
(
	F1063_CODE			char(48)	not null,	--虚回路代码（Circ00001~Circ99999） -RSC-50
	F1046_CODE_IEDSEND		char(48)	not null,	--输出端IED代码  -RSC-50
	F1061_CODE_PSEND		char(48)	null,		--输出端虚端子代码  -RSC-50
	F1046_CODE_IEDRECV		char(48)	null,		--接收端IED代码  -RSC-50
	F1062_CODE_PRECV		char(48)	null,		--接收端虚端子代码  -RSC-50
	F1065_CODE			char(48)	null,		--虚回路对应的逻辑链路代码  -RSC-50
	F1061_CODE_CONVCHK1		char(48)	null,		--跳合闸信号接收反校点虚端子1代码 -RSC-50
	F1061_CODE_CONVCHK2		char(48)	null		--跳合闸信号出口反校点虚端子2代码 -RSC-50
);
alter table TB1063_CIRCUIT
add constraint   TB1063_CIRCUITindex1 primary key (F1063_CODE);

--==============创建压板表（参数表）====================
drop table TB1064_STRAP;
create table TB1064_STRAP
(
	F1064_CODE				char(48)	not null,	--压板代码（Strap00001~99999） -RSC-50
	F1046_CODE				char(48)	not null,	--所属IED代码 -RSC-50
	F1011_NO				number(3)	null,		--压板类型 -RSC-50
	F1064_NUM				char(48)	null,		--压板调度编号 -RSC-50
	F1064_DESC				char(96)	null,		--压板调度名称 -RSC-50
	F1042_CODE_RELATEDBAY			char(48)	null		--压板所关联的间隔代码 
);
alter table TB1064_STRAP
add constraint   TB1064_STRAPindex1 primary key (F1064_CODE);

--==============创建过程层逻辑链路表（参数表）====================
drop table TB1065_LOGICALLINK;
create table TB1065_LOGICALLINK
(
	F1065_CODE			char(48)	not null,	--逻辑链路代码(LLink0001~LLink9999) -RSC-50
	F1065_TYPE			number(3)	null,		--链路类型 -RSC-50
	F1046_CODE_IEDSEND		char(48)	null,		--发送装置 -RSC-50
	F1046_CODE_IEDRECV		char(48)	null,		--接收装置, 逻辑链路的父对象 -RSC-50
	F1065_CBCODDE			char(24)	null		--过程层链路对应的GCB或SVCB对象代码 -RSC-50
);
alter table TB1065_LOGICALLINK
add constraint   TB1065_LOGICALLINKindex1 primary key (F1065_CODE);

--==============创建过程层逻辑链路与物理回路的关系表（参数表）====================
drop table TB1073_LLINKPHYRELATION;
create table TB1073_LLINKPHYRELATION
(
	F1073_CODE			char(48)	not null,	--关联关系代码(LPRelation0001 ~LPRelation9999) -RSC-50
	F1065_CODE			char(48)	null,		--逻辑链路代码(LLink0001~LLink9999) -RSC-50
	F1053_CODE	                char(48)	null		--物理网络回路代码（PHC0001~PHC9999） -RSC-50
);
alter table TB1073_LLINKPHYRELATION
add constraint   TB1073_LLINKPHYRELATIONindex1 primary key (F1073_CODE);

--==============创建电流/电压保护采样值表（参数表）====================
drop table TB1066_PROTMMXU;
create table TB1066_PROTMMXU
(
	F1066_CODE			char(48)	not null,	--采样值代码（Meas0001~Meas9999） -RSC-50
	F1067_CODE			char(48)	null,		--对应的互感器次级代码 -RSC-50
	F1006_CODE		        char(48)	null		--采样值数据点
);
alter table TB1066_PROTMMXU
add constraint   TB1066_PROTMMXUindex1 primary key (F1066_CODE);

--==============创建电流/电压互感器次级表（参数表）====================
drop table TB1067_CTVTSECONDARY;
create table TB1067_CTVTSECONDARY
(
	F1067_CODE			char(48)	not null,	--互感器次级代码（CVTSnd0001 ~ CVTSnd9999） -RSC-50
	F1043_CODE			char(48)	not null,	--互感器次级对应的互感器对象代码 -RSC-50
        F1067_NAME			char(24)	null,		--互感器次级名称 -RSC-50
	F1067_INDEX			number(3)	null,		--次级序号（缺省为空） -RSC-50
	F1067_TYPE			number(3)	null,		--SV虚端子分组类型 -RSC-50	
	F1067_TERMNO			char(96)	null,		--次级对应的端子编号 -RSC-50
	F1067_CIRCNO			char(96)	null,		--次级对应的回路编号 -RSC-50
	F1067_DESC			char(96)	null		--次级用途描述（如线路保护用电流回路） -RSC-50
);
alter table TB1067_CTVTSECONDARY
add constraint   TB1067_CTVTSECONDARYindex1 primary key (F1067_CODE);

--==============创建SV虚端子与互感器的对应关系表（参数表）====================
drop table TB1074_SVCTVTRELATION;
create table TB1074_SVCTVTRELATION
(
	F1074_CODE			char(48)	not null,	--关系代码（SVCVR00001 ~ SVCVR99999） -RSC-50
	F1067_CODE			char(48)	null,		--互感器次级对象代码 -RSC-50
        F1061_CODE			char(48)	null		--SV输出通道虚端子代码 -RSC-50
);
alter table TB1074_SVCTVTRELATION
add constraint   TB1074_SVCTVTRELATIONindex1 primary key (F1074_CODE);

--==============创建断路器保护控制回路表（参数表）====================
drop table TB1068_PROTCTRL;
create table TB1068_PROTCTRL
(
	F1068_CODE			char(48)	not null,	--控制回路代码（Ctrl0001~Ctrl9999） -RSC-50
	F1046_CODE_PROT			char(48)	null,		--保护装置代码 -RSC-50
	F1046_CODE_IO			char(48)	null,		--智能终端装置代码 -RSC-50
	F1043_CODE			char(48)	null,		--跳合闸的断路器代码 -RSC-50
	F1065_CODE			char(48)	null		--跳合闸回路逻辑链路代码 -RSC-50
);
alter table TB1068_PROTCTRL
add constraint   TB1068_PROTCTRLindex1 primary key (F1068_CODE);

--==============保护录波模拟量通道表（参数表）====================
drop table TB1069_RCDCHANNELA;
create table TB1069_RCDCHANNELA
(
	F1069_CODE			char(48)	not null,	--录波通道代码（RCDCH00001~RCDCH99999） -RSC-50
	F1046_CODE			char(48)	null,		--对应的装置代码 -RSC-50
	F1069_INDEX			number(5)	null,		--通道序号 -RSC-50
	F1069_TYPE			number(3)	null,		--通道类型 -RSC-50
	F1069_PHASE			number(3)	null,		--通道相别 -RSC-50
	F1043_CODE			char(48)	null,		--对应的互感器代码 -RSC-50
	F1061_CODE			char(48)	null,		--对应的SV虚端子代码 -RSC-50
	F1058_CODE			char(48)	null		--对应的MMS点代码 -RSC-50
);
alter table TB1069_RCDCHANNELA
add constraint   TB1069_RCDCHANNELAindex1 primary key (F1069_CODE);

--==============创建MMS服务器表（参数表）====================
drop table TB1070_MMSSERVER;
create table TB1070_MMSSERVER
(
	F1070_CODE			char(48)	not null,	--MMS服务器（MMSSvr0001~MMSSvr9999） -RSC-50
	F1046_CODE			char(48)	null,		--对应的装置代码 -RSC-50
	F1070_IP_A			char(24)	null,		--MMS服务器IP地址 -RSC-50
	F1070_IP_B			char(24)	null		--MMS服务器IP地址 -RSC-50
);
alter table TB1070_MMSSERVER
add constraint   TB1070_MMSSERVERindex1 primary key (F1070_CODE);

--==============创建前置采集单元表（参数表）====================
drop table TB1071_DAU;
create table TB1071_DAU
(
	F1071_CODE			char(48)	not null,	--采集器代码(DAU01~DAU99) -RSC-50
	F1071_DESC			char(96)	null,		--采集器描述 -RSC-50
	F1071_IPADDR			char(24)	null		--采集器IP地址 -RSC-50
);
alter table TB1071_DAU
add constraint   TB1071_DAUindex1 primary key (F1071_CODE);


--==============创建保护录波状态量通道表（参数表）====================
drop table TB1072_RCDCHANNELD;
create table TB1072_RCDCHANNELD
(
	F1072_CODE			char(48)	not null,	--录波通道代码（RCDCHD00001 ~ RCDCHD99999） -RSC-50
	F1046_CODE			char(48)	null,		--对应的装置代码 -RSC-50
	F1072_INDEX			number(5)	null,		--通道序号 -RSC-50
	F1072_TYPE			number(3)	null,		--通道类型 -RSC-50
	F1061_CODE			char(48)	null,		--对应的GOOSE虚端子代码 -RSC-50
	F1058_CODE			char(48)	null		--对应的MMS点代码 -RSC-50
);
alter table TB1072_RCDCHANNELD
add constraint   TB1072_RCDCHANNELDindex1 primary key (F1072_CODE);



--==============创建线路保护纵联光纤表（参数表）====================
drop table TB1090_LINEPROTFIBER;
create table TB1090_LINEPROTFIBER
(
	F1090_CODE			char(48)	not null,	--线路保护纵联光纤代码（LPFiber0001~ LPFiber9999） -RSC-50
	F1046_CODE			char(48)	null,		--对应的线路保护装置代码 -RSC-50
	F1090_DESC			char(96)	null,		--光纤描述 -RSC-50
	F1090_FIBERNO			char(24)	null,		--对应的光纤编号 -RSC-50
	F1090_PORTNO			char(24)	null		--对应的端口编号 -RSC-50
);
alter table TB1090_LINEPROTFIBER
add constraint   TB1090_LINEPROTFIBERindex1 primary key (F1090_CODE);

--==============创建智能终端闭锁另一套重合回路端子表（参数表）====================
drop table TB1091_IOTERM;
create table TB1091_IOTERM
(
	F1091_CODE			char(48)	not null,	--智能终端闭锁另一套重合回路端子代码（IOTerm0001~IOTerm9999） -RSC-50
	F1046_CODE			char(48)	null,		--对应的智能终端装置代码 -RSC-50
	F1091_DESC			char(96)	null,		--端子描述 -RSC-50
	F1091_TERMNO			char(96)	null,		--对应的端子编号 -RSC-50
	F1091_CIRCNO			char(96)	null		--对应的回路编号 -RSC-50
);
alter table TB1091_IOTERM
add constraint   TB1091_IOTERMindex1 primary key (F1091_CODE);

--==============创建装置电源空气开关表（参数表）====================
drop table TB1092_POWERKK;
create table TB1092_POWERKK
(
	F1092_CODE			char(48)	not null,	--电源空开代码（IOTerm0001~IOTerm9999） -RSC-50
	F1046_CODE			char(48)	null,		--对应的装置代码 -RSC-50
	F1092_DESC			char(96)	null,		--空开描述 -RSC-50
	F1092_KKNO			char(96)	null		--空开编号 -RSC-50
);
alter table TB1092_POWERKK
add constraint   TB1092_POWERKKindex1 primary key (F1092_CODE);

--==============创建保护电压回路空气开关表（参数表）====================
drop table TB1093_VOLTAGEKK;
create table TB1093_VOLTAGEKK
(
	F1093_CODE			char(48)	not null,	--电源空开代码（IOTerm0001~IOTerm9999） -RSC-50
	F1067_CODE			char(48)	null,		--对应的电压互感器次级代码 -RSC-50
	F1093_DESC			char(96)	null,		--空开描述 -RSC-50
	F1093_KKNO			char(96)	null		--空开编号 -RSC-50
);
alter table TB1093_VOLTAGEKK
add constraint   TB1093_VOLTAGEKKindex1 primary key (F1093_CODE);

--==========创建程序注册表(程序注册表)============
drop table TB1037_APPREG;
create table TB1037_APPREG
(
  F1037_CODE      char(48)    not null,    --代码
  F1037_DESC      char(96)    null,        --描述
  F1037_INTVAL    number(10)  null,        --整型数键值
  F1037_FLOATVAL  real        null,        --浮点数键值
  F1037_STRVAL    char(48)    null         --字符串键值
);
alter table TB1037_APPREG
add constraint TB1037_APPREG1 primary key (F1037_CODE);


--=================================================================值表=================================================================


--==============创建模拟量值表（模拟量值表）=================
drop table TB1017_ANALOGVALUE;
create table TB1017_ANALOGVALUE
(
	F1006_CODE		char(48)		not null,	--代码
	F1017_VAL		float			null,		--值或幅值
	F1017_REVVAL		float			null,		--采集值
	F1017_ANGLE		float			null,		--相角
	F1017_QUALITY		number(10)		null,		--数据质量
	F1017_TIME		number(10)		null,		--时标 
	F1017_INTERVAL		float			null,		--间隔累加值   
	F1017_INTERVALCNT	number(10)		null,		--间隔累加次数 
	F1017_DAYVAL		float			null,		--日累加值    
	F1017_DAYCNT		number(10)		null,		--日累加次数          
	F1017_MAXVAL		float			null,		--最大值      
	F1017_MAXVALH		number(3)		null,		--最大值小时        
	F1017_MAXVALM		number(3)		null,		--最大值分钟        
	F1017_MAXVALS		number(3)		null,		--最大值秒   
	F1017_MINVAL		float			null,		--最小值     
	F1017_MINVALH		number(3)		null,		--最小值小时      
	F1017_MINVALM		number(3)		null,		--最小值分钟        
	F1017_MINVALS		number(3)		null,		--最小值秒  
	F1017_OVERHCNT		number(5)		null,		--越上限次数  
	F1017_OVERLCNT		number(5)		null,		--越下限次数  
	F1017_OVERHHCNT		number(5)		null,		--越上上限次数 
	F1017_OVERLLCNT		number(5)		null,		--越下下限次数  
	F1017_OVERHTIME		number(10)		null,       --越上限时间   
	F1017_OVERLTIME		number(10)		null,		--越下限时间 
	F1017_OVERHHTIME	number(10)		null,		--越上上限时间   
	F1017_OVERLLTIME	number(10)		null,		--越下下限时间        
	F1017_DEADTIME		number(10)		null,		--死数时间    
	F1017_DAYDEADTIME	number(10)		null,		--日死数时间  
	F1017_DAYSTOPTIME	number(10)		null,		--日停运时间  
	F1017_PREPROCTIME	number(10)		null,		--上次处理时间         
	F1017_INVALID		number(3)		null,		--非法标志  
	F1017_PUTEVTFLAG	number(3)		null,		--已生成事项标志        
	F1017_DEADFLAG		number(3)		null,		--死数标志  
	F1017_RELIABILITY	number(3)		null,		--不可靠标志  
	F1017_ALARMLEVEL	number(3)		null,		--越限等级        
	F1017_NOALARM		number(3)		null,		--抑制告警
	F1017_UPDATE		number(3)		null,		--更新标志
	F1017_MANSET		number(3)		null,       --置入标志
	F1017_OVERLIMITFLAG	number(3)		null,       --越限标志
	F1017_SAMPLECPFLAG	number(3)		null,       --同期比较标志
	F1017_TRENDCPFLAG	number(3)		null,       --趋势比较标志
	F1017_SALTATIONFLAG	number(3)		null        --突变标志
);
alter table TB1017_ANALOGVALUE add constraint TB1017_ANALOGVALUE1 primary key (F1006_CODE);


--================创建状态值表（状态值表）=================
drop table TB1018_STATEVALUE;
create table TB1018_STATEVALUE
(
	F1016_CODE		char(48)		not null,	--代码
	F1018_VAL		number(10)		null,		--值
	F1018_REVVAL		number(3)		null,		--采集值
	F1018_QUALITY		number(10)		null,		--数据质量
	F1018_TIME		number(10)		null,		--时标
	F1018_PREPROCTIME	number(10)		null,		--上次处理时间
	F1018_RUNTIME		number(10)		null,		--运行时间
	F1018_CHGCNT		number(5)		null,		--变位计数
	F1018_CHGFLAG		number(3)		null,		--变位标志
	F1018_ERRFLAG		number(3)		null,		--异常标志
	F1018_INITFLAG		number(3)		null,		--初始化标志
	F1018_NOALARM		number(3)		null,		--抑制告警
	F1018_UPDATE		number(3)		null,		--更新标志
	F1018_MANSET		number(3)		null        --置入标志
);
alter table TB1018_STATEVALUE add constraint TB1018_STATEVALUE1 primary key(F1016_CODE);


--================创建字符串值表（字符串值表）=================
drop table TB1019_STRINGVALUE;
create table TB1019_STRINGVALUE
(
	F1026_CODE		char(96)		not null,	--代码
	F1019_VAL		char(48)		null,		--值
	F1019_REVVAL		char(48)		null,		--采集值
	F1019_QUALITY		number(10)		null,		--数据质量
	F1019_TIME		number(10)		null,		--时标
	F1019_PREPROCTIME	number(10)		null,		--上次处理时间
	F1019_RUNTIME		number(10)		null,		--运行时间
	F1019_CHGCNT		number(5)		null,		--变位计数
	F1019_CHGFLAG		number(3)		null,		--变位标志
	F1019_ERRFLAG		number(3)		null,		--异常标志
	F1019_INITFLAG		number(3)		null,		--初始化标志
	F1019_NOALARM		number(3)		null,		--抑制告警
	F1019_UPDATE		number(3)		null		--更新标志
);
alter table TB1019_STRINGVALUE add constraint TB1019_STRINGVALUE1 primary key(F1026_CODE);

-- ==================创建采集组表： TB1200_FEGROUP=================
drop table TB1200_FEGROUP;
CREATE TABLE TB1200_FEGROUP
(
  F1200_CODE    char(48)            not null,       --关键字代码
  F1200_DESC    char(96)            null           --组描述
);
alter table TB1200_FEGROUP add constraint TB1200_FEGROUPindex1 primary key (F1200_CODE);
