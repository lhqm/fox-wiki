package com.zyplayer.doc.db.framework.db.bean;

import lombok.Data;

/**
 * 数据库配置信息
 * @author 离狐千慕
 * @since 2023年8月8日
 */
@Data
public class DbConfigBean {
	private String driverClassName;
	private String url;
	private String username;
	private String password;
}
