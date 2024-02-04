
package com.zyplayer.doc.api.controller.vo;

import com.google.common.collect.ComparisonChain;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * swagger资源信息
 *
 * @author 离狐千慕
 * @since 2023-11-04
 */
@Data
@NoArgsConstructor
public class SwaggerResourceVo implements Comparable<SwaggerResourceVo> {
	private String name;
	private String url;
	private String swaggerVersion;
	private String rewriteDomain;

	@Override
	public int compareTo(SwaggerResourceVo other) {
		return ComparisonChain.start().compare(this.swaggerVersion, other.swaggerVersion).compare(this.name, other.name).result();
	}
}
