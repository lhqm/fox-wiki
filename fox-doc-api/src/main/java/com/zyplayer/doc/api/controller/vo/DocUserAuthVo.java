package com.zyplayer.doc.api.controller.vo;

import lombok.Data;

/**
 * 用户权限返回值对象
 *
 * @author 离狐千慕
 * @since 2023年12月12日
 */
@Data
public class DocUserAuthVo {

	/**
	 * 权限类型
	 */
	private Integer authType;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 用户编号，用于登录等
	 */
	private String userNo;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 性别 0=女 1=男
	 */
	private Integer sex;
}
