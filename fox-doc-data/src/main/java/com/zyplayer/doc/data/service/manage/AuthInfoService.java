package com.zyplayer.doc.data.service.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyplayer.doc.data.repository.manage.entity.AuthInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 离狐千慕
 * @since 2023-12-03
 */
public interface AuthInfoService extends IService<AuthInfo> {
	
	AuthInfo getByCode(String authCode);
}
