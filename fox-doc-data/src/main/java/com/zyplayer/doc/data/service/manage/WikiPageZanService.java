package com.zyplayer.doc.data.service.manage;

import com.zyplayer.doc.data.repository.manage.entity.WikiPageZan;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 离狐千慕
 * @since 2023-03-05
 */
public interface WikiPageZanService extends IService<WikiPageZan> {
	void zanPage(WikiPageZan wikiPageZan);
}
