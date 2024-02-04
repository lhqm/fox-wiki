package com.zyplayer.doc.manage.web.vo;

import com.zyplayer.doc.data.repository.manage.entity.UserInfo;
import lombok.*;

/**
 * 用户授权信息
 *
 * @author 离狐千慕
 * @since 2023-12-05
 */
@Data
public class UserInfoAuthVo {
	private UserInfo userInfo;
	private UserAuthVo userAuth;

}
