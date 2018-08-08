package org.basex.query.func;

import static org.basex.query.QueryText.ADMIN_URI;
import static org.basex.query.QueryText.ARCHIVE_URI;
import static org.basex.query.QueryText.ARRAY_URI;
import static org.basex.query.QueryText.BIN_URI;
import static org.basex.query.QueryText.CLIENT_URI;
import static org.basex.query.QueryText.CONVERT_URI;
import static org.basex.query.QueryText.CRYPTO_URI;
import static org.basex.query.QueryText.CSV_URI;
import static org.basex.query.QueryText.DB_URI;
import static org.basex.query.QueryText.FETCH_URI;
import static org.basex.query.QueryText.FILE_URI;
import static org.basex.query.QueryText.FN_URI;
import static org.basex.query.QueryText.FT_URI;
import static org.basex.query.QueryText.HASH_URI;
import static org.basex.query.QueryText.HOF_URI;
import static org.basex.query.QueryText.HTML_URI;
import static org.basex.query.QueryText.HTTP_URI;
import static org.basex.query.QueryText.INDEX_URI;
import static org.basex.query.QueryText.INSPECT_URI;
import static org.basex.query.QueryText.JOBS_URI;
import static org.basex.query.QueryText.JSON_URI;
import static org.basex.query.QueryText.MAP_URI;
import static org.basex.query.QueryText.MATH_URI;
import static org.basex.query.QueryText.OUT_URI;
import static org.basex.query.QueryText.PROC_URI;
import static org.basex.query.QueryText.PROF_URI;
import static org.basex.query.QueryText.RANDOM_URI;
import static org.basex.query.QueryText.REPO_URI;
import static org.basex.query.QueryText.SEP;
import static org.basex.query.QueryText.SQL_URI;
import static org.basex.query.QueryText.STREAM_URI;
import static org.basex.query.QueryText.STRINGS_URI;
import static org.basex.query.QueryText.UNIT_URI;
import static org.basex.query.QueryText.USER_URI;
import static org.basex.query.QueryText.UTIL_URI;
import static org.basex.query.QueryText.VALIDATE_URI;
import static org.basex.query.QueryText.WEB_URI;
import static org.basex.query.QueryText.XQUERY_URI;
import static org.basex.query.QueryText.XSLT_URI;
import static org.basex.query.QueryText.ZIP_URI;
import static org.basex.query.expr.Expr.Flag.CNS;
import static org.basex.query.expr.Expr.Flag.CTX;
import static org.basex.query.expr.Expr.Flag.HOF;
import static org.basex.query.expr.Expr.Flag.NDT;
import static org.basex.query.expr.Expr.Flag.UPD;
import static org.basex.query.value.type.SeqType.AAT;
import static org.basex.query.value.type.SeqType.AAT_ZM;
import static org.basex.query.value.type.SeqType.AAT_ZO;
import static org.basex.query.value.type.SeqType.ARRAY_O;
import static org.basex.query.value.type.SeqType.ARRAY_ZM;
import static org.basex.query.value.type.SeqType.ATT_ZM;
import static org.basex.query.value.type.SeqType.B64;
import static org.basex.query.value.type.SeqType.B64_ZM;
import static org.basex.query.value.type.SeqType.B64_ZO;
import static org.basex.query.value.type.SeqType.BIN;
import static org.basex.query.value.type.SeqType.BLN;
import static org.basex.query.value.type.SeqType.BLN_ZO;
import static org.basex.query.value.type.SeqType.BYT_ZM;
import static org.basex.query.value.type.SeqType.DAT;
import static org.basex.query.value.type.SeqType.DAT_ZO;
import static org.basex.query.value.type.SeqType.DBL;
import static org.basex.query.value.type.SeqType.DBL_ZM;
import static org.basex.query.value.type.SeqType.DBL_ZO;
import static org.basex.query.value.type.SeqType.DEC_ZO;
import static org.basex.query.value.type.SeqType.DOC_O;
import static org.basex.query.value.type.SeqType.DOC_ZM;
import static org.basex.query.value.type.SeqType.DOC_ZO;
import static org.basex.query.value.type.SeqType.DTD;
import static org.basex.query.value.type.SeqType.DTD_ZO;
import static org.basex.query.value.type.SeqType.DTM;
import static org.basex.query.value.type.SeqType.DTM_ZO;
import static org.basex.query.value.type.SeqType.DUR_ZO;
import static org.basex.query.value.type.SeqType.ELM;
import static org.basex.query.value.type.SeqType.ELM_ZM;
import static org.basex.query.value.type.SeqType.EMP;
import static org.basex.query.value.type.SeqType.FLT;
import static org.basex.query.value.type.SeqType.FUN_O;
import static org.basex.query.value.type.SeqType.FUN_OZ;
import static org.basex.query.value.type.SeqType.FUN_ZM;
import static org.basex.query.value.type.SeqType.HEX;
import static org.basex.query.value.type.SeqType.ITEM;
import static org.basex.query.value.type.SeqType.ITEM_OM;
import static org.basex.query.value.type.SeqType.ITEM_ZM;
import static org.basex.query.value.type.SeqType.ITEM_ZO;
import static org.basex.query.value.type.SeqType.ITR;
import static org.basex.query.value.type.SeqType.ITR_ZM;
import static org.basex.query.value.type.SeqType.ITR_ZO;
import static org.basex.query.value.type.SeqType.LAN;
import static org.basex.query.value.type.SeqType.MAP_O;
import static org.basex.query.value.type.SeqType.MAP_ZM;
import static org.basex.query.value.type.SeqType.MAP_ZO;
import static org.basex.query.value.type.SeqType.NCN_ZO;
import static org.basex.query.value.type.SeqType.NOD;
import static org.basex.query.value.type.SeqType.NOD_ZM;
import static org.basex.query.value.type.SeqType.NOD_ZO;
import static org.basex.query.value.type.SeqType.NUM;
import static org.basex.query.value.type.SeqType.NUM_ZO;
import static org.basex.query.value.type.SeqType.QNM;
import static org.basex.query.value.type.SeqType.QNM_ZO;
import static org.basex.query.value.type.SeqType.STR;
import static org.basex.query.value.type.SeqType.STR_ZM;
import static org.basex.query.value.type.SeqType.STR_ZO;
import static org.basex.query.value.type.SeqType.TIM;
import static org.basex.query.value.type.SeqType.TIM_ZO;
import static org.basex.query.value.type.SeqType.TXT_ZM;
import static org.basex.query.value.type.SeqType.URI;
import static org.basex.query.value.type.SeqType.URI_ZM;
import static org.basex.query.value.type.SeqType.URI_ZO;

import java.util.Collections;
import java.util.EnumSet;
import java.util.regex.Pattern;

