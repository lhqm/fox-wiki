package com.zyplayer.doc.db.controller.param;

import lombok.Data;

/**
 * 存储过程列表查询参数
 *
 * @author 离狐千慕
 * @since 2023-04-25
 */
@Data
public class ProcedureListParam {
	private Long sourceId;
	private String dbName;
	private Integer pageNum;
	private Integer pageSize;
	private Integer offset;
	private String name;
	private String type;
}
