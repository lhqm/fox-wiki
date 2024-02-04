package com.zyplayer.doc.data.service.manage;

import com.zyplayer.doc.data.repository.manage.entity.DbTableRelation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyplayer.doc.data.repository.manage.param.TableRelationParam;

/**
 * <p>
 * 表关系 服务类
 * </p>
 *
 * @author 离狐千慕
 * @since 2023-06-07
 */
public interface DbTableRelationService extends IService<DbTableRelation> {
	
	void update(TableRelationParam param);
	
}
