package com.zyplayer.doc.wiki.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zyplayer.doc.core.annotation.AuthMan;
import com.zyplayer.doc.core.json.DocResponseJson;
import com.zyplayer.doc.core.json.ResponseJson;
import com.zyplayer.doc.data.config.security.DocUserDetails;
import com.zyplayer.doc.data.config.security.DocUserUtil;
import com.zyplayer.doc.data.repository.manage.entity.UserMessage;
import com.zyplayer.doc.data.repository.manage.entity.WikiPage;
import com.zyplayer.doc.data.repository.manage.entity.WikiPageZan;
import com.zyplayer.doc.data.repository.manage.entity.WikiSpace;
import com.zyplayer.doc.data.repository.support.consts.DocSysType;
import com.zyplayer.doc.data.repository.support.consts.UserMsgType;
import com.zyplayer.doc.data.service.manage.UserMessageService;
import com.zyplayer.doc.data.service.manage.WikiPageService;
import com.zyplayer.doc.data.service.manage.WikiPageZanService;
import com.zyplayer.doc.data.service.manage.WikiSpaceService;
import com.zyplayer.doc.wiki.framework.consts.SpaceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * 文档控制器
 *
 * @author 离狐千慕
 * @since 2019年2月17日
 */
@Slf4j
@AuthMan
@RestController
@RequestMapping("/zyplayer-doc-wiki/page/zan")
@RequiredArgsConstructor
public class WikiPageZanController {

    private final WikiPageZanService wikiPageZanService;
    private final WikiSpaceService wikiSpaceService;
    private final WikiPageService wikiPageService;
    private final UserMessageService userMessageService;

    @PostMapping("/list")
    public ResponseJson<List<WikiPageZan>> list(WikiPageZan wikiPageZan) {
        DocUserDetails currentUser = DocUserUtil.getCurrentUser();
        WikiPage wikiPageSel = wikiPageService.getById(wikiPageZan.getPageId());
        WikiSpace wikiSpaceSel = wikiSpaceService.getById(wikiPageSel.getSpaceId());
        // 私人空间
        if (SpaceType.isOthersPrivate(wikiSpaceSel.getType(), currentUser.getUserId(), wikiSpaceSel.getCreateUserId())) {
            return DocResponseJson.warn("您没有获取该空间的点赞列表权限！");
        }
        UpdateWrapper<WikiPageZan> wrapper = new UpdateWrapper<>();
        wrapper.eq("page_id", wikiPageZan.getPageId());
        wrapper.eq(wikiPageZan.getCommentId() != null, "comment_id", wikiPageZan.getCommentId());
        wrapper.eq("yn", 1);
        List<WikiPageZan> zanList = wikiPageZanService.list(wrapper);
        return DocResponseJson.ok(zanList);
    }

    @PostMapping("/update")
    public ResponseJson<Object> update(WikiPageZan wikiPageZan) {
        DocUserDetails currentUser = DocUserUtil.getCurrentUser();
        Long id = wikiPageZan.getId();
        Long pageId;
        if (id != null && id > 0) {
            WikiPageZan wikiPageZanSel = wikiPageZanService.getById(id);
            pageId = wikiPageZanSel.getPageId();
        } else if (wikiPageZan.getPageId() != null) {
            pageId = wikiPageZan.getPageId();
        } else {
            return DocResponseJson.warn("需指定所属页面！");
        }
        WikiPage wikiPageSel = wikiPageService.getById(pageId);
        WikiSpace wikiSpaceSel = wikiSpaceService.getById(wikiPageSel.getSpaceId());
        // 私人空间
        if (SpaceType.isOthersPrivate(wikiSpaceSel.getType(), currentUser.getUserId(), wikiSpaceSel.getCreateUserId())) {
            return DocResponseJson.warn("您没有该空间的点赞权限！");
        }
        wikiPageZanService.zanPage(wikiPageZan);
        // 给相关人发送消息
        UserMessage userMessage = userMessageService.createUserMessage(currentUser, wikiPageSel.getId(), wikiPageSel.getName(), DocSysType.WIKI, UserMsgType.WIKI_PAGE_ZAN);
        if (!Objects.equals(wikiPageZan.getYn(), 1)) {
            userMessage.setMsgType(UserMsgType.WIKI_PAGE_ZAN_CANCEL.getType());
        }
        userMessage.setAffectUserId(wikiPageSel.getCreateUserId());
        userMessage.setAffectUserName(wikiPageSel.getCreateUserName());
        userMessageService.addWikiMessage(userMessage);
        return DocResponseJson.ok();
    }
}

