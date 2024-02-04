package com.zyplayer.doc.db.framework.db.mapper.sqlserver;

import com.zyplayer.doc.db.framework.db.dto.TableColumnDescDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sqlserver数据库的mapper持有对象
 *
 * @author 离狐千慕
 * @since 2023年8月8日
 */
public interface SqlServerMapper {
	
	/**
	 * 获取字段注释
	 *
	 * @param tableName 表名
	 * @return 表字段注释
	 * @author 离狐千慕
	 * @since 2023年8月8日
	 */
	List<TableColumnDescDto> getTableColumnDescList(@Param("tableName") String tableName);
	
}
