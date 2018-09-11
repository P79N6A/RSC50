
/*==============================================================*/
/* Table: TB1006_AnalogData                                     */
/*==============================================================*/
create table TB1006_AnalogData
(
   F1006_CODE           varchar(48) not null,
   F1006_DESC           varchar(96),
   F1006_SAFELEVEL      INT,
   Parent_CODE          varchar(48) not null,
   F1011_NO             INT not null,
   F1006_BYNAME         varchar(24),
   F0008_NAME           varchar(48),
   F0009_NAME           varchar(48),
   F1006_CALCFLAG       INT,
   F1006_PICNAME        varchar(96),
   F1006_PDRMODE        INT,
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
   F1006_SAVEPERIOD     INT,
   F1006_PLANTIME       INT,
   F1006_DEADTIME       INT,
   F1006_ALARMLEVEL     INT,
   F1006_SAVETYPE       INT,
   primary key (F1006_CODE)
);

/*==============================================================*/
/* Table: TB1016_StateData                                      */
/*==============================================================*/
create table TB1016_StateData
(
   F1016_CODE           varchar(48) not null,
   F1016_DESC           varchar(96),
   F1016_SAFELEVEL      INT,
   Parent_CODE          varchar(48) not null,
   F1011_NO             INT not null,
   F1016_BYNAME         varchar(24),
   F0008_NAME           varchar(48),
   F0009_NAME           varchar(48),
   F1016_CALCFLAG       INT,
   F1016_PICNAME        varchar(96),
   F1016_ISPDR          INT,
   F1016_PDRNO          INT,
   F1016_DPSFLAG        INT,
   F1016_MAINSTFLAG     INT,
   F1016_DPSCALCFLAG    INT,
   F1016_SOE            varchar(48),
   F1016_SGLIMITVAL     INT,
   F1016_ALARMPROCMODE  INT,
   F1016_PROCBAND       INT,
   F1016_SGPROCNAME     varchar(48),
   F1016_REVFLAG        INT,
   F1016_ISSTA          INT,
   primary key (F1016_CODE)
);

/*==============================================================*/
/* Table: TB1022_FaultConfig                                    */
/*==============================================================*/
create table TB1022_FaultConfig
(
   F1022_CODE           varchar(48) not null,
   F1011_NO             INT not null,
   F1022_FAULTLEVEL     INT not null,
   F1022_T1             INT,
   F1022_T2             INT,
   F1022_K              INT,
   primary key (F1022_CODE)
);

/*==============================================================*/
/* Table: TB1026_StringData                                     */
/*==============================================================*/
create table TB1026_StringData
(
   F1026_CODE           varchar(48) not null,
   F1026_DESC           varchar(96),
   F1026_SAFELEVEL      INT,
   Parent_CODE          varchar(48) not null,
   F1011_NO             INT not null,
   F1026_BYNAME         varchar(24),
   F0008_NAME           varchar(48),
   F0009_NAME           varchar(48),
   F1026_CALCFLAG       INT,
   F1026_PICNAME        varchar(96),
   F1026_ISPDR          INT,
   F1026_PDRNO          INT,
   F1026_ISSTA          INT,
   primary key (F1026_CODE)
);

/*==============================================================*/
/* Table: TB1041_Substation                                     */
/*==============================================================*/
create table TB1041_Substation
(
   F1041_CODE           varchar(48) not null,
   F1041_Name           varchar(48) not null,
   F1041_Desc           varchar(96),
   F1041_DQName         varchar(48),
   F1041_DQDESC         varchar(96),
   F1041_Company        varchar(96),
   F1042_VoltageH       INT,
   F1042_VoltageM       INT,
   F1042_VoltageL       INT,
   primary key (F1041_CODE)
);

