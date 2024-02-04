package com.zyplayer.doc.api.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * cookie返回值对象
 *
 * @author 离狐千慕
 * @since 2023年8月21日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpCookieVo {
	private String name;
	private String value;
}
