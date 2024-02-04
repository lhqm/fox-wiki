package com.zyplayer.doc.manage.framework.upgrade;

import lombok.*;

/**
 * 升级信息对象
 *
 * @author 离狐千慕
 * @since 2019年4月27日
 */
@Data
public class UpgradeInfo {
	private String nowVersion;
	private String lastVersion;
	private String upgradeContent;
	private String upgradeUrl;
	private String nextStep;

}