/*==============================================================*/
/* Table: TB1042_Bay                                            */
/*==============================================================*/
create table TB1042_Bay
(
   F1042_CODE           varchar(48) not null,
   F1041_CODE           varchar(48) not null,
   F1042_Name           varchar(48) not null,
   F1042_Desc           varchar(96),
   F1042_Voltage        INT not null,
   F1042_ConnType       INT,
   F1042_DevType        INT,
   F1042_IEDSolution    INT,
   primary key (F1042_CODE)
);

/*==============================================================*/
/* Table: TB1043_Equipment                                      */
/*==============================================================*/
create table TB1043_Equipment
(
   F1043_CODE           varchar(48) not null,
   F1042_CODE           varchar(48) not null,
   F1043_Name           varchar(48) not null,
   F1043_Desc           varchar(96),
   F1043_IsVirtual      INT not null,
   F1043_Type           INT not null,
   F1016_Code			varchar(48),
   primary key (F1043_CODE)
);

/*==============================================================*/
/* Table: TB1044_Terminal                                       */
/*==============================================================*/
create table TB1044_Terminal
(
   F1044_CODE           varchar(48) not null,
   F1043_CODE           varchar(48) not null,
   F1045_CODE           varchar(48),
   F1044_Name           varchar(48) not null,
   F1044_Desc           varchar(96),
   primary key (F1044_CODE)
);

/*==============================================================*/
/* Table: TB1045_ConnectivityNode                               */
/*==============================================================*/
create table TB1045_ConnectivityNode
(
   F1045_CODE           varchar(48) not null,
   F1045_Name           varchar(48) not null,
   F1045_Desc           varchar(96),
   primary key (F1045_CODE)
);

/*==============================================================*/
/* Table: TB1046_IED                                            */
/*==============================================================*/
create table TB1046_IED
(
   F1046_CODE           varchar(48) not null,
   F1042_CODE           varchar(48),
   F1050_Code           varchar(48),
   F1046_Name           varchar(48) not null,
   F1046_Desc           varchar(96),
   F1046_Manufacturor   varchar(48),
   F1046_Model          varchar(48),
   F1046_ConfigVersion  varchar(48),
   F1046_AorB           INT,
   F1046_IsVirtual      INT,
   F1046_Type           INT,
   F1046_CRC            varchar(24),
   F1046_AnetIp		    varchar(48),
   F1046_BnetIp		    varchar(48),
   F1046_Version        varchar(48),
   F1046_ProtectCategory  varchar(48),
   F1046_ProtectType    varchar(48),
   F1046_ProtectModel   varchar(48),
   F1046_ProtectCrc     varchar(48),
   F1046_OperateDate     varchar(48),
   F1046_ProductDate     varchar(48),
   F1046_ProductNo  	varchar(48),
   F1046_DataGatType  	varchar(48),
   F1046_OutType  		varchar(48),
   F1046_BoardNum  		varchar(48) default '0', 
   primary key (F1046_CODE)
);

/*==============================================================*/
/* Table: TB1047_Board                                          */
/*==============================================================*/
create table TB1047_Board
(
   F1047_CODE           varchar(48) not null,
   F1046_CODE           varchar(48) not null,
   F1047_Slot           varchar(24) not null,
   F1047_Desc           varchar(96),
   F1047_Type           varchar(48) not null,
   primary key (F1047_CODE)
);

/*==============================================================*/
/* Table: TB1048_Port                                           */
/*==============================================================*/
create table TB1048_Port
(
   F1048_CODE           varchar(48) not null,
   F1047_CODE           varchar(48) not null,
   F1048_No             varchar(24) not null,
   F1048_Desc           varchar(96),
   F1048_Direction      INT not null,
   F1048_Plug           INT not null,
   primary key (F1048_CODE)
);

/*==============================================================*/
/* Table: TB1049_Region                                         */
/*==============================================================*/
create table TB1049_Region
(
   F1049_CODE           varchar(48) not null,
   F1041_CODE           varchar(48) not null,
   F1049_Name           varchar(48) not null,
   F1049_Desc           varchar(96),
   F1049_Area           INT,
   primary key (F1049_CODE)
);

