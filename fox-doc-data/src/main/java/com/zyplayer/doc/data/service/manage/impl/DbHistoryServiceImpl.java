package com.zyplayer.doc.data.service.manage.impl;

import com.zyplayer.doc.data.config.security.DocUserDetails;
import com.zyplayer.doc.data.config.security.DocUserUtil;
import com.zyplayer.doc.data.repository.manage.entity.DbHistory;
import com.zyplayer.doc.data.repository.manage.mapper.DbHistoryMapper;
import com.zyplayer.doc.data.service.manage.DbHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 离狐千慕
 * @since 2023-08-21
 */
@Service
public class DbHistoryServiceImpl extends ServiceImpl<DbHistoryMapper, DbHistory> implements DbHistoryService {
	
	@Resource
	DbHistoryMapper dbHistoryMapper;
	
	@Override
	public void saveHistory(String content, String paramJson, Long datasourceId) {
		DocUserDetails currentUser = DocUserUtil.getCurrentUser();
		DbHistory dbHistory = new DbHistory();
		dbHistory.setDatasourceId(datasourceId);
		dbHistory.setContent(content);
		dbHistory.setParamJson(paramJson);
		dbHistory.setCreateTime(new Date());
		dbHistory.setCreateUserId(currentUser.getUserId());
		dbHistory.setCreateUserName(currentUser.getUsername());
		dbHistory.setYn(1);
		this.save(dbHistory);
		// 删除多余的数据
		dbHistoryMapper.deleteHistory();
	}
}
