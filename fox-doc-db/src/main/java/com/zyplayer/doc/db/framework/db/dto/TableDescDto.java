package com.zyplayer.doc.db.framework.db.dto;

import lombok.Data;

/**
 * 表注释信息
 *
 * @author 离狐千慕
 * @since 2023-11-27
 */
@Data
public class TableDescDto {
	private String tableName;
	private String description;
	private String majorId;
}
