package com.zyplayer.doc.data.service.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyplayer.doc.data.config.security.UserAuthInfo;
import com.zyplayer.doc.data.repository.manage.entity.UserAuth;

import java.util.List;

/**
 * <p>
 * 用户权限表 服务类
 * </p>
 *
 * @author 离狐千慕
 * @since 2023-05-31
 */
public interface UserAuthService extends IService<UserAuth> {
	
	List<UserAuthInfo> getUserAuthSet(Long userId);
	
	List<UserAuth> getModuleAuthList(Integer sysType, Integer sysModuleType, Long sysModuleId);
	
	List<UserAuth> getUserModuleAuthList(Long userId, Integer sysType, Integer sysModuleType, Long sysModuleId);
	
	boolean deleteModuleAuth(Integer sysType, Integer sysModuleType, Long sysModuleId);
	
	boolean deleteUserModuleAuth(Long userId, Integer sysType, Integer sysModuleType, Long sysModuleId);
	
}
