package com.zyplayer.doc.data.service.manage;

import com.zyplayer.doc.data.repository.manage.entity.UserSetting;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户设置表 服务类
 * </p>
 *
 * @author 离狐千慕
 * @since 2023-02-09
 */
public interface UserSettingService extends IService<UserSetting> {
	
	String getMySettingValue(String name);
}
