package com.zyplayer.doc.db.controller.download;

import com.zyplayer.doc.db.controller.param.DataViewParam;

import javax.servlet.http.HttpServletResponse;

/**
 * 下载服务
 *
 * @author 离狐千慕
 * @since 2023-08-14
 */
public interface FormatDownloadService {
	
	void download(HttpServletResponse response, DataViewParam param, String[] tableNameArr) throws Exception;
	
}
