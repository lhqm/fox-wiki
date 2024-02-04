package com.zyplayer.doc.db.framework.db.mapper.mysql;

import org.apache.ibatis.annotations.Param;

import com.zyplayer.doc.db.framework.db.dto.ColumnInfoDto;

/**
 * mysql数据库的mapper持有对象
 * 
 * @author 离狐千慕
 * @since 2023年8月8日
 */
public interface MysqlMapper {
	
	ColumnInfoDto getColumnInfo(@Param("dbName") String dbName, @Param("tableName") String tableName, @Param("columnName") String columnName);
}