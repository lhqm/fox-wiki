package com.zyplayer.doc.manage.web;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyplayer.doc.core.annotation.AuthMan;
import com.zyplayer.doc.core.json.DocResponseJson;
import com.zyplayer.doc.core.json.ResponseJson;
import com.zyplayer.doc.data.config.security.DocUserDetails;
import com.zyplayer.doc.data.config.security.DocUserUtil;
import com.zyplayer.doc.data.config.security.UserAuthInfo;
import com.zyplayer.doc.data.repository.manage.entity.AuthInfo;
import com.zyplayer.doc.data.repository.manage.entity.UserAuth;
import com.zyplayer.doc.data.repository.manage.entity.UserInfo;
import com.zyplayer.doc.data.repository.support.consts.DocAuthConst;
import com.zyplayer.doc.data.service.manage.AuthInfoService;
import com.zyplayer.doc.data.service.manage.UserAuthService;
import com.zyplayer.doc.data.service.manage.UserInfoService;
import com.zyplayer.doc.manage.web.param.UserListParam;
import com.zyplayer.doc.manage.web.vo.AuthInfoVo;
import com.zyplayer.doc.manage.web.vo.UserAuthVo;
import com.zyplayer.doc.manage.web.vo.UserInfoAuthVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户信息控制器
 *
 * @author 离狐千慕
 * @since 2023-12-08
 */
@RestController
@RequestMapping("/user/info")
public class UserInfoController {
	
	@Resource
	UserInfoService userInfoService;
	@Resource
	AuthInfoService authInfoService;
	@Resource
	UserAuthService userAuthService;
	
	@AuthMan
	@PostMapping("/selfInfo")
	public ResponseJson<Object> selfInfo() {
		DocUserDetails currentUser = DocUserUtil.getCurrentUser();
		UserInfo userInfo = userInfoService.getById(currentUser.getUserId());
		userInfo.setPassword(null);
		return DocResponseJson.ok(userInfo);
	}
	
	@AuthMan
	@PostMapping("/selfInfoWithAuth")
	public ResponseJson<UserInfoAuthVo> selfInfoWithAuth() {
		DocUserDetails currentUser = DocUserUtil.getCurrentUser();
		UserInfo userInfo = userInfoService.getById(currentUser.getUserId());
		userInfo.setPassword(null);
		UserAuthVo sysAuthInfoVo = new UserAuthVo();
		sysAuthInfoVo.setUserManage(DocUserUtil.haveAuth(DocAuthConst.USER_MANAGE));
		UserInfoAuthVo selfInfoVo = new UserInfoAuthVo();
		selfInfoVo.setUserInfo(userInfo);
		selfInfoVo.setUserAuth(sysAuthInfoVo);
		return DocResponseJson.ok(selfInfoVo);
	}
	
	@AuthMan
	@PostMapping("/search")
	public ResponseJson<Object> search(String search) {
		if (StringUtils.isBlank(search)) {
			return DocResponseJson.ok();
		}
		QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.and(con -> con.and(conSub -> conSub.like("user_name", search).or().like("user_no", search)
				.or().like("email", search).or().like("phone", search)).and(conSub -> conSub.eq("del_flag", 0)));
		queryWrapper.select("id", "user_name");
		// 搜索最多返回20条
		IPage<UserInfo> page =  Page.of(1, 20, false);
		userInfoService.page(page, queryWrapper);
		return DocResponseJson.ok(page);
	}
	
