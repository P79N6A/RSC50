
/*==============================================================*/
/* Table: TB1006_AnalogData                                     */
/*==============================================================*/
create table TB1006_AnalogData
(
   F1006_CODE           char(48) not null,
   F1006_DESC           char(96),
   F1006_SAFELEVEL      INT,
   Parent_CODE          char(48) not null,
   F1011_NO             INT not null,
   F1006_BYNAME         char(24),
   F0008_NAME           char(48) not null,
   F0009_NAME           char(48) not null,
   F1006_CALCFLAG       INT not null,
   F1006_PICNAME        char(96),
   F1006_PDRMODE        INT,
   F1006_K              float not null,
   F1006_B              float not null,
   F1006_ZERODBAND      float not null,
   F1006_OVERFLOW       float not null,
   F1006_LOWFLOW        float not null,
   F1006_MAXINC         float not null,
   F1006_HIWARN         float,
   F1006_LOWARN         float,
   F1006_HIALARM        float,
   F1006_LOALARM        float,
   F1006_SAVEPERIOD     INT not null,
   F1006_PLANTIME       INT not null,
   F1006_DEADTIME       INT not null,
   F1006_ALARMLEVEL     INT not null,
   F1006_SAVETYPE       INT,
   primary key (F1006_CODE)
);

/*==============================================================*/
/* Table: TB1016_StateData                                      */
/*==============================================================*/
create table TB1016_StateData
(
   F1016_CODE           char(48) not null,
   F1016_DESC           char(96),
   F1016_SAFELEVEL      INT,
   Parent_CODE          char(48) not null,
   F1011_NO             INT not null,
   F1016_BYNAME         char(24),
   F0008_NAME           char(48) not null,
   F0009_NAME           char(48) not null,
   F1016_CALCFLAG       INT not null,
   F1016_PICNAME        char(96),
   F1016_ISPDR          INT not null,
   F1016_PDRNO          INT,
   F1016_DPSFLAG        INT not null,
   F1016_MAINSTFLAG     INT,
   F1016_DPSCALCFLAG    INT,
   F1016_SOE            char(48),
   F1016_SGLIMITVAL     INT not null,
   F1016_ALARMPROCMODE  INT not null,
   F1016_PROCBAND       INT,
   F1016_SGPROCNAME     char(48),
   F1016_REVFLAG        INT,
   F1016_ISSTA          INT,
   primary key (F1016_CODE)
);

/*==============================================================*/
/* Table: TB1022_FaultConfig                                    */
/*==============================================================*/
create table TB1022_FaultConfig
(
   F1022_CODE           char(48) not null,
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
   F1026_CODE           char(48) not null,
   F1026_DESC           char(96),
   F1026_SAFELEVEL      INT,
   Parent_CODE          char(48) not null,
   F1011_NO             INT not null,
   F1026_BYNAME         char(24),
   F0008_NAME           char(48) not null,
   F0009_NAME           char(48) not null,
   F1026_CALCFLAG       INT not null,
   F1026_PICNAME        char(96),
   F1026_ISPDR          INT not null,
   F1026_PDRNO          INT,
   F1026_ISSTA          INT,
   primary key (F1026_CODE)
);

/*==============================================================*/
/* Table: TB1041_Substation                                     */
/*==============================================================*/
create table TB1041_Substation
(
   F1041_CODE           char(48) not null,
   F1041_Name           char(48) not null,
   F1041_Desc           char(96),
   F1041_DQName         char(48) not null,
   F1041_DQDESC         char(96),
   F1041_Company        char(96),
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
   F1042_CODE           char(48) not null,
   F1041_CODE           char(48) not null,
   F1042_Name           char(48) not null,
   F1042_Desc           char(96) not null,
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
   F1043_CODE           char(48) not null,
   F1042_CODE           char(48) not null,
   F1043_Name           char(48) not null,
   F1043_Desc           char(96),
   F1043_IsVirtual      INT not null,
   F1043_Type           INT not null,
   primary key (F1043_CODE)
);

/*==============================================================*/
/* Table: TB1044_Terminal                                       */
/*==============================================================*/
create table TB1044_Terminal
(
   F1044_CODE           char(48) not null,
   F1043_CODE           char(48) not null,
   F1045_CODE           char(48) not null,
   F1044_Name           char(48) not null,
   F1044_Desc           char(96),
   primary key (F1044_CODE)
);

/*==============================================================*/
/* Table: TB1045_ConnectivityNode                               */
/*==============================================================*/
create table TB1045_ConnectivityNode
(
   F1045_CODE           char(48) not null,
   F1045_Name           char(48) not null,
   F1045_Desc           char(96),
   primary key (F1045_CODE)
);

