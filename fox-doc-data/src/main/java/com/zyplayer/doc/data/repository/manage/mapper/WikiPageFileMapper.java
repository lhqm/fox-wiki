package com.zyplayer.doc.data.repository.manage.mapper;

import com.zyplayer.doc.data.repository.manage.entity.WikiPageFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 离狐千慕
 * @since 2023-03-06
 */
public interface WikiPageFileMapper extends BaseMapper<WikiPageFile> {
	
	@Update("update wiki_page_file set download_num=download_num+1 where id=#{id}")
	void addDownloadNum(@Param("id") Long id);
}
