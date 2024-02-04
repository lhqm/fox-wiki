package com.zyplayer.doc.data.repository.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户消息表
 * </p>
 *
 * @author 离狐千慕
 * @since 2023-06-23
 */
@Data
@TableName("user_message")
public class UserMessage implements Serializable {

    /**
     * 主键自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 系统类型 1=manage 2=wiki 3=db
     */
    private Integer sysType;

    /**
     * 消息类型 1=普通文本消息 2=wiki文档创建 3=wiki文档删除 4=wiki文档编辑 5=wiki文档权限修改 6=wiki文档评论 7=wiki文档删除评论
     */
    private Integer msgType;

    /**
     * 消息关联的数据ID
     */
    private Long dataId;

    /**
     * 消息关联的数据说明
     */
    private String dataDesc;

    /**
     * 消息内容
     */
    private String msgContent;

    /**
     * 消息状态 0=未读 1=已读 2=已删除
     */
    private Integer msgStatus;

    /**
     * 操作人用户ID
     */
    private Long operatorUserId;

    /**
     * 操作人用户名
     */
    private String operatorUserName;

    /**
     * 影响用户ID
     */
    private Long affectUserId;

    /**
     * 影响人用户名
     */
    private String affectUserName;

    /**
     * 接收人用户ID
     */
    private Long acceptUserId;

    /**
     * 创建时间
     */
    private Date creationTime;

}
