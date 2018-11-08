DROP TABLE TB1093_VOLTAGEKK;
DROP TABLE TB1092_POWERKK;
DROP TABLE TB1091_IOTERM;
DROP TABLE TB1090_LINEPROTFIBER;
DROP TABLE TB1073_LLINKPHYRELATION;
DROP TABLE TB1072_RCDCHANNELD;
DROP TABLE TB1071_DAU;
DROP TABLE TB1070_MMSSERVER;
DROP TABLE TB1069_RCDCHANNELA;
DROP TABLE TB1068_PROTCTRL;
DROP TABLE TB1067_CTVTSECONDARY;
DROP TABLE TB1066_PROTMMXU;
DROP TABLE TB1065_LOGICALLINK;
DROP TABLE TB1064_STRAP;
DROP TABLE TB1063_CIRCUIT;
DROP TABLE TB1062_PIN;
DROP TABLE TB1061_POUT;
DROP TABLE TB1060_SPFCDA;
DROP TABLE TB1059_SGFCDA;
DROP TABLE TB1058_MMSFCDA;
DROP TABLE TB1057_SGCB;
DROP TABLE TB1056_SVCB;
DROP TABLE TB1055_GCB;
DROP TABLE TB1054_RCB;
DROP TABLE TB1053_PHYSCONN;
DROP TABLE TB1052_CORE;
DROP TABLE TB1051_CABLE;
DROP TABLE TB1050_CUBICLE;
DROP TABLE TB1049_REGION;
DROP TABLE TB1048_PORT;
DROP TABLE TB1047_BOARD;
DROP TABLE TB1046_IED;
DROP TABLE TB1045_CONNECTIVITYNODE;
DROP TABLE TB1044_TERMINAL;
DROP TABLE TB1043_EQUIPMENT;
DROP TABLE TB1042_BAY;
DROP TABLE TB1041_SUBSTATION;
DROP TABLE TB1026_STRINGDATA;
DROP TABLE TB1022_FAULTCONFIG;
DROP TABLE TB1016_STATEDATA;
DROP TABLE TB1006_ANALOGDATA;
DROP TABLE TB1074_SVCTVTRelation;

/*==============================================================*/
/* Table: TB1006_AnalogData                                     */
/*==============================================================*/
create table TB1006_AnalogData
(
   F1006_CODE           VARCHAR(48) not null,
   F1006_DESC           VARCHAR(96),
   F1006_SAFELEVEL      NUMBER(5),
   Parent_CODE          VARCHAR(48),
   F1011_NO             NUMBER(5),
   F1006_BYNAME         VARCHAR(24),
   F0008_NAME           VARCHAR(48),
   F0009_NAME           VARCHAR(48),
   F1006_CALCFLAG       NUMBER(5),
   F1006_PICNAME        VARCHAR(96),
   F1006_PDRMODE        NUMBER(5),
   F1006_K              float,
   F1006_B              float,
   F1006_ZERODBAND      float,
   F1006_OVERFLOW       float,
   F1006_LOWFLOW        float,
   F1006_MAXINC         float,
   F1006_HIWARN         float,
   F1006_LOWARN         float,
   F1006_HIALARM        float,
   F1006_LOALARM        float,
   F1006_SAVEPERIOD     NUMBER(5),
   F1006_PLANTIME       NUMBER(5),
   F1006_DEADTIME       NUMBER(5),
   F1006_ALARMLEVEL     NUMBER(5),
   F1006_SAVETYPE       NUMBER(5),
   primary key (F1006_CODE)
);