import org.basex.query.StaticContext;
import org.basex.query.expr.Expr;
import org.basex.query.expr.Expr.Flag;
import org.basex.query.func.admin.AdminDeleteLogs;
import org.basex.query.func.admin.AdminLogs;
import org.basex.query.func.admin.AdminSessions;
import org.basex.query.func.admin.AdminWriteLog;
import org.basex.query.func.archive.ArchiveCreate;
import org.basex.query.func.archive.ArchiveCreateFrom;
import org.basex.query.func.archive.ArchiveDelete;
import org.basex.query.func.archive.ArchiveEntries;
import org.basex.query.func.archive.ArchiveExtractBinary;
import org.basex.query.func.archive.ArchiveExtractText;
import org.basex.query.func.archive.ArchiveExtractTo;
import org.basex.query.func.archive.ArchiveOptions;
import org.basex.query.func.archive.ArchiveUpdate;
import org.basex.query.func.array.ArrayAppend;
import org.basex.query.func.array.ArrayFilter;
import org.basex.query.func.array.ArrayFlatten;
import org.basex.query.func.array.ArrayFoldLeft;
import org.basex.query.func.array.ArrayFoldRight;
import org.basex.query.func.array.ArrayForEach;
import org.basex.query.func.array.ArrayForEachPair;
import org.basex.query.func.array.ArrayGet;
import org.basex.query.func.array.ArrayHead;
import org.basex.query.func.array.ArrayInsertBefore;
import org.basex.query.func.array.ArrayJoin;
import org.basex.query.func.array.ArrayPut;
import org.basex.query.func.array.ArrayRemove;
import org.basex.query.func.array.ArrayReverse;
import org.basex.query.func.array.ArraySize;
import org.basex.query.func.array.ArraySort;
import org.basex.query.func.array.ArraySubarray;
import org.basex.query.func.array.ArrayTail;
import org.basex.query.func.bin.BinAnd;
import org.basex.query.func.bin.BinBin;
import org.basex.query.func.bin.BinDecodeString;
import org.basex.query.func.bin.BinEncodeString;
import org.basex.query.func.bin.BinFind;
import org.basex.query.func.bin.BinFromOctets;
import org.basex.query.func.bin.BinHex;
import org.basex.query.func.bin.BinInsertBefore;
import org.basex.query.func.bin.BinJoin;
import org.basex.query.func.bin.BinLength;
import org.basex.query.func.bin.BinNot;
import org.basex.query.func.bin.BinOctal;
import org.basex.query.func.bin.BinOr;
import org.basex.query.func.bin.BinPackDouble;
import org.basex.query.func.bin.BinPackFloat;
import org.basex.query.func.bin.BinPackInteger;
import org.basex.query.func.bin.BinPadLeft;
import org.basex.query.func.bin.BinPadRight;
import org.basex.query.func.bin.BinPart;
import org.basex.query.func.bin.BinShift;
import org.basex.query.func.bin.BinToOctets;
import org.basex.query.func.bin.BinUnpackDouble;
import org.basex.query.func.bin.BinUnpackFloat;
import org.basex.query.func.bin.BinUnpackInteger;
import org.basex.query.func.bin.BinUnpackUnsignedInteger;
import org.basex.query.func.bin.BinXor;
import org.basex.query.func.client.ClientClose;
import org.basex.query.func.client.ClientConnect;
import org.basex.query.func.client.ClientExecute;
import org.basex.query.func.client.ClientInfo;
import org.basex.query.func.client.ClientQuery;
import org.basex.query.func.convert.ConvertBinaryToBytes;
import org.basex.query.func.convert.ConvertBinaryToString;
import org.basex.query.func.convert.ConvertBytesToBase64;
import org.basex.query.func.convert.ConvertBytesToHex;
import org.basex.query.func.convert.ConvertDateTimeToInteger;
import org.basex.query.func.convert.ConvertDayTimeToInteger;
import org.basex.query.func.convert.ConvertIntegerFromBase;
import org.basex.query.func.convert.ConvertIntegerToBase;
import org.basex.query.func.convert.ConvertIntegerToDateTime;
import org.basex.query.func.convert.ConvertIntegerToDayTime;
import org.basex.query.func.convert.ConvertStringToBase64;
import org.basex.query.func.convert.ConvertStringToHex;
import org.basex.query.func.crypto.CryptoDecrypt;
import org.basex.query.func.crypto.CryptoEncrypt;
import org.basex.query.func.crypto.CryptoGenerateSignature;
import org.basex.query.func.crypto.CryptoHmac;
import org.basex.query.func.crypto.CryptoValidateSignature;
import org.basex.query.func.csv.CsvParse;
import org.basex.query.func.csv.CsvSerialize;
import org.basex.query.func.db.DbAdd;
import org.basex.query.func.db.DbAlter;
import org.basex.query.func.db.DbAttribute;
import org.basex.query.func.db.DbAttributeRange;
import org.basex.query.func.db.DbBackups;
import org.basex.query.func.db.DbContentType;
import org.basex.query.func.db.DbCopy;
import org.basex.query.func.db.DbCreate;
import org.basex.query.func.db.DbCreateBackup;
import org.basex.query.func.db.DbDelete;
import org.basex.query.func.db.DbDrop;
import org.basex.query.func.db.DbDropBackup;
import org.basex.query.func.db.DbExists;
import org.basex.query.func.db.DbExport;
import org.basex.query.func.db.DbFlush;
import org.basex.query.func.db.DbInfo;
import org.basex.query.func.db.DbIsRaw;
import org.basex.query.func.db.DbIsXml;
import org.basex.query.func.db.DbList;
import org.basex.query.func.db.DbListDetails;
import org.basex.query.func.db.DbName;
import org.basex.query.func.db.DbNodeId;
import org.basex.query.func.db.DbNodePre;
import org.basex.query.func.db.DbOpen;
import org.basex.query.func.db.DbOpenId;
import org.basex.query.func.db.DbOpenPre;
import org.basex.query.func.db.DbOptimize;
import org.basex.query.func.db.DbOutput;
import org.basex.query.func.db.DbOutputCache;
import org.basex.query.func.db.DbPath;
import org.basex.query.func.db.DbProperty;
import org.basex.query.func.db.DbRename;
import org.basex.query.func.db.DbReplace;
import org.basex.query.func.db.DbRestore;
import org.basex.query.func.db.DbRetrieve;
import org.basex.query.func.db.DbStore;
import org.basex.query.func.db.DbSystem;
import org.basex.query.func.db.DbText;
import org.basex.query.func.db.DbTextRange;
import org.basex.query.func.db.DbToken;
import org.basex.query.func.fetch.FetchBinary;
import org.basex.query.func.fetch.FetchContentType;
import org.basex.query.func.fetch.FetchText;
import org.basex.query.func.fetch.FetchXml;
import org.basex.query.func.file.FileAppend;
import org.basex.query.func.file.FileAppendBinary;
import org.basex.query.func.file.FileAppendText;
import org.basex.query.func.file.FileAppendTextLines;
import org.basex.query.func.file.FileBaseDir;
import org.basex.query.func.file.FileChildren;
import org.basex.query.func.file.FileCopy;
import org.basex.query.func.file.FileCreateDir;
import org.basex.query.func.file.FileCreateTempDir;
import org.basex.query.func.file.FileCreateTempFile;
import org.basex.query.func.file.FileCurrentDir;
import org.basex.query.func.file.FileDelete;
import org.basex.query.func.file.FileDirSeparator;
import org.basex.query.func.file.FileExists;
import org.basex.query.func.file.FileIsAbsolute;
import org.basex.query.func.file.FileIsDir;
import org.basex.query.func.file.FileIsFile;
import org.basex.query.func.file.FileLastModified;
import org.basex.query.func.file.FileList;
import org.basex.query.func.file.FileMove;
import org.basex.query.func.file.FileName;
import org.basex.query.func.file.FileParent;
import org.basex.query.func.file.FilePathSeparator;
import org.basex.query.func.file.FilePathToNative;
import org.basex.query.func.file.FilePathToUri;
import org.basex.query.func.file.FileReadBinary;
import org.basex.query.func.file.FileReadText;
import org.basex.query.func.file.FileReadTextLines;
import org.basex.query.func.file.FileResolvePath;
import org.basex.query.func.file.FileSize;
import org.basex.query.func.file.FileWrite;
import org.basex.query.func.file.FileWriteBinary;
import org.basex.query.func.file.FileWriteText;
import org.basex.query.func.file.FileWriteTextLines;
import org.basex.query.func.fn.*;
import org.basex.query.func.ft.FtContains;
import org.basex.query.func.ft.FtCount;
import org.basex.query.func.ft.FtExtract;
import org.basex.query.func.ft.FtMark;
import org.basex.query.func.ft.FtNormalize;
import org.basex.query.func.ft.FtScore;
import org.basex.query.func.ft.FtSearch;
import org.basex.query.func.ft.FtTokenize;
import org.basex.query.func.ft.FtTokens;
import org.basex.query.func.hash.HashHash;
import org.basex.query.func.hash.HashMd5;
import org.basex.query.func.hash.HashSha1;
import org.basex.query.func.hash.HashSha256;
import org.basex.query.func.hof.HofConst;
import org.basex.query.func.hof.HofFoldLeft1;
import org.basex.query.func.hof.HofId;
import org.basex.query.func.hof.HofScanLeft;
import org.basex.query.func.hof.HofSortWith;
import org.basex.query.func.hof.HofTakeWhile;
import org.basex.query.func.hof.HofTopKBy;
import org.basex.query.func.hof.HofTopKWith;
import org.basex.query.func.hof.HofUntil;
import org.basex.query.func.html.HtmlParse;
import org.basex.query.func.html.HtmlParser;
import org.basex.query.func.http.HttpSendRequest;
import org.basex.query.func.index.IndexAttributeNames;
import org.basex.query.func.index.IndexAttributes;
import org.basex.query.func.index.IndexElementNames;
import org.basex.query.func.index.IndexFacets;
import org.basex.query.func.index.IndexTexts;
import org.basex.query.func.index.IndexTokens;
import org.basex.query.func.inspect.InspectContext;
import org.basex.query.func.inspect.InspectFunction;
import org.basex.query.func.inspect.InspectFunctionAnnotations;
import org.basex.query.func.inspect.InspectFunctions;
import org.basex.query.func.inspect.InspectModule;
import org.basex.query.func.inspect.InspectStaticContext;
import org.basex.query.func.inspect.InspectXqdoc;
import org.basex.query.func.jobs.JobsCurrent;
import org.basex.query.func.jobs.JobsEval;
import org.basex.query.func.jobs.JobsFinished;
import org.basex.query.func.jobs.JobsList;
import org.basex.query.func.jobs.JobsListDetails;
import org.basex.query.func.jobs.JobsResult;
import org.basex.query.func.jobs.JobsStop;
import org.basex.query.func.json.JsonParse;
import org.basex.query.func.json.JsonSerialize;
import org.basex.query.func.map.MapContains;
import org.basex.query.func.map.MapEntry;
import org.basex.query.func.map.MapForEach;
import org.basex.query.func.map.MapGet;
import org.basex.query.func.map.MapKeys;
import org.basex.query.func.map.MapMerge;
import org.basex.query.func.map.MapPut;
import org.basex.query.func.map.MapRemove;
import org.basex.query.func.map.MapSize;
import org.basex.query.func.math.MathAcos;
import org.basex.query.func.math.MathAsin;
import org.basex.query.func.math.MathAtan;
import org.basex.query.func.math.MathAtan2;
import org.basex.query.func.math.MathCos;
import org.basex.query.func.math.MathCosh;
import org.basex.query.func.math.MathCrc32;
import org.basex.query.func.math.MathE;
import org.basex.query.func.math.MathExp;
import org.basex.query.func.math.MathExp10;
import org.basex.query.func.math.MathLog;
import org.basex.query.func.math.MathLog10;
import org.basex.query.func.math.MathPi;
import org.basex.query.func.math.MathPow;
import org.basex.query.func.math.MathSin;
import org.basex.query.func.math.MathSinh;
import org.basex.query.func.math.MathSqrt;
import org.basex.query.func.math.MathTan;
import org.basex.query.func.math.MathTanh;
import org.basex.query.func.out.OutFormat;
import org.basex.query.func.out.OutNl;
import org.basex.query.func.out.OutTab;
import org.basex.query.func.proc.ProcExecute;
import org.basex.query.func.proc.ProcProperty;
import org.basex.query.func.proc.ProcPropertyNames;
import org.basex.query.func.proc.ProcSystem;
import org.basex.query.func.prof.ProfCurrentMs;
import org.basex.query.func.prof.ProfCurrentNs;
import org.basex.query.func.prof.ProfDump;
import org.basex.query.func.prof.ProfHuman;
import org.basex.query.func.prof.ProfMem;
import org.basex.query.func.prof.ProfSleep;
import org.basex.query.func.prof.ProfTime;
import org.basex.query.func.prof.ProfType;
import org.basex.query.func.prof.ProfVariables;
import org.basex.query.func.prof.ProfVoid;
import org.basex.query.func.random.RandomDouble;
import org.basex.query.func.random.RandomGaussian;
import org.basex.query.func.random.RandomInteger;
import org.basex.query.func.random.RandomSeededDouble;
import org.basex.query.func.random.RandomSeededInteger;
import org.basex.query.func.random.RandomSeededPermutation;
import org.basex.query.func.random.RandomUuid;
import org.basex.query.func.repo.RepoDelete;
import org.basex.query.func.repo.RepoInstall;
import org.basex.query.func.repo.RepoList;
import org.basex.query.func.sql.SqlClose;
import org.basex.query.func.sql.SqlCommit;
import org.basex.query.func.sql.SqlConnect;
import org.basex.query.func.sql.SqlExecute;
import org.basex.query.func.sql.SqlExecutePrepared;
import org.basex.query.func.sql.SqlInit;
import org.basex.query.func.sql.SqlPrepare;
import org.basex.query.func.sql.SqlRollback;
import org.basex.query.func.stream.StreamIsStreamable;
import org.basex.query.func.stream.StreamMaterialize;
import org.basex.query.func.strings.StringsColognePhonetic;
import org.basex.query.func.strings.StringsLevenshtein;
import org.basex.query.func.strings.StringsSoundex;
import org.basex.query.func.unit.UnitAssert;
import org.basex.query.func.unit.UnitAssertEquals;
import org.basex.query.func.unit.UnitFail;
import org.basex.query.func.user.UserAlter;
import org.basex.query.func.user.UserCheck;
import org.basex.query.func.user.UserCreate;
import org.basex.query.func.user.UserCurrent;
import org.basex.query.func.user.UserDrop;
import org.basex.query.func.user.UserExists;
import org.basex.query.func.user.UserGrant;
import org.basex.query.func.user.UserInfo;
import org.basex.query.func.user.UserList;
import org.basex.query.func.user.UserListDetails;
import org.basex.query.func.user.UserPassword;
import org.basex.query.func.user.UserUpdateInfo;
import org.basex.query.func.util.UtilDeepEqual;
import org.basex.query.func.util.UtilItemAt;
import org.basex.query.func.util.UtilItemRange;
import org.basex.query.func.util.UtilLastFrom;
import org.basex.query.func.validate.ValidateDtd;
import org.basex.query.func.validate.ValidateDtdInfo;
import org.basex.query.func.validate.ValidateDtdReport;
import org.basex.query.func.validate.ValidateRng;
import org.basex.query.func.validate.ValidateRngInfo;
import org.basex.query.func.validate.ValidateRngReport;
import org.basex.query.func.validate.ValidateXsd;
import org.basex.query.func.validate.ValidateXsdInfo;
import org.basex.query.func.validate.ValidateXsdReport;
import org.basex.query.func.web.WebContentType;
import org.basex.query.func.web.WebCreateUrl;
import org.basex.query.func.web.WebDecodeUrl;
import org.basex.query.func.web.WebEncodeUrl;
import org.basex.query.func.web.WebRedirect;
import org.basex.query.func.web.WebResponseHeader;
import org.basex.query.func.xquery.XQueryEval;
import org.basex.query.func.xquery.XQueryForkJoin;
import org.basex.query.func.xquery.XQueryInvoke;
import org.basex.query.func.xquery.XQueryParse;
import org.basex.query.func.xquery.XQueryParseUri;
import org.basex.query.func.xquery.XQueryUpdate;
import org.basex.query.func.xslt.XsltProcessor;
import org.basex.query.func.xslt.XsltTransform;
import org.basex.query.func.xslt.XsltTransformText;
import org.basex.query.func.xslt.XsltVersion;
import org.basex.query.func.zip.ZipBinaryEntry;
import org.basex.query.func.zip.ZipEntries;
import org.basex.query.func.zip.ZipHtmlEntry;
import org.basex.query.func.zip.ZipTextEntry;
import org.basex.query.func.zip.ZipUpdateEntries;
import org.basex.query.func.zip.ZipXmlEntry;
import org.basex.query.func.zip.ZipZipFile;
import org.basex.query.util.NSGlobal;
import org.basex.query.util.list.AnnList;
import org.basex.query.value.item.QNm;
import org.basex.query.value.type.FuncType;
import org.basex.query.value.type.SeqType;
import org.basex.util.InputInfo;
import org.basex.util.Reflect;
import org.basex.util.Strings;
import org.basex.util.Token;
import org.basex.util.TokenBuilder;
import org.basex.util.hash.TokenSet;

/**
 * Definitions of all built-in XQuery functions.
 * New namespace mappings for function prefixes and URIs must be added to the static initializer of
 * the {@link NSGlobal} class.
 *
 * @author BaseX Team 2005-16, BSD License
 * @author Christian Gruen
 */
public enum Function {

  // Standard functions

