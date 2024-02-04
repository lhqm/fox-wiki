package com.zyplayer.doc.data.service.manage;

import com.zyplayer.doc.data.repository.manage.entity.WikiPageHistory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 离狐千慕
 * @since 2023-09-05
 */
public interface WikiPageHistoryService extends IService<WikiPageHistory> {
	
	/**
	 * 保存或更新
	 */
	WikiPageHistory saveRecord(Long spaceId, Long pageId, String content);
}