/*==============================================================*/
/* Table: TB1050_Cubicle                                        */
/*==============================================================*/
create table TB1050_Cubicle
(
   F1050_CODE           varchar(48) not null,
   F1049_CODE           varchar(48) not null,
   F1050_Name           varchar(48) not null,
   F1050_Desc           varchar(96),
   primary key (F1050_CODE)
);

/*==============================================================*/
/* Table: TB1051_Cable                                          */
/*==============================================================*/
create table TB1051_Cable
(
   F1051_CODE           varchar(48) not null,
   F1041_CODE           varchar(48) not null,
   F1051_Name           varchar(48) not null,
   F1051_Desc           varchar(96),
   F1051_Length         INT,
   F1051_CoreNum        INT,
   F1050_CODE_A         varchar(48) not null,
   F1050_CODE_B         varchar(48) not null,
   F1051_Type           INT not null,
   primary key (F1051_CODE)
);

/*==============================================================*/
/* Table: TB1052_Core                                           */
/*==============================================================*/
create table TB1052_Core
(
   F1052_CODE           varchar(48) not null,
   Parent_CODE          varchar(48) not null,
   F1052_Type           INT not null,
   F1052_No             INT,
   F1048_CODE_A         varchar(48),
   F1048_CODE_B         varchar(48),
   primary key (F1052_CODE)
);

/*==============================================================*/
/* Table: TB1053_PhysConn                                       */
/*==============================================================*/
create table TB1053_PhysConn
(
   F1053_CODE           varchar(48) not null,
   F1041_CODE           varchar(48) not null,
   F1048_CODE_A         varchar(48),
   F1048_CODE_B         varchar(48),
   primary key (F1053_CODE)
);

/*==============================================================*/
/* Table: TB1054_RCB                                            */
/*==============================================================*/
create table TB1054_RCB
(
   F1054_CODE           varchar(48) not null,
   F1046_CODE           varchar(48) not null,
   F1054_RPTID          varchar(48) not null,
   F1054_Dataset        varchar(48) not null,
   F1054_DSDesc         varchar(96),
   F1054_IsBRCB         INT not null,
   F1054_CBType         INT not null,
   primary key (F1054_CODE)
);

/*==============================================================*/
/* Table: TB1055_GCB                                            */
/*==============================================================*/
create table TB1055_GCB
(
   CB_CODE          varchar(48) not null,
   F1046_CODE       varchar(48) not null,
   CBNAME         	varchar(48) not null,
   CBID           	varchar(48) not null,
   MACAddr        	varchar(24) not null,
   VLANID         	varchar(24) not null,
   VLANPriority   	varchar(24) not null,
   APPID          	varchar(24) not null,
   DATASET        	varchar(24) not null,
   DSDesc         	varchar(96),
   F1071_CODE       varchar(48),
   primary key (CB_CODE)
);

/*==============================================================*/
/* Table: TB1056_SVCB                                           */
/*==============================================================*/
create table TB1056_SVCB
(
   CB_CODE          varchar(48) not null,
   F1046_CODE       varchar(48) not null,
   CBName         	varchar(48) not null,
   CBID           	varchar(48) not null,
   MACAddr        	varchar(24) not null,
   VLANID         	varchar(24) not null,
   VLANPriority   	varchar(24) not null,
   APPID          	varchar(24) not null,
   Dataset        	varchar(24) not null,
   DSDesc         	varchar(96),
   F1071_CODE           varchar(48),
   primary key (CB_CODE)
);

/*==============================================================*/
/* Table: TB1057_SGCB                                           */
/*==============================================================*/
create table TB1057_SGCB
(
   F1057_CODE           varchar(48) not null,
   F1046_CODE           varchar(48) not null,
   F1057_CBName         varchar(48) not null,
   F1057_Dataset        varchar(48) not null,
   F1057_DSDesc         varchar(96),
   primary key (F1057_CODE)
);

