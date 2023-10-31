package com.homestay.homestay.config.mybatis;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 张喜龙
 * @Date: 2022/09/21/14:50
 * @Description: mybatis-plus分页插件,复制到新项目中记得配置mapper扫描路径！！！
 */
@Configuration
@MapperScan("com.homestay.homestay.mapper")
public class MybatisPlusConfig {
	/**
	 * 旧版分页插件*/
//    @Bean
//    public PaginationInterceptor paginationInterceptor(){
//        return new PaginationInterceptor();
//    }

	/**
	 * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

		// 乐观锁
		interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
		// 禁止全表更新或删除
		interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

		return interceptor;
	}

	//    @Bean
//    public ConfigurationCustomizer configurationCustomizer() {
//        return configuration -> configuration.setUseDeprecatedExecutor(false);
//    }
//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor() {
//        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
//        // 乐观锁
//        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
//        // 禁止全表更新或删除
//        mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
//        return mybatisPlusInterceptor;
//    }
}
