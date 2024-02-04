package com.zyplayer.doc.api.controller;

import com.zyplayer.doc.api.framework.utils.SwaggerDocUtil;
import com.zyplayer.doc.api.service.SwaggerHttpRequestService;
import com.zyplayer.doc.core.json.DocResponseJson;
import com.zyplayer.doc.core.json.ResponseJson;
import com.zyplayer.doc.data.repository.manage.entity.ApiDoc;
import com.zyplayer.doc.data.service.manage.ApiDocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * 文档控制器
 *
 * @author 离狐千慕
 * @since 2023年10月16日
 */
@RestController
@RequestMapping("/doc-api/share")
public class ApiShareDocumentController {
	private static final Logger logger = LoggerFactory.getLogger(ApiShareDocumentController.class);
	
	@Resource
	private ApiDocService swaggerDocService;
	@Resource
	private SwaggerHttpRequestService swaggerHttpRequestService;
	
	/**
	 * 获取文档内容
	 *
	 * @return 文档内容
	 * @author 离狐千慕
	 * @since 2023年10月16日
	 */
	@ResponseBody
	@PostMapping(value = "/detail")
	public ResponseJson<List<ApiDoc>> detail(String shareUuid) {
		ApiDoc apiDoc = swaggerDocService.getByShareUuid(shareUuid);
		if (apiDoc == null) {
			return DocResponseJson.warn("文档不存在");
		}
		apiDoc.setDocUrl(null);
		apiDoc.setJsonContent(null);
		return DocResponseJson.ok(apiDoc);
	}
	
	@RequestMapping("/apis/detail")
	public ResponseJson<Object> detail(HttpServletRequest request, String shareUuid) {
		ApiDoc apiDoc = swaggerDocService.getByShareUuid(shareUuid);
		if (apiDoc == null) {
			return DocResponseJson.warn("文档不存在");
		}
		if (Objects.equals(apiDoc.getDocType(), 1)) {
			try {
				String docsDomain = SwaggerDocUtil.getV2ApiDocsDomain(apiDoc.getDocUrl());
				String contentStr = swaggerHttpRequestService.requestSwaggerUrl(request, apiDoc.getId(), apiDoc.getDocUrl(), docsDomain);
				return DocResponseJson.ok(contentStr);
			} catch (Exception e) {
				e.printStackTrace();
				return DocResponseJson.warn("请求文档失败");
			}
		}
		if (Objects.equals(apiDoc.getDocType(), 2) || Objects.equals(apiDoc.getDocType(), 4)) {
			return DocResponseJson.ok(apiDoc.getJsonContent());
		}
		return DocResponseJson.warn("暂不支持的文档类型");
	}
}
