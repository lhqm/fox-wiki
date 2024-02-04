package com.zyplayer.doc.db.framework.db.mapper.base;

import java.util.Map;

/**
 * 执行结果回调
 *
 * @author 离狐千慕
 * @since 2023年8月18日
 */
public interface ResultHandler {
	
	void handleResult(Map<String, Object> resultMap);
	
}
