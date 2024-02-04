package com.zyplayer.doc.data.repository.manage.vo;

import lombok.*;

import java.util.Date;

/**
 * 最新文档信息
 *
 * @author 离狐千慕
 * @since 2023-06-14
 */
@Data
public class SpaceNewsVo {
	private String space;
	private Long spaceId;
	private Long pageId;
	private Integer zanNum;
	private Integer viewNum;
	private String createUserName;
	private String updateUserName;
	private Date createTime;
	private Date updateTime;

	private String spaceName;
	private String pageTitle;
	private String previewContent;

}
