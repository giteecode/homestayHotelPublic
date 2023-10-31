package com.homestay.homestay.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler
{
	@Override
	public void insertFill(MetaObject metaObject)
	{
		log.info("start insert fill ....");
		this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
	}

	@Override
	public void updateFill(MetaObject metaObject)
	{
		log.info("start update fill ....");
		this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
	}
}
