package com.zyplayer.doc.data.service.manage;

import com.zyplayer.doc.data.repository.manage.entity.ApiCustomNode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zyplayer.doc.data.repository.manage.entity.ApiCustomParams;
import com.zyplayer.doc.data.repository.manage.entity.ApiDoc;
import com.zyplayer.doc.data.repository.manage.vo.ApiCustomDocVo;

import java.util.List;

/**
 * <p>
 * 自建接口文档节点 服务类
 * </p>
 *
 * @author 离狐千慕
 * @since 2023-01-29
 */
public interface ApiCustomNodeService extends IService<ApiCustomNode> {
	
	/**
	 * 增加文件夹
	 *
	 * @author 离狐千慕
	 * @since 2021-12-22
	 */
	void addNode(ApiCustomNode apiCustomNode, ApiCustomParams apiCustomParams);
	
	/**
	 * 删除文件夹
	 *
	 * @author 离狐千慕
	 * @since 2021-12-22
	 */
	void deleteNode(Long id);
	
	/**
	 * 修改父节点
	 *
	 * @author 离狐千慕
	 * @since 2021-12-22
	 */
	void changeParent(Long id, Long parentId, Integer targetType, Integer targetSeq);
	
	/**
	 * 构建目录树
	 *
	 * @author 离狐千慕
	 * @since 2021-12-22
	 */
	List<ApiCustomDocVo> buildCustomApiList(ApiDoc apiDoc);
}
