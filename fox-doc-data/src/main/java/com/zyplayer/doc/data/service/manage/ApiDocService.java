package com.zyplayer.doc.data.service.manage;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyplayer.doc.data.repository.manage.entity.ApiDoc;
import com.zyplayer.doc.data.repository.manage.vo.ApiDocVo;

import java.util.List;

/**
 * <p>
 * api文档地址 服务类
 * </p>
 *
 * @author 离狐千慕
 * @since 2023-11-25
 */
public interface ApiDocService extends IService<ApiDoc> {
	/**
	 * 用于管理列表搜索
	 *
	 * @author 离狐千慕
	 * @since 2023-12-12
	 */
	IPage<ApiDocVo> getApiDocList(ApiDoc apiDoc, Integer pageNum, Integer pageSize);
	
	/**
	 * 用于获取左侧可下拉接口列表
	 *
	 * @author 离狐千慕
	 * @since 2023-12-12
	 */
	List<ApiDoc> getApiDocList();
	
	/**
	 * 通过开放文档uuid查询
	 *
	 * @author 离狐千慕
	 * @since 2023-12-12
	 */
	ApiDoc getByShareUuid(String shareUuid);
}
