package com.zyplayer.doc.db.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zyplayer.doc.core.annotation.AuthMan;
import com.zyplayer.doc.data.config.security.DocUserDetails;
import com.zyplayer.doc.data.config.security.DocUserUtil;
import com.zyplayer.doc.data.repository.manage.entity.DbTransferTask;
import com.zyplayer.doc.data.service.manage.DbTransferTaskService;
import com.zyplayer.doc.db.framework.db.transfer.SqlParseUtil;
import com.zyplayer.doc.db.framework.db.transfer.TransferDataServer;
import com.zyplayer.doc.db.framework.json.DocDbResponseJson;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 数据互导工具
 *
 * @author 离狐千慕
 * @since 2023年9月28日
 */
@AuthMan
@RestController
@CrossOrigin
@RequestMapping("/zyplayer-doc-db/transfer")
public class DbTransferDataController {

	@Resource
	TransferDataServer transferDataServer;
	@Resource
	DbTransferTaskService dbTransferTaskService;

	@PostMapping(value = "/start")
	public DocDbResponseJson doTransfer(Long id) {
		transferDataServer.transferData(id);
		return DocDbResponseJson.ok();
	}

	@PostMapping(value = "/cancel")
	public DocDbResponseJson cancel(Long id) {
		transferDataServer.cancel(id);
		return DocDbResponseJson.ok();
	}

	@PostMapping(value = "/list")
	public DocDbResponseJson list() {
		QueryWrapper<DbTransferTask> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("del_flag", 0);
		queryWrapper.select(
				"id", "name", "query_datasource_id", "storage_datasource_id", "need_count", "last_execute_status",
				"query_sql", "storage_sql", "last_execute_time", "create_user_name", "create_time"
		);
		List<DbTransferTask> taskList = dbTransferTaskService.list(queryWrapper);
		return DocDbResponseJson.ok(taskList);
	}

	@PostMapping(value = "/detail")
	public DocDbResponseJson detail(Long id) {
		DbTransferTask transferTask = dbTransferTaskService.getById(id);
		return DocDbResponseJson.ok(transferTask);
	}

	@PostMapping(value = "/update")
	public DocDbResponseJson update(DbTransferTask transferTask) {
		DbTransferTask transferTaskUp = new DbTransferTask();
		if (transferTask.getId() == null) {
			DocUserDetails currentUser = DocUserUtil.getCurrentUser();
			transferTaskUp.setCreateTime(new Date());
			transferTaskUp.setCreateUserId(currentUser.getUserId());
			transferTaskUp.setCreateUserName(currentUser.getUsername());
			transferTaskUp.setDelFlag(0);
		} else {
			transferTaskUp.setId(transferTask.getId());
		}
		transferTaskUp.setName(transferTask.getName());
		transferTaskUp.setQueryDatasourceId(transferTask.getQueryDatasourceId());
		transferTaskUp.setStorageDatasourceId(transferTask.getStorageDatasourceId());
		transferTaskUp.setQuerySql(transferTask.getQuerySql());
		transferTaskUp.setStorageSql(transferTask.getStorageSql());
		transferTaskUp.setNeedCount(transferTask.getNeedCount());
		transferTaskUp.setDelFlag(transferTask.getDelFlag());
		dbTransferTaskService.saveOrUpdate(transferTaskUp);
		return DocDbResponseJson.ok();
	}

	@PostMapping("/sqlColumns")
	public DocDbResponseJson sqlColumns(String sql) {
		List<String> selectNames = SqlParseUtil.getSelectNames(sql);
		return DocDbResponseJson.ok(selectNames);
	}
}
