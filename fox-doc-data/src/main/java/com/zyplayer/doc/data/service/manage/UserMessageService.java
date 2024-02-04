package com.zyplayer.doc.data.service.manage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zyplayer.doc.data.config.security.DocUserDetails;
import com.zyplayer.doc.data.repository.manage.entity.UserMessage;
import com.zyplayer.doc.data.repository.support.consts.DocSysType;
import com.zyplayer.doc.data.repository.support.consts.UserMsgType;

/**
 * <p>
 * 用户消息表 服务类
 * </p>
 *
 * @author 离狐千慕
 * @since 2023-06-23
 */
public interface UserMessageService extends IService<UserMessage> {
	
	void addWikiMessage(UserMessage userMessage);
	
	UserMessage createUserMessage(DocUserDetails currentUser, Long pageId, String dataDesc, DocSysType sysType, UserMsgType msgType);
}