/*==============================================================*/
/* Table: TB1046_IED                                            */
/*==============================================================*/
create table TB1046_IED
(
   F1046_CODE           char(48) not null,
   F1042_CODE           char(48) not null,
   F1050_Code           char(48),
   F1046_Name           char(48) not null,
   F1046_Desc           char(96),
   F1046_Manufacturor   char(48),
   F1046_Model          char(48),
   F1046_ConfigVersion  char(48),
   F1046_AorB           INT,
   F1046_IsVirtual      INT not null,
   F1046_Type           INT,
   F1046_CRC            char(24),
   primary key (F1046_CODE)
);

/*==============================================================*/
/* Table: TB1047_Board                                          */
/*==============================================================*/
create table TB1047_Board
(
   F1047_CODE           char(48) not null,
   F1046_CODE           char(48) not null,
   F1047_Slot           char(24) not null,
   F1047_Desc           char(96),
   F1047_Type           char(48) not null,
   primary key (F1047_CODE)
);

/*==============================================================*/
/* Table: TB1048_Port                                           */
/*==============================================================*/
create table TB1048_Port
(
   F1048_CODE           char(48) not null,
   F1047_CODE           char(48) not null,
   F1048_No             char(24) not null,
   F1048_Desc           char(96),
   F1048_Direction      INT not null,
   F1048_Plug           INT not null,
   primary key (F1048_CODE)
);

/*==============================================================*/
/* Table: TB1049_Region                                         */
/*==============================================================*/
create table TB1049_Region
(
   F1049_CODE           char(48) not null,
   F1041_CODE           char(48) not null,
   F1049_Name           char(48) not null,
   F1049_Desc           char(96),
   F1049_Area           INT,
   primary key (F1049_CODE)
);

/*==============================================================*/
/* Table: TB1050_Cubicle                                        */
/*==============================================================*/
create table TB1050_Cubicle
(
   F1050_CODE           char(48) not null,
   F1049_CODE           char(48) not null,
   F1050_Name           char(48) not null,
   F1050_Desc           char(96),
   primary key (F1050_CODE)
);

/*==============================================================*/
/* Table: TB1051_Cable                                          */
/*==============================================================*/
create table TB1051_Cable
(
   F1051_CODE           char(48) not null,
   F1041_CODE           char(48) not null,
   F1051_Name           char(48) not null,
   F1051_Desc           char(96),
   F1051_Length         INT,
   F1051_CoreNum        INT,
   F1050_CODE_A         char(48) not null,
   F1050_CODE_B         char(48) not null,
   F1051_Type           INT not null,
   primary key (F1051_CODE)
);

/*==============================================================*/
/* Table: TB1052_Core                                           */
/*==============================================================*/
create table TB1052_Core
(
   F1052_CODE           char(48) not null,
   Parent_CODE          char(48) not null,
   F1052_Type           INT not null,
   F1052_No             INT,
   F1048_CODE_A         char(48),
   F1048_CODE_B         char(48),
   primary key (F1052_CODE)
);

/*==============================================================*/
/* Table: TB1053_PhysConn                                       */
/*==============================================================*/
create table TB1053_PhysConn
(
   F1053_CODE           char(48) not null,
   F1041_CODE           char(48) not null,
   F1048_CODE_A         char(48),
   F1048_CODE_B         char(48),
   primary key (F1053_CODE)
);

/*==============================================================*/
/* Table: TB1054_RCB                                            */
/*==============================================================*/
create table TB1054_RCB
(
   F1054_CODE           char(48) not null,
   F1046_CODE           char(48) not null,
   F1054_RPTID          char(48) not null,
   F1054_Dataset        char(48) not null,
   F1054_DSDesc         char(96),
   F1054_IsBRCB         INT not null,
   F1054_CBType         INT not null,
   primary key (F1054_CODE)
);

/*==============================================================*/
/* Table: TB1055_GCB                                            */
/*==============================================================*/
create table TB1055_GCB
(
   F1055_CODE           char(48) not null,
   F1046_CODE           char(48) not null,
   F1055_CBNAME         char(48) not null,
   F1055_CBID           char(48) not null,
   F1055_MACAddr        char(24) not null,
   F1055_VLANID         char(24) not null,
   F1055_VLANPriority   char(24) not null,
   F1055_APPID          char(24) not null,
   F1055_DATASET        char(24) not null,
   F1055_DESC           char(96),
   F1071_CODE           char(48),
   primary key (F1055_CODE)
);