  /** XQuery function. */
  ABS(FnAbs.class, "abs(num)", arg(NUM_ZO), NUM_ZO),
  /** XQuery function. */
  ADJUST_DATE_TO_TIMEZONE(FnAdjustDateToTimezone.class, "adjust-date-to-timezone(date[,zone])",
      arg(DAT_ZO, DTD_ZO), DAT_ZO),
  /** XQuery function. */
  ADJUST_DATETIME_TO_TIMEZONE(FnAdustDateTimeToTimezone.class,
      "adjust-dateTime-to-timezone(date[,zone])", arg(DTM_ZO, DTD_ZO), DTM_ZO),
  /** XQuery function. */
  ADJUST_TIME_TO_TIMEZONE(FnAdjustTimeToTimezone.class, "adjust-time-to-timezone(date[,zone])",
      arg(TIM_ZO, DTD_ZO), TIM_ZO),
  /** XQuery function. */
  ANALYZE_STRING(FnAnalyzeString.class, "analyze-string(input,pattern[,mod])",
      arg(STR_ZO, STR, STR), ELM, flag(CNS)),
  /** XQuery function. */
  APPLY(FnApply.class, "apply(function,args)", arg(FUN_O, ARRAY_O), ITEM_ZM, flag(HOF)),
  /** XQuery function. */
  AVAILABLE_ENVIRONMENT_VARIABLES(FnAvailableEnvironmentVariables.class,
      "available-environment-variables()", arg(), STR_ZM),
  /** XQuery function. */
  AVG(FnAvg.class, "avg(items)", arg(AAT_ZM), AAT_ZO),
  /** XQuery function. */
  BASE_URI(FnBaseUri.class, "base-uri([node])", arg(NOD_ZO), URI_ZO),
  /** XQuery function. */
  BOOLEAN(FnBoolean.class, "boolean(items)", arg(ITEM_ZM), BLN),
  /** XQuery function. */
  CEILING(FnCeiling.class, "ceiling(num)", arg(NUM_ZO), NUM_ZO),
  /** XQuery function. */
  CODEPOINT_EQUAL(FnCodepointEqual.class, "codepoint-equal(string1,string2)",
      arg(STR_ZO, STR_ZO), BLN_ZO),
  /** XQuery function. */
  CODEPOINTS_TO_STRING(FnCodepointsToString.class, "codepoints-to-string(nums)", arg(ITR_ZM), STR),
  /** XQuery function. */
  COLLECTION(FnCollection.class, "collection([uri])", arg(STR_ZO), DOC_ZM),
  /** XQuery function. */
  COMPARE(FnCompare.class, "compare(first,second[,collation])", arg(STR_ZO, STR_ZO, STR), ITR_ZO),
  /** XQuery function. */
  CONCAT(FnConcat.class, "concat(atom1,atom2[,...])", arg(AAT_ZO, AAT_ZO), STR),
  /** XQuery function. */
  CONTAINS(FnContains.class, "contains(string,sub[,collation])", arg(STR_ZO, STR_ZO, STR), BLN),
  /** XQuery function. */
  CONTAINS_TOKEN(FnContainsToken.class, "contains-token(strings,token[,collation])",
      arg(STR_ZM, STR, STR), BLN),
  /** XQuery function. */
  COUNT(FnCount.class, "count(items)", arg(ITEM_ZM), ITR),
  /** XQuery function. */
  CURRENT_DATE(FnCurrentDate.class, "current-date()", arg(), DAT),
  /** XQuery function. */
  CURRENT_DATETIME(FnCurrentDateTime.class, "current-dateTime()", arg(), DTM),
  /** XQuery function. */
  CURRENT_TIME(FnCurrentTime.class, "current-time()", arg(), TIM),
  /** XQuery function. */
  DATA(FnData.class, "data([items])", arg(ITEM_ZM), AAT_ZM),
  /** XQuery function. */
  DATETIME(FnDateTime.class, "dateTime(date,time)", arg(DAT_ZO, TIM_ZO), DTM_ZO),
  /** XQuery function. */
  DAY_FROM_DATE(FnDayFromDate.class, "day-from-date(date)", arg(DAT_ZO), ITR_ZO),
  /** XQuery function. */
  DAY_FROM_DATETIME(FnDayFromDateTime.class, "day-from-dateTime(datetime)", arg(DTM_ZO), ITR_ZO),
  /** XQuery function. */
  DAYS_FROM_DURATION(FnDayFromDuration.class, "days-from-duration(duration)", arg(DUR_ZO), ITR_ZO),
  /** XQuery function. */
  DEEP_EQUAL(FnDeepEqual.class, "deep-equal(items1,items2[,collation])",
      arg(ITEM_ZM, ITEM_ZM, STR), BLN),
  /** XQuery function. */
  DEFAULT_COLLATION(FnDefaultCollation.class, "default-collation()", arg(), STR),
  /** XQuery function. */
  DEFAULT_LANGUAGE(FnDefaultLanguage.class, "default-language()", arg(), LAN),
  /** XQuery function. */
  DISTINCT_VALUES(FnDistinctValues.class, "distinct-values(items[,collation])",
      arg(AAT_ZM, STR), AAT_ZM),
  /** XQuery function. */
  DOC(FnDoc.class, "doc(uri)", arg(STR_ZO), DOC_ZO),
  /** XQuery function. */
  DOC_AVAILABLE(FnDocAvailable.class, "doc-available(uri)", arg(STR_ZO), BLN),
  /** XQuery function. */
  DOCUMENT_URI(FnDocumentUri.class, "document-uri([node])", arg(NOD_ZO), URI_ZO),
  /** XQuery function. */
  ELEMENT_WITH_ID(FnElementWithId.class, "element-with-id(string[,node])",
      arg(STR_ZM, NOD), ELM_ZM),
  /** XQuery function. */
  EMPTY(FnEmpty.class, "empty(items)", arg(ITEM_ZM), BLN),
  /** XQuery function. */
  ENCODE_FOR_URI(FnEncodeForUri.class, "encode-for-uri(string)", arg(STR_ZO), STR),
  /** XQuery function. */
  ENDS_WITH(FnEndsWith.class, "ends-with(string,sub[,collation])", arg(STR_ZO, STR_ZO, STR), BLN),
  /** XQuery function. */
  ENVIRONMENT_VARIABLE(FnEnvironmentVariable.class, "environment-variable(string)",
      arg(STR), STR_ZO),
  /** XQuery function. */
  ERROR(FnError.class, "error([code[,desc[,object]]])",
      arg(QNM_ZO, STR, ITEM_ZM), ITEM_ZM, flag(NDT)),
  /** XQuery function. */
  ESCAPE_HTML_URI(FnEscapeHtmlUri.class, "escape-html-uri(string)", arg(STR_ZO), STR),
  /** XQuery function. */
  EXACTLY_ONE(FnExactlyOne.class, "exactly-one(items)", arg(ITEM_ZM), ITEM),
  /** XQuery function. */
  EXISTS(FnExists.class, "exists(items)", arg(ITEM_ZM), BLN),
  /** XQuery function. */
  FALSE(FnFalse.class, "false()", arg(), BLN),
  /** XQuery function. */
  FILTER(FnFilter.class, "filter(items,function)",
      arg(ITEM_ZM, FuncType.get(BLN, ITEM).seqType()), ITEM_ZM, flag(HOF)),
  /** XQuery function. */
  FLOOR(FnFloor.class, "floor(num)", arg(NUM_ZO), NUM_ZO),
  /** XQuery function. */
  FOLD_LEFT(FnFoldLeft.class, "fold-left(items,zero,function)",
      arg(ITEM_ZM, ITEM_ZM, FuncType.get(ITEM_ZM, ITEM_ZM, ITEM).seqType()),
      ITEM_ZM, flag(HOF)),
  /** XQuery function. */
  FOLD_RIGHT(FnFoldRight.class, "fold-right(items,zero,function)",
      arg(ITEM_ZM, ITEM_ZM, FuncType.get(ITEM_ZM, ITEM, ITEM_ZM).seqType()),
      ITEM_ZM, flag(HOF)),
  /** XQuery function. */
  FOR_EACH(FnForEach.class, "for-each(items,function)",
      arg(ITEM_ZM, FuncType.get(ITEM_ZM, ITEM).seqType()), ITEM_ZM, flag(HOF)),
  /** XQuery function. */
  FOR_EACH_PAIR(FnForEachPair.class, "for-each-pair(items1,items2,function)",
      arg(ITEM_ZM, ITEM_ZM, FuncType.get(ITEM_ZM, ITEM, ITEM).seqType()),
      ITEM_ZM, flag(HOF)),
  /** XQuery function. */
  FORMAT_DATE(FnFormatDate.class, "format-date(date,picture,[language,calendar,place])",
      arg(DAT_ZO, STR, STR_ZO, STR_ZO, STR_ZO), STR_ZO),
  /** XQuery function. */
  FORMAT_DATETIME(FnFormatDateTime.class,
      "format-dateTime(number,picture,[language,calendar,place])",
      arg(DTM_ZO, STR, STR_ZO, STR_ZO, STR_ZO), STR_ZO),
  /** XQuery function. */
  FORMAT_INTEGER(FnFormatInteger.class, "format-integer(number,picture[,language])",
      arg(ITR_ZO, STR, STR), STR),
  /** XQuery function. */
  FORMAT_NUMBER(FnFormatNumber.class, "format-number(number,picture[,format])",
      arg(NUM_ZO, STR, STR_ZO), STR),
  /** XQuery function. */
  FORMAT_TIME(FnFormatTime.class, "format-time(number,picture,[language,calendar,place])",
      arg(TIM_ZO, STR, STR_ZO, STR_ZO, STR_ZO), STR_ZO),
  /** XQuery function. */
  FUNCTION_ARITY(FnFunctionArity.class, "function-arity(function)", arg(FUN_O), ITR),
  /** XQuery function. */
  FUNCTION_LOOKUP(FnFunctionLookup.class, "function-lookup(name,arity)",
      arg(QNM, ITR), FUN_OZ, flag(CTX, Flag.POS, NDT, HOF)),
  /** XQuery function. */
  FUNCTION_NAME(FnFunctionName.class, "function-name(function)", arg(FUN_O), QNM_ZO),
  /** XQuery function. */
  GENERATE_ID(FnGenerateId.class, "generate-id([node])", arg(NOD_ZO), STR),
  /** XQuery function. */
  HAS_CHILDREN(FnHasChildren.class, "has-children([node])", arg(NOD_ZM), BLN),
  /** XQuery function. */
  HEAD(FnHead.class, "head(items)", arg(ITEM_ZM), ITEM_ZO),
  /** XQuery function. */
  HOURS_FROM_DATETIME(FnHoursFromDateTime.class, "hours-from-dateTime(datetime)",
      arg(DTM_ZO), ITR_ZO),
  /** XQuery function. */
  HOURS_FROM_DURATION(FnHoursFromDuration.class, "hours-from-duration(duration)",
      arg(DUR_ZO), ITR_ZO),
  /** XQuery function. */
  HOURS_FROM_TIME(FnHoursFromTime.class, "hours-from-time(time)", arg(TIM_ZO), ITR_ZO),
  /** XQuery function. */
  ID(FnId.class, "id(ids[,node])", arg(STR_ZM, NOD), ELM_ZM),
  /** XQuery function. */
  IDREF(FnIdref.class, "idref(ids[,node])", arg(STR_ZM, NOD), NOD_ZM),
  /** XQuery function. */
  IMPLICIT_TIMEZONE(FnImplicitTimezone.class, "implicit-timezone()", arg(), DTD),
  /** XQuery function. */
  IN_SCOPE_PREFIXES(FnInScopePrefixes.class, "in-scope-prefixes(elem)", arg(ELM), STR_ZM),
  /** XQuery function. */
  INDEX_OF(FnIndexOf.class, "index-of(items,item[,collation])", arg(AAT_ZM, AAT, STR), ITR_ZM),
  /** XQuery function. */
  INNERMOST(FnInnermost.class, "innermost(nodes)", arg(NOD_ZM), NOD_ZM),
  /** XQuery function. */
  INSERT_BEFORE(FnInsertBefore.class, "insert-before(items,pos,insert)",
      arg(ITEM_ZM, ITR, ITEM_ZM), ITEM_ZM),
  /** XQuery function. */
  IRI_TO_URI(FnIriToUri.class, "iri-to-uri(string)", arg(STR_ZO), STR),
  /** XQuery function. */
  JSON_DOC(FnJsonDoc.class, "json-doc(uri[,options])", arg(STR_ZO, MAP_O), DOC_ZO),
  /** XQuery function. */
  LANG(FnLang.class, "lang(ids[,node])", arg(STR_ZO, NOD), BLN),
  /** XQuery function. */
  LAST(FnLast.class, "last()", arg(), ITR, flag(Flag.POS, CTX)),
  /** XQuery function. */
  LOCAL_NAME(FnLocalName.class, "local-name([node])", arg(NOD_ZO), STR),
  /** XQuery function. */
  LOCAL_NAME_FROM_QNAME(FnLocalNameFromQName.class, "local-name-from-QName(qname)",
      arg(QNM_ZO), NCN_ZO),
  /** XQuery function. */
  LOWER_CASE(FnLowerCase.class, "lower-case(string)", arg(STR_ZO), STR),
  /** XQuery function. */
  MATCHES(FnMatches.class, "matches(string,pattern[,mod])", arg(STR_ZO, STR, STR), BLN),
  /** XQuery function. */
  MAX(FnMax.class, "max(items[,collation])", arg(AAT_ZM, STR), AAT_ZO),
  /** XQuery function. */
  MIN(FnMin.class, "min(items[,collation])", arg(AAT_ZM, STR), AAT_ZO),
  /** XQuery function. */
  MINUTES_FROM_DATETIME(FnMinutesFromDateTime.class, "minutes-from-dateTime(datetime)",
      arg(DTM_ZO), ITR_ZO),
  /** XQuery function. */
  MINUTES_FROM_DURATION(FnMinutesFromDuration.class, "minutes-from-duration(duration)",
      arg(DUR_ZO), ITR_ZO),
  /** XQuery function. */
  MINUTES_FROM_TIME(FnMinutesFromTime.class, "minutes-from-time(time)", arg(TIM_ZO), ITR_ZO),
  /** XQuery function. */
  MONTH_FROM_DATE(FnMonthFromDate.class, "month-from-date(date)", arg(DAT_ZO), ITR_ZO),
  /** XQuery function. */
  MONTH_FROM_DATETIME(FnMonthFromDateTime.class, "month-from-dateTime(datetime)",
      arg(DTM_ZO), ITR_ZO),
  /** XQuery function. */
  MONTHS_FROM_DURATION(FnMonthsFromDuration.class, "months-from-duration(duration)",
      arg(DUR_ZO), ITR_ZO),
  /** XQuery function. */
  NAME(FnName.class, "name([node])", arg(NOD_ZO), STR),
  /** XQuery function. */
  NAMESPACE_URI(FnNamespaceUri.class, "namespace-uri([node])", arg(NOD_ZO), URI),
  /** XQuery function. */
  NAMESPACE_URI_FOR_PREFIX(FnNamespaceUriForPrefix.class, "namespace-uri-for-prefix(pref,elem)",
      arg(STR_ZO, ELM), URI_ZO),
  /** XQuery function. */
  NAMESPACE_URI_FROM_QNAME(FnNamespaceUriFromQName.class, "namespace-uri-from-QName(qname)",
      arg(QNM_ZO), URI_ZO),
  /** XQuery function. */
  NILLED(FnNilled.class, "nilled([node])", arg(NOD_ZO), BLN_ZO),
  /** XQuery function. */
  NODE_NAME(FnNodeName.class, "node-name([node])", arg(NOD_ZO), QNM_ZO),
  /** XQuery function. */
  NORMALIZE_SPACE(FnNormalizeSpace.class, "normalize-space([string])", arg(STR_ZO), STR),
  /** XQuery function. */
  NORMALIZE_UNICODE(FnNormalizeUnicode.class, "normalize-unicode(string[,form])",
      arg(STR_ZO, STR), STR),
  /** XQuery function. */
  NOT(FnNot.class, "not(items)", arg(ITEM_ZM), BLN),
  /** XQuery function. */
  NUMBER(FnNumber.class, "number([item])", arg(AAT_ZO), DBL),
  /** XQuery function. */
  ONE_OR_MORE(FnOneOrMore.class, "one-or-more(items)", arg(ITEM_ZM), ITEM_OM),
  /** XQuery function. */
  OUTERMOST(FnOutermost.class, "outermost(nodes)", arg(NOD_ZM), NOD_ZM),
  /** XQuery function. */
  PARSE_IETF_DATE(FnParseIetfDate.class, "parse-ietf-date(string)", arg(STR_ZO), DTM_ZO),
  /** XQuery function. */
  JSON_TO_XML(FnJsonToXml.class, "json-to-xml(string[,options])", arg(STR_ZO, MAP_O), NOD_ZO),
  /** XQuery function. */
  XML_TO_JSON(FnXmlToJson.class, "xml-to-json(node[,options])", arg(NOD_ZO, MAP_O), STR_ZO),
  /** XQuery function. */
  PARSE_JSON(FnParseJson.class, "parse-json(string[,options])", arg(STR_ZO, MAP_O), ITEM_ZO),
  /** XQuery function. */
  PARSE_XML(FnParseXml.class, "parse-xml(string)", arg(STR_ZO), DOC_O, flag(CNS)),
  /** XQuery function. */
  PARSE_XML_FRAGMENT(FnParseXmlFragment.class, "parse-xml-fragment(string)", arg(STR_ZO), DOC_ZO,
      flag(CNS)),
  /** XQuery function. */
  PATH(FnPath.class, "path([node])", arg(NOD_ZO), STR_ZO),
  /** XQuery function. */
  POSITION(FnPosition.class, "position()", arg(), ITR, flag(Flag.POS, CTX)),
  /** XQuery function. */
  PREFIX_FROM_QNAME(FnPrefixFromQName.class, "prefix-from-QName(qname)", arg(QNM_ZO), NCN_ZO),
  /** XQuery function. */
  PUT(FnPut.class, "put(node,uri)", arg(NOD, STR_ZO), EMP, flag(UPD)),
  /** XQuery function. */
  QNAME(FnQName.class, "QName(uri,name)", arg(STR_ZO, STR), QNM),
  /** XQuery function. */
  RANDOM_NUMBER_GENERATOR(FnRandomNumberGenerator.class, "random-number-generator([seed])",
      arg(AAT), MAP_O),
  /** XQuery function. */
  REMOVE(FnRemove.class, "remove(items,pos)", arg(ITEM_ZM, ITR), ITEM_ZM),
  /** XQuery function. */
  REPLACE(FnReplace.class, "replace(string,pattern,replace[,mod])",
      arg(STR_ZO, STR, STR, STR), STR),
  /** XQuery function. */
  RESOLVE_QNAME(FnResolveQName.class, "resolve-QName(name,base)", arg(STR_ZO, ELM), QNM_ZO),
  /** XQuery function. */
  RESOLVE_URI(FnResolveUri.class, "resolve-uri(name[,elem])", arg(STR_ZO, STR), URI_ZO),
  /** XQuery function. */
  REVERSE(FnReverse.class, "reverse(items)", arg(ITEM_ZM), ITEM_ZM),
  /** XQuery function. */
  ROOT(FnRoot.class, "root([node])", arg(NOD_ZO), NOD_ZO),
  /** XQuery function. */
  ROUND(FnRound.class, "round(num[,prec])", arg(NUM_ZO, ITR), NUM_ZO),
  /** XQuery function. */
  ROUND_HALF_TO_EVEN(FnRoundHalfToEven.class, "round-half-to-even(num[,prec])",
      arg(NUM_ZO, ITR), NUM_ZO),
  /** XQuery function. */
  SECONDS_FROM_DATETIME(FnSecondsFromDateTime.class, "seconds-from-dateTime(datetime)",
      arg(DTM_ZO), DEC_ZO),
  /** XQuery function. */
  SECONDS_FROM_DURATION(FnSecondsFromDuration.class, "seconds-from-duration(duration)",
      arg(DUR_ZO), DEC_ZO),
  /** XQuery function. */
  SECONDS_FROM_TIME(FnSecondsFromTime.class, "seconds-from-time(time)", arg(TIM_ZO), DEC_ZO),
  /** XQuery function. */
  SERIALIZE(FnSerialize.class, "serialize(items[,params])", arg(ITEM_ZM, ITEM_ZO), STR),
  /** XQuery function. */
  SORT(FnSort.class, "sort(items[,key-func])",
      arg(ITEM_ZM, FuncType.arity(1).seqType()), ITEM_ZM, flag(HOF)),
  /** XQuery function. */
  STARTS_WITH(FnStartsWith.class, "starts-with(string,sub[,collation])",
      arg(STR_ZO, STR_ZO, STR), BLN),
  /** XQuery function. */
  STATIC_BASE_URI(FnStaticBaseUri.class, "static-base-uri()", arg(), URI_ZO),
  /** XQuery function. */
  STRING(FnString.class, "string([item])", arg(ITEM_ZO), STR),
  /** XQuery function. */
  STRING_JOIN(FnStringJoin.class, "string-join(items[,sep])", arg(ITEM_ZM, STR), STR),
  /** XQuery function. */
  STRING_LENGTH(FnStringLength.class, "string-length([string])", arg(STR_ZO), ITR),
  /** XQuery function. */
  STRING_TO_CODEPOINTS(FnStringToCodepoints.class, "string-to-codepoints(string)",
      arg(STR_ZO), ITR_ZM),
  /** XQuery function. */
  SUBSEQUENCE(FnSubsequence.class, "subsequence(items,first[,len])",
      arg(ITEM_ZM, DBL, DBL), ITEM_ZM),
  /** XQuery function. */
  SUBSTRING(FnSubstring.class, "substring(string,start[,len])", arg(STR_ZO, DBL, DBL), STR),
  /** XQuery function. */
  SUBSTRING_AFTER(FnSubstringAfter.class, "substring-after(string,sub[,collation])",
      arg(STR_ZO, STR_ZO, STR), STR),
  /** XQuery function. */
  SUBSTRING_BEFORE(FnSubstringBefore.class, "substring-before(string,sub[,collation])",
      arg(STR_ZO, STR_ZO, STR), STR),
  /** XQuery function. */
  SUM(FnSum.class, "sum(items[,zero])", arg(AAT_ZM, AAT_ZO), AAT_ZO),
  /** XQuery function. */
  TAIL(FnTail.class, "tail(items)", arg(ITEM_ZM), ITEM_ZM),
  /** XQuery function. */
  TIMEZONE_FROM_DATE(FnTimezoneFromDate.class, "timezone-from-date(date)", arg(DAT_ZO), DTD_ZO),
  /** XQuery function. */
  TIMEZONE_FROM_DATETIME(FnTimezoneFromDateTime.class, "timezone-from-dateTime(dateTime)",
      arg(DTM_ZO), DTD_ZO),
  /** XQuery function. */
  TIMEZONE_FROM_TIME(FnTimezoneFromTime.class, "timezone-from-time(time)", arg(TIM_ZO), DTD_ZO),
  /** XQuery function. */
  TOKENIZE(FnTokenize.class, "tokenize(string[,pattern[,mod]])", arg(STR_ZO, STR, STR), STR_ZM),
  /** XQuery function. */
  TRACE(FnTrace.class, "trace(value[,label])", arg(ITEM_ZM, STR), ITEM_ZM, flag(NDT)),
  /** XQuery function. */
  TRANSLATE(FnTranslate.class, "translate(string,map,trans)", arg(STR_ZO, STR, STR), STR),
  /** XQuery function. */
  TRUE(FnTrue.class, "true()", arg(), BLN),
  /** XQuery function. */
  UNORDERED(FnUnordered.class, "unordered(items)", arg(ITEM_ZM), ITEM_ZM),
  /** XQuery function. */
  UNPARSED_TEXT(FnUnparsedText.class, "unparsed-text(uri[,encoding])", arg(STR_ZO, STR), STR_ZO),
  /** XQuery function. */
  UNPARSED_TEXT_AVAILABLE(FnUnparsedTextAvailable.class, "unparsed-text-available(uri[,encoding])",
      arg(STR_ZO, STR), BLN),
  /** XQuery function. */
  UNPARSED_TEXT_LINES(FnUnparsedTextLines.class, "unparsed-text-lines(uri[,encoding])",
      arg(STR_ZO, STR), STR_ZM),
  /** XQuery function. */
  UPPER_CASE(FnUpperCase.class, "upper-case(string)", arg(STR_ZO), STR),
  /** XQuery function. */
  URI_COLLECTION(FnUriCollection.class, "uri-collection([uri])", arg(STR_ZO), URI_ZM),
  /** XQuery function. */
  YEAR_FROM_DATE(FnYearFromDate.class, "year-from-date(date)", arg(DAT_ZO), ITR_ZO),
  /** XQuery function. */
  YEAR_FROM_DATETIME(FnYearFromDateTime.class, "year-from-dateTime(datetime)", arg(DTM_ZO), ITR_ZO),
  /** XQuery function. */
  YEARS_FROM_DURATION(FnYearsFromDuration.class, "years-from-duration(duration)",
      arg(DUR_ZO), ITR_ZO),
  /** XQuery function. */
  ZERO_OR_ONE(FnZeroOrOne.class, "zero-or-one(items)", arg(ITEM_ZM), ITEM_ZO),

