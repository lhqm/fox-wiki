package com.zyplayer.doc.db.service.database;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyplayer.doc.core.exception.ConfirmException;
import com.zyplayer.doc.data.config.security.DocUserUtil;
import com.zyplayer.doc.data.repository.support.consts.DocAuthConst;
import com.zyplayer.doc.data.repository.support.consts.DocSysModuleType;
import com.zyplayer.doc.data.repository.support.consts.DocSysType;
import com.zyplayer.doc.db.controller.download.FormatDownloadConst;
import com.zyplayer.doc.db.controller.param.DataViewParam;
import com.zyplayer.doc.db.controller.param.ProcedureListParam;
import com.zyplayer.doc.db.controller.vo.TableColumnVo;
import com.zyplayer.doc.db.controller.vo.TableDdlVo;
import com.zyplayer.doc.db.controller.vo.TableStatusVo;
import com.zyplayer.doc.db.framework.consts.DbAuthType;
import com.zyplayer.doc.db.framework.db.bean.DatabaseFactoryBean;
import com.zyplayer.doc.db.framework.db.bean.DatabaseRegistrationBean;
import com.zyplayer.doc.db.framework.db.dto.*;
import com.zyplayer.doc.db.framework.db.enums.DatabaseProductEnum;
import com.zyplayer.doc.db.framework.db.mapper.base.BaseMapper;
import com.zyplayer.doc.db.framework.db.mapper.base.ExecuteParam;
import com.zyplayer.doc.db.framework.db.mapper.base.ExecuteResult;
import com.zyplayer.doc.db.framework.db.mapper.base.SqlExecutor;
import com.zyplayer.doc.db.service.download.BaseDownloadService;
import com.zyplayer.doc.db.service.download.DownloadService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * 数据库的mapper持有对象接口
 *
 * @author 离狐千慕
 * @since 2023年8月8日
 */
public abstract class DbBaseService {
	private static final Logger logger = LoggerFactory.getLogger(DbBaseService.class);

	@Resource
	SqlExecutor sqlExecutor;
	@Resource
	DatabaseServiceFactory databaseServiceFactory;
	@Resource
	BaseDownloadService baseDownloadService;
	@Resource
	DatabaseRegistrationBean databaseRegistrationBean;

	/**
	 * 判断查看权和获取BaseMapper
	 *
	 * @author 离狐千慕
	 */
	public BaseMapper getViewAuthBaseMapper(Long sourceId) {
		this.judgeAuth(sourceId, DbAuthType.VIEW.getName(), "没有查看该库表信息的权限");
		BaseMapper baseMapper = databaseRegistrationBean.getBaseMapperById(sourceId);
		if (baseMapper == null) {
			throw new ConfirmException("未找到对应的数据库连接");
		}
		return baseMapper;
	}

	/**
	 * 权限判断
	 *
	 * @author 离狐千慕
	 */
	public void judgeAuth(Long sourceId, String authName, String noAuthInfo) {
		if (!DocUserUtil.haveAuth(DocAuthConst.DB_DATASOURCE_MANAGE)
				&& !DocUserUtil.haveCustomAuth(authName, DocSysType.DB.getType(), DocSysModuleType.Db.DATASOURCE.getType(), sourceId)) {
			throw new ConfirmException(noAuthInfo);
		}
	}

	/**
	 * 获取当前是什么数据源服务
	 *
	 * @return 服务类型
	 */
	public abstract DatabaseProductEnum getDatabaseProduct();

	/**
	 * 获取库列表
	 *
	 * @return 数据库列表
	 * @author 离狐千慕
	 * @since 2023年8月8日
	 */
	public TableDdlVo getTableDdl(Long sourceId, String dbName, String tableName) {
		// 需要各数据服务自己实现，各数据库产品的实现都不一样
		throw new ConfirmException("暂未支持的数据库类型");
	}

