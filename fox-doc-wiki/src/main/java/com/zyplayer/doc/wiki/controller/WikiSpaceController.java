package com.zyplayer.doc.wiki.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyplayer.doc.core.annotation.AuthMan;
import com.zyplayer.doc.core.json.DocResponseJson;
import com.zyplayer.doc.core.json.ResponseJson;
import com.zyplayer.doc.data.config.security.DocUserDetails;
import com.zyplayer.doc.data.config.security.DocUserUtil;
import com.zyplayer.doc.data.repository.manage.entity.UserGroupAuth;
import com.zyplayer.doc.data.repository.manage.entity.UserSetting;
import com.zyplayer.doc.data.repository.manage.entity.WikiSpace;
import com.zyplayer.doc.data.repository.manage.entity.WikiSpaceFavorite;
import com.zyplayer.doc.data.repository.support.consts.DocSysType;
import com.zyplayer.doc.data.repository.support.consts.UserSettingConst;
import com.zyplayer.doc.data.service.manage.UserGroupAuthService;
import com.zyplayer.doc.data.service.manage.UserSettingService;
import com.zyplayer.doc.data.service.manage.WikiSpaceFavoriteService;
import com.zyplayer.doc.data.service.manage.WikiSpaceService;
import com.zyplayer.doc.wiki.controller.vo.UserSpaceAuthVo;
import com.zyplayer.doc.wiki.controller.vo.WikiSpaceVo;
import com.zyplayer.doc.wiki.framework.consts.WikiAuthType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 文档控制器
 *
 * @author 离狐千慕
 * @since 2019年2月17日
 */
@Slf4j
@AuthMan
@RestController
@RequestMapping("/zyplayer-doc-wiki/space")
@RequiredArgsConstructor
public class WikiSpaceController {
	
	private final WikiSpaceService wikiSpaceService;
	private final UserGroupAuthService userGroupAuthService;
	private final WikiSpaceFavoriteService wikiSpaceFavoriteService;
	private final UserSettingService userSettingService;
	
	@PostMapping("/list")
	public ResponseJson<List<WikiSpaceVo>> list(WikiSpace wikiSpace, Integer ignoreFavorite, Long pageNum, Long pageSize) {
		DocUserDetails currentUser = DocUserUtil.getCurrentUser();
		LambdaQueryWrapper<WikiSpace> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(WikiSpace::getDelFlag, 0);
		wrapper.eq(wikiSpace.getId() != null, WikiSpace::getId, wikiSpace.getId());
		wrapper.and(con -> con.and(conSub -> conSub.eq(WikiSpace::getType, 3).eq(WikiSpace::getCreateUserId, currentUser.getUserId())).or().in(WikiSpace::getType, 1, 2));
		// 收藏的空间
		List<WikiSpaceFavorite> favoriteList = wikiSpaceFavoriteService.myFavoriteSpaceList();
		Set<Long> favoriteSpaceIds = favoriteList.stream().map(WikiSpaceFavorite::getSpaceId).collect(Collectors.toSet());
		// 只展示收藏的空间
		if (!Objects.equals(ignoreFavorite, 1)) {
			String onlyShowFavorite = userSettingService.getMySettingValue(UserSettingConst.WIKI_ONLY_SHOW_FAVORITE);
			if (Objects.equals(onlyShowFavorite, "1")) {
				if (favoriteSpaceIds.isEmpty()) {
					return DocResponseJson.ok();
				}
				wrapper.in(WikiSpace::getId, favoriteSpaceIds);
			}
		}
		pageNum = Optional.ofNullable(pageNum).orElse(1L);
		pageSize = Optional.ofNullable(pageSize).orElse(500L);
		pageNum = Math.min(Math.max(pageNum, 1L), 1000);
		pageSize = Math.min(Math.max(pageSize, 10L), 100);
		IPage<WikiSpace> page = new Page<>(pageNum, pageSize, Objects.equals(pageNum, 1L));
		wikiSpaceService.page(page, wrapper);
		// 设置收藏状态
		List<WikiSpaceVo> spaceVoList = page.getRecords().stream().map(WikiSpaceVo::new).collect(Collectors.toList());
		for (WikiSpaceVo spaceVo : spaceVoList) {
			spaceVo.setFavorite(favoriteSpaceIds.contains(spaceVo.getId()) ? 1 : 0);
		}
		DocResponseJson<List<WikiSpaceVo>> responseJson = DocResponseJson.ok(spaceVoList);
		responseJson.setTotal(page.getTotal());
		return responseJson;
	}
	
