package com.zyplayer.doc.db.framework.db.dto;

import lombok.Data;

/**
 * 表字段注释信息
 *
 * @author 离狐千慕
 * @since 2023-11-27
 */
@Data
public class QueryTableColumnDescDto {
	private String tableName;
	private String columnName;
	private String description;
}