	/**
	 * 获取库列表
	 *
	 * @return 数据库列表
	 * @author 离狐千慕
	 * @since 2023年8月8日
	 */
	public List<DatabaseInfoDto> getDatabaseList(Long sourceId) {
		BaseMapper baseMapper = this.getViewAuthBaseMapper(sourceId);
		return baseMapper.getDatabaseList();
	}

	/**
	 * 获取表列表
	 *
	 * @param dbName 数据库名
	 * @return 数据库表列表
	 * @author 离狐千慕
	 * @since 2023年8月8日
	 */
	public List<TableInfoDto> getTableList(Long sourceId, String dbName) {
		BaseMapper baseMapper = this.getViewAuthBaseMapper(sourceId);
		return baseMapper.getTableList(dbName);
	}

	/**
	 * 获取字段列表
	 *
	 * @param dbName    数据库名
	 * @param tableName 表名
	 * @return 字段列表
	 * @author 离狐千慕
	 * @since 2023年8月8日
	 */
	public TableColumnVo getTableColumnList(Long sourceId, String dbName, String tableName) {
		BaseMapper baseMapper = this.getViewAuthBaseMapper(sourceId);
		List<TableColumnDescDto> columnDescDto = baseMapper.getTableColumnList(dbName, tableName);
		TableColumnVo tableColumnVo = new TableColumnVo();
		tableColumnVo.setColumnList(columnDescDto);
		// 表注释
		TableColumnVo.TableInfoVo tableInfoVo = new TableColumnVo.TableInfoVo();
		List<TableDescDto> tableDescList = baseMapper.getTableDescList(dbName, tableName);
		String description = null;
		if (!tableDescList.isEmpty()) {
			TableDescDto descDto = tableDescList.get(0);
			description = descDto.getDescription();
		}
		description = Optional.ofNullable(description).orElse("");
		tableInfoVo.setDescription(description);
		tableInfoVo.setTableName(tableName);
		tableColumnVo.setTableInfo(tableInfoVo);
		return tableColumnVo;
	}

	/**
	 * 模糊搜索表和字段
	 *
	 * @param dbName     数据库名
	 * @param searchText 搜索内容
	 * @return 表和字段信息
	 * @author 离狐千慕
	 * @since 2023年8月8日
	 */
	public List<QueryTableColumnDescDto> getTableAndColumnBySearch(Long sourceId, String dbName, String searchText) {
		BaseMapper baseMapper = this.getViewAuthBaseMapper(sourceId);
		searchText = "%" + searchText + "%";
		return baseMapper.getTableAndColumnBySearch(dbName, searchText);
	}

	/**
	 * 获取表注释
	 *
	 * @param tableName 可不传，传了只查询指定表的注释
	 * @return 表注释
	 * @author 离狐千慕
	 * @since 2023年8月8日
	 */
	public List<TableDescDto> getTableDescList(Long sourceId, String dbName, String tableName) {
		BaseMapper baseMapper = this.getViewAuthBaseMapper(sourceId);
		return baseMapper.getTableDescList(dbName, tableName);
	}

	/**
	 * 增加表注释
	 *
	 * @param tableName 表名
	 * @param newDesc   新的注释
	 * @author 离狐千慕
	 * @since 2023年8月8日
	 */
	public void updateTableDesc(Long sourceId, String dbName, String tableName, String newDesc) {
		BaseMapper baseMapper = this.getViewAuthBaseMapper(sourceId);
		baseMapper.updateTableDesc(dbName, tableName, newDesc);
	}

	/**
	 * 增加字段注释
	 *
	 * @param dbName     数据库名
	 * @param tableName  表名
	 * @param columnName 字段名
	 * @param newDesc    新的注释
	 * @author 离狐千慕
	 * @since 2023年8月8日
	 */
	public void updateTableColumnDesc(Long sourceId, String dbName, String tableName, String columnName, String newDesc) {
		BaseMapper baseMapper = this.getViewAuthBaseMapper(sourceId);
		baseMapper.updateTableColumnDesc(dbName, tableName, columnName, newDesc, null);
	}

