<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${model.objectName}VO">

	<insert id="insert">
		INSERT INTO ${model.tableName}
		(${model.sqlFields})
		VALUES
		(${model.modelFields})
	</insert>
	
	<delete id="deleteByPrimaryKey">
		delete from ${model.tableName} where ${model.pkName}=${r'#{'}${model.modelId}${r'}'}
	</delete>

	<update id="updateByPrimaryKey">
		update ${model.tableName}
		set 
		${model.updateFields}
		where
		${model.pkName}=${r'#{'}${model.modelId}${r'}'}
	</update>

	<select id="countByWhere_Clause" parameterClass="com.whollyframework.base.dao.support.SQLUtils"
		resultClass="java.lang.Integer">
		select count(*) from (select ${model.selectfileds} from ${model.tableName}) t
		<include refid="MyBatisDefault.Where_Clause" />
	</select>

	<select id="getAll" resultClass="${model.packageName}.model.${model.objectName}VO">
		select ${model.selectfileds} from ${model.tableName} t order by sortid
	</select>

	<select id="selectByPrimaryKey" resultClass="${model.packageName}.model.${model.objectName}VO">
		select ${model.selectfileds} from ${model.tableName} t	where t.${model.pkName}=${r'#{'}${model.modelId}${r'}'}
	</select>

	<select id="selectByWhere_Clause" parameterClass="com.whollyframework.base.dao.support.SQLUtils"
		resultClass="${model.packageName}.model.${model.objectName}VO">
		select * from (select ${model.selectfileds} from ${model.tableName}) t
		<isParameterPresent>
			<include refid="MyBatisDefault.Where_Clause" />
			<isNotEmpty property="orderByClause">
				order by ${r'${orderByClause}'}
      		</isNotEmpty>
		</isParameterPresent>
	</select>

	<delete id="deleteByWhere_Clause" parameterClass="com.whollyframework.base.dao.support.SQLUtils">
		delete from ${model.tableName} where ${model.pkName} in (select t.id from (select ${model.selectfileds} from ${model.tableName}) t
		<include refid="MyBatisDefault.Where_Clause" />)
	</delete>
	<!-- 常规调用 end -->
</mapper>