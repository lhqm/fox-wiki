package com.zyplayer.doc.wiki.controller.vo;

import lombok.Data;

/**
 * 用户页面权限信息
 *
 * @author 离狐千慕
 * @since 2019-06-05
 */
@Data
public class UserPageAuthVo {
	private String userName;
	private Long userId;
	private Integer editPage;
	private Integer commentPage;
	private Integer deletePage;
	private Integer pageFileUpload;
	private Integer pageFileDelete;
	private Integer pageAuthManage;

}