  /* Map Module. */

  /** XQuery function. */
  _MAP_MERGE(MapMerge.class, "merge(maps)", arg(MAP_ZM), MAP_O, MAP_URI),
  /** XQuery function. */
  _MAP_PUT(MapPut.class, "put(map,key,value)", arg(MAP_O, AAT, ITEM_ZM), MAP_O, MAP_URI),
  /** XQuery function. */
  _MAP_ENTRY(MapEntry.class, "entry(key,value)", arg(AAT, ITEM_ZM), MAP_O, MAP_URI),
  /** XQuery function. */
  _MAP_GET(MapGet.class, "get(map,key)", arg(MAP_O, AAT), ITEM_ZM, MAP_URI),
  /** XQuery function. */
  _MAP_CONTAINS(MapContains.class, "contains(map,key)", arg(MAP_O, AAT), BLN, MAP_URI),
  /** XQuery function. */
  _MAP_REMOVE(MapRemove.class, "remove(map,keys)", arg(MAP_O, AAT_ZM), MAP_O, MAP_URI),
  /** XQuery function. */
  _MAP_SIZE(MapSize.class, "size(map)", arg(MAP_O), ITR, MAP_URI),
  /** XQuery function. */
  _MAP_KEYS(MapKeys.class, "keys(map)", arg(MAP_O), AAT_ZM, MAP_URI),
  /** XQuery function. */
  _MAP_FOR_EACH(MapForEach.class, "for-each(map,function)",
      arg(MAP_O, FuncType.get(ITEM_ZM, AAT, ITEM_ZM).seqType()), ITEM_ZM, flag(HOF), MAP_URI),

  /* Array Module. */

  /** XQuery function. */
  _ARRAY_SIZE(ArraySize.class, "size(array)", arg(ARRAY_O), ITR, ARRAY_URI),
  /** XQuery function. */
  _ARRAY_GET(ArrayGet.class, "get(array,pos)", arg(ARRAY_O, ITR), ITEM_ZM, ARRAY_URI),
  /** XQuery function. */
  _ARRAY_APPEND(ArrayAppend.class, "append(array,value)",
      arg(ARRAY_O, ITEM_ZM), ARRAY_O, ARRAY_URI),
  /** XQuery function. */
  _ARRAY_SUBARRAY(ArraySubarray.class, "subarray(array,pos[,length])", arg(ARRAY_O, ITR, ITR),
      ARRAY_O, ARRAY_URI),
  /** XQuery function. */
  _ARRAY_REMOVE(ArrayRemove.class, "remove(array,pos)", arg(ARRAY_O, ITR_ZM), ARRAY_O, ARRAY_URI),
  /** XQuery function. */
  _ARRAY_PUT(ArrayPut.class, "put(array,pos,member)", arg(ARRAY_O, ITR, ITEM_ZM),
      ARRAY_O, ARRAY_URI),
  /** XQuery function. */
  _ARRAY_INSERT_BEFORE(ArrayInsertBefore.class, "insert-before(array,pos,value)",
      arg(ARRAY_O, ITR, ITEM_ZO), ARRAY_O, ARRAY_URI),
  /** XQuery function. */
  _ARRAY_HEAD(ArrayHead.class, "head(array)", arg(ARRAY_O), ITEM_ZM, ARRAY_URI),
  /** XQuery function. */
  _ARRAY_TAIL(ArrayTail.class, "tail(array)", arg(ARRAY_O), ARRAY_O, ARRAY_URI),
  /** XQuery function. */
  _ARRAY_REVERSE(ArrayReverse.class, "reverse(array)", arg(ARRAY_O), ARRAY_O, ARRAY_URI),
  /** XQuery function. */
  _ARRAY_JOIN(ArrayJoin.class, "join(array)", arg(ARRAY_ZM), ARRAY_O, ARRAY_URI),
  /** XQuery function. */
  _ARRAY_FLATTEN(ArrayFlatten.class, "flatten(item()*)", arg(ITEM_ZM), ITEM_ZM, ARRAY_URI),
  /** XQuery function. */
  _ARRAY_FOR_EACH(ArrayForEach.class, "for-each(array,function)",
      arg(ARRAY_O, FuncType.get(ITEM_ZM, ITEM_ZM).seqType()), ARRAY_O, flag(HOF),
      ARRAY_URI),
  /** XQuery function. */
  _ARRAY_FILTER(ArrayFilter.class, "filter(array,function)",
      arg(ARRAY_O, FuncType.get(BLN, ITEM_ZM).seqType()), ARRAY_O, flag(HOF), ARRAY_URI),
  /** XQuery function. */
  _ARRAY_FOLD_LEFT(ArrayFoldLeft.class, "fold-left(array,zero,function)",
      arg(ARRAY_O, ITEM_ZM, FuncType.get(ITEM_ZM, ITEM_ZM, ITEM_ZM).seqType()), ITEM_ZM,
      flag(HOF), ARRAY_URI),
  /** XQuery function. */
  _ARRAY_FOLD_RIGHT(ArrayFoldRight.class, "fold-right(array,zero,function)",
      arg(ARRAY_O, ITEM_ZM, FuncType.get(ITEM_ZM, ITEM_ZM, ITEM_ZM).seqType()), ITEM_ZM,
      flag(HOF), ARRAY_URI),
  /** XQuery function. */
  _ARRAY_FOR_EACH_PAIR(ArrayForEachPair.class, "for-each-pair(array1,array2,function)",
      arg(ARRAY_O, ARRAY_O, FuncType.get(ITEM_ZM, ITEM_ZM, ITEM_ZM).seqType()), ARRAY_O,
      flag(HOF), ARRAY_URI),
  /** XQuery function. */
  _ARRAY_SORT(ArraySort.class, "sort(array[,key-func])",
      arg(ARRAY_O, FuncType.arity(1).seqType()), ARRAY_O, flag(HOF), ARRAY_URI),

  /* Math Module. */

  /** XQuery function. */
  _MATH_SQRT(MathSqrt.class, "sqrt(number)", arg(DBL_ZO), DBL_ZO, MATH_URI),
  /** XQuery function. */
  _MATH_SIN(MathSin.class, "sin(number)", arg(DBL_ZO), DBL_ZO, MATH_URI),
  /** XQuery function. */
  _MATH_COS(MathCos.class, "cos(number)", arg(DBL_ZO), DBL_ZO, MATH_URI),
  /** XQuery function. */
  _MATH_TAN(MathTan.class, "tan(number)", arg(DBL_ZO), DBL_ZO, MATH_URI),
  /** XQuery function. */
  _MATH_ASIN(MathAsin.class, "asin(number)", arg(DBL_ZO), DBL_ZO, MATH_URI),
  /** XQuery function. */
  _MATH_ACOS(MathAcos.class, "acos(number)", arg(DBL_ZO), DBL_ZO, MATH_URI),
  /** XQuery function. */
  _MATH_ATAN(MathAtan.class, "atan(number)", arg(DBL_ZO), DBL_ZO, MATH_URI),
  /** XQuery function. */
  _MATH_ATAN2(MathAtan2.class, "atan2(number1,number2)", arg(DBL, DBL), DBL, MATH_URI),
  /** XQuery function. */
  _MATH_POW(MathPow.class, "pow(number1,number2)", arg(DBL_ZO, NUM), DBL_ZO, MATH_URI),
  /** XQuery function. */
  _MATH_EXP(MathExp.class, "exp(number)", arg(DBL_ZO), DBL_ZO, MATH_URI),
  /** XQuery function. */
  _MATH_EXP10(MathExp10.class, "exp10(number)", arg(DBL_ZO), DBL_ZO, MATH_URI),
  /** XQuery function. */
  _MATH_LOG(MathLog.class, "log(number)", arg(DBL_ZO), DBL_ZO, MATH_URI),
  /** XQuery function. */
  _MATH_LOG10(MathLog10.class, "log10(number)", arg(DBL_ZO), DBL_ZO, MATH_URI),
  /** XQuery function. */
  _MATH_PI(MathPi.class, "pi()", arg(), DBL, MATH_URI),

  /** XQuery function. */
  _MATH_SINH(MathSinh.class, "sinh(number)", arg(DBL_ZO), DBL_ZO, MATH_URI),
  /** XQuery function. */
  _MATH_COSH(MathCosh.class, "cosh(number)", arg(DBL_ZO), DBL_ZO, MATH_URI),
  /** XQuery function. */
  _MATH_TANH(MathTanh.class, "tanh(number)", arg(DBL_ZO), DBL_ZO, MATH_URI),
  /** XQuery function. */
  _MATH_CRC32(MathCrc32.class, "crc32(string)", arg(STR), HEX, MATH_URI),
  /** XQuery function. */
  _MATH_E(MathE.class, "e()", arg(), DBL, MATH_URI),

  /* Admin Module. */

  /** XQuery function. */
  _ADMIN_SESSIONS(AdminSessions.class, "sessions()", arg(), ELM_ZM, flag(NDT), ADMIN_URI),
  /** XQuery function. */
  _ADMIN_LOGS(AdminLogs.class, "logs([date[,merge]])", arg(STR, BLN), ELM_ZM, flag(NDT), ADMIN_URI),
  /** XQuery function. */
  _ADMIN_WRITE_LOG(AdminWriteLog.class, "write-log(message[,type])",
      arg(STR, STR), EMP, flag(NDT), ADMIN_URI),
  /** XQuery function. */
  _ADMIN_DELETE_LOGS(AdminDeleteLogs.class, "delete-logs(date)",
      arg(STR), EMP, flag(NDT), ADMIN_URI),

  /* Archive Module. */