/*==============================================================*/
/* Table: TB1016_StateData                                      */
/*==============================================================*/
create table TB1016_StateData
(
   F1016_CODE           VARCHAR(48) not null,
   F1016_DESC           VARCHAR(96),
   F1016_SAFELEVEL      NUMBER(5),
   Parent_CODE          VARCHAR(48),
   F1011_NO             NUMBER(5),
   F1016_BYNAME         VARCHAR(24),
   F0008_NAME           VARCHAR(48),
   F0009_NAME           VARCHAR(48),
   F1016_CALCFLAG       NUMBER(5),
   F1016_PICNAME        VARCHAR(96),
   F1016_ISPDR          NUMBER(5),
   F1016_PDRNO          NUMBER(5),
   F1016_DPSFLAG        NUMBER(5),
   F1016_MAINSTFLAG     NUMBER(5),
   F1016_DPSCALCFLAG    NUMBER(5),
   F1016_SOE            VARCHAR(48),
   F1016_SGLIMITVAL     NUMBER(5),
   F1016_ALARMPROCMODE  NUMBER(5),
   F1016_PROCBAND       NUMBER(5),
   F1016_SGPROCNAME     VARCHAR(48),
   F1016_REVFLAG        NUMBER(5),
   F1016_ISSTA          NUMBER(5),
   primary key (F1016_CODE)
);
/*==============================================================*/
/* Table: TB1026_StringData                                     */
/*==============================================================*/
create table TB1026_StringData
(
   F1026_CODE           VARCHAR(48) not null,
   F1026_DESC           VARCHAR(96),
   F1026_SAFELEVEL      NUMBER(5),
   Parent_CODE          VARCHAR(48),
   F1011_NO             NUMBER(5),
   F1026_BYNAME         VARCHAR(24),
   F0008_NAME           VARCHAR(48),
   F0009_NAME           VARCHAR(48),
   F1026_CALCFLAG       NUMBER(5),
   F1026_PICNAME        VARCHAR(96),
   F1026_ISPDR          NUMBER(5),
   F1026_PDRNO          NUMBER(5),
   F1026_ISSTA          NUMBER(5),
   primary key (F1026_CODE)
);

/*==============================================================*/
/* Table: TB1022_FaultConfig                                    */
/*==============================================================*/
create table TB1022_FaultConfig
(
   F1022_CODE           VARCHAR(48) not null,
   F1011_NO             NUMBER(5),
   F1022_FAULTLEVEL     NUMBER(5),
   F1022_T1             NUMBER(5),
   F1022_T2             NUMBER(5),
   F1022_K              NUMBER(5),
   primary key (F1022_CODE)
);


/*==============================================================*/
/* Table: TB1041_Substation                                     */
/*==============================================================*/
create table TB1041_Substation
(
   F1041_CODE           VARCHAR(48) not null,
   F1041_Name           VARCHAR(48),
   F1041_Desc           VARCHAR(96),
   F1041_DQName         VARCHAR(48),
   F1041_DQDESC         VARCHAR(96),
   F1041_Company        VARCHAR(96),
   F1042_VoltageH       NUMBER(5),
   F1042_VoltageM       NUMBER(5),
   F1042_VoltageL       NUMBER(5),
   primary key (F1041_CODE)
);

/*==============================================================*/
/* Table: TB1042_Bay                                            */
/*==============================================================*/
create table TB1042_Bay
(
   F1042_CODE           VARCHAR(48) not null,
   F1041_CODE           VARCHAR(48),
   F1042_Name           VARCHAR(48),
   F1042_Desc           VARCHAR(96),
   F1042_Voltage        NUMBER(5),
   F1042_ConnType       NUMBER(5),
   F1042_DevType        NUMBER(5),
   F1042_IEDSolution    NUMBER(5),
   primary key (F1042_CODE)
);

/*==============================================================*/
/* Table: TB1043_Equipment                                      */
/*==============================================================*/
create table TB1043_Equipment
(
   F1043_CODE           VARCHAR(48) not null,
   F1042_CODE           VARCHAR(48),
   F1043_Name           VARCHAR(48),
   F1043_Desc           VARCHAR(96),
   F1043_IsVirtual      NUMBER(5),
   F1043_Type           NUMBER(5),
   F1016_Code			VARCHAR(48),
   primary key (F1043_CODE)
);

/*==============================================================*/
/* Table: TB1044_Terminal                                       */
/*==============================================================*/
create table TB1044_Terminal
(
   F1044_CODE           VARCHAR(48) not null,
   F1043_CODE           VARCHAR(48),
   F1045_CODE           VARCHAR(48),
   F1044_Name           VARCHAR(48),
   F1044_Desc           VARCHAR(96),
   primary key (F1044_CODE)
);