/*==============================================================*/
/* Table: TB1058_MMSFCDA                                        */
/*==============================================================*/
create table TB1058_MMSFCDA
(
   F1058_CODE           varchar(48) not null,
   F1046_CODE           varchar(48) not null,
   F1054_CODE           varchar(48) not null,
   F1058_RefAddr        varchar(48) not null,
   F1058_Index          INT not null,
   F1058_Desc           varchar(96),
   F1058_DataType       INT not null,
   DATA_CODE            varchar(48) not null,
   primary key (F1058_CODE)
);

/*==============================================================*/
/* Table: TB1059_SGFCDA                                         */
/*==============================================================*/
create table TB1059_SGFCDA
(
   F1059_CODE           varchar(48) not null,
   F1057_CODE           varchar(48) not null,
   F1059_RefAddr        varchar(48) not null,
   F1059_Index          INT not null,
   F1059_DataType       INT not null,
   F1059_Desc           varchar(96),
   F1059_Unit           varchar(24),
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
   F1060_CODE           varchar(48) not null,
   F1046_CODE           varchar(48) not null,
   F1060_RefAddr        varchar(48) not null,
   F1060_Index          INT not null,
   F1060_DataType       INT not null,
   F1060_Desc           varchar(96),
   F1060_Unit           varchar(24),
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
   F1061_CODE           varchar(48) not null,
   F1046_CODE           varchar(48) not null,
   CB_CODE              varchar(48) not null,
   F1061_RefAddr        varchar(48) not null,
   F1061_Index          INT not null,
   F1061_Desc           varchar(96),
   F1061_Type           INT,
   F1064_CODE           varchar(48),
   DATA_CODE            varchar(48) not null,
   primary key (F1061_CODE)
);

/*==============================================================*/
/* Table: TB1062_PIN                                            */
/*==============================================================*/
create table TB1062_PIN
(
   F1062_CODE           varchar(48) not null,
   F1046_CODE           varchar(48) not null,
   F1062_RefAddr        varchar(48) not null,
   F1062_Type           INT not null,
   F1062_Desc           varchar(96),
   F1062_IsUsed         INT not null,
   F1064_CODE           varchar(48),
   primary key (F1062_CODE)
);

/*==============================================================*/
/* Table: TB1063_Circuit                                        */
/*==============================================================*/
create table TB1063_Circuit
(
   F1063_CODE           varchar(48) not null,
   F1046_CODE_IEDSend   varchar(48) not null,
   F1061_CODE_PSend     varchar(48),
   F1046_CODE_IEDRecv   varchar(48) not null,
   F1062_CODE_PRecv     varchar(48) not null,
   F1063_Type           INT not null,
   F1065_CODE           varchar(48) not null,
   F1061_CODE_ConvChk1  varchar(48),
   F1061_CODE_ConvChk2  varchar(48),
   primary key (F1063_CODE)
);

/*==============================================================*/
/* Table: TB1064_Strap                                          */
/*==============================================================*/
create table TB1064_Strap
(
   F1064_CODE           varchar(48) not null,
   F1046_CODE           varchar(48) not null,
   F1064_TYPE           INT not null,
   F1064_NUM            varchar(48),
   F1064_DESC           varchar(96),
   F1042_CODE_RelatedBay varchar(48),
   primary key (F1064_CODE)
);

/*==============================================================*/
/* Table: TB1065_LogicalLink                                    */
/*==============================================================*/
create table TB1065_LogicalLink
(
   F1065_CODE           varchar(48) not null,
   F1065_Type           INT not null,
   F1046_CODE_IEDSend   varchar(48) not null,
   F1046_CODE_IEDRecv   varchar(48),
   CB_CODE          varchar(48),
   primary key (F1065_CODE)
);

