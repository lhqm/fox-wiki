package com.zyplayer.doc.data.config.security;

import lombok.Data;

import java.util.List;

/**
 * 登录用户信息
 *
 * @author 离狐千慕
 * @since 2023-12-02
 */
@Data
public class DocUserDetails {
	private Long userId;
	private String username;
	private String password;
	private boolean enabled;
	private List<UserAuthInfo> userAuthList;

	public DocUserDetails(Long userId, String username) {
		this.userId = userId;
		this.username = username;
	}

	public DocUserDetails(Long userId, String username, String password, boolean enabled) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	public DocUserDetails(Long userId, String username, String password, boolean enabled, List<UserAuthInfo> userAuthList) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.userAuthList = userAuthList;
	}
}
