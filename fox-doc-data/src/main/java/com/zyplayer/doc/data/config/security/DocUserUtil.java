package com.zyplayer.doc.data.config.security;

import com.zyplayer.doc.data.utils.CachePrefix;
import com.zyplayer.doc.data.utils.CacheUtil;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户工具类
 *
 * @author 离狐千慕
 * @since 2023年05月25日
 */
public class DocUserUtil {
	private static final ThreadLocal<DocUserDetails> DOC_USER_DETAILS = new ThreadLocal<>();
	private static final ThreadLocal<String> ACCESS_TOKEN = new ThreadLocal<>();
	
	public static void setAccessToken(String accessToken) {
		DocUserUtil.ACCESS_TOKEN.set(accessToken);
	}
	
	public static boolean haveCustomAuth(String authName, Integer sysType, Integer sysModuleType, Long sysModuleId) {
		DocUserDetails currentUser = getCurrentUser();
		if (currentUser == null) {
			return false;
		}
		return currentUser.getUserAuthList().stream().anyMatch(auth ->
				Objects.equals(auth.getAuthCode(), authName)
				&& Objects.equals(auth.getSysType(), sysType)
				&& Objects.equals(auth.getSysModuleType(), sysModuleType)
				&& Objects.equals(auth.getSysModuleId(), sysModuleId)
		);
	}
	
	public static boolean haveAuth(String... authNames) {
		DocUserDetails currentUser = getCurrentUser();
		if (currentUser == null) {
			return false;
		}
		Set<String> authCodeSet = currentUser.getUserAuthList().stream().map(UserAuthInfo::getAuthCode).collect(Collectors.toSet());
		for (String authName : authNames) {
			if (!authCodeSet.contains(authName)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 获取当前用户
	 *
	 * @return 用户信息
	 */
	public static DocUserDetails getCurrentUser() {
		DocUserDetails docUser = DOC_USER_DETAILS.get();
		if (docUser == null) {
			docUser = CacheUtil.get(ACCESS_TOKEN.get());
			if (docUser != null) {
				DOC_USER_DETAILS.set(docUser);
			}
		}
		return docUser;
	}
	
	/**
	 * 设置当前用户
	 */
	public static void setCurrentUser(String accessToken, DocUserDetails docUser) {
		DOC_USER_DETAILS.set(docUser);
		CacheUtil.put(accessToken, docUser);
		CacheUtil.put(CachePrefix.LOGIN_USER_ID_TOKEN + docUser.getUserId(), accessToken);
	}
	
	/**
	 * 设置当前用户权限
	 */
	public static void setUserAuth(Long userId, List<UserAuthInfo> userAuthList) {
		String userToken = CacheUtil.get(CachePrefix.LOGIN_USER_ID_TOKEN + userId);
		if (userToken != null) {
			DocUserDetails docUser = CacheUtil.get(userToken);
			if (docUser != null) {
				docUser.setUserAuthList(userAuthList);
				CacheUtil.put(userToken, docUser);
			}
		}
	}
	
	/**
	 * 退出登录
	 */
	public static void logout() {
		CacheUtil.remove(ACCESS_TOKEN.get());
	}
	
	public static void clean() {
		DocUserUtil.DOC_USER_DETAILS.remove();
		DocUserUtil.ACCESS_TOKEN.remove();
	}
}