/*==============================================================*/
/* Table: TB1045_ConnectivityNode                               */
/*==============================================================*/
create table TB1045_ConnectivityNode
(
   F1045_CODE           VARCHAR(48) not null,
   F1045_Name           VARCHAR(48),
   F1045_Desc           VARCHAR(96),
   primary key (F1045_CODE)
);

/*==============================================================*/
/* Table: TB1046_IED                                            */
/*==============================================================*/
create table TB1046_IED
(
   F1046_CODE           VARCHAR(48) not null,
   F1042_CODE           VARCHAR(48),
   F1050_Code           VARCHAR(48),
   F1046_Name           VARCHAR(48),
   F1046_Desc           VARCHAR(96),
   F1046_Manufacturor   VARCHAR(48),
   F1046_Model          VARCHAR(48),
   F1046_ConfigVersion  VARCHAR(48),
   F1046_AorB           NUMBER(5),
   F1046_IsVirtual      NUMBER(5),
   F1046_Type           NUMBER(5),
   F1046_CRC            VARCHAR(24),
   F1200_CODE			VARCHAR(48),
   primary key (F1046_CODE)
);

/*==============================================================*/
/* Table: TB1047_Board                                          */
/*==============================================================*/
create table TB1047_Board
(
   F1047_CODE           VARCHAR(48) not null,
   F1046_CODE           VARCHAR(48),
   F1047_Slot           VARCHAR(24),
   F1047_Desc           VARCHAR(96),
   F1047_Type           VARCHAR(48),
   primary key (F1047_CODE)
);

/*==============================================================*/
/* Table: TB1048_Port                                           */
/*==============================================================*/
create table TB1048_Port
(
   F1048_CODE           VARCHAR(48) not null,
   F1047_CODE           VARCHAR(48),
   F1048_No             VARCHAR(24),
   F1048_Desc           VARCHAR(96),
   F1048_Direction      NUMBER(5),
   F1048_Plug           NUMBER(5),
   primary key (F1048_CODE)
);

/*==============================================================*/
/* Table: TB1049_Region                                         */
/*==============================================================*/
create table TB1049_Region
(
   F1049_CODE           VARCHAR(48) not null,
   F1041_CODE           VARCHAR(48),
   F1049_Name           VARCHAR(48),
   F1049_Desc           VARCHAR(96),
   F1049_Area           NUMBER(5),
   primary key (F1049_CODE)
);

/*==============================================================*/
/* Table: TB1050_Cubicle                                        */
/*==============================================================*/
create table TB1050_Cubicle
(
   F1050_CODE           VARCHAR(48) not null,
   F1049_CODE           VARCHAR(48),
   F1050_Name           VARCHAR(48),
   F1050_Desc           VARCHAR(96),
   primary key (F1050_CODE)
);

/*==============================================================*/
/* Table: TB1051_Cable                                          */
/*==============================================================*/
create table TB1051_Cable
(
   F1051_CODE           VARCHAR(48) not null,
   F1041_CODE           VARCHAR(48),
   F1051_Name           VARCHAR(48),
   F1051_Desc           VARCHAR(96),
   F1051_Length         NUMBER(5),
   F1051_CoreNum        NUMBER(5),
   F1050_CODE_A         VARCHAR(48),
   F1050_CODE_B         VARCHAR(48),
   F1051_Type           NUMBER(5),
   primary key (F1051_CODE)
);

/*==============================================================*/
/* Table: TB1052_Core                                           */
/*==============================================================*/
create table TB1052_Core
(
   F1052_CODE           VARCHAR(48) not null,
   F1052_PARENT_CODE    VARCHAR(48),
   F1052_Type           NUMBER(5),
   F1052_No             NUMBER(5),
   F1048_CODE_A         VARCHAR(48),
   F1048_CODE_B         VARCHAR(48),
   F1053_CODE           VARCHAR(48),
   primary key (F1052_CODE)
);

/*==============================================================*/
/* Table: TB1053_PhysConn                                       */
/*==============================================================*/
create table TB1053_PhysConn
(
   F1053_CODE           VARCHAR(48) not null,
   F1041_CODE           VARCHAR(48),
   F1048_CODE_A         VARCHAR(48),
   F1048_CODE_B         VARCHAR(48),
   primary key (F1053_CODE)
);

