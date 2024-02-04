package com.zyplayer.doc.data.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zyplayer.doc.data.repository.support.interceptor.SqlLogInterceptor;
import com.zyplayer.doc.data.utils.DruidDataSourceUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * mybatis plus数据库配置
 *
 * @author 离狐千慕
 * @since 2023-02-16
 */
@Configuration
public class MybatisPlusConfig {
	
	/**
	 * 数据库配置
	 */
	@Configuration
	@EnableTransactionManagement
	@MapperScan(value = "com.zyplayer.doc.data.repository.manage.mapper", sqlSessionFactoryRef = "manageSqlSessionFactory")
	static class ManageMybatisDbConfig {
		
		@Value("${zyplayer.doc.manage.datasource.driverClassName}")
		private String driverClassName;
		@Value("${zyplayer.doc.manage.datasource.url}")
		private String url;
		@Value("${zyplayer.doc.manage.datasource.username}")
		private String username;
		@Value("${zyplayer.doc.manage.datasource.password}")
		private String password;
		@Resource
		private MybatisPlusInterceptor paginationInterceptor;
		
		@Bean(name = "manageDatasource")
		public DataSource manageDatasource() throws Exception {
			return DruidDataSourceUtil.createDataSource(driverClassName, url, username, password, false);
		}
		
		@Bean(name = "manageSqlSessionFactory")
		public MybatisSqlSessionFactoryBean manageSqlSessionFactory() throws Exception {
			MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
			sqlSessionFactoryBean.setDataSource(manageDatasource());
			sqlSessionFactoryBean.setPlugins(new SqlLogInterceptor(), paginationInterceptor);
			
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mapper/manage/*Mapper.xml"));
			return sqlSessionFactoryBean;
		}
	}
	
	@Bean
	public MybatisPlusInterceptor paginationInterceptor() {
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
		mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
		return mybatisPlusInterceptor;
	}
}
