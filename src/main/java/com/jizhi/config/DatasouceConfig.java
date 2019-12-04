package com.jizhi.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value= {"classpath:dataSource.properties"})
public class DatasouceConfig {
	
	@Value("${spring.datasource.url}")
    private String url;
    
    @Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;
    
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    
    @Value("${spring.datasource.initialSize}")
    private int initialSize;
    
    @Value("${spring.datasource.maxActive}")
    private int maxActive;
    
    @Value("${spring.datasource.minIdle}")
    private int minIdle;
    
    @Value("${spring.datasource.maxWait}")
    private int maxWait;
    
    @Value("${spring.datasource.testOnBorrow}")
    private boolean testOnBorrow;
    
    @Value("${spring.datasource.testWhileIdle}")
    private boolean testWhileIdle;
    
    
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        DataSource dataSource = new DataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);//用户名
        dataSource.setPassword(password);//密码
        dataSource.setDriverClassName(driverClassName);
        dataSource.setInitialSize(initialSize);//初始化时建立物理连接的个数
        dataSource.setMaxActive(maxActive);//最大连接池数量
        dataSource.setMinIdle(minIdle);//最小连接池数量
        dataSource.setMaxWait(maxWait);//获取连接时最大等待时间，单位毫秒。
        dataSource.setValidationQuery("SELECT 1");//用来检测连接是否有效的sql
        dataSource.setTestOnBorrow(testOnBorrow);//申请连接时执行validationQuery检测连接是否有效
        dataSource.setTestWhileIdle(testWhileIdle);//建议配置为true，不影响性能，并且保证安全性。
        return dataSource;
}
}
