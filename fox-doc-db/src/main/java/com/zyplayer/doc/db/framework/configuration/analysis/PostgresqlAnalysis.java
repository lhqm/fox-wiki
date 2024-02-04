package com.zyplayer.doc.db.framework.configuration.analysis;

import com.zyplayer.doc.db.framework.db.bean.DatabaseFactoryBean;
import com.zyplayer.doc.db.framework.db.enums.DatabaseProductEnum;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * Postgresql链接url解析
 *
 * @author 离狐千慕
 * @since 2023-05-13
 */
public class PostgresqlAnalysis implements AnalysisApi {
	
	@Override
	public Resource[] process(String dbUrl, DatabaseFactoryBean databaseFactoryBean) throws Exception {
		// jdbc:mysql://192.168.0.1:3306/user_info?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&autoReconnect=true
		String[] urlParamArr = dbUrl.split("\\?");
		String[] urlDbNameArr = urlParamArr[0].split("/");
		if (urlDbNameArr.length >= 2) {
			databaseFactoryBean.setDbName(urlDbNameArr[urlDbNameArr.length - 1]);
			//databaseFactoryBean.setHost(urlDbNameArr[urlDbNameArr.length - 2]);
		}
		databaseFactoryBean.setDatabaseProduct(DatabaseProductEnum.POSTGRESQL);
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		return resolver.getResources("classpath:com/zyplayer/doc/db/framework/db/mapper/postgresql/*.xml");
	}
	
}