	/**
	 * 获取表基本信息
	 *
	 * @param dbName    数据库名
	 * @param tableName 表名
	 * @author 离狐千慕
	 * @since 2023年9月1日
	 */
	public TableStatusVo getTableStatus(Long sourceId, String dbName, String tableName) {
		BaseMapper baseMapper = this.getViewAuthBaseMapper(sourceId);
		TableStatusVo tableStatusVo = baseMapper.getTableStatus(dbName, tableName);
		DatabaseFactoryBean factoryBean = databaseRegistrationBean.getOrCreateFactoryById(sourceId);
		tableStatusVo.setDbType(factoryBean.getDatabaseProduct().name().toLowerCase());
		return tableStatusVo;
	}

	/**
	 * 获取存储过程列表
	 *
	 * @param procedureParam 参数
	 * @author 离狐千慕
	 * @since 2023年4月24日
	 */
	public Long getProcedureCount(ProcedureListParam procedureParam) {
		BaseMapper baseMapper = this.getViewAuthBaseMapper(procedureParam.getSourceId());
		return baseMapper.getProcedureCount(procedureParam);
	}

	/**
	 * 获取存储过程列表
	 *
	 * @param procedureParam 参数
	 * @author 离狐千慕
	 * @since 2023年4月24日
	 */
	public List<ProcedureDto> getProcedureList(ProcedureListParam procedureParam) {
		BaseMapper baseMapper = this.getViewAuthBaseMapper(procedureParam.getSourceId());
		// MySQL是加%，其他数据库不一样的话需要改到各自的实现里面去
		if (StringUtils.isNotBlank(procedureParam.getName())) {
			procedureParam.setName("%" + procedureParam.getName() + "%");
		}
		return baseMapper.getProcedureList(procedureParam);
	}

	/**
	 * 获取存储过程详情
	 *
	 * @param dbName 数据库名
	 * @author 离狐千慕
	 * @since 2023年4月24日
	 */
	public ProcedureDto getProcedureDetail(Long sourceId, String dbName, String typeName, String procName) {
		BaseMapper baseMapper = this.getViewAuthBaseMapper(sourceId);
		return baseMapper.getProcedureDetail(dbName, typeName, procName);
	}

	/**
	 * 删除存储过程
	 *
	 * @param dbName 数据库名
	 * @author 离狐千慕
	 * @since 2023年4月24日
	 */
	public void deleteProcedure(Long sourceId, String dbName, String typeName, String procName) {
		BaseMapper baseMapper = this.getViewAuthBaseMapper(sourceId);
		baseMapper.deleteProcedure(dbName, typeName, procName);
	}

	/**
	 * 保存存储过程
	 *
	 * @param procSql 存储过程SQL
	 * @return
	 * @author 离狐千慕
	 * @since 2023年4月24日
	 */
	public ExecuteResult saveProcedure(Long sourceId, String dbName, String typeName, String procName, String procSql) {
		// 需要各数据服务自己实现，各数据库产品的实现都不一样
		throw new ConfirmException("暂未支持的数据库类型");
	}

	/**
	 * 获取全量数据查询的SQL
	 *
	 * @return 分页查询的SQL
	 * @author 离狐千慕
	 * @since 2023年4月24日
	 */
	public String getQueryAllSql(DataViewParam dataViewParam) {
		String queryColumns = StringUtils.defaultIfBlank(dataViewParam.getRetainColumn(), "*");
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append(String.format("select %s from %s.%s", queryColumns, dataViewParam.getDbName(), dataViewParam.getTableName()));
		if (StringUtils.isNotBlank(dataViewParam.getCondition())) {
			sqlSb.append(String.format(" where %s", dataViewParam.getCondition()));
		}
		return sqlSb.toString();
	}

