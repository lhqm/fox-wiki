package com.zyplayer.doc.db.framework.utils;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.sql.Timestamp;
import java.util.Date;

/**
 * json工具类
 *
 * @author 离狐千慕
 * @since 2023-05-20
 */
public class JSONUtil {
	
	public static final SerializeConfig serializeConfig = new SerializeConfig();
	
	static {
		serializeConfig.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
		serializeConfig.put(Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
	}
}
