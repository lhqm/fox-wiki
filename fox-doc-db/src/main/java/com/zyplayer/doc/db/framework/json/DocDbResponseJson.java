package com.zyplayer.doc.db.framework.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 数据库文档返回数据格式
 *
 * @author 离狐千慕
 * @since 2023年8月8日
 */
@Data
public class DocDbResponseJson {
	private static SerializeConfig mapping = new SerializeConfig();
	static {
		mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
	}
	private Integer errCode;
	private String errMsg;
	private Object data;
	private Long total;

	public DocDbResponseJson() {
		this.errCode = 200;
	}

	public DocDbResponseJson(Object data) {
		this.setData(data);
		this.errCode = 200;
	}

	public DocDbResponseJson(int errCode, String errMsg) {
		super();
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public DocDbResponseJson(int errCode, String errMsg, Object data) {
		super();
		this.setData(data);
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public DocDbResponseJson(Integer errCode) {
		super();
		this.errCode = errCode;
	}

	public void setData(Object data) {
		if (null != data) {
			if (data instanceof IPage) {
				IPage<?> iPage = (IPage<?>) data;
				this.data = iPage.getRecords();
				this.total = iPage.getTotal();
			} else {
				this.data = data;
			}
		}
	}

	/**
	 * 提示语
	 *
	 * @author 离狐千慕
	 * @since 2023年8月7日
	 * @param errMsg 提示信息
	 * @return 对象
	 */
	public static DocDbResponseJson warn(String errMsg) {
		return new DocDbResponseJson(300, errMsg);
	}

	/**
	 * 错误
	 *
	 * @author 离狐千慕
	 * @since 2023年8月7日
	 * @param errMsg 错误信息
	 * @return 对象
	 */
	public static DocDbResponseJson error(String errMsg) {
		return new DocDbResponseJson(500, errMsg);
	}

	/**
	 * 成功的返回方法
	 *
	 * @author 离狐千慕
	 * @since 2023年8月7日
	 * @return 对象
	 */
	public static DocDbResponseJson ok() {
		return new DocDbResponseJson();
	}

	/**
	 * 成功的返回方法
	 *
	 * @author 离狐千慕
	 * @since 2023年8月7日
	 * @param data 数据
	 * @return 对象
	 */
	public static DocDbResponseJson ok(Object data) {
		return new DocDbResponseJson(data);
	}

	public String toJson() {
		return JSON.toJSONString(this, mapping);
	}

	public void send(HttpServletResponse response) {
		try {
			response.setStatus(200);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			response.getWriter().write(toJson());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "DefaultResponseJson [errCode=" + errCode + ", errMsg=" + errMsg + ", data=" + data + "]";
	}

}
