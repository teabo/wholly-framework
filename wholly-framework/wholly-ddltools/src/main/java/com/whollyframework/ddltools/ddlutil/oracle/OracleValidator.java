package com.whollyframework.ddltools.ddlutil.oracle;

import java.sql.Connection;

import com.whollyframework.ddltools.ddlutil.AbstractValidator;
import com.whollyframework.ddltools.ddlutil.DBUtils;

/**
 * 
 * @author chris
 * @since 2011-12-20
 * 
 */
public class OracleValidator extends AbstractValidator {

	public OracleValidator(Connection conn) {
		super(conn, new OracleBuilder());
		this.schema = DBUtils.getSchema(conn, DBUtils.DBTYPE_ORACLE);
		_builder.setSchema(schema);
	}

	protected String getCatalog() {
		return null;
	}

	protected String getSchemaPattern() {
		return schema;
	}

}
