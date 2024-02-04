package com.zyplayer.doc.data.service.manage;

import com.zyplayer.doc.data.repository.manage.entity.WikiSpaceFavorite;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户空间收藏记录表 服务类
 * </p>
 *
 * @author 离狐千慕
 * @since 2023-02-09
 */
public interface WikiSpaceFavoriteService extends IService<WikiSpaceFavorite> {
	
	List<WikiSpaceFavorite> myFavoriteSpaceList();
}
