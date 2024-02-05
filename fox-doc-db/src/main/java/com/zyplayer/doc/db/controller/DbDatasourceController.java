package com.zyplayer.doc.db.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyplayer.doc.core.annotation.AuthMan;
import com.zyplayer.doc.core.exception.ConfirmException;
import com.zyplayer.doc.data.config.security.DocUserDetails;
import com.zyplayer.doc.data.config.security.DocUserUtil;
import com.zyplayer.doc.data.repository.manage.entity.DbDatasource;
import com.zyplayer.doc.data.repository.support.consts.DocAuthConst;
import com.zyplayer.doc.data.service.manage.DbDatasourceService;
import com.zyplayer.doc.db.framework.configuration.DatasourceUtil;
import com.zyplayer.doc.db.framework.db.bean.DatabaseFactoryBean;
import com.zyplayer.doc.db.framework.db.bean.DatabaseRegistrationBean;
import com.zyplayer.doc.db.framework.json.DocDbResponseJson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据源控制器
 *
 * @author 离狐千慕
 * @since 2023年6月29日
 */
@RestController
@CrossOrigin
@AuthMan(DocAuthConst.DB_DATASOURCE_MANAGE)
@RequestMapping("/zyplayer-doc-db/datasource")
public class DbDatasourceController {

	@Resource
	DatabaseRegistrationBean databaseRegistrationBean;
	@Resource
	DbDatasourceService dbDatasourceService;

	@PostMapping(value = "/list")
	public DocDbResponseJson list(Integer pageNum, Integer pageSize, String name, String groupName) {
		QueryWrapper<DbDatasource> wrapper = new QueryWrapper<>();
		wrapper.eq("yn", 1);
		wrapper.eq(StringUtils.isNotBlank(groupName), "group_name", groupName);
		wrapper.like(StringUtils.isNotBlank(name), "name", "%" + name + "%");
		IPage<DbDatasource> page = new Page<>(pageNum, pageSize, pageNum == 1);
		dbDatasourceService.page(page, wrapper);
		for (DbDatasource dbDatasource : page.getRecords()) {
			dbDatasource.setSourcePassword("***");
		}
		return DocDbResponseJson.ok(page);
	}

	@PostMapping(value = "/groups")
	public DocDbResponseJson groups() {
		QueryWrapper<DbDatasource> wrapper = new QueryWrapper<>();
		wrapper.eq("yn", 1);
		wrapper.isNotNull("group_name");
		wrapper.select("group_name");
		wrapper.groupBy("group_name");
		List<DbDatasource> datasourceList = dbDatasourceService.list(wrapper);
		if (CollectionUtils.isEmpty(datasourceList)) {
			return DocDbResponseJson.ok();
		}
		Set<String> groupNameSet = datasourceList.stream().map(DbDatasource::getGroupName).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
		return DocDbResponseJson.ok(groupNameSet);
	}

	@PostMapping(value = "/test")
	public DocDbResponseJson test(DbDatasource dbDatasource) {
		// 验证新的数据源
		try {
			// 获取原始密码
			if (Objects.equals("***", dbDatasource.getSourcePassword()) && dbDatasource.getId() != null) {
				DbDatasource dbDatasourceSel = dbDatasourceService.getById(dbDatasource.getId());
				if (dbDatasourceSel != null) {
					dbDatasource.setSourcePassword(dbDatasourceSel.getSourcePassword());
				}
			}
			DatabaseFactoryBean databaseFactoryBean = DatasourceUtil.createDatabaseFactoryBean(dbDatasource, true);
			databaseFactoryBean.getDataSource().close();
		} catch (ConfirmException e) {
			return DocDbResponseJson.warn(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return DocDbResponseJson.warn(ExceptionUtils.getStackTrace(e));
		}
		return DocDbResponseJson.ok();
	}

	@PostMapping(value = "/update")
	public DocDbResponseJson update(DbDatasource dbDatasource) {
		if (StringUtils.isBlank(dbDatasource.getName())) {
			return DocDbResponseJson.warn("名字必填");
		} else if (StringUtils.isBlank(dbDatasource.getDriverClassName())) {
			return DocDbResponseJson.warn("驱动类必选");
		} else if (StringUtils.isBlank(dbDatasource.getSourceUrl())) {
			return DocDbResponseJson.warn("地址必填");
		} else if (StringUtils.isBlank(dbDatasource.getSourceName())) {
			return DocDbResponseJson.warn("用户名必填");
		} else if (StringUtils.isBlank(dbDatasource.getSourcePassword())) {
			return DocDbResponseJson.warn("密码必填");
		}
		if (Objects.equals("***", dbDatasource.getSourcePassword())) {
			dbDatasource.setSourcePassword(null);
		}
		// 这三项不需要修改
		dbDatasource.setCreateTime(null);
		dbDatasource.setCreateUserId(null);
		dbDatasource.setCreateUserName(null);
		Long sourceId = Optional.ofNullable(dbDatasource.getId()).orElse(0L);
		if (sourceId > 0) {
			dbDatasourceService.updateById(dbDatasource);
			// 关闭旧数据源
			databaseRegistrationBean.closeDatasource(dbDatasource.getId());
		} else {
			DocUserDetails currentUser = DocUserUtil.getCurrentUser();
			dbDatasource.setCreateTime(new Date());
			dbDatasource.setCreateUserId(currentUser.getUserId());
			dbDatasource.setCreateUserName(currentUser.getUsername());
			dbDatasource.setYn(1);
			dbDatasourceService.save(dbDatasource);
		}
		return DocDbResponseJson.ok();
	}
}