	@PostMapping("/update")
	public ResponseJson<WikiSpace> update(WikiSpace wikiSpace) {
		Long id = wikiSpace.getId();
		DocUserDetails currentUser = DocUserUtil.getCurrentUser();
		if (id != null && id > 0) {
			WikiSpace wikiSpaceSel = wikiSpaceService.getById(id);
			// 不是创建人不能修改空间
			if (!Objects.equals(currentUser.getUserId(), wikiSpaceSel.getCreateUserId())) {
				return DocResponseJson.warn("您没有该空间的编辑权！");
			}
			wikiSpace.setUuid(null);
			wikiSpace.setEditType(null);
			wikiSpaceService.updateById(wikiSpace);
		} else {
			wikiSpace.setUuid(IdUtil.simpleUUID());
			wikiSpace.setCreateTime(new Date());
			wikiSpace.setCreateUserId(currentUser.getUserId());
			wikiSpace.setCreateUserName(currentUser.getUsername());
			wikiSpaceService.save(wikiSpace);
		}
		return DocResponseJson.ok(wikiSpace);
	}
	
	@PostMapping("/setting/update")
	public ResponseJson<WikiSpace> settingUpdate(String name, String value) {
		DocUserDetails currentUser = DocUserUtil.getCurrentUser();
		QueryWrapper<UserSetting> wrapper = new QueryWrapper<>();
		wrapper.eq("user_id", currentUser.getUserId());
		wrapper.eq("name", name);
		UserSetting userSettingSel = userSettingService.getOne(wrapper);
		UserSetting userSettingUp = new UserSetting();
		if (userSettingSel != null) {
			userSettingUp.setId(userSettingSel.getId());
		} else {
			userSettingUp.setCreateTime(new Date());
		}
		userSettingUp.setName(name);
		userSettingUp.setValue(value);
		userSettingUp.setDelFlag(0);
		userSettingUp.setUserId(currentUser.getUserId());
		userSettingService.saveOrUpdate(userSettingUp);
		return DocResponseJson.ok();
	}
	
	@PostMapping("/setting/list")
	public ResponseJson<WikiSpace> settingList() {
		DocUserDetails currentUser = DocUserUtil.getCurrentUser();
		QueryWrapper<UserSetting> wrapper = new QueryWrapper<>();
		wrapper.eq("user_id", currentUser.getUserId());
		wrapper.eq("name", UserSettingConst.WIKI_ONLY_SHOW_FAVORITE);
		wrapper.eq("del_flag", 0);
		List<UserSetting> settingList = userSettingService.list(wrapper);
		if (CollectionUtils.isEmpty(settingList)) {
			return DocResponseJson.ok();
		}
		Map<String, String> userSettingMap = settingList.stream().collect(Collectors.toMap(UserSetting::getName, UserSetting::getValue));
		return DocResponseJson.ok(userSettingMap);
	}
	
	@PostMapping("/favorite/update")
	public ResponseJson<Object> groupAuth(Long spaceId, Integer delFlag) {
		DocUserDetails currentUser = DocUserUtil.getCurrentUser();
		QueryWrapper<WikiSpaceFavorite> wrapper = new QueryWrapper<>();
		wrapper.eq("space_id", spaceId);
		wrapper.eq("user_id", currentUser.getUserId());
		WikiSpaceFavorite favoriteSel = wikiSpaceFavoriteService.getOne(wrapper);
		WikiSpaceFavorite favoriteUp = new WikiSpaceFavorite();
		if (favoriteSel != null) {
			favoriteUp.setId(favoriteSel.getId());
		} else {
			favoriteUp.setCreateTime(new Date());
		}
		favoriteUp.setDelFlag(delFlag);
		favoriteUp.setUserId(currentUser.getUserId());
		favoriteUp.setSpaceId(spaceId);
		wikiSpaceFavoriteService.saveOrUpdate(favoriteUp);
		return DocResponseJson.ok();
	}
	