	/**
	 * 获取分页查询的SQL
	 *
	 * @return 分页查询的SQL
	 * @author 离狐千慕
	 * @since 2023年4月24日
	 */
	public String getQueryPageSql(DataViewParam dataViewParam) {
		String queryColumns = StringUtils.defaultIfBlank(dataViewParam.getRetainColumn(), "*");
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append(String.format("select %s from %s.%s", queryColumns, dataViewParam.getDbName(), dataViewParam.getTableName()));
		if (StringUtils.isNotBlank(dataViewParam.getCondition())) {
			sqlSb.append(String.format(" where %s", dataViewParam.getCondition()));
		}
		if (StringUtils.isNotBlank(dataViewParam.getOrderColumn()) && StringUtils.isNotBlank(dataViewParam.getOrderType())) {
			sqlSb.append(String.format(" order by %s %s", dataViewParam.getOrderColumn(), dataViewParam.getOrderType()));
		}
		sqlSb.append(String.format(" limit %s offset %s", dataViewParam.getPageSize(), dataViewParam.getOffset()));
		return sqlSb.toString();
	}

	/**
	 * 获取分页查询的SQL
	 *
	 * @return 分页查询的SQL
	 * @author diantu
	 * @since 2023年2月22日
	 */
	public String getQueryPageSqlBySql(String sql,Integer pageSize,Integer pageNum) {
		Integer offset = (pageNum-1)*pageSize;
		String sqlSb = String.format("select * from (%s) r", sql) +
				String.format(" limit %s offset %s", pageSize, offset);
		return sqlSb;
	}

	/**
	 * 获取指定数据库的SQL
	 *
	 * @return use db
	 * @author 离狐千慕
	 * @since 2023年4月24日
	 */
	public String getUseDbSql(String dbName) {
		if (StringUtils.isNotBlank(dbName)) {
			return "use " + dbName;
		}
		return null;
	}

	/**
	 * 获取查询总条数的SQL
	 *
	 * @return 查询总条数的SQL
	 * @author 离狐千慕
	 * @since 2023年4月24日
	 */
	public String getQueryCountSql(DataViewParam dataViewParam) {
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append(String.format("select count(1) as counts from %s.%s", dataViewParam.getDbName(), dataViewParam.getTableName()));
		if (StringUtils.isNotBlank(dataViewParam.getCondition())) {
			sqlSb.append(String.format(" where %s", dataViewParam.getCondition()));
		}
		return sqlSb.toString();
	}

	/**
	 * 获取表数据
	 *
	 * @author 离狐千慕
	 * @since 2023年6月5日
	 */
	public String getDownloadTableData(DataViewParam param, ExecuteParam executeParam, List<TableColumnDescDto> dataCols, Set<String> conditionSet) throws Exception {
		DatabaseProductEnum databaseProduct = databaseServiceFactory.getDbBaseService(param.getSourceId()).getDatabaseProduct();
		DownloadService downloadService = databaseServiceFactory.getDownloadService(databaseProduct);
		if (Objects.equals(param.getDownloadType(), FormatDownloadConst.INSERT)) {
			return downloadService.downloadDataByInsert(param, executeParam, dataCols, conditionSet);
		} else if (Objects.equals(param.getDownloadType(), FormatDownloadConst.UPDATE)) {
			return downloadService.downloadDataByUpdate(param, executeParam, dataCols, conditionSet);
		} else if (Objects.equals(param.getDownloadType(), FormatDownloadConst.JSON)) {
			return downloadService.downloadDataByJson(param, executeParam, dataCols, conditionSet);
		}
		return null;
	}

	public void deleteTableLineData(Long sourceId, String dbName, String tableName, JSONArray lineJsonArr) {
		for (int i = 0; i < lineJsonArr.size(); i++) {
			JSONObject lineParam = lineJsonArr.getJSONObject(i);
			if (lineParam.isEmpty()) {
				logger.error("待删除行的条件参数为空，不允许删除");
				continue;
			}
			BaseMapper baseMapper = this.getViewAuthBaseMapper(sourceId);
			baseMapper.deleteTableLineData(dbName, tableName, lineParam);
		}
	}
}