/*==============================================================*/
/* Table: TB1054_RCB                                            */
/*==============================================================*/
create table TB1054_RCB
(
   F1054_CODE           VARCHAR(48) not null, 
   F1046_CODE           VARCHAR(48),
   F1054_RPTREF          VARCHAR(48),
   F1054_Dataset        VARCHAR(48),
   F1054_DSDesc         VARCHAR(96),
   F1054_IsBRCB         NUMBER(5),
   F1054_CBType         NUMBER(5),
   primary key (F1054_CODE)
);

/*==============================================================*/
/* Table: TB1055_GCB                                            */
/*==============================================================*/
create table TB1055_GCB
(
   F1055_CODE          VARCHAR(48) not null,
   F1046_CODE       VARCHAR(48),
   F1055_CBNAME         	VARCHAR(48),
   F1055_CBREF           	VARCHAR(48),
   F1055_MACAddr        	VARCHAR(24),
   F1055_VLANID         	VARCHAR(24),
   F1055_VLANPriority   	VARCHAR(24),
   F1055_APPID          	VARCHAR(24),
   F1055_DATASET        	VARCHAR(24),
   F1055_Desc         	VARCHAR(96),
   F1071_CODE       VARCHAR(48),
   primary key (F1055_CODE)
);

/*==============================================================*/
/* Table: TB1056_SVCB                                           */
/*==============================================================*/
create table TB1056_SVCB
(
   F1056_CODE          VARCHAR(48) not null,
   F1046_CODE       VARCHAR(48),
   F1056_CBName         	VARCHAR(48),
   F1056_CBREF           	VARCHAR(48),
   F1056_MACAddr        	VARCHAR(24),
   F1056_VLANID         	VARCHAR(24),
   F1056_VLANPriority   	VARCHAR(24),
   F1056_APPID          	VARCHAR(24),
   F1056_Dataset        	VARCHAR(24),
   F1056_Desc         	VARCHAR(96),
   F1071_CODE           VARCHAR(48),
   primary key (F1056_CODE)
);

/*==============================================================*/
/* Table: TB1057_SGCB                                           */
/*==============================================================*/
create table TB1057_SGCB
(
   F1057_CODE           VARCHAR(48) not null,
   F1046_CODE           VARCHAR(48),
   F1057_CBName         VARCHAR(48),
   F1057_CBRef	        VARCHAR(96),
   F1057_Dataset        VARCHAR(48),
   F1057_DSDesc         VARCHAR(96),
   primary key (F1057_CODE)
);

/*==============================================================*/
/* Table: TB1058_MMSFCDA                                        */
/*==============================================================*/
create table TB1058_MMSFCDA
(
   F1058_CODE           VARCHAR(48) not null,
   F1046_CODE           VARCHAR(48),
   F1054_CODE           VARCHAR(48),
   F1058_RefAddr        VARCHAR(48),
   F1058_Index          NUMBER(5),
   F1058_Desc           VARCHAR(96),
   F1058_DataType       NUMBER(5),
   DATA_CODE            VARCHAR(48),
   primary key (F1058_CODE)
);

/*==============================================================*/
/* Table: TB1059_SGFCDA                                         */
/*==============================================================*/
create table TB1059_SGFCDA
(
   F1059_CODE           VARCHAR(48) not null,
   F1057_CODE           VARCHAR(48),
   F1059_RefAddr        VARCHAR(48),
   F1059_Index          NUMBER(5),
   F1059_DataType       NUMBER(5),
   F1059_Desc           VARCHAR(96),
   F1059_Unit           VARCHAR(24),
   F1059_StepSize       float,
   F1059_ValueMin       float,
   F1059_ValueMax       float,
   F1059_BaseValue      float,
   primary key (F1059_CODE)
);

/*==============================================================*/
/* Table: TB1060_SPFCDA                                         */
/*==============================================================*/
create table TB1060_SPFCDA
(
   F1060_CODE           VARCHAR(48) not null,
   F1046_CODE           VARCHAR(48),
   F1060_RefAddr        VARCHAR(48),
   F1060_Index          NUMBER(5),
   F1060_DataType       NUMBER(5),
   F1060_Desc           VARCHAR(96),
   F1060_Unit           VARCHAR(24),
   F1060_StepSize       float,
   F1060_ValueMin       float,
   F1060_ValueMax       float,
   primary key (F1060_CODE)
);

