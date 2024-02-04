package com.zyplayer.doc.db.framework.db.dto;

import lombok.Data;

/**
 * 字段信息
 *
 * @author 离狐千慕
 * @since 2023-11-27
 */
@Data
public class ColumnInfoDto {
	private String isNullable;
	private String columnType;
	private String columnDefault;
	private String extra;
}