/*==============================================================*/
/* Table: TB1066_ProtMMXU                                       */
/*==============================================================*/
create table TB1066_ProtMMXU
(
   F1066_CODE           varchar(48) not null,
   F1067_CODE           varchar(48),
   F1066_Type           INT not null,
   F1046_Code_Prot      varchar(48),
   F1006_CODE_A         varchar(48),
   F1006_CODE_B         varchar(48),
   F1006_CODE_C         varchar(48),
   primary key (F1066_CODE)
);

/*==============================================================*/
/* Table: TB1067_CTVTSecondary                                  */
/*==============================================================*/
create table TB1067_CTVTSecondary
(
   F1067_Code           varchar(48) not null,
   F1043_CODE           varchar(48) not null,
   F1067_Index          INT,
   F1067_Model          varchar(24),
   F1067_Type           varchar(48),
   F1061_CODE_A1        varchar(48),
   F1061_CODE_A2        varchar(48),
   F1061_CODE_B1        varchar(48),
   F1061_CODE_B2        varchar(48),
   F1061_CODE_C1        varchar(48),
   F1061_CODE_C2        varchar(48),
   F1067_TermNo         varchar(96) not null,
   F1067_CircNo         varchar(96),
   F1067_Desc           varchar(96),
   primary key (F1067_Code)
);

/*==============================================================*/
/* Table: TB1068_ProtCtrl                                       */
/*==============================================================*/
create table TB1068_ProtCtrl
(
   F1068_CODE           varchar(48) not null,
   F1046_CODE_Prot      varchar(48) not null,
   F1046_CODE_IO        varchar(48) not null,
   F1043_CODE           varchar(48) not null,
   F1065_CODE           varchar(48) not null,
   primary key (F1068_CODE)
);

/*==============================================================*/
/* Table: TB1069_RCDChannelA                                    */
/*==============================================================*/
create table TB1069_RCDChannelA
(
   F1069_CODE           varchar(48) not null,
   IED_CODE             varchar(48) not null,
   F1069_Index          varchar(48) not null,
   F1069_Type           INT not null,
   F1069_Phase          INT not null,
   F1043_CODE           varchar(48) not null,
   F1061_CODE           varchar(48),
   F1058_CODE           varchar(48) not null,
   primary key (F1069_CODE)
);

/*==============================================================*/
/* Table: TB1070_MMSServer                                      */
/*==============================================================*/
create table TB1070_MMSServer
(
   F1070_CODE           varchar(48) not null,
   F1046_CODE           varchar(48) not null,
   F1070_IP_A           varchar(24) not null,
   F1070_IP_B           varchar(24),
   primary key (F1070_CODE)
);

/*==============================================================*/
/* Table: TB1071_DAU                                            */
/*==============================================================*/
create table TB1071_DAU
(
   F1071_CODE           varchar(48) not null,
   F1071_DESC           varchar(96),
   F1071_IPAddr         varchar(24) not null,
   primary key (F1071_CODE)
);

/*==============================================================*/
/* Table: TB1072_RCDChannelD                                    */
/*==============================================================*/
create table TB1072_RCDChannelD
(
   F1072_CODE           varchar(48) not null,
   IED_CODE             varchar(48) not null,
   F1072_Index          varchar(48) not null,
   F1072_Type           INT not null,
   F1061_CODE           varchar(48),
   F1058_CODE           varchar(48) not null,
   primary key (F1072_CODE)
);

/*==============================================================*/
/* Table: TB1073_LLinkPhyRelation                               */
/*==============================================================*/
create table TB1073_LLinkPhyRelation
(
   F1073_CODE           varchar(48) not null,
   F1065_CODE           varchar(48),
   F1053_CODE           varchar(48),
   primary key (F1073_CODE)
);

/*==============================================================*/
/* Table: TB1090_LineProtFiber                                  */
/*==============================================================*/
create table TB1090_LineProtFiber
(
   F1090_CODE           varchar(48) not null,
   F1046_CODE           varchar(48) not null,
   F1090_DESC           varchar(96) not null,
   F1090_FiberNo        varchar(24),
   F1090_PortNo         varchar(24),
   primary key (F1090_CODE)
);