/*==============================================================*/
/* Table: TB1061_POUT                                           */
/*==============================================================*/
create table TB1061_POUT
(
   F1061_CODE           VARCHAR(48) not null,
   F1046_CODE           VARCHAR(48),
   CB_CODE              VARCHAR(48),
   F1061_RefAddr        VARCHAR(48),
   F1061_Index          NUMBER(5),
   F1061_Desc           VARCHAR(96),
   F1064_CODE           VARCHAR(48),
   DATA_CODE            VARCHAR(48),
   primary key (F1061_CODE)
);

/*==============================================================*/
/* Table: TB1062_PIN                                            */
/*==============================================================*/
create table TB1062_PIN
(
   F1062_CODE           VARCHAR(48) not null,
   F1046_CODE           VARCHAR(48),
   F1062_RefAddr        VARCHAR(48),
   F1011_NO				NUMBER(5)	not null,
   F1062_Desc           VARCHAR(96),
   F1062_IsUsed         NUMBER(5),
   F1064_CODE           VARCHAR(48),
   primary key (F1062_CODE)
);

/*==============================================================*/
/* Table: TB1063_Circuit                                        */
/*==============================================================*/
create table TB1063_Circuit
(
   F1063_CODE           VARCHAR(48) not null,
   F1046_CODE_IEDSend   VARCHAR(48),
   F1061_CODE_PSend     VARCHAR(48),
   F1046_CODE_IEDRecv   VARCHAR(48),
   F1062_CODE_PRecv     VARCHAR(48),
   F1065_CODE           VARCHAR(48),
   F1061_CODE_ConvChk1  VARCHAR(48),
   F1061_CODE_ConvChk2  VARCHAR(48),
   primary key (F1063_CODE)
);

/*==============================================================*/
/* Table: TB1064_Strap                                          */
/*==============================================================*/
create table TB1064_Strap
(
   F1064_CODE           VARCHAR(48) not null,
   F1046_CODE           VARCHAR(48),
   F1011_NO           	NUMBER(5),
   F1064_NUM            VARCHAR(48),
   F1064_DESC           VARCHAR(96),
   F1042_CODE_RelatedBay VARCHAR(48),
   primary key (F1064_CODE)
);

/*==============================================================*/
/* Table: TB1065_LogicalLink                                    */
/*==============================================================*/
create table TB1065_LogicalLink
(
   F1065_CODE           VARCHAR(48) not null,
   F1065_Type           NUMBER(5),
   F1046_CODE_IEDSend   VARCHAR(48),
   F1046_CODE_IEDRecv   VARCHAR(48),
   F1065_CBCODE        VARCHAR(48),
   primary key (F1065_CODE)
);

/*==============================================================*/
/* Table: TB1066_ProtMMXU                                       */
/*==============================================================*/
create table TB1066_ProtMMXU
(
   F1066_CODE           varchar(48) not null,
   F1067_CODE           varchar(48),
   F1006_CODE         	varchar(48),
   primary key (F1066_CODE)
);

/*==============================================================*/
/* Table: TB1067_CTVTSecondary                                  */
/*==============================================================*/
create table TB1067_CTVTSecondary
(
   F1067_Code           varchar(48) not null,
   F1043_CODE           varchar(48) not null,
   F1067_Name         	varchar(96),
   F1067_Index          INT,
   F1067_Type           INT,
   F1067_TermNo         varchar(96),
   F1067_CircNo         varchar(96),
   F1067_Desc           varchar(96),
   primary key (F1067_Code)
);

/*==============================================================*/
/* Table: TB1074_SVCTVTRelation                                 */
/*==============================================================*/
create table TB1074_SVCTVTRelation
(
   F1074_CODE           VARCHAR(48) not null,
   F1067_CODE           VARCHAR(48) not null,
   F1061_CODE           VARCHAR(48) not null,
   primary key (F1074_CODE)
);