/*==============================================================*/
/* Table: TB1056_SVCB                                           */
/*==============================================================*/
create table TB1056_SVCB
(
   F1056_CODE           char(48) not null,
   F1046_CODE           char(48) not null,
   F1056_CBName         char(48) not null,
   F1056_CBID           char(48) not null,
   F1056_MACAddr        char(24) not null,
   F1056_VLANID         char(24) not null,
   F1056_VLANPriority   char(24) not null,
   F1056_APPID          char(24) not null,
   F1056_Dataset        char(24) not null,
   F1056_DSDesc         char(96),
   F1071_CODE           char(48),
   primary key (F1056_CODE)
);

/*==============================================================*/
/* Table: TB1057_SGCB                                           */
/*==============================================================*/
create table TB1057_SGCB
(
   F1057_CODE           char(48) not null,
   F1046_CODE           char(48) not null,
   F1057_CBName         char(48) not null,
   F1057_Dataset        char(48) not null,
   F1057_DSDesc         char(96),
   primary key (F1057_CODE)
);

/*==============================================================*/
/* Table: TB1058_MMSFCDA                                        */
/*==============================================================*/
create table TB1058_MMSFCDA
(
   F1058_CODE           char(48) not null,
   F1046_CODE           char(48) not null,
   F1054_CODE           char(48) not null,
   F1058_RefAddr        char(48) not null,
   F1058_Index          INT not null,
   F1058_Desc           char(96),
   F1058_DataType       INT not null,
   DATA_CODE            char(48) not null,
   primary key (F1058_CODE)
);

/*==============================================================*/
/* Table: TB1059_SGFCDA                                         */
/*==============================================================*/
create table TB1059_SGFCDA
(
   F1059_CODE           char(48) not null,
   F1057_CODE           char(48) not null,
   F1059_RefAddr        char(48) not null,
   F1059_Index          INT not null,
   F1059_DataType       INT not null,
   F1059_Desc           char(96),
   F1059_Unit           char(24),
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
   F1060_CODE           char(48) not null,
   F1046_CODE           char(48) not null,
   F1060_RefAddr        char(48) not null,
   F1060_Index          INT not null,
   F1060_DataType       INT not null,
   F1060_Desc           char(96),
   F1060_Unit           char(24),
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
   F1061_CODE           char(48) not null,
   F1046_CODE           char(48) not null,
   CB_CODE              char(48) not null,
   F1061_RefAddr        char(48) not null,
   F1061_Index          INT not null,
   F1061_Desc           char(96),
   F1061_Type           INT,
   F1064_CODE           char(48),
   DATA_CODE            char(48) not null,
   primary key (F1061_CODE)
);

/*==============================================================*/
/* Table: TB1062_PIN                                            */
/*==============================================================*/
create table TB1062_PIN
(
   F1062_CODE           char(48) not null,
   F1046_CODE           char(48) not null,
   F1062_RefAddr        char(48) not null,
   F1020_NO             char(48) not null,
   F1062_Desc           char(96),
   F1062_IsUsed         INT not null,
   F1064_CODE           char(48),
   primary key (F1062_CODE)
);

/*==============================================================*/
/* Table: TB1063_Circuit                                        */
/*==============================================================*/
create table TB1063_Circuit
(
   F1063_CODE           char(48) not null,
   F1046_CODE_IEDSend   char(48) not null,
   F1061_CODE_PSend     char(48) not null,
   F1046_CODE_IEDRecv   char(48) not null,
   F1062_CODE_PRecv     char(48) not null,
   F1020_NO             INT not null,
   F1065_CODE           char(48) not null,
   F1061_CODE_ConvChk1  char(48),
   F1061_CODE_ConvChk2  char(48),
   primary key (F1063_CODE)
);

/*==============================================================*/
/* Table: TB1064_Strap                                          */
/*==============================================================*/
create table TB1064_Strap
(
   F1064_CODE           char(48) not null,
   F1046_CODE           char(48) not null,
   F1021_NO             INT not null,
   F1064_NUM            char(48),
   F1064_DESC           char(96) not null,
   F1042_CODE_RelatedBay char(48),
   primary key (F1064_CODE)
);

/*==============================================================*/
/* Table: TB1065_LogicalLink                                    */
/*==============================================================*/
create table TB1065_LogicalLink
(
   F1065_CODE           char(48) not null,
   F1065_Type           INT not null,
   F1046_CODE_IEDSend   char(48) not null,
   F1046_CODE_IEDRecv   char(48),
   F1065_APPID          char(24) not null,
   primary key (F1065_CODE)
);

/*==============================================================*/
/* Table: TB1066_ProtMMXU                                       */
/*==============================================================*/
create table TB1066_ProtMMXU
(
   F1066_CODE           char(48) not null,
   F1043_CODE           char(48) not null,
   F1067_CODE           char(48),
   F1046_Code_MU        char(48),
   F1046_Code_Prot      char(48) not null,
   F1066_Type           INT not null,
   F1006_CODE_A         char(48) not null,
   F1006_CODE_B         char(48),
   F1006_CODE_C         char(48),
   primary key (F1066_CODE)
);

