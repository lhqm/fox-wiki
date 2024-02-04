package com.zyplayer.doc.data.service.manage;

import com.zyplayer.doc.data.repository.manage.entity.DbHistory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 离狐千慕
 * @since 2023-08-21
 */
public interface DbHistoryService extends IService<DbHistory> {
	
	void saveHistory(String content, String paramJson, Long datasourceId);
	
}