	@PostMapping("/auth/assign")
	public ResponseJson<Object> authAssign(Long spaceId, String authList) {
		// 判断是否具有授权的权限
		DocUserDetails currentUser = DocUserUtil.getCurrentUser();
		WikiSpace wikiSpaceSel = wikiSpaceService.getById(spaceId);
		// 只有空间创建人可以管理该空间对用户组的授权
		if (!Objects.equals(currentUser.getUserId(), wikiSpaceSel.getCreateUserId())) {
			return DocResponseJson.warn("您没有权限管理该空间的权限");
		}
		// 先删除页面的所有用户的权限
		QueryWrapper<UserGroupAuth> updateWrapper = new QueryWrapper<>();
		updateWrapper.eq("data_id", spaceId);
		updateWrapper.eq("project_type", DocSysType.WIKI.getType());
		userGroupAuthService.remove(updateWrapper);
		// 在创建权限
		List<UserSpaceAuthVo> authVoList = JSON.parseArray(authList, UserSpaceAuthVo.class);
		for (UserSpaceAuthVo authVo : authVoList) {
			List<UserGroupAuth> userAuthList = new LinkedList<>();
			this.createUserAuth(userAuthList, authVo.getEditPage(), spaceId, WikiAuthType.EDIT_PAGE, authVo.getGroupId());
			this.createUserAuth(userAuthList, authVo.getDeletePage(), spaceId, WikiAuthType.DELETE_PAGE, authVo.getGroupId());
			this.createUserAuth(userAuthList, authVo.getPageFileUpload(), spaceId, WikiAuthType.PAGE_FILE_UPLOAD, authVo.getGroupId());
			this.createUserAuth(userAuthList, authVo.getPageFileDelete(), spaceId, WikiAuthType.PAGE_FILE_DELETE, authVo.getGroupId());
			this.createUserAuth(userAuthList, authVo.getPageAuthManage(), spaceId, WikiAuthType.PAGE_AUTH_MANAGE, authVo.getGroupId());
			if (!userAuthList.isEmpty()) {
				userGroupAuthService.saveBatch(userAuthList);
			}
		}
		return DocResponseJson.ok();
	}
	
	@PostMapping("/auth/list")
	public ResponseJson<Object> authList(Long spaceId) {
		// 判断是否具有授权的权限
		DocUserDetails currentUser = DocUserUtil.getCurrentUser();
		WikiSpace wikiSpaceSel = wikiSpaceService.getById(spaceId);
		// 只有空间创建人可以管理该空间对用户组的授权
		if (!Objects.equals(currentUser.getUserId(), wikiSpaceSel.getCreateUserId())) {
			return DocResponseJson.warn("您没有权限管理该空间的权限");
		}
		QueryWrapper<UserGroupAuth> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("data_id", spaceId);
		queryWrapper.eq("project_type", DocSysType.WIKI.getType());
		List<UserGroupAuth> authList = userGroupAuthService.list(queryWrapper);
		if (CollectionUtils.isEmpty(authList)) {
			return DocResponseJson.ok();
		}
		// 查询用户信息
		Map<Long, List<UserGroupAuth>> userAuthGroup = authList.stream().collect(Collectors.groupingBy(UserGroupAuth::getGroupId));
		List<UserSpaceAuthVo> authVoList = new LinkedList<>();
		// 组装结果集
		userAuthGroup.forEach((key, value) -> {
			Set<Integer> authNameSet = value.stream().map(UserGroupAuth::getAuthType).collect(Collectors.toSet());
			UserSpaceAuthVo authVo = new UserSpaceAuthVo();
			authVo.setEditPage(this.haveAuth(authNameSet, WikiAuthType.EDIT_PAGE));
			authVo.setDeletePage(this.haveAuth(authNameSet, WikiAuthType.DELETE_PAGE));
			authVo.setPageFileUpload(this.haveAuth(authNameSet, WikiAuthType.PAGE_FILE_UPLOAD));
			authVo.setPageFileDelete(this.haveAuth(authNameSet, WikiAuthType.PAGE_FILE_DELETE));
			authVo.setPageAuthManage(this.haveAuth(authNameSet, WikiAuthType.PAGE_AUTH_MANAGE));
			authVo.setGroupId(key);
			authVoList.add(authVo);
		});
		return DocResponseJson.ok(authVoList);
	}
	
	private Integer haveAuth(Set<Integer> authNameSet, WikiAuthType wikiAuthType) {
		return authNameSet.contains(wikiAuthType.getType()) ? 1 : 0;
	}
	
	private void createUserAuth(List<UserGroupAuth> userAuthList, Integer authValue, Long spaceId, WikiAuthType authType, Long groupId) {
		if (Objects.equals(authValue, 1)) {
			DocUserDetails currentUser = DocUserUtil.getCurrentUser();
			UserGroupAuth userAuth = new UserGroupAuth();
			userAuth.setDataId(spaceId);
			userAuth.setAuthType(authType.getType());
			userAuth.setGroupId(groupId);
			userAuth.setCreateTime(new Date());
			userAuth.setCreateUserId(currentUser.getUserId());
			userAuth.setCreateUserName(currentUser.getUsername());
			userAuth.setProjectType(DocSysType.WIKI.getType());
			userAuth.setDelFlag(0);
			userAuthList.add(userAuth);
		}
	}
}

