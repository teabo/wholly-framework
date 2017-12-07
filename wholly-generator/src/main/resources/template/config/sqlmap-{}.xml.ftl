<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE sqlMap PUBLIC "-//iBATIS.com//DTD SQL Map 2.0//EN" 
	"http://ibatis.apache.org/dtd/sql-map-2.dtd" >
<sqlMap namespace="${model.objectName}VO">

	<insert id="insert">
		INSERT INTO ${model.tableName}
		(${model.pkName}, ${model.sqlFields})
		VALUES
		(#${model.modelId}#, ${model.modelFields})
	</insert>
	
	<insert id="insertMysql">
		INSERT INTO ${model.tableName}
		(${model.pkName}, ${model.sqlFields})
		VALUES
		(_nextval('${model.tableName}'), ${model.modelFields})
		<selectKey resultClass="java.lang.Integer" >
       		 SELECT _currval('${model.tableName}') as id
        </selectKey>
	</insert>
	
	<delete id="deleteByPrimaryKey">
		delete from ${model.tableName} where ${model.pkName}=#${model.modelId}#
	</delete>

	<update id="updateByPrimaryKey">
		update ${model.tableName}
		set 
		${model.updateFields}
		where
		${model.pkName}=#${model.modelId}#
	</update>

	<select id="countByWhere_Clause" parameterClass="com.whollyframework.base.dao.support.SQLUtils"
		resultClass="java.lang.Integer">
		select count(*) from (select ${model.selectfileds} from ${model.tableName}) t
		<include refid="IBatisDefault.Where_Clause" />
	</select>

	<select id="getAll" resultClass="${model.packageName}.model.${model.objectName}VO">
		select ${model.selectfileds} from ${model.tableName} t order by sortid
	</select>

	<select id="selectByPrimaryKey" resultClass="${model.packageName}.model.${model.objectName}VO">
		select ${model.selectfileds} from ${model.tableName} t	where t.${model.pkName}=#${model.modelId}#
	</select>

	<select id="selectByWhere_Clause" parameterClass="com.whollyframework.base.dao.support.SQLUtils"
		resultClass="${model.packageName}.model.${model.objectName}VO">
		select * from (select ${model.selectfileds} from ${model.tableName}) t
		<isParameterPresent>
			<include refid="IBatisDefault.Where_Clause" />
			<isNotEmpty property="orderByClause">
				order by $orderByClause$
      		</isNotEmpty>
		</isParameterPresent>
	</select>

	<delete id="deleteByWhere_Clause" parameterClass="com.whollyframework.base.dao.support.SQLUtils">
		delete from ${model.tableName} where ${model.pkName} in (select t.id from (select ${model.selectfileds} from ${model.tableName}) t
		<include refid="IBatisDefault.Where_Clause" />)
	</delete>
	<!-- 常规调用 end -->
</sqlMap>