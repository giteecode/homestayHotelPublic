package com.homestay.homestay.utils;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.builder.Mapper;
import com.baomidou.mybatisplus.generator.config.builder.Service;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;

import java.util.Collections;

public class MybatisPlusGeneratorUtil
{
	public static void main(String[] args)
	{
		// 配置数据源
		DataSourceConfig.Builder dataSourceConfigBuilder = new DataSourceConfig.Builder
				(
						"jdbc:mysql://localhost:3306/homestay?useUnicode=true&characterEncoding=UTF-8",
						"root",
						"heheda66"
				);
		dataSourceConfigBuilder.schema("homestay");
		DataSourceConfig dataSourceConfig = dataSourceConfigBuilder.build();

		// 全局配置
		GlobalConfig.Builder globalConfigBuilder = new GlobalConfig.Builder();
		// 设置作者
		globalConfigBuilder.author("zhangxilong");
		// 生成完毕后不自动打开目录
		globalConfigBuilder.disableOpenDir();
		// 指定输出目录
//		globalConfigBuilder.outputDir("D:/Downloads");
		globalConfigBuilder.outputDir("/Users/zhangxilong/Downloads");
		// 注释日期 默认值: yyyy-MM-dd
		globalConfigBuilder.commentDate("yyyy-MM-dd HH:mm:ss");
		// 时间策略：DateType.ONLY_DATE 生成的datetime类行为Date；DateType.SQL_PACK为timestamp;DateType.TIME_PACK为LocalDateTime；
		globalConfigBuilder.dateType(DateType.ONLY_DATE);


		// 包配置
		PackageConfig.Builder packageConfigBuilder = new PackageConfig.Builder();
		// 设置父包名
		packageConfigBuilder.parent("com.homestay.homestay");
		// 设置父包模块名
//		packageConfigBuilder.moduleName("db");
		// 设置mapperXml生成路径"D:\Downloads\index.html"
//		packageConfigBuilder.pathInfo(Collections.singletonMap(OutputFile.xml, "D:/Downloads/mapper"));
		packageConfigBuilder.pathInfo(Collections.singletonMap(OutputFile.xml, "/Users/zhangxilong/Downloads/mapper"));

		// 模板配置
		TemplateConfig.Builder templateConfigBuilder = new TemplateConfig.Builder();
		// 不生成Controller
		templateConfigBuilder.disable(TemplateType.CONTROLLER);
		// 指定自定义模板位置
		templateConfigBuilder.controller("/generator/templates/controller.java");
		templateConfigBuilder.entity("/generator/templates/entity.java");
		templateConfigBuilder.service("/generator/templates/service.java");
		templateConfigBuilder.serviceImpl("/generator/templates/serviceImpl.java");
		templateConfigBuilder.mapper("/generator/templates/mapper.java");
		templateConfigBuilder.xml("/generator/templates/mapper.xml");

		/*
		策略配置配置
		包括：Entity 策略、Service 策略、ServiceImpl 策略、Mapper 策略、Mapper XML 策略等等
		 */
		StrategyConfig.Builder strategyConfigBuilder = new StrategyConfig.Builder();

		// Entity 策略配置
		Entity.Builder entityBuilder = strategyConfigBuilder.entityBuilder();
		// 覆盖已存在的文件
		entityBuilder.fileOverride();
		// 使用Lombok
		entityBuilder.enableLombok();
		// 使用TableField注解，因为有一些傻逼会用数据库关键字作为字段名称，导致Mybatis报错，所以这里我手动调整了Entity的模板，需要开启这个注解
		entityBuilder.enableTableFieldAnnotation();
		// 增加表字段填充，我不懂
		entityBuilder.addTableFills(new Column("create_time", FieldFill.INSERT));
		// 增加表字段填充，我不懂
		entityBuilder.addTableFills(new Property("updateTime", FieldFill.INSERT_UPDATE));
		// 乐观锁字段名
		entityBuilder.versionColumnName("version");
		// 乐观锁属性名
		entityBuilder.versionPropertyName("version");
		// 逻辑删除字段名
		entityBuilder.logicDeleteColumnName("deleted");
		// 逻辑删除属性名
		entityBuilder.logicDeletePropertyName("deleted");
		// 在生成的时候，文件名后面加上Entity
//		entityBuilder.formatFileName("%sEntity");

		// Service 策略配置
		Service.Builder serviceBuilder = strategyConfigBuilder.serviceBuilder();
		// 覆盖已存在的文件
		serviceBuilder.fileOverride();
		// 在生成的时候，文件名后面加上EntityService
		serviceBuilder.formatServiceFileName("%sService");
		// 在生成的时候，文件名后面加上ServiceImpl
		serviceBuilder.formatServiceImplFileName("%sServiceImpl");

		// Mapper 策略配置
		Mapper.Builder mapperBuilder = strategyConfigBuilder.mapperBuilder();
		// 覆盖已存在的文件
		mapperBuilder.fileOverride();
		// 增加 @Mapper 注解
		mapperBuilder.enableMapperAnnotation();
		// 在生成的时候，文件名后面加上EntityMapper
		mapperBuilder.formatMapperFileName("%sMapper");
		// XML里面会有一个BaseResultMap
		mapperBuilder.enableBaseResultMap();
		// XML里面会有一个BaseColumnList
		mapperBuilder.enableBaseColumnList();
		// 在生成的时候，XML文件名后面加上EntityMapper
		mapperBuilder.formatXmlFileName("%sMapper");

		// 设置过滤表前缀
		// builder.addTablePrefix("t_", "c_");

		// 设置需要生成的表名
		strategyConfigBuilder.addInclude("h_order");

		AutoGenerator generator = new AutoGenerator(dataSourceConfig);
		generator.global(globalConfigBuilder.build());
		generator.packageInfo(packageConfigBuilder.build());
		generator.template(templateConfigBuilder.build());
		generator.strategy(strategyConfigBuilder.build());
		generator.execute();
	}
}