/*==============================================================*/
/* Table: TB1067_CTVTSecondary                                  */
/*==============================================================*/
create table TB1067_CTVTSecondary
(
   F1067_Code           char(48) not null,
   F1043_CODE           char(48) not null,
   F1067_Index          INT,
   F1067_Model          char(24),
   F1067_Type           char(48) not null,
   F1061_CODE_A1        char(48) not null,
   F1061_CODE_A2        char(48) not null,
   F1061_CODE_B1        char(48),
   F1061_CODE_B2        char(48),
   F1061_CODE_C1        char(48),
   F1061_CODE_C2        char(48),
   F1061_TermNo         char(96),
   F1061_CircNo         char(96),
   F1061_Desc           char(96),
   primary key (F1067_Code)
);

/*==============================================================*/
/* Table: TB1068_ProtCtrl                                       */
/*==============================================================*/
create table TB1068_ProtCtrl
(
   F1068_CODE           char(48) not null,
   F1046_CODE_Prot      char(48) not null,
   F1046_CODE_IO        char(48) not null,
   F1043_CODE           char(48) not null,
   F1065_CODE           char(48) not null,
   primary key (F1068_CODE)
);

/*==============================================================*/
/* Table: TB1069_RCDChannelA                                    */
/*==============================================================*/
create table TB1069_RCDChannelA
(
   F1069_CODE           char(48) not null,
   IED_CODE             char(48) not null,
   F1069_Index          char(48) not null,
   F1069_Type           INT not null,
   F1069_Phase          INT not null,
   F1043_CODE           char(48) not null,
   F1061_CODE           char(48),
   F1058_CODE           char(48) not null,
   primary key (F1069_CODE)
);

/*==============================================================*/
/* Table: TB1070_MMSServer                                      */
/*==============================================================*/
create table TB1070_MMSServer
(
   F1070_CODE           char(48) not null,
   F1046_CODE           char(48) not null,
   F1070_IP_A           char(24) not null,
   F1070_IP_B           char(24),
   primary key (F1070_CODE)
);

/*==============================================================*/
/* Table: TB1071_DAU                                            */
/*==============================================================*/
create table TB1071_DAU
(
   F1071_CODE           char(48) not null,
   F1071_DESC           char(96),
   F1071_IPAddr         char(24) not null,
   primary key (F1071_CODE)
);

/*==============================================================*/
/* Table: TB1072_RCDChannelD                                    */
/*==============================================================*/
create table TB1072_RCDChannelD
(
   F1072_CODE           char(48) not null,
   IED_CODE             char(48) not null,
   F1072_Index          char(48) not null,
   F1072_Type           INT not null,
   F1061_CODE           char(48),
   F1058_CODE           char(48) not null,
   primary key (F1072_CODE)
);

/*==============================================================*/
/* Table: TB1073_LLinkPhyRelation                               */
/*==============================================================*/
create table TB1073_LLinkPhyRelation
(
   F1073_CODE           char(48) not null,
   F1065_CODE           char(48),
   F1053_CODE           char(48),
   primary key (F1073_CODE)
);

/*==============================================================*/
/* Table: TB1090_LineProtFiber                                  */
/*==============================================================*/
create table TB1090_LineProtFiber
(
   F1090_CODE           char(48) not null,
   F1046_CODE           char(48) not null,
   F1090_DESC           char(96) not null,
   F1090_FiberNo        char(24),
   F1090_PortNo         char(24),
   primary key (F1090_CODE)
);

/*==============================================================*/
/* Table: TB1091_IOTerm                                         */
/*==============================================================*/
create table TB1091_IOTerm
(
   F1091_CODE           char(48) not null,
   F1046_CODE           char(48) not null,
   F1091_DESC           char(96) not null,
   F1091_TermNo         char(96),
   F1091_CircNo         char(96),
   primary key (F1091_CODE)
);

/*==============================================================*/
/* Table: TB1092_PowerKK                                        */
/*==============================================================*/
create table TB1092_PowerKK
(
   F1092_CODE           char(48) not null,
   F1046_CODE           char(48) not null,
   F1092_DESC           char(96) not null,
   F1092_KKNo           char(96),
   primary key (F1092_CODE)
);

/*==============================================================*/
/* Table: TB1093_VoltageKK                                      */
/*==============================================================*/
create table TB1093_VoltageKK
(
   F1093_CODE           char(48) not null,
   F1067_CODE           char(48) not null,
   F1093_DESC           char(96) not null,
   F1093_KKNo           char(96),
   primary key (F1093_CODE)
);
