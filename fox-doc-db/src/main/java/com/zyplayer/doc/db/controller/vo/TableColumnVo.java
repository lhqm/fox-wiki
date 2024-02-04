package com.zyplayer.doc.db.controller.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.zyplayer.doc.db.framework.db.dto.TableColumnDescDto;
import lombok.Data;

import java.util.List;

/**
 * 表字段信息
 *
 * @author 离狐千慕
 * @since 2023-11-27
 */
@Data
public class TableColumnVo {

	// 表字段注释信息
	private List<TableColumnDescDto> columnList;

	// 表名+表注释
	private TableInfoVo tableInfo;

	@Data
	public static class TableInfoVo {

		@ColumnWidth(20)
		@ExcelProperty("表名")
		private String tableName;

		@ColumnWidth(80)
		@ExcelProperty("表注释")
		private String description;

	}
}