/*==============================================================*/
/* Table: TB1091_IOTerm                                         */
/*==============================================================*/
create table TB1091_IOTerm
(
   F1091_CODE           varchar(48) not null,
   F1046_CODE           varchar(48) not null,
   F1091_DESC           varchar(96) not null,
   F1091_TermNo         varchar(96),
   F1091_CircNo         varchar(96),
   primary key (F1091_CODE)
);

/*==============================================================*/
/* Table: TB1092_PowerKK                                        */
/*==============================================================*/
create table TB1092_PowerKK
(
   F1092_CODE           varchar(48) not null,
   F1046_CODE           varchar(48) not null,
   F1092_DESC           varchar(96) not null,
   F1092_KKNo           varchar(96),
   primary key (F1092_CODE)
);

/*==============================================================*/
/* Table: TB1093_VoltageKK                                      */
/*==============================================================*/
create table TB1093_VoltageKK
(
   F1093_CODE           varchar(48) not null,
   F1067_CODE           varchar(48) not null,
   F1093_DESC           varchar(96) not null,
   F1093_KKNo           varchar(96),
   primary key (F1093_CODE)
);

/*==============================================================*/
/* Table: IM100_File_Info                                     */
/*==============================================================*/
create table IM100_File_Info
(
   IM100_CODE           varchar(48) not null,
   FILE_NAME           	varchar(96) not null,
   FILE_PATH           	varchar(96),
   FILE_TYPE           	INT not null,
   primary key (IM100_CODE)
);

/*==============================================================*/
/* Table: IED_LIST                                      */
/*==============================================================*/
create table IM101_Ied_List
(
   IM101_CODE           varchar(48) not null,
   IM100_CODE           varchar(48) not null,
   DEV_NAME           	varchar(96),
   DEV_DESC           	varchar(96),
   BAY           		varchar(96),
   CUBICLE           	varchar(96),
   MANUFACTUROR			varchar(96),
   DEV_TYPE           	varchar(96),
   DEV_VERSION          varchar(96),
   A_OR_B           	varchar(96),
   PROT_CLASSIFY        varchar(96),
   PROT_MODEL           varchar(96),
   PROT_TYPE           	varchar(96),
   DATE_SERVICE         varchar(96),
   DATE_PRODUCT         varchar(96),
   PRODUCT_CODE         varchar(96),
   DATA_COLLECT_TYPE    varchar(96),
   OUT_TYPE           	varchar(96),
   BOARD_NUM           	varchar(96),
   MATCHED_IED_CODE     varchar(96),
   MATCHED           	INT,
   primary key (IM101_CODE)
);

/*==============================================================*/
/* Table: IM102_Fibre_List                                      */
/*==============================================================*/
create table IM102_Fibre_List
(
   IM102_CODE           varchar(48) not null,
   IM100_CODE           varchar(48) not null,
   CABLE_CODE           varchar(48),
   CORE_CODE           	varchar(96),
   DEV_CODEA           	varchar(96),
   DEV_NAMEA           	varchar(96),
   DEV_DESCA           	varchar(96),
   BOARD_CODEA          varchar(96),
   PORT_CODEA           varchar(96),
   CUBICLE_CODEA        varchar(96),
   CUBICLE_DESCA        varchar(96),
   CORE_CODEA           varchar(96),
   DISTRIB_FRAME_CODEA  varchar(96),
   DEV_CODEB           	varchar(96),
   DEV_NAMEB          	varchar(96),
   DEV_DESCB           	varchar(96),
   BOARD_CODEB          varchar(96),
   PORT_CODEB           varchar(96),
   CUBICLE_CODEB        varchar(96),
   CUBICLE_DESCB        varchar(96),
   CORE_CODEB           varchar(96),
   DISTRIB_FRAME_CODEB  varchar(96),
   MATCHED           	INT,
   primary key (IM102_CODE)
);