  /** XQuery function. */
  _ARCHIVE_CREATE(ArchiveCreate.class, "create(entries,contents[,options])",
      arg(ITEM_ZM, ITEM_ZM, MAP_O), B64, ARCHIVE_URI),
  /** XQuery function. */
  _ARCHIVE_CREATE_FROM(ArchiveCreateFrom.class, "create-from(path[,options[,entries]])",
      arg(STR, MAP_O, ITEM_ZM), EMP, ARCHIVE_URI),
  /** XQuery function. */
  _ARCHIVE_ENTRIES(ArchiveEntries.class, "entries(archive)", arg(B64), ELM_ZM, ARCHIVE_URI),
  /** XQuery function. */
  _ARCHIVE_EXTRACT_TEXT(ArchiveExtractText.class, "extract-text(archive[,entries[,encoding]])",
      arg(B64, ITEM_ZM, STR), STR_ZM, ARCHIVE_URI),
  /** XQuery function. */
  _ARCHIVE_EXTRACT_BINARY(ArchiveExtractBinary.class, "extract-binary(archive[,entries])",
      arg(B64, ITEM_ZM), B64_ZM, ARCHIVE_URI),
  /** XQuery function. */
  _ARCHIVE_EXTRACT_TO(ArchiveExtractTo.class, "extract-to(path,archive[,entries])",
      arg(STR, B64, ITEM_ZM), EMP, ARCHIVE_URI),
  /** XQuery function. */
  _ARCHIVE_UPDATE(ArchiveUpdate.class, "update(archive,entries,contents)",
      arg(B64, ITEM_ZM, ITEM_ZM), B64, ARCHIVE_URI),
  /** XQuery function. */
  _ARCHIVE_DELETE(ArchiveDelete.class, "delete(archive,entries)",
      arg(B64, ITEM_ZM), B64, ARCHIVE_URI),
  /** XQuery function. */
  _ARCHIVE_OPTIONS(ArchiveOptions.class, "options(archive)", arg(B64), MAP_O, ARCHIVE_URI),

  /* Binary Module. */

  /** XQuery function. */
  _BIN_HEX(BinHex.class, "hex(string)", arg(STR_ZO), B64_ZO, BIN_URI),
  /** XQuery function. */
  _BIN_BIN(BinBin.class, "bin(string)", arg(STR_ZO), B64_ZO, BIN_URI),
  /** XQuery function. */
  _BIN_OCTAL(BinOctal.class, "octal(string)", arg(STR_ZO), B64_ZO, BIN_URI),
  /** XQuery function. */
  _BIN_TO_OCTETS(BinToOctets.class, "to-octets(binary)", arg(B64_ZO), ITR_ZM, BIN_URI),
  /** XQuery function. */
  _BIN_FROM_OCTETS(BinFromOctets.class, "from-octets(integers)",
      arg(ITR_ZM), B64, BIN_URI),
  /** XQuery function. */
  _BIN_LENGTH(BinLength.class, "length(binary)", arg(B64), ITR, BIN_URI),
  /** XQuery function. */
  _BIN_PART(BinPart.class, "part(binary,offset[,size])",
      arg(B64_ZO, ITR, ITR), B64_ZO, BIN_URI),
  /** XQuery function. */
  _BIN_JOIN(BinJoin.class, "join(binaries)", arg(B64_ZM), B64, BIN_URI),
  /** XQuery function. */
  _BIN_INSERT_BEFORE(BinInsertBefore.class, "insert-before(binary,offset,extra)",
      arg(B64_ZO, ITR, B64_ZO), B64_ZO, BIN_URI),
  /** XQuery function. */
  _BIN_PAD_LEFT(BinPadLeft.class, "pad-left(binary,size[,octet])",
      arg(B64_ZO, ITR, ITR), B64_ZO, BIN_URI),
  /** XQuery function. */
  _BIN_PAD_RIGHT(BinPadRight.class, "pad-right(binary,size[,octet])",
      arg(B64_ZO, ITR, ITR), B64_ZO, BIN_URI),
  /** XQuery function. */
  _BIN_FIND(BinFind.class, "find(binary,offset,search)",
      arg(B64_ZO, ITR, B64_ZO), ITR_ZO, BIN_URI),
  /** XQuery function. */
  _BIN_DECODE_STRING(BinDecodeString.class, "decode-string(binary[,encoding[,offset[,size]]])",
      arg(B64_ZO, STR, ITR, ITR), STR_ZO, BIN_URI),
  /** XQuery function. */
  _BIN_ENCODE_STRING(BinEncodeString.class, "encode-string(string[,encoding])",
      arg(STR_ZO, STR), B64_ZO, BIN_URI),
  /** XQuery function. */
  _BIN_PACK_DOUBLE(BinPackDouble.class, "pack-double(double[,order])", arg(DBL, STR), B64, BIN_URI),
  /** XQuery function. */
  _BIN_PACK_FLOAT(BinPackFloat.class, "pack-float(float[,order])", arg(FLT, STR), B64, BIN_URI),
  /** XQuery function. */
  _BIN_PACK_INTEGER(BinPackInteger.class, "pack-integer(integer,size[,order])",
      arg(ITR, ITR, STR), B64, BIN_URI),
  /** XQuery function. */
  _BIN_UNPACK_DOUBLE(BinUnpackDouble.class, "unpack-double(binary,offset[,order])",
      arg(B64, ITR, STR), DBL, BIN_URI),
  /** XQuery function. */
  _BIN_UNPACK_FLOAT(BinUnpackFloat.class, "unpack-float(binary,offset[,order])",
      arg(B64, ITR, STR), FLT, BIN_URI),
  /** XQuery function. */
  _BIN_UNPACK_INTEGER(BinUnpackInteger.class, "unpack-integer(binary,offset,size[,order])",
      arg(B64, ITR, ITR, STR), ITR, BIN_URI),
  /** XQuery function. */
  _BIN_UNPACK_UNSIGNED_INTEGER(BinUnpackUnsignedInteger.class,
      "unpack-unsigned-integer(binary,offset,size[,order])",
      arg(B64, ITR, ITR, STR), ITR, BIN_URI),
  /** XQuery function. */
  _BIN_OR(BinOr.class, "or(binary1,binary2)", arg(B64_ZO, B64_ZO), B64_ZO, BIN_URI),
  /** XQuery function. */
  _BIN_XOR(BinXor.class, "xor(binary1,binary2)", arg(B64_ZO, B64_ZO), B64_ZO, BIN_URI),
  /** XQuery function. */
  _BIN_AND(BinAnd.class, "and(binary1,binary2)", arg(B64_ZO, B64_ZO), B64_ZO, BIN_URI),
  /** XQuery function. */
  _BIN_NOT(BinNot.class, "not(binary)", arg(B64_ZO), B64_ZO, BIN_URI),
  /** XQuery function. */
  _BIN_SHIFT(BinShift.class, "shift(binary,by)", arg(B64_ZO, ITR), B64_ZO, BIN_URI),

  /* Client Module. */

  /** XQuery function. */
  _CLIENT_CONNECT(ClientConnect.class, "connect(url,port,user,password)",
      arg(STR, ITR, STR, STR), URI, flag(NDT), CLIENT_URI),
  /** XQuery function. */
  _CLIENT_EXECUTE(ClientExecute.class, "execute(id,command)", arg(URI, STR), STR, flag(NDT),
      CLIENT_URI),
  /** XQuery function. */
  _CLIENT_INFO(ClientInfo.class, "info(id)", arg(URI), STR, flag(NDT), CLIENT_URI),
  /** XQuery function. */
  _CLIENT_QUERY(ClientQuery.class, "query(id,query[,bindings])",
      arg(URI, STR, MAP_ZO), ITEM_ZO, flag(NDT), CLIENT_URI),
  /** XQuery function. */
  _CLIENT_CLOSE(ClientClose.class, "close(id)", arg(URI), EMP, flag(NDT), CLIENT_URI),

  /* Conversion Module. */

  /** XQuery function. */
  _CONVERT_INTEGER_TO_BASE(ConvertIntegerToBase.class, "integer-to-base(number,base)",
      arg(ITR, ITR), STR, CONVERT_URI),
  /** XQuery function. */
  _CONVERT_INTEGER_FROM_BASE(ConvertIntegerFromBase.class, "integer-from-base(string,base)",
      arg(STR, ITR), ITR, CONVERT_URI),
  /** XQuery function. */
  _CONVERT_BINARY_TO_BYTES(ConvertBinaryToBytes.class, "binary-to-bytes(binary)",
      arg(AAT), BYT_ZM, CONVERT_URI),
  /** XQuery function. */
  _CONVERT_BINARY_TO_STRING(ConvertBinaryToString.class,
      "binary-to-string(binary[,encoding[,fallback]])", arg(ITEM, STR, BLN), STR, CONVERT_URI),
  /** XQuery function. */
  _CONVERT_BYTES_TO_HEX(ConvertBytesToHex.class, "bytes-to-hex(bytes)", arg(BYT_ZM), HEX,
      CONVERT_URI),
  /** XQuery function. */
  _CONVERT_BYTES_TO_BASE64(ConvertBytesToBase64.class, "bytes-to-base64(bytes)", arg(BYT_ZM), B64,
      CONVERT_URI),
  /** XQuery function. */
  _CONVERT_STRING_TO_BASE64(ConvertStringToBase64.class, "string-to-base64(string[,encoding])",
      arg(STR, STR), B64, CONVERT_URI),
  /** XQuery function. */
  _CONVERT_STRING_TO_HEX(ConvertStringToHex.class, "string-to-hex(string[,encoding])",
      arg(STR, STR), HEX, CONVERT_URI),
  /** XQuery function. */
  _CONVERT_INTEGER_TO_DATETIME(ConvertIntegerToDateTime.class, "integer-to-dateTime(ms)", arg(ITR),
      DTM, CONVERT_URI),
  /** XQuery function. */
  _CONVERT_DATETIME_TO_INTEGER(ConvertDateTimeToInteger.class, "dateTime-to-integer(date)",
      arg(DTM), ITR, CONVERT_URI),
  /** XQuery function. */
  _CONVERT_INTEGER_TO_DAYTIME(ConvertIntegerToDayTime.class, "integer-to-dayTime(ms)", arg(ITR),
      DTD, CONVERT_URI),
  /** XQuery function. */
  _CONVERT_DAYTIME_TO_INTEGER(ConvertDayTimeToInteger.class, "dayTime-to-integer(duration)",
      arg(DTD), ITR, CONVERT_URI),

  /* FNCrypto functions (EXPath Cryptographic module). */

  /** XQuery function. */
  _CRYPTO_HMAC(CryptoHmac.class, "hmac(message,key,algorithm[,encoding])",
      arg(STR, STR, STR, STR_ZO), STR, CRYPTO_URI),
  /** XQuery function. */
  _CRYPTO_ENCRYPT(CryptoEncrypt.class, "encrypt(input,encryption,key,algorithm)",
      arg(STR, STR, STR, STR), STR, CRYPTO_URI),
  /** XQuery function. */
  _CRYPTO_DECRYPT(CryptoDecrypt.class, "decrypt(input,type,key,algorithm)",
      arg(STR, STR, STR, STR), STR, CRYPTO_URI),
  /** XQuery function. */
  _CRYPTO_GENERATE_SIGNATURE(CryptoGenerateSignature.class, "generate-signature" +
      "(input,canonicalization,digest,signature,prefix,type[,item1][,item2])",
      arg(NOD, STR, STR, STR, STR, STR, ITEM_ZO, ITEM_ZO), NOD, CRYPTO_URI),
  /** XQuery function. */
  _CRYPTO_VALIDATE_SIGNATURE(CryptoValidateSignature.class, "validate-signature(node)",
      arg(NOD), BLN, CRYPTO_URI),

  /* CSV Module. */

  /** XQuery function. */
  _CSV_PARSE(CsvParse.class, "parse(string[,config])", arg(STR, MAP_O), ITEM, CSV_URI),
  /** XQuery function. */
  _CSV_SERIALIZE(CsvSerialize.class, "serialize(item[,params])", arg(ITEM_ZO, ITEM_ZO), STR,
      CSV_URI),

  /* Database Module. */