/*==============================================================*/
/* Table: TB1068_ProtCtrl                                       */
/*==============================================================*/
create table TB1068_ProtCtrl
(
   F1068_CODE           VARCHAR(48) not null,
   F1046_CODE_Prot      VARCHAR(48),
   F1046_CODE_IO        VARCHAR(48),
   F1043_CODE           VARCHAR(48),
   F1065_CODE           VARCHAR(48),
   primary key (F1068_CODE)
);

/*==============================================================*/
/* Table: TB1069_RCDChannelA                                    */
/*==============================================================*/
create table TB1069_RCDChannelA
(
   F1069_CODE           VARCHAR(48) not null,
   IED_CODE             VARCHAR(48),
   F1069_Index          VARCHAR(48),
   F1069_Type           NUMBER(5),
   F1069_Phase          NUMBER(5),
   F1043_CODE           VARCHAR(48),
   F1061_CODE           VARCHAR(48),
   F1058_CODE           VARCHAR(48),
   primary key (F1069_CODE)
);

/*==============================================================*/
/* Table: TB1070_MMSServer                                      */
/*==============================================================*/
create table TB1070_MMSServer
(
   F1070_CODE           VARCHAR(48) not null,
   F1046_CODE           VARCHAR(48),
   F1070_IP_A           VARCHAR(24),
   F1070_IP_B           VARCHAR(24),
   F1070_IEDCRC         VARCHAR(24),
   F1070_CRCPATH        VARCHAR(96),
   primary key (F1070_CODE)
);

/*==============================================================*/
/* Table: TB1071_DAU                                            */
/*==============================================================*/
create table TB1071_DAU
(
   F1071_CODE           VARCHAR(48) not null,
   F1046_CODE           varchar(48) not null,
   F1071_DESC           VARCHAR(96),
   F1071_IPAddr         VARCHAR(24),
   primary key (F1071_CODE)
);

/*==============================================================*/
/* Table: TB1072_RCDChannelD                                    */
/*==============================================================*/
create table TB1072_RCDChannelD
(
   F1072_CODE           VARCHAR(48) not null,
   IED_CODE             VARCHAR(48),
   F1072_Index          VARCHAR(48),
   F1072_Type           NUMBER(5),
   F1061_CODE           VARCHAR(48),
   F1058_CODE           VARCHAR(48),
   primary key (F1072_CODE)
);

/*==============================================================*/
/* Table: TB1073_LLinkPhyRelation                               */
/*==============================================================*/
create table TB1073_LLinkPhyRelation
(
   F1073_CODE           VARCHAR(48) not null,
   F1065_CODE           VARCHAR(48),
   F1053_CODE           VARCHAR(48),
   primary key (F1073_CODE)
);

/*==============================================================*/
/* Table: TB1090_LineProtFiber                                  */
/*==============================================================*/
create table TB1090_LineProtFiber
(
   F1090_CODE           VARCHAR(48) not null,
   F1046_CODE           VARCHAR(48),
   F1090_DESC           VARCHAR(96),
   F1090_FiberNo        VARCHAR(24),
   F1090_PortNo         VARCHAR(24),
   primary key (F1090_CODE)
);

/*==============================================================*/
/* Table: TB1091_IOTerm                                         */
/*==============================================================*/
create table TB1091_IOTerm
(
   F1091_CODE           VARCHAR(48) not null,
   F1046_CODE           VARCHAR(48),
   F1091_DESC           VARCHAR(96),
   F1091_TermNo         VARCHAR(96),
   F1091_CircNo         VARCHAR(96),
   primary key (F1091_CODE)
);

/*==============================================================*/
/* Table: TB1092_PowerKK                                        */
/*==============================================================*/
create table TB1092_PowerKK
(
   F1092_CODE           VARCHAR(48) not null,
   F1046_CODE           VARCHAR(48),
   F1092_DESC           VARCHAR(96),
   F1092_KKNo           VARCHAR(96),
   primary key (F1092_CODE)
);

/*==============================================================*/
/* Table: TB1093_VoltageKK                                      */
/*==============================================================*/
create table TB1093_VoltageKK
(
   F1093_CODE           VARCHAR(48) not null,
   F1067_CODE           VARCHAR(48),
   F1093_DESC           VARCHAR(96),
   F1093_KKNo           VARCHAR(96),
   primary key (F1093_CODE)
);