	@PostMapping("/list")
	@AuthMan(DocAuthConst.USER_MANAGE)
	public ResponseJson<Object> list(UserListParam param) {
		QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(param.getKeyword())) {
			queryWrapper.like(param.getType() == 1, "id", param.getKeyword());
			queryWrapper.like(param.getType() == 2, "user_no", param.getKeyword());
			queryWrapper.like(param.getType() == 3, "user_name", param.getKeyword());
			queryWrapper.like(param.getType() == 4, "phone", param.getKeyword());
			queryWrapper.like(param.getType() == 5, "email", param.getKeyword());
		}
		queryWrapper.ne("del_flag", 1);
		IPage<UserInfo> page = new Page<>(param.getPageNum(), param.getPageSize(), true);
		userInfoService.page(page, queryWrapper);
		List<UserInfo> userInfoList = page.getRecords();
		if (CollectionUtils.isNotEmpty(userInfoList)) {
			userInfoList.forEach(val -> val.setPassword(""));
		}
		return DocResponseJson.ok(page);
	}
	
	@AuthMan(DocAuthConst.USER_MANAGE)
	@PostMapping("/update")
	public ResponseJson<Object> update(UserInfo userInfo) {
		if (StringUtils.isBlank(userInfo.getUserNo())) {
			return DocResponseJson.warn("用户账号必填");
		}
		if (StringUtils.isBlank(userInfo.getUserName())) {
			return DocResponseJson.warn("用户名必填");
		}
		long userId = Optional.ofNullable(userInfo.getId()).orElse(0L);
		QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_no", userInfo.getUserNo());
		queryWrapper.ne(userId > 0, "id", userInfo.getId());
		long count = userInfoService.count(queryWrapper);
		if (count > 0) {
			return DocResponseJson.warn("改用户账号已存在");
		}
		// 密码支持自定义修改，管理权限都有，开放了随便改吧~
		String password = userInfo.getPassword();
		if (StringUtils.isNotBlank(password)) {
			String newPassword = DigestUtils.md5DigestAsHex(password.getBytes());
			userInfo.setPassword(newPassword);
		} else {
			// 防止改为空
			userInfo.setPassword(null);
		}
		if (userId > 0) {
			userInfo.setUpdateTime(new Date());
			userInfoService.updateById(userInfo);
		} else {
			DocUserDetails currentUser = DocUserUtil.getCurrentUser();
			userInfo.setCreationTime(new Date());
			userInfo.setCreateUid(currentUser.getUserId());
			userInfoService.save(userInfo);
		}
		return DocResponseJson.ok();
	}
	
	@AuthMan(DocAuthConst.USER_MANAGE)
	@PostMapping("/resetPassword")
	public ResponseJson<Object> resetPassword(UserInfo userInfo) {
		String password = RandomUtil.randomNumbers(6);
		UserInfo userInfoUp = new UserInfo();
		if (StringUtils.isNotBlank(password)) {
			String newPassword = DigestUtils.md5DigestAsHex(password.getBytes());
			userInfoUp.setPassword(newPassword);
		}
		userInfoUp.setId(userInfo.getId());
		userInfoUp.setUpdateTime(new Date());
		userInfoService.updateById(userInfoUp);
		return DocResponseJson.ok(password);
	}
	
	@AuthMan
	@PostMapping("/updateSelfPwd")
	public ResponseJson<Object> updateSelfPwd(String currentPwd, String newPwd) {
		DocUserDetails currentUser = DocUserUtil.getCurrentUser();
		// 检查密码是否正确
		if (StringUtils.isBlank(currentPwd) || StringUtils.isBlank(newPwd)) {
			return DocResponseJson.warn("密码不能为空");
		}
		UserInfo userInfo = userInfoService.getById(currentUser.getUserId());
		String oldPasswordInput = DigestUtils.md5DigestAsHex(currentPwd.getBytes());
		if (!Objects.equals(oldPasswordInput, userInfo.getPassword())) {
			return DocResponseJson.warn("当前密码密码错误");
		}
		String newPassword = DigestUtils.md5DigestAsHex(newPwd.getBytes());
		UserInfo userInfoUp = new UserInfo();
		userInfoUp.setId(currentUser.getUserId());
		userInfoUp.setPassword(newPassword);
		userInfoUp.setUpdateTime(new Date());
		userInfoService.updateById(userInfoUp);
		return DocResponseJson.ok();
	}
	
	@AuthMan(DocAuthConst.USER_MANAGE)
	@PostMapping("/delete")
	public ResponseJson<Object> delete(Long id) {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(id);
		userInfo.setDelFlag(1);
		userInfo.setUpdateTime(new Date());
		userInfoService.updateById(userInfo);
		return DocResponseJson.ok();
	}
	
	@AuthMan(DocAuthConst.AUTH_ASSIGN)
	@PostMapping("/auth/list")
	public ResponseJson<Object> authList(String userIds) {
		// 所有权限
		QueryWrapper<AuthInfo> authWrapper = new QueryWrapper<>();
		authWrapper.eq("auth_type", 1);
		List<AuthInfo> authList = authInfoService.list(authWrapper);
		// 用户权限
		QueryWrapper<UserAuth> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("del_flag", 0);
		queryWrapper.in("user_id", Arrays.asList(userIds.split(",")));
		List<UserAuth> userAuthList = userAuthService.list(queryWrapper);
		Map<Long, UserAuth> userAuthMap = userAuthList.stream().collect(Collectors.toMap(UserAuth::getAuthId, Function.identity(), (val1, val2) -> val1));
		List<AuthInfoVo> authInfoVoList = new LinkedList<>();
		authList.forEach(val -> {
			UserAuth userAuth = userAuthMap.get(val.getId());
			AuthInfoVo infoVo = new AuthInfoVo(val);
			infoVo.setChecked((userAuth == null) ? 0 : 1);
			authInfoVoList.add(infoVo);
		});
		return DocResponseJson.ok(authInfoVoList);
	}
	
	@AuthMan(DocAuthConst.AUTH_ASSIGN)
	@PostMapping("/auth/update")
	public ResponseJson<Object> updateAuth(String userIds, String authIds) {
		List<Long> userIdsList = Arrays.stream(userIds.split(",")).map(Long::valueOf).collect(Collectors.toList());
		List<Long> authIdsList = Collections.emptyList();
		if (StringUtils.isNotBlank(authIds)) {
			authIdsList = Arrays.stream(authIds.split(",")).map(Long::valueOf).collect(Collectors.toList());
		}
		DocUserDetails currentUser = DocUserUtil.getCurrentUser();
		QueryWrapper<UserAuth> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("user_id", userIdsList);
		userAuthService.remove(queryWrapper);
		List<UserAuth> createList = new LinkedList<>();
		for (Long userId : userIdsList) {
			for (Long authId : authIdsList) {
				UserAuth userAuth = new UserAuth();
				userAuth.setUserId(userId);
				userAuth.setAuthId(authId);
				userAuth.setCreateUid(currentUser.getUserId());
				userAuth.setCreationTime(new Date());
				userAuth.setDelFlag(0);
				createList.add(userAuth);
			}
		}
		userAuthService.saveBatch(createList);
		for (Long userId : userIdsList) {
			List<UserAuthInfo> userAuthListNew = userAuthService.getUserAuthSet(userId);
			DocUserUtil.setUserAuth(userId, userAuthListNew);
		}
		return DocResponseJson.ok();
	}
}