  /** XQuery function. */
  _DB_OPEN(DbOpen.class, "open(database[,path])", arg(STR, STR), DOC_ZM, DB_URI),
  /** XQuery function. */
  _DB_OPEN_PRE(DbOpenPre.class, "open-pre(database,pre)", arg(STR, ITR), NOD_ZM, DB_URI),
  /** XQuery function. */
  _DB_OPEN_ID(DbOpenId.class, "open-id(database,id)", arg(STR, ITR), NOD_ZM, DB_URI),
  /** XQuery function. */
  _DB_TEXT(DbText.class, "text(database,string)", arg(STR, ITEM), TXT_ZM, flag(NDT), DB_URI),
  /** XQuery function. */
  _DB_TEXT_RANGE(DbTextRange.class, "text-range(database,from,to)",
      arg(STR, ITEM, ITEM), TXT_ZM, flag(NDT), DB_URI),
  /** XQuery function. */
  _DB_ATTRIBUTE(DbAttribute.class, "attribute(database,string[,name])",
      arg(STR, ITEM, STR), ATT_ZM, flag(NDT), DB_URI),
  /** XQuery function. */
  _DB_ATTRIBUTE_RANGE(DbAttributeRange.class, "attribute-range(database,from,to[,name])",
      arg(STR, ITEM, ITEM, STR), ATT_ZM, flag(NDT), DB_URI),
  /** XQuery function. */
  _DB_TOKEN(DbToken.class, "token(database,string[,name])",
      arg(STR, ITEM, STR), ATT_ZM, flag(NDT), DB_URI),
  /** XQuery function. */
  _DB_LIST(DbList.class, "list([database[,path]])", arg(STR, STR), STR_ZM, flag(NDT), DB_URI),
  /** XQuery function. */
  _DB_LIST_DETAILS(DbListDetails.class, "list-details([database[,path]])",
      arg(STR, STR), ELM_ZM, flag(NDT), DB_URI),
  /** XQuery function. */
  _DB_BACKUPS(DbBackups.class, "backups([database])", arg(ITEM), ELM_ZM, DB_URI),
  /** XQuery function. */
  _DB_CREATE_BACKUP(DbCreateBackup.class, "create-backup(database)",
      arg(STR), EMP, flag(UPD), DB_URI),
  /** XQuery function. */
  _DB_COPY(DbCopy.class, "copy(database, new-name)", arg(STR, STR), EMP, flag(UPD), DB_URI),
  /** XQuery function. */
  _DB_ALTER(DbAlter.class, "alter(database, new-name)", arg(STR, STR), EMP, flag(UPD), DB_URI),
  /** XQuery function. */
  _DB_DROP_BACKUP(DbDropBackup.class, "drop-backup(name)", arg(STR), EMP, flag(UPD), DB_URI),
  /** XQuery function. */
  _DB_RESTORE(DbRestore.class, "restore(backup)", arg(STR), EMP, flag(UPD), DB_URI),
  /** XQuery function. */
  _DB_SYSTEM(DbSystem.class, "system()", arg(), STR, DB_URI),
  /** XQuery function. */
  _DB_INFO(DbInfo.class, "info(database)", arg(STR), STR, DB_URI),
  /** XQuery function. */
  _DB_PROPERTY(DbProperty.class, "property(database,name)", arg(STR, STR), ITEM, DB_URI),
  /** XQuery function. */
  _DB_NODE_ID(DbNodeId.class, "node-id(nodes)", arg(NOD_ZM), ITR_ZM, DB_URI),
  /** XQuery function. */
  _DB_NODE_PRE(DbNodePre.class, "node-pre(nodes)", arg(NOD_ZM), ITR_ZM, DB_URI),
  /** XQuery function. */
  _DB_OUTPUT(DbOutput.class, "output(result)", arg(ITEM_ZM), EMP, flag(UPD), DB_URI),
  /** XQuery function. */
  _DB_OUTPUT_CACHE(DbOutputCache.class, "output-cache()", arg(), ITEM_ZO, flag(NDT), DB_URI),
  /** XQuery function. */
  _DB_ADD(DbAdd.class, "add(database,input[,path[,options]])",
      arg(STR, NOD, STR, MAP_O), EMP, flag(UPD), DB_URI),
  /** XQuery function. */
  _DB_DELETE(DbDelete.class, "delete(database,path)", arg(STR, STR), EMP, flag(UPD), DB_URI),
  /** XQuery function. */
  _DB_CREATE(DbCreate.class, "create(name[,inputs[,paths[,options]]])",
      arg(STR, ITEM_ZM, STR_ZM, MAP_O), EMP, flag(UPD), DB_URI),
  /** XQuery function. */
  _DB_DROP(DbDrop.class, "drop(database)", arg(ITEM), EMP, flag(UPD), DB_URI),
  /** XQuery function. */
  _DB_RENAME(DbRename.class, "rename(database,path,new-path)", arg(STR, STR, STR), EMP,
      flag(UPD), DB_URI),
  /** XQuery function. */
  _DB_REPLACE(DbReplace.class, "replace(database,path,input[,options])",
      arg(STR, STR, ITEM, MAP_O), EMP, flag(UPD), DB_URI),
  /** XQuery function. */
  _DB_OPTIMIZE(DbOptimize.class, "optimize(database[,all[,options]])",
      arg(STR, BLN, MAP_O), EMP, flag(UPD), DB_URI),
  /** XQuery function. */
  _DB_RETRIEVE(DbRetrieve.class, "retrieve(database,path)", arg(STR, STR), B64, flag(NDT), DB_URI),
  /** XQuery function. */
  _DB_STORE(DbStore.class, "store(database,path,input)",
      arg(STR, STR, ITEM), EMP, flag(UPD), DB_URI),
  /** XQuery function. */
  _DB_IS_XML(DbIsXml.class, "is-xml(database,path)", arg(STR, STR), BLN, DB_URI),
  /** XQuery function. */
  _DB_IS_RAW(DbIsRaw.class, "is-raw(database,path)", arg(STR, STR), BLN, DB_URI),
  /** XQuery function. */
  _DB_EXISTS(DbExists.class, "exists(database[,path])", arg(STR, STR), BLN, flag(NDT), DB_URI),
  /** XQuery function. */
  _DB_CONTENT_TYPE(DbContentType.class, "content-type(database,path)", arg(STR, STR), STR, DB_URI),
  /** XQuery function. */
  _DB_FLUSH(DbFlush.class, "flush(database)", arg(ITEM), EMP, flag(UPD), DB_URI),
  /** XQuery function. */
  _DB_EXPORT(DbExport.class, "export(database,path[,param]])",
      arg(STR, STR, ITEM), EMP, flag(NDT), DB_URI),
  /** XQuery function. */
  _DB_NAME(DbName.class, "name(node)", arg(NOD), STR, DB_URI),
  /** XQuery function. */
  _DB_PATH(DbPath.class, "path(node)", arg(NOD), STR, DB_URI),

  /* Fetch Module. */

  /** XQuery function. */
  _FETCH_TEXT(FetchText.class, "text(uri[,encoding[,fallback]])",
      arg(STR, STR, BLN), STR, flag(NDT), FETCH_URI),
  /** XQuery function. */
  _FETCH_BINARY(FetchBinary.class, "binary(uri)", arg(STR), B64, flag(NDT), FETCH_URI),
  /** XQuery function. */
  _FETCH_XML(FetchXml.class, "xml(uri[,options])", arg(STR, MAP_O), DOC_O, flag(NDT), FETCH_URI),
  /** XQuery function. */
  _FETCH_CONTENT_TYPE(FetchContentType.class, "content-type(uri)", arg(STR), STR, flag(NDT),
      FETCH_URI),

  /* File Module. */