/*==============================================================*/
/* Table: IM103_Ied_Board                                      */
/*==============================================================*/
create table IM103_Ied_Board
(
   IM103_CODE           varchar(48) not null,
   IM100_CODE           varchar(48) not null,
   DEV_NAME           	varchar(96),
   DEV_DESC           	varchar(96),
   MANUFACTUROR			varchar(96),
   CONFIG_VERSION		varchar(96),
   BOARD_CODE           varchar(96),
   BOARD_INDEX			varchar(96),
   BOARD_MODEL			varchar(96),	
   BOARD_TYPE			varchar(96),	
   PORT_NUM       		varchar(96),
   MATCHED           	INT,
   primary key (IM103_CODE)
);

/*==============================================================*/
/* Table: IM104_Status_In                                      */
/*==============================================================*/
create table IM104_Status_In
(
   IM104_CODE           varchar(48) not null,
   IM100_CODE           varchar(48) not null,
   DEV_NAME           	varchar(96),
   PIN_REF_ADDR         varchar(96),
   PIN_DESC           	varchar(96),
   MMS_REF_ADDR         varchar(96),
   MMS_DESC          	varchar(96),
   MATCHED           	INT,
   primary key (IM104_CODE)
);

/*==============================================================*/
/* Table: IM105_Board_Warn                                      */
/*==============================================================*/
create table IM105_Board_Warn
(
   IM105_CODE           varchar(48) not null,
   IM100_CODE           varchar(48) not null,
   DEV_NAME           	varchar(96),
   ALARM_REF_ADDR       varchar(96),
   ALARM_DESC           varchar(96),
   BOARD_CODE           varchar(96),
   MATCHED           	INT,
   primary key (IM105_CODE)
);

/*==============================================================*/
/* Table: IM106_Port_Light                                      */
/*==============================================================*/
create table IM106_Port_Light
(
   IM106_CODE           varchar(48) not null,
   IM100_CODE           varchar(48) not null,
   DEV_NAME           	varchar(96),
   OPTICAL_REF_ADDR     varchar(96),
   OPTICAL_DESC         varchar(96),
   BOARD_CODE           varchar(96),
   PORT_CODE            varchar(96),
   MATCHED           	INT,
   primary key (IM106_CODE)
);

/*==============================================================*/
/* Table: IM107_Ter_Strap                                      */
/*==============================================================*/
create table IM107_Ter_Strap
(
   IM107_CODE           varchar(48) not null,
   IM100_CODE           varchar(48) not null,
   DEV_NAME           	varchar(96),
   STRAP_REF_ADDR     	varchar(96),
   STRAP_DESC         	varchar(96),
   STRAP_TYPE         	varchar(96),
   VP_REF_ADDR     		varchar(96),
   VP_DESC         		varchar(96),
   VP_TYPE         		varchar(96),
   MATCHED           	INT,
   primary key (IM107_CODE)
);

/*==============================================================*/
/* Table: IM108_Brk_Cfm                                      */
/*==============================================================*/
create table IM108_Brk_Cfm
(
   IM108_CODE           varchar(48) not null,
   IM100_CODE           varchar(48) not null,
   DEV_NAME           	varchar(96),
   PIN_REF_ADDR     	varchar(96),
   PIN_DESC         	varchar(96),
   CMDACKVP_REF_ADDR    varchar(96),
   CMDACKVP_DESC        varchar(96),
   CMDOUTVP_REF_ADDR    varchar(96),
   CMDOUTVP_DESC        varchar(96),
   MATCHED           	INT,
   primary key (IM108_CODE)
);

/*==============================================================*/
/* Table: IM109_Sta_Info                                      */
/*==============================================================*/
create table IM109_Sta_Info
(
   IM109_CODE           varchar(48) not null,
   IM100_CODE           varchar(48) not null,
   DEV_NAME           	varchar(96),
   MMS_DESC	            varchar(96),
   MMS_REF_ADDR     	varchar(96),
   MATCHED           	INT,
   primary key (IM109_CODE)
);