  /** XQuery function. */
  _FILE_PATH_SEPARATOR(FilePathSeparator.class, "path-separator()", arg(), STR, FILE_URI),
  /** XQuery function. */
  _FILE_DIR_SEPARATOR(FileDirSeparator.class, "dir-separator()", arg(), STR, FILE_URI),
  /** XQuery function. */
  _FILE_LINE_SEPARATOR(FileLineSeparator.class, "line-separator()", arg(), STR, FILE_URI),
  /** XQuery function. */
  _FILE_TEMP_DIR(FileTempDir.class, "temp-dir()", arg(), STR, FILE_URI),
  /** XQuery function. */
  _FILE_NAME(FileName.class, "name(path)", arg(STR), STR, FILE_URI),
  /** XQuery function. */
  _FILE_PARENT(FileParent.class, "parent(path)", arg(STR), STR_ZO, FILE_URI),
  /** XQuery function. */
  _FILE_PATH_TO_URI(FilePathToUri.class, "path-to-uri(path)", arg(STR), URI, FILE_URI),
  /** XQuery function. */
  _FILE_EXISTS(FileExists.class, "exists(path)", arg(STR), BLN, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_IS_DIR(FileIsDir.class, "is-dir(path)", arg(STR), BLN, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_IS_ABSOLUTE(FileIsAbsolute.class, "is-absolute(path)", arg(STR), BLN, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_IS_FILE(FileIsFile.class, "is-file(path)", arg(STR), BLN, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_LAST_MODIFIED(FileLastModified.class, "last-modified(path)",
      arg(STR), DTM, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_SIZE(FileSize.class, "size(path)", arg(STR), ITR, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_PATH_TO_NATIVE(FilePathToNative.class, "path-to-native(path)",
      arg(STR), STR, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_RESOLVE_PATH(FileResolvePath.class, "resolve-path(path[,base])",
      arg(STR, STR), STR, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_LIST(FileList.class, "list(path[,recursive[,pattern]])",
      arg(STR, BLN, STR), STR_ZM, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_CREATE_DIR(FileCreateDir.class, "create-dir(path)", arg(STR), EMP, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_CREATE_TEMP_DIR(FileCreateTempDir.class, "create-temp-dir(prefix,suffix[,dir])",
      arg(STR, STR, STR), STR, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_CREATE_TEMP_FILE(FileCreateTempFile.class, "create-temp-file(prefix,suffix[,dir])",
      arg(STR, STR, STR), STR, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_DELETE(FileDelete.class, "delete(path[,recursive])",
      arg(STR, BLN), EMP, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_READ_TEXT(FileReadText.class, "read-text(path[,encoding[,fallback]])",
      arg(STR, STR, BLN), STR, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_READ_TEXT_LINES(FileReadTextLines.class, "read-text-lines(path[,encoding[,fallback]])",
      arg(STR, STR, BLN), STR_ZM, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_READ_BINARY(FileReadBinary.class, "read-binary(path[,offset[,length]])",
      arg(STR, ITR, ITR), B64, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_WRITE(FileWrite.class, "write(path,data[,params])",
      arg(STR, ITEM_ZM, ITEM), EMP, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_WRITE_BINARY(FileWriteBinary.class, "write-binary(path,item[,offset])",
      arg(STR, BIN, ITR), EMP, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_WRITE_TEXT(FileWriteText.class, "write-text(path,text[,encoding])",
      arg(STR, STR, STR), EMP, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_WRITE_TEXT_LINES(FileWriteTextLines.class, "write-text-lines(path,texts[,encoding])",
      arg(STR, STR_ZM, STR), EMP, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_APPEND(FileAppend.class, "append(path,data[,params])",
      arg(STR, ITEM_ZM, ITEM), EMP, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_APPEND_BINARY(FileAppendBinary.class, "append-binary(path,item)",
      arg(STR, BIN), EMP, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_APPEND_TEXT(FileAppendText.class, "append-text(path,text[,encoding])",
      arg(STR, STR, STR), EMP, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_APPEND_TEXT_LINES(FileAppendTextLines.class, "append-text-lines(path,texts[,encoding])",
      arg(STR, STR_ZM, STR), EMP, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_COPY(FileCopy.class, "copy(source,target)", arg(STR, STR), EMP, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_MOVE(FileMove.class, "move(source,target)", arg(STR, STR), EMP, flag(NDT), FILE_URI),
  /** XQuery function. */
  _FILE_CURRENT_DIR(FileCurrentDir.class, "current-dir()", arg(), STR, FILE_URI),
  /** XQuery function. */
  _FILE_BASE_DIR(FileBaseDir.class, "base-dir()", arg(), STR, FILE_URI),
  /** XQuery function. */
  _FILE_CHILDREN(FileChildren.class, "children(path)", arg(STR), STR_ZM, flag(NDT), FILE_URI),

  /* Fulltext Module. */

  /** XQuery function. */
  _FT_CONTAINS(FtContains.class, "contains(input,terms[,options])",
      arg(ITEM, ITEM_ZM, MAP_O), BLN, flag(NDT), FT_URI),
  /** XQuery function. */
  _FT_SEARCH(FtSearch.class, "search(database,terms[,options])",
      arg(STR, ITEM_ZM, MAP_O), TXT_ZM, flag(NDT), FT_URI),
  /** XQuery function. */
  _FT_COUNT(FtCount.class, "count(nodes)", arg(NOD_ZM), ITR, FT_URI),
  /** XQuery function. */
  _FT_MARK(FtMark.class, "mark(nodes[,name])", arg(NOD_ZM, STR), NOD_ZM, FT_URI),
  /** XQuery function. */
  _FT_EXTRACT(FtExtract.class, "extract(nodes[,name[,length]])", arg(ITEM_ZM, STR, ITR), NOD_ZM,
      FT_URI),
  /** XQuery function. */
  _FT_SCORE(FtScore.class, "score(items)", arg(ITEM_ZM), DBL_ZM, FT_URI),
  /** XQuery function. */
  _FT_TOKENS(FtTokens.class, "tokens(database[,prefix])", arg(STR, STR), ITEM_ZM, flag(NDT),
      FT_URI),
  /** XQuery function. */
  _FT_TOKENIZE(FtTokenize.class, "tokenize(string[,options])", arg(STR, MAP_O), STR_ZM, FT_URI),
  /** XQuery function. */
  _FT_NORMALIZE(FtNormalize.class, "normalize(string[,options])", arg(STR, MAP_O), STR, FT_URI),

  /* Hash Module. */

  /** XQuery function. */
  _HASH_MD5(HashMd5.class, "md5(value)", arg(AAT), B64, HASH_URI),
  /** XQuery function. */
  _HASH_SHA1(HashSha1.class, "sha1(value)", arg(AAT), B64, HASH_URI),
  /** XQuery function. */
  _HASH_SHA256(HashSha256.class, "sha256(value)", arg(AAT), B64, HASH_URI),
  /** XQuery function. */
  _HASH_HASH(HashHash.class, "hash(value,algorithm)", arg(AAT, STR), B64, HASH_URI),

  /* HOF Module. */

  /** XQuery function. */
  _HOF_SORT_WITH(HofSortWith.class, "sort-with(items,lt-fun)",
      arg(ITEM_ZM, FuncType.get(BLN, ITEM, ITEM).seqType()), ITEM_ZM, flag(HOF), HOF_URI),
  /** XQuery function. */
  _HOF_TAKE_WHILE(HofTakeWhile.class, "take-while(items,pred)",
      arg(ITEM_ZM, FuncType.get(BLN, ITEM).seqType()), ITEM_ZM, flag(HOF), HOF_URI),
  /** XQuery function. */
  _HOF_SCAN_LEFT(HofScanLeft.class, "scan-left(items,start,function)",
      arg(ITEM_ZM, ITEM_ZM, FuncType.get(ITEM_ZM, ITEM_ZM, ITEM).seqType()), ITEM_ZM, flag(HOF),
      HOF_URI),
  /** XQuery function. */
  _HOF_ID(HofId.class, "id(value)", arg(ITEM_ZM), ITEM_ZM, HOF_URI),
  /** XQuery function. */
  _HOF_CONST(HofConst.class, "const(return,ignore)", arg(ITEM_ZM, ITEM_ZM), ITEM_ZM,
      HOF_URI),
  /** XQuery function. */
  _HOF_UNTIL(HofUntil.class, "until(pred,function,start)", arg(FuncType.get(BLN, ITEM_ZM).seqType(),
      FuncType.get(ITEM_ZM, ITEM_ZM).seqType(), ITEM_ZM), ITEM_ZM, flag(HOF), HOF_URI),
  /** XQuery function. */
  _HOF_FOLD_LEFT1(HofFoldLeft1.class, "fold-left1(non-empty-items,function)",
      arg(ITEM_OM, FuncType.get(ITEM_ZM, ITEM_ZM, ITEM).seqType()), ITEM_ZM, flag(HOF),
      HOF_URI),
  /** XQuery function. */
  _HOF_TOP_K_BY(HofTopKBy.class, "top-k-by(items,key-func,k)",
      arg(ITEM_ZM, FuncType.arity(1).seqType(), ITR), ITEM_ZM, flag(HOF), HOF_URI),
  /** XQuery function. */
  _HOF_TOP_K_WITH(HofTopKWith.class, "top-k-with(items,less-than-func,k)",
      arg(ITEM_ZM, FuncType.get(BLN, ITEM_ZO, ITEM_ZO).seqType(), ITR), ITEM_ZM, flag(HOF),
      HOF_URI),

  /* HTML Module. */

  /** XQuery function. */
  _HTML_PARSER(HtmlParser.class, "parser()", arg(), STR, HTML_URI),
  /** XQuery function. */
  _HTML_PARSE(HtmlParse.class, "parse(input[,options)", arg(STR, MAP_O), DOC_O, HTML_URI),

  /* HTTP Module. */

  /** XQuery function. */
  _HTTP_SEND_REQUEST(HttpSendRequest.class, "send-request(request[,href,[bodies]])",
      arg(NOD, STR_ZO, ITEM_ZM), ITEM_ZM, flag(NDT), HTTP_URI),

  /* Index Module. */

  /** XQuery function. */
  _INDEX_FACETS(IndexFacets.class, "facets(database[,type])", arg(STR, STR), DOC_O, flag(NDT),
      INDEX_URI),
  /** XQuery function. */
  _INDEX_TEXTS(IndexTexts.class, "texts(database[,prefix[,ascending]])",
      arg(STR, STR, BLN), ELM_ZM, flag(NDT), INDEX_URI),
  /** XQuery function. */
  _INDEX_ATTRIBUTES(IndexAttributes.class, "attributes(database[,prefix[,ascending]])",
      arg(STR, STR, BLN), ELM_ZM, flag(NDT), INDEX_URI),
  /** XQuery function. */
  _INDEX_TOKENS(IndexTokens.class, "tokens(database)", arg(STR), ELM_ZM, flag(NDT), INDEX_URI),
  /** XQuery function. */
  _INDEX_ELEMENT_NAMES(IndexElementNames.class, "element-names(database)", arg(STR), ELM_ZM,
      INDEX_URI),
  /** XQuery function. */
  _INDEX_ATTRIBUTE_NAMES(IndexAttributeNames.class, "attribute-names(database)", arg(STR), ELM_ZM,
      INDEX_URI),

  /* Inspection Module. */

  /** XQuery function. */
  _INSPECT_FUNCTION(InspectFunction.class, "function(function)",
      arg(STR), ELM, flag(HOF), INSPECT_URI),
  /** XQuery function. */
  _INSPECT_MODULE(InspectModule.class, "module(uri)", arg(STR), ELM, INSPECT_URI),
  /** XQuery function. */
  _INSPECT_CONTEXT(InspectContext.class, "context()", arg(), ELM, INSPECT_URI),
  /** XQuery function. */
  _INSPECT_FUNCTIONS(InspectFunctions.class, "functions([uri])",
      arg(STR), FUN_ZM, flag(HOF), INSPECT_URI),
  /** XQuery function. */
  _INSPECT_FUNCTION_ANNOTATIONS(InspectFunctionAnnotations.class, "function-annotations(function)",
      arg(FUN_O), MAP_ZO, UTIL_URI),
  /** XQuery function. */
  _INSPECT_STATIC_CONTEXT(InspectStaticContext.class, "static-context(function,name)",
      arg(FUN_O, STR), ITEM_ZM, INSPECT_URI),
  /** XQuery function. */
  _INSPECT_XQDOC(InspectXqdoc.class, "xqdoc(uri)", arg(STR), ELM, INSPECT_URI),

  /* Jobs Module. */

  /** XQuery function. */
  _JOBS_EVAL(JobsEval.class, "eval(string[,bindings[,options]])",
      arg(STR, MAP_ZO, MAP_O), STR, flag(NDT), JOBS_URI),
  /** XQuery function. */
  _JOBS_RESULT(JobsResult.class, "result(id)", arg(STR), ITEM_ZM, flag(NDT), JOBS_URI),
  /** XQuery function. */
  _JOBS_FINISHED(JobsFinished.class, "finished(id)", arg(STR), BLN, flag(NDT), JOBS_URI),
  /** XQuery function. */
  _JOBS_STOP(JobsStop.class, "stop(id)", arg(STR), BLN, flag(NDT), JOBS_URI),
  /** XQuery function. */
  _JOBS_LIST(JobsList.class, "list()", arg(), STR_ZM, flag(NDT), JOBS_URI),
  /** XQuery function. */
  _JOBS_LIST_DETAILS(JobsListDetails.class, "list-details([id])",
      arg(STR), ELM_ZM, flag(NDT), JOBS_URI),
  /** XQuery function. */
  _JOBS_CURRENT(JobsCurrent.class, "current()", arg(), STR, flag(NDT), JOBS_URI),

  /* JSON Module. */

  /** XQuery function. */
  _JSON_PARSE(JsonParse.class, "parse(string[,config])", arg(STR, MAP_O), ITEM, JSON_URI),
  /** XQuery function. */
  _JSON_SERIALIZE(JsonSerialize.class, "serialize(items[,params])", arg(ITEM_ZO, ITEM_ZO), STR,
      JSON_URI),

  /* Output Module. */

  /** XQuery function. */
  _OUT_NL(OutNl.class, "nl()", arg(), STR, OUT_URI),
  /** XQuery function. */
  _OUT_TAB(OutTab.class, "tab()", arg(), STR, OUT_URI),
  /** XQuery function. */
  _OUT_FORMAT(OutFormat.class, "format(format,item1[,...])", arg(STR, ITEM), STR, OUT_URI),

  /* Process Module. */

  /** XQuery function. */
  _PROC_SYSTEM(ProcSystem.class, "system(command[,args[,encoding]])",
      arg(STR, STR_ZM, STR), STR, flag(NDT), PROC_URI),
  /** XQuery function. */
  _PROC_EXECUTE(ProcExecute.class, "execute(command[,args[,encoding]]])",
      arg(STR, STR_ZM, STR), ELM, flag(NDT), PROC_URI),
  /** XQuery function. */
  _PROC_PROPERTY_NAMES(ProcPropertyNames.class, "property-names()",
      arg(), STR_ZO, flag(NDT), PROC_URI),
  /** XQuery function. */
  _PROC_PROPERTY(ProcProperty.class, "property(name)", arg(STR), STR, flag(NDT), PROC_URI),

  /* Profiling Module. */

  /** XQuery function. */
  _PROF_MEM(ProfMem.class, "mem(value[,cache[,label]])", arg(ITEM_ZM, BLN, STR), ITEM_ZM, flag(NDT),
      PROF_URI),
  /** XQuery function. */
  _PROF_TIME(ProfTime.class, "time(value[,cache[,label]])",
      arg(ITEM_ZM, BLN, STR), ITEM_ZM, flag(NDT), PROF_URI),
  /** XQuery function. */
  _PROF_SLEEP(ProfSleep.class, "sleep(ms)", arg(ITR), EMP, flag(NDT), PROF_URI),
  /** XQuery function. */
  _PROF_CURRENT_MS(ProfCurrentMs.class, "current-ms()", arg(), ITR, flag(NDT), PROF_URI),
  /** XQuery function. */
  _PROF_CURRENT_NS(ProfCurrentNs.class, "current-ns()", arg(), ITR, flag(NDT), PROF_URI),
  /** XQuery function. */
  _PROF_DUMP(ProfDump.class, "dump(value[,label])", arg(ITEM_ZM, STR), EMP, flag(NDT), PROF_URI),
  /** XQuery function. */
  _PROF_HUMAN(ProfHuman.class, "human(integer)", arg(ITR), STR, flag(NDT), PROF_URI),
  /** XQuery function. */
  _PROF_VOID(ProfVoid.class, "void(value)", arg(ITEM_ZM), EMP, flag(NDT), PROF_URI),
  /** XQuery function. */
  _PROF_VARIABLES(ProfVariables.class, "variables()", arg(), EMP, flag(NDT), PROF_URI),
  /** XQuery function. */
  _PROF_TYPE(ProfType.class, "type(value)", arg(ITEM_ZM), ITEM_ZM, PROF_URI),

  /* Random Module. */

  /** XQuery function. */
  _RANDOM_DOUBLE(RandomDouble.class, "double()", arg(), DBL, flag(NDT), RANDOM_URI),
  /** XQuery function. */
  _RANDOM_INTEGER(RandomInteger.class, "integer([max])", arg(ITR), ITR, flag(NDT), RANDOM_URI),
  /** XQuery function. */
  _RANDOM_SEEDED_DOUBLE(RandomSeededDouble.class, "seeded-double(seed,num)",
      arg(ITR, ITR), DBL_ZM, RANDOM_URI),
  /** XQuery function. */
  _RANDOM_SEEDED_INTEGER(RandomSeededInteger.class, "seeded-integer(seed,num[,max])",
      arg(ITR, ITR, ITR), ITR_ZM, RANDOM_URI),
  /** XQuery function. */
  _RANDOM_GAUSSIAN(RandomGaussian.class, "gaussian(num)", arg(ITR), DBL_ZM, flag(NDT), RANDOM_URI),
  /** XQuery function. */
  _RANDOM_SEEDED_PERMUTATION(RandomSeededPermutation.class, "seeded-permutation(seed,items)",
      arg(ITR, ITEM_ZM), ITEM_ZM, RANDOM_URI),
  /** XQuery function. */
  _RANDOM_UUID(RandomUuid.class, "uuid()", arg(), STR, flag(NDT), RANDOM_URI),

  /* Repository Module. */

  /** XQuery function. */
  _REPO_INSTALL(RepoInstall.class, "install(uri)", arg(STR), EMP, flag(NDT), REPO_URI),
  /** XQuery function. */
  _REPO_DELETE(RepoDelete.class, "delete(uri)", arg(STR), EMP, flag(NDT), REPO_URI),
  /** XQuery function. */
  _REPO_LIST(RepoList.class, "list()", arg(), STR_ZM, flag(NDT), REPO_URI),

  /* SQL Module. */

  /** XQuery function. */
  _SQL_INIT(SqlInit.class, "init(class)", arg(STR), EMP, flag(NDT), SQL_URI),
  /** XQuery function. */
  _SQL_CONNECT(SqlConnect.class, "connect(url[,user[,pass[,options]]]]])",
      arg(STR, STR, STR, MAP_O), ITR, flag(NDT), SQL_URI),
  /** XQuery function. */
  _SQL_PREPARE(SqlPrepare.class, "prepare(id,statement)", arg(ITR, STR), ITR, flag(NDT), SQL_URI),
  /** XQuery function. */
  _SQL_EXECUTE(SqlExecute.class, "execute(id,query)", arg(ITR, STR), ELM_ZM, flag(NDT), SQL_URI),
  /** XQuery function. */
  _SQL_EXECUTE_PREPARED(SqlExecutePrepared.class, "execute-prepared(id[,params])",
      arg(ITR, ELM), ELM_ZM, flag(NDT), SQL_URI),
  /** XQuery function. */
  _SQL_CLOSE(SqlClose.class, "close(id)", arg(ITR), EMP, flag(NDT), SQL_URI),
  /** XQuery function. */
  _SQL_COMMIT(SqlCommit.class, "commit(id)", arg(ITR), EMP, flag(NDT), SQL_URI),
  /** XQuery function. */
  _SQL_ROLLBACK(SqlRollback.class, "rollback(id)", arg(ITR), EMP, flag(NDT), SQL_URI),

  /* Streaming Module. */

  /** XQuery function. */
  _STREAM_MATERIALIZE(StreamMaterialize.class, "materialize(value)", arg(ITEM_ZM), ITEM_ZM,
      STREAM_URI),
  /** XQuery function. */
  _STREAM_IS_STREAMABLE(StreamIsStreamable.class, "is-streamable(item)", arg(ITEM), BLN,
      STREAM_URI),

  /* Strings Module. */

  /** XQuery function. */
  _STRINGS_LEVENSHTEIN(StringsLevenshtein.class, "levenshtein(string1,string2)",
      arg(STR, STR), DBL, STRINGS_URI),
  /** XQuery function. */
  _STRINGS_SOUNDEX(StringsSoundex.class, "soundex(string)", arg(STR), STR, STRINGS_URI),
  /** XQuery function. */
  _STRINGS_COLOGNE_PHONETIC(StringsColognePhonetic.class, "cologne-phonetic(string)",
      arg(STR), STR, STRINGS_URI),

  /* Unit Module. */

  /** XQuery function. */
  _UNIT_ASSERT(UnitAssert.class, "assert(test[,failure])", arg(ITEM_ZM, ITEM), EMP, flag(NDT),
      UNIT_URI),
  /** XQuery function. */
  _UNIT_ASSERT_EQUALS(UnitAssertEquals.class, "assert-equals(result,expected[,failure])",
      arg(ITEM_ZM, ITEM_ZM, ITEM), EMP, flag(NDT), UNIT_URI),
  /** XQuery function. */
  _UNIT_FAIL(UnitFail.class, "fail([failure])", arg(ITEM), ITEM_ZM, flag(NDT), UNIT_URI),

  /* User Module. */

  /** XQuery function. */
  _USER_CURRENT(UserCurrent.class, "current()", arg(), STR, USER_URI),
  /** XQuery function. */
  _USER_EXISTS(UserExists.class, "exists(name)", arg(STR), BLN, flag(NDT), USER_URI),
  /** XQuery function. */
  _USER_CHECK(UserCheck.class, "check(name,password)", arg(STR, STR), EMP, flag(NDT), USER_URI),
  /** XQuery function. */
  _USER_LIST(UserList.class, "list()", arg(), ELM_ZM, flag(NDT), USER_URI),
  /** XQuery function. */
  _USER_LIST_DETAILS(UserListDetails.class, "list-details([name])",
      arg(STR), ELM_ZM, flag(NDT), USER_URI),
  /** XQuery function. */
  _USER_CREATE(UserCreate.class, "create(name,password[,permissions[,patterns]])",
      arg(STR, STR, STR_ZM, STR_ZM), EMP, flag(UPD), USER_URI),
  /** XQuery function. */
  _USER_GRANT(UserGrant.class, "grant(name,permissions[,patterns])",
      arg(STR, STR_ZM, STR_ZM), EMP, flag(UPD), USER_URI),
  /** XQuery function. */
  _USER_DROP(UserDrop.class, "drop(name[,patterns])", arg(STR, STR_ZM), EMP, flag(UPD), USER_URI),
  /** XQuery function. */
  _USER_ALTER(UserAlter.class, "alter(name,newname)", arg(STR, STR), EMP, flag(UPD), USER_URI),
  /** XQuery function. */
  _USER_PASSWORD(UserPassword.class, "password(name,password)",
      arg(STR, STR), EMP, flag(UPD), USER_URI),
  /** XQuery function. */
  _USER_INFO(UserInfo.class, "info()", arg(), ELM, USER_URI),
  /** XQuery function. */
  _USER_UPDATE_INFO(UserUpdateInfo.class, "update-info(element)",
      arg(ELM), EMP, flag(UPD), USER_URI),

  /* Utility Module. */

  /** XQuery function. */
  _UTIL_DEEP_EQUAL(UtilDeepEqual.class, "deep-equal(items1,items2[,options])",
      arg(ITEM_ZM, ITEM_ZM, ITEM), BLN, UTIL_URI),
  /** XQuery function. */
  _UTIL_ITEM_AT(UtilItemAt.class, "item-at(items,pos)", arg(ITEM_ZM, DBL), ITEM_ZO, UTIL_URI),
  /** XQuery function. */
  _UTIL_ITEM_RANGE(UtilItemRange.class, "item-range(items,first,last)",
      arg(ITEM_ZM, DBL, DBL), ITEM_ZM, UTIL_URI),
  /** XQuery function. */
  _UTIL_LAST_FROM(UtilLastFrom.class, "last-from(items)", arg(ITEM_ZM), ITEM_ZO, UTIL_URI),

  /* Validate Module. */

  /** XQuery function. */
  _VALIDATE_XSD(ValidateXsd.class, "xsd(input[,schema[,version]])",
      arg(ITEM, ITEM, STR), EMP, flag(NDT), VALIDATE_URI),
  /** XQuery function. */
  _VALIDATE_XSD_INFO(ValidateXsdInfo.class, "xsd-info(input[,schema[,version]])",
      arg(ITEM, ITEM, STR), STR_ZM, flag(NDT), VALIDATE_URI),
  /** XQuery function. */
  _VALIDATE_XSD_REPORT(ValidateXsdReport.class, "xsd-report(input[,schema[,version]])",
      arg(ITEM, ITEM, STR), ELM, flag(NDT), VALIDATE_URI),
  /** XQuery function. */
  _VALIDATE_DTD(ValidateDtd.class, "dtd(input[,schema])",
      arg(ITEM, ITEM), EMP, flag(NDT), VALIDATE_URI),
  /** XQuery function. */
  _VALIDATE_DTD_INFO(ValidateDtdInfo.class, "dtd-info(input[,schema])",
      arg(ITEM, ITEM), STR_ZM, flag(NDT), VALIDATE_URI),
  /** XQuery function. */
  _VALIDATE_DTD_REPORT(ValidateDtdReport.class, "dtd-report(input[,schema])",
      arg(ITEM, ITEM), ELM, flag(NDT), VALIDATE_URI),
  /** XQuery function. */
  _VALIDATE_RNG(ValidateRng.class, "rng(input,schema[,compact])",
      arg(ITEM, ITEM, BLN), STR_ZM, flag(NDT), VALIDATE_URI),
  /** XQuery function. */
  _VALIDATE_RNG_INFO(ValidateRngInfo.class, "rng-info(input,schema[,compact])",
      arg(ITEM, ITEM, BLN), STR_ZM, flag(NDT), VALIDATE_URI),
  /** XQuery function. */
  _VALIDATE_RNG_REPORT(ValidateRngReport.class, "rng-report(input,schema[,compact])",
      arg(ITEM, ITEM, BLN), ELM, flag(NDT), VALIDATE_URI),

  /* Web Module. */

  /** XQuery function. */
  _WEB_CONTENT_TYPE(WebContentType.class, "content-type(path)", arg(STR), STR, WEB_URI),
  /** XQuery function. */
  _WEB_CREATE_URL(WebCreateUrl.class, "create-url(url,params)", arg(STR, MAP_O), STR, WEB_URI),
  /** XQuery function. */
  _WEB_REDIRECT(WebRedirect.class, "redirect(location[,params])", arg(STR, MAP_O), ELM, WEB_URI),
  /** XQuery function. */
  _WEB_RESPONSE_HEADER(WebResponseHeader.class, "response-header([headers[,output]])",
      arg(MAP_O, MAP_O), ELM, WEB_URI),
  /** XQuery function. */
  _WEB_ENCODE_URL(WebEncodeUrl.class, "encode-url(string)", arg(STR), STR, WEB_URI),
  /** XQuery function. */
  _WEB_DECODE_URL(WebDecodeUrl.class, "decode-url(string)", arg(STR), STR, WEB_URI),

  /* XQuery Module. */

  /** XQuery function. */
  _XQUERY_EVAL(XQueryEval.class, "eval(string[,bindings[,options]])",
      arg(STR, MAP_ZO, MAP_O), ITEM_ZM, flag(NDT), XQUERY_URI),
  /** XQuery function. */
  _XQUERY_UPDATE(XQueryUpdate.class, "update(string[,bindings[,options]])",
      arg(STR, MAP_ZO, MAP_O), ITEM_ZM, flag(UPD), XQUERY_URI),
  /** XQuery function. */
  _XQUERY_INVOKE(XQueryInvoke.class, "invoke(uri[,bindings[,options]])",
      arg(STR, MAP_ZO, MAP_O), ITEM_ZM, flag(NDT), XQUERY_URI),
  /** XQuery function. */
  _XQUERY_PARSE(XQueryParse.class, "parse(string[,options])",
      arg(STR, MAP_O), NOD, flag(NDT), XQUERY_URI),
  /** XQuery function. */
  _XQUERY_PARSE_URI(XQueryParseUri.class, "parse-uri(uri[,options])",
      arg(STR, MAP_O), NOD, flag(NDT), XQUERY_URI),
  /** XQuery function. */
  _XQUERY_FORK_JOIN(XQueryForkJoin.class, "fork-join(functions[,options])",
      arg(FUN_ZM, MAP_O), ITEM_ZM, flag(HOF), XQUERY_URI),

  /* XSLT Module. */

  /** XQuery function. */
  _XSLT_PROCESSOR(XsltProcessor.class, "processor()", arg(), STR, XSLT_URI),
  /** XQuery function. */
  _XSLT_VERSION(XsltVersion.class, "version()", arg(), STR, XSLT_URI),
  /** XQuery function. */
  _XSLT_TRANSFORM(XsltTransform.class, "transform(input,stylesheet[,params])",
      arg(ITEM, ITEM, MAP_O), NOD, flag(NDT), XSLT_URI),
  /** XQuery function. */
  _XSLT_TRANSFORM_TEXT(XsltTransformText.class, "transform-text(input,stylesheet[,params])",
      arg(ITEM, ITEM, MAP_O), STR, flag(NDT), XSLT_URI),

  /* ZIP Module. */

  /** XQuery function. */
  _ZIP_BINARY_ENTRY(ZipBinaryEntry.class, "binary-entry(path,entry)",
      arg(STR, STR), B64, flag(NDT), ZIP_URI),
  /** XQuery function. */
  _ZIP_TEXT_ENTRY(ZipTextEntry.class, "text-entry(path,entry[,encoding])",
      arg(STR, STR, STR), STR, flag(NDT), ZIP_URI),
  /** XQuery function. */
  _ZIP_HTML_ENTRY(ZipHtmlEntry.class, "html-entry(path,entry)",
      arg(STR, STR), NOD, flag(NDT), ZIP_URI),
  /** XQuery function. */
  _ZIP_XML_ENTRY(ZipXmlEntry.class, "xml-entry(path,entry)",
      arg(STR, STR), NOD, flag(NDT), ZIP_URI),
  /** XQuery function. */
  _ZIP_ENTRIES(ZipEntries.class, "entries(path)", arg(STR), ELM, flag(NDT), ZIP_URI),
  /** XQuery function. */
  _ZIP_ZIP_FILE(ZipZipFile.class, "zip-file(zip)", arg(ELM), EMP, flag(NDT), ZIP_URI),
  /** XQuery function. */
  _ZIP_UPDATE_ENTRIES(ZipUpdateEntries.class, "update-entries(zip,output)",
      arg(ELM, STR), EMP, flag(NDT), ZIP_URI);

  /** URIs of built-in functions. */
  public static final TokenSet URIS = new TokenSet();

  static {
    for(final Function f : values()) {
      final byte[] u = f.uri;
      if(u != null) URIS.add(u);
    }
  }

  /** Argument pattern. */
  private static final Pattern ARG = Pattern.compile(
      "^([-\\w_:\\.]*\\(|<|\"|\\$| ).*", Pattern.DOTALL);

  /** Cached enums (faster). */
  public static final Function[] VALUES = values();
  /** Minimum and maximum number of arguments. */
  public final int[] minMax;
  /** Argument types. */
  public final SeqType[] args;

  /** Descriptions. */
  final String desc;
  /** Return type. */
  final SeqType type;

  /** Compiler flags. */
  private final EnumSet<Flag> flags;
  /** Function classes. */
  private final Class<? extends StandardFunc> func;
  /** URI. */
  private final byte[] uri;

  /**
   * Constructs a function signature; calls
   * {@link #Function(Class, String, SeqType[], SeqType, EnumSet)}.
   * @param func reference to the class containing the function implementation
   * @param desc descriptive function string
   * @param args types of the function arguments
   * @param type return type
   */
  Function(final Class<? extends StandardFunc> func, final String desc, final SeqType[] args,
      final SeqType type) {
    this(func, desc, args, type, EnumSet.noneOf(Flag.class));
  }

  /**
   * Constructs a function signature; calls
   * {@link #Function(Class, String, SeqType[], SeqType, EnumSet)}.
   * @param func reference to the class containing the function implementation
   * @param desc descriptive function string
   * @param args types of the function arguments
   * @param type return type
   * @param uri uri
   */
  Function(final Class<? extends StandardFunc> func, final String desc, final SeqType[] args,
      final SeqType type, final byte[] uri) {
    this(func, desc, args, type, EnumSet.noneOf(Flag.class), uri);
  }

  /**
   * Constructs a function signature; calls
   * {@link #Function(Class, String, SeqType[], SeqType, EnumSet, byte[])}.
   * @param func reference to the class containing the function implementation
   * @param desc descriptive function string
   * @param args types of the function arguments
   * @param type return type
   * @param flag static function properties
   */
  Function(final Class<? extends StandardFunc> func, final String desc, final SeqType[] args,
      final SeqType type, final EnumSet<Flag> flag) {
    this(func, desc, args, type, flag, FN_URI);
  }

  /**
   * Constructs a function signature.
   * @param func reference to the class containing the function implementation
   * @param desc descriptive function string, containing the function name and its
   *             arguments in parentheses. Optional arguments are represented in nested
   *             square brackets; three dots indicate that the number of arguments of a
   *             function is not limited
   * @param args types of the function arguments
   * @param type return type
   * @param flags static function properties
   * @param uri uri
   */
  Function(final Class<? extends StandardFunc> func, final String desc, final SeqType[] args,
      final SeqType type, final EnumSet<Flag> flags, final byte[] uri) {

    this.func = func;
    this.desc = desc;
    this.type = type;
    this.args = args;
    this.flags = flags;
    this.uri = uri;
    minMax = minMax(desc, args);
  }

  /**
   * Computes the minimum and maximum number of arguments by analyzing the description string.
   * @param desc description
   * @param args arguments
   * @return min/max values
   */
  public static int[] minMax(final String desc, final SeqType[] args) {
    // count number of minimum and maximum arguments by analyzing the description
    final int b = desc.indexOf('['), al = args.length;
    if(b == -1) return new int[] { al, al };

    int c = b + 1 < desc.length() && desc.charAt(b + 1) == ',' ? 1 : 0;
    for(int i = 0; i < b; i++) if(desc.charAt(i) == ',') c++;
    return new int[] { c, desc.contains("...") ? Integer.MAX_VALUE : al };
  }

  /**
   * Creates a new instance of the function.
   * @param sc static context
   * @param info input info
   * @param exprs arguments
   * @return function
   */
  public StandardFunc get(final StaticContext sc, final InputInfo info, final Expr... exprs) {
    return Reflect.get(func).init(sc, info, this, exprs);
  }

  /**
   * Returns the namespace URI of this function.
   * @return function
   */
  final byte[] uri() {
    return uri;
  }

  /**
   * Indicates if an expression has the specified compiler property.
   * @param flag flag to be found
   * @return result of check
   * @see Expr#has(Flag)
    */
  boolean has(final Flag flag) {
    return flags.contains(flag);
  }

  /**
   * Returns the function type of this function with the given arity.
   * @param arity number of arguments
   * @param anns annotations
   * @return function type
   */
  final FuncType type(final int arity, final AnnList anns) {
    final SeqType[] st = new SeqType[arity];
    if(arity != 0 && minMax[1] == Integer.MAX_VALUE) {
      final int al = args.length;
      System.arraycopy(args, 0, st, 0, al);
      final SeqType var = args[al - 1];
      for(int a = al; a < arity; a++) st[a] = var;
    } else {
      System.arraycopy(args, 0, st, 0, arity);
    }
    return FuncType.get(anns, type, st);
  }

  /**
   * Returns an array representation of the specified sequence types.
   * @param arg arguments
   * @return array
   */
  private static SeqType[] arg(final SeqType... arg) { return arg; }

  /**
   * Returns a set representation of the specified compiler flags.
   * @param flags flags
   * @return set
   */
  private static EnumSet<Flag> flag(final Flag... flags) {
    final EnumSet<Flag> set = EnumSet.noneOf(Flag.class);
    Collections.addAll(set, flags);
    return set;
  }

  /**
   * Returns the function's variable names.
   * @return array of variable names
   */
  final String[] names() {
    final String names = desc.replaceFirst(".*?\\(", "").replace(",...", "").
        replaceAll("[\\[\\]\\)\\s]", "");
    return names.isEmpty() ? new String[0] : Strings.split(names, ',');
  }

  /**
   * Returns the local name of the function.
   * @return name
   */
  public byte[] local() {
    return new TokenBuilder(desc.substring(0, desc.indexOf('('))).finish();
  }

  /**
   * Returns the prefixed name of the annotation.
   * @return name
   */
  public byte[] id() {
    final TokenBuilder tb = new TokenBuilder();
    if(!Token.eq(uri, FN_URI)) tb.add(NSGlobal.prefix(uri)).add(':');
    return tb.add(local()).finish();
  }

  /**
   * Returns the the variable names for an instance of this function with the given arity.
   * @param arity number of arguments
   * @return array of argument names
   */
  final QNm[] argNames(final int arity) {
    final String[] names = names();
    final QNm[] res = new QNm[arity];
    final int nl = names.length;
    for(int i = Math.min(arity, nl); --i >= 0;) res[i] = new QNm(names[i]);
    if(arity > nl) {
      final String[] parts = names[nl - 1].split("(?=\\d+$)", 2);
      final int start = Integer.parseInt(parts[1]);
      for(int i = nl; i < arity; i++) res[i] = new QNm(parts[0] + (start + i - nl + 1), "");
    }
    return res;
  }

  /**
   * Returns a string representation of the function with the specified
   * arguments. All objects are wrapped with quotes, except for the following ones:
   * <ul>
   * <li>integers</li>
   * <li>booleans (which will be suffixed with parentheses)</li>
   * <li>strings starting with an optional NCName and opening parenthesis</li>
   * <li>strings starting with angle bracket, quote, dollar sign, or space</li>
   * </ul>
   * @param arg arguments
   * @return string representation
   */
  public final String args(final Object... arg) {
    final TokenBuilder tb = new TokenBuilder();
    for(final Object a : arg) {
      if(!tb.isEmpty()) tb.add(',');
      final String s = a.toString();
      if(ARG.matcher(s).matches() || a instanceof Integer || a instanceof Long) {
        tb.add(s);
      } else if(a instanceof Boolean) {
        tb.add(s + "()");
      } else {
        tb.add('"' + s.replaceAll("\"", "\"\"") + '"');
      }
    }
    return toString().replaceAll("\\(.*", "(") + tb + ')';
  }

  @Override
  public final String toString() {
    return new TokenBuilder(NSGlobal.prefix(uri)).add(':').add(desc).toString();
  }

  /**
   * Returns a string representation of the function with the specified arguments.
   * @param exprs arguments
   * @return function
   */
  public String toString(final Expr... exprs) {
    return new TokenBuilder(id()).add('(').addSep(exprs, SEP).add(')').toString();
  }

  /*
   * Returns the names of all functions. Used to update MediaWiki syntax highlighter.
   * All function names are listed in reverse order to give precedence to longer names.
   * @param args ignored
  public static void main(final String... args) {
    final org.basex.util.list.StringList sl = new org.basex.util.list.StringList();
    for(Function f : VALUES) {
      sl.add(f.toString().replaceAll("^fn:|\\(.*", ""));
    }
    for(final String s : sl.sort(false, false)) {
      Util.out(s + ' ');
    }
    Util.outln("fn:");
  }
   */
}
