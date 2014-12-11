package com.whollyframework.base.dao.support;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import com.whollyframework.base.dao.support.OrderByClause.SortOrder;
import com.whollyframework.base.dao.support.criterion.Criterion;
import com.whollyframework.base.dao.support.criterion.Criterions;
import com.whollyframework.base.dao.support.criterion.Junction;
import com.whollyframework.base.model.ParamsTable;
import com.whollyframework.util.DateUtil;
import com.whollyframework.util.StringUtil;

/**
 * 
 * @author Chris Xu
 * 
 */
public abstract class AbstractSQLUtils {

	public static final String SQLFLAG_ORDERBY_FIELD = "strIndexRow";

	public static final String SQLFLAG_ORDERBY_SCORTDESC = "strScortDesc";

	public static final String SQLFLAG_QUERYCONDITION = "strAddtionalQueryCondition";

	public static final String SEQUENCE_PUB_ATTACH = "SEQ_PUB_ATTACH";

	public static final String SEQUENCE_PUB_FLOW = "SEQ_PUB_FLOW";

	public static final String SEQUENCE_PUB_SYSTEM = "SEQ_PUB_SYSTEM";

	public static final String SEQUENCE_PUB_IDGRANTOR = "SEQ_PUB_IDGRANTOR";

	private static final Logger log = Logger.getLogger(AbstractSQLUtils.class);

	protected static final Map<String, String> OPERATIONS = new HashMap<String, String>();

	public static final String OP_NOT_NULL = "NOTNULL";
	public static final String OP_NULL = "NULL";
	public static final String OP_BLANK = "BLANK";
	public static final String OP_NOT_ZERO = "!=0";
	public static final String OP_POSITIVE_NUM = "+n";
	public static final String OP_NEGATIVE_NUM = "-n";
	public static final String OP_NUM_EQUALS = "n:=";
	public static final String OP_DATE_EQUALS = "d:=";
	public static final String OP_STRING_EQUALS = "s:=";
	public static final String OP_NUM_NOT_EQUALS = "n:<>";
	public static final String OP_DATE_NOT_EQUALS = "d:<>";
	public static final String OP_STRING_NOT_EQUALS = "s:<>";
	public static final String OP_NUM_GT = "n:>";
	public static final String OP_DATE_GT = "d:>";
	public static final String OP_STRING_GT = "s:>";
	public static final String OP_NUM_LT = "n:<";
	public static final String OP_DATE_LT = "d:<";
	public static final String OP_STRING_LT = "s:<";
	public static final String OP_NUM_GTE = "n:>=";
	public static final String OP_DATE_GTE = "d:>=";
	public static final String OP_STRING_GTE = "s:>=";
	public static final String OP_NUM_LTE = "n:<=";
	public static final String OP_DATE_LTE = "d:<=";
	public static final String OP_STRING_LTE = "s:<=";
	public static final String OP_LIKE = "like";

	static {
		// $b$[aField!=bField]n_wField 或 $b$[aField=bField]n_wField
		// $b$[aField!=bField]s_wField 或 $b$[aField=bField]s_wField
		OPERATIONS.put(OP_NOT_NULL, "snull_"); // 非空
		OPERATIONS.put(OP_NULL, "null_"); // 为空
		OPERATIONS.put(OP_BLANK, "blank_"); // 非空串
		OPERATIONS.put(OP_NOT_ZERO, "nz_");// 非零(not zero)
		OPERATIONS.put(OP_POSITIVE_NUM, "pn_");// 正数(Positive number)
		OPERATIONS.put(OP_NEGATIVE_NUM, "nn_");// 负数(Negative number)
		OPERATIONS.put(OP_NUM_EQUALS, "n_");// 等于(number equals)
		OPERATIONS.put(OP_DATE_EQUALS, "d_");// (date equals)
		OPERATIONS.put(OP_STRING_EQUALS, "s_");// 等于(string equals)
		OPERATIONS.put(OP_NUM_NOT_EQUALS, "ne_");// 不等于(number not equals)
		OPERATIONS.put(OP_DATE_NOT_EQUALS, "dne_");// 不等于(date not equals)
		OPERATIONS.put(OP_STRING_NOT_EQUALS, "sne_");// 不等于(string not equals)
		OPERATIONS.put(OP_NUM_GT, "gt_"); // 大于(number greate then)
		OPERATIONS.put(OP_DATE_GT, "dgt_"); // 大于(date greate then)
		OPERATIONS.put(OP_STRING_GT, "sgt_"); // 大于(string greate then)
		OPERATIONS.put(OP_NUM_LT, "lt_"); // 小于(number less then)
		OPERATIONS.put(OP_DATE_LT, "dlt_"); // 小于(date less then)
		OPERATIONS.put(OP_STRING_LT, "slt_"); // 小于(string less then)
		OPERATIONS.put(OP_NUM_GTE, "gte_"); // 大于等于(number less then or equals)
		OPERATIONS.put(OP_DATE_GTE, "dgte_"); // 大于等于(date less then or equals)
		OPERATIONS.put(OP_STRING_GTE, "sgte_"); // 大于等于(string less then or
												// equals)
		OPERATIONS.put(OP_NUM_LTE, "lte_");// 小于等于(number less then or equals)
		OPERATIONS.put(OP_DATE_LTE, "dlte_");// 小于等于(date less then or equals)
		OPERATIONS.put(OP_STRING_LTE, "slte_");// 小于等于(string less then or
												// equals)
		OPERATIONS.put(OP_LIKE, "sm_");// 模糊
	}

	protected OrderByClause[] orderByClauses;

	protected List<Criterion> criterionList = new ArrayList<Criterion>();

	public AbstractSQLUtils() {

	}

	public void addCriterion(Criterion criteria) {
		criterionList.add(criteria);
	}

	public void clear() {
		criterionList.clear();
	}
	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table test_y
	 * 
	 * @ibatorgenerated Mon Mar 15 11:03:37 CST 2010
	 */
	protected AbstractSQLUtils(AbstractSQLUtils example) {
		this.orderByClauses = example.orderByClauses;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table test_y
	 * 
	 * @ibatorgenerated Mon Mar 15 11:03:37 CST 2010
	 */
	public void setOrderByClauses(OrderByClause[] orderByClauses) {
		this.orderByClauses = orderByClauses;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method corresponds to the database table test_y
	 * 
	 * @ibatorgenerated Mon Mar 15 11:03:37 CST 2010
	 */
	public OrderByClause[] getOrderByClauses() {
		return orderByClauses;
	}

	public String getOrderByClause() {
		return createOrderByWithString(orderByClauses);
	}

	public abstract Object getFilterCriterions();

	/**
	 * Create the where statement.
	 * 
	 * @param params The parameter table
	 * @return The where statement.
	 */
	public void createWhere(ParamsTable params) {
		// If the paramter is null, return the "";
		if (params == null) return;

		Iterator<String> iter = params.getParameterNames();
		criterionList = new ArrayList<Criterion>();

		while (iter.hasNext()) {
			// String prmn = (String) iter.next();
			String prmn = iter.next();
			String paramsValue = params.getParameterAsString(prmn);
			int st = prmn.indexOf("_");

			if (st > 0 && !StringUtil.isBlank(paramsValue)) {
				String fieldname = prmn.substring(st + 1);

				// 非空
				if (prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NOT_NULL))) {
					criterionList.add(Criterions.isNotNull(fieldname));
					continue;
				}

				// 为空
				if (prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NULL))) {
					criterionList.add(Criterions.isNull(fieldname));
					continue;
				}

				// 非空串
				if (prmn.toLowerCase().startsWith(OPERATIONS.get(OP_BLANK))) {
					criterionList.add(Criterions.isNotEmpty(fieldname));
					continue;
				}
				// 非零(no zero)
				if (prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NOT_ZERO))) {
					criterionList.add(Criterions.ne(fieldname, 0));
					continue;
				}
				// 正数(Positive number)
				if (prmn.toLowerCase().startsWith(OPERATIONS.get(OP_POSITIVE_NUM))) {
					criterionList.add(Criterions.ge(fieldname, 0));
					continue;
				}
				// 负数(Negative number)
				if (prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NEGATIVE_NUM))) {
					criterionList.add(Criterions.le(fieldname, 0));
					continue;
				}

				String[] vallist = null;
				try {
					vallist = StringUtil.split(paramsValue, "|");
				} catch (Exception ex) {
				}
				String val = "";
				Junction disjunction = Criterions.disjunction();

				for (int j = 0; j < vallist.length; j++) {
					val = vallist[j].trim();

					// $b$[aField!=bField]n_wField 或 $b$[aField=bField]n_wField
					// $b$[aField!=bField]s_wField 或 $b$[aField=bField]s_wField
					if (prmn.toLowerCase().startsWith("$")) {
						Pattern pattern = Pattern.compile("[$(\\[){1}(!=){1}=(\\]){1}]+");
						String[] fields = pattern.split(prmn);
						if (fields != null && fields.length == 5) {
							fieldname = fields[4].substring(fields[4].indexOf("_") + 1);
							StringBuilder sql = new StringBuilder();
							sql.append("(select ").append(fields[3]).append(" from ").append(fields[1]);

							ParamsTable p = new ParamsTable();
							p.setParameter(fields[4], val);
							sql.append(" where ").append(this.createWhereWithString(p)).append(")");
							if (prmn.indexOf("!=") > 0) {
								disjunction.add(Criterions.sqlRestriction(fields[2] + " not in " + sql));
							} else {
								disjunction.add(Criterions.sqlRestriction(fields[2] + " in " + sql));
							}
						}
						continue;
					}
					// 等于(number equals) 或 等于(string equals)
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NUM_EQUALS)) || prmn.toLowerCase().startsWith(
							OPERATIONS.get(OP_STRING_EQUALS)))
							&& val.length() > 0) {
						disjunction.add(Criterions.eq(fieldname, val));
						continue;
					}
					// 等于(date equals)
					if (prmn.toLowerCase().startsWith(OPERATIONS.get(OP_DATE_EQUALS)) && val.length() > 0) {
						Date dValue = DateUtil.parseToDate(val);
						if (dValue != null) {
							disjunction.add(Criterions.eq(fieldname, dValue));
						}
						continue;
					}

					// 不等于(not equals)
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NUM_NOT_EQUALS)) || prmn.toLowerCase()
							.startsWith(OPERATIONS.get(OP_STRING_NOT_EQUALS)) && val.length() > 0)) {
						disjunction.add(Criterions.ne(fieldname, val));
						continue;
					}
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_DATE_NOT_EQUALS)) && val.length() > 0)) {
						Date dValue = DateUtil.parseToDate(val);
						if (dValue != null) {
							disjunction.add(Criterions.ne(fieldname, dValue));
						}
						continue;
					}

					// 大于
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NUM_GT)) || prmn.toLowerCase().startsWith(
							OPERATIONS.get(OP_STRING_GT)))
							&& val.length() > 0) {
						disjunction.add(Criterions.gt(fieldname, val));
						continue;
					}
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_DATE_GT)) && val.length() > 0)) {
						Date dValue = DateUtil.parseToDate(val);
						if (dValue != null) {
							disjunction.add(Criterions.gt(fieldname, dValue));
						}
						continue;
					}
					// 小于
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NUM_LT)) || prmn.toLowerCase().startsWith(
							OPERATIONS.get(OP_STRING_LT)))
							&& val.length() > 0) {
						disjunction.add(Criterions.lt(fieldname, val));
						continue;
					}
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_DATE_LT)) && val.length() > 0)) {
						Date dValue = DateUtil.parseToDate(val);
						if (dValue != null) {
							disjunction.add(Criterions.lt(fieldname, dValue));
						}
						continue;
					}
					// 大于等于
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NUM_GTE)) || prmn.toLowerCase().startsWith(
							OPERATIONS.get(OP_STRING_GTE)))
							&& val.length() > 0) {
						disjunction.add(Criterions.ge(fieldname, val));
						continue;
					}
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_DATE_GTE)) && val.length() > 0)) {
						Date dValue = DateUtil.parseToDate(val);
						if (dValue != null) {
							disjunction.add(Criterions.ge(fieldname, dValue));
						}
						continue;
					}

					// 小于等于
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NUM_LTE)) || prmn.toLowerCase().startsWith(
							OPERATIONS.get(OP_STRING_LTE)))
							&& val.length() > 0) {
						disjunction.add(Criterions.le(fieldname, val));
						continue;
					}
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_DATE_LTE)) && val.length() > 0)) {
						Date dValue = DateUtil.parseToDate(val);
						if (dValue != null) {
							disjunction.add(Criterions.le(fieldname, dValue));
						}
						continue;
					}

					// 模糊
					if (prmn.toLowerCase().startsWith(OPERATIONS.get(OP_LIKE)) && val.length() > 0) {
						disjunction.add(Criterions.like(fieldname, val));
						continue;
					}
				}
				criterionList.add(disjunction);
			}
		}
	}

	/**
	 * Create the where statement.
	 * 
	 * @param params The parameter table
	 * @return The where statement.
	 */
	public String createWhereWithString(ParamsTable params) {
		// If the paramter is null, return the "";
		if (params == null) return "";

		Iterator<String> iter = params.getParameterNames();
		StringBuilder cndtn = new StringBuilder(250);

		while (iter.hasNext()) {
			// String prmn = (String) iter.next();
			String prmn = iter.next();
			String paramsValue = params.getParameterAsString(prmn);
			int st = prmn.indexOf("_");

			if (st > 0 && !StringUtil.isBlank(paramsValue)) {
				String fieldname = prmn.substring(st + 1);

				// 非空
				if (prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NOT_NULL))) {
					cndtn.append(fieldname).append(" is not null and ");
					continue;
				}
				// 为空
				if (prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NULL))) {
					cndtn.append(fieldname).append(" is null and ");
					continue;
				}
				// 非空串
				if (prmn.toLowerCase().startsWith(OPERATIONS.get(OP_BLANK))) {
					cndtn.append(fieldname).append(" <> '' and ");
					continue;
				}
				// 非零(no zero)
				if (prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NOT_ZERO))) {
					cndtn.append(fieldname).append(" <> 0 and ");
					continue;
				}
				// 正数(Positive number)
				if (prmn.toLowerCase().startsWith(OPERATIONS.get(OP_POSITIVE_NUM))) {
					cndtn.append(fieldname).append(" > 0 and ");
					continue;
				}
				// 负数(Negative number)
				if (prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NEGATIVE_NUM))) {
					cndtn.append(fieldname).append(" < 0 and ");
					continue;
				}

				String[] vallist = null;
				try {
					vallist = StringUtil.split(paramsValue, "|");
				} catch (Exception ex) {
				}
				String val = "";

				cndtn.append(" ( ");
				for (int j = 0; j < vallist.length; j++) {
					val = vallist[j];

					// 等于(number equals) 或 等于(string equals)
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NUM_EQUALS))) && val.length() > 0) {
						cndtn.append(fieldname).append(" = ").append(val).append(" or ");
						continue;
					}
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_STRING_EQUALS))) && val.length() > 0) {
						cndtn.append(fieldname).append(" = '").append(val).append("' or ");
						continue;
					}
					// 等于(date equals)
					if (prmn.toLowerCase().startsWith(OPERATIONS.get(OP_DATE_EQUALS)) && val.length() > 0) {
						cndtn.append(fieldname).append(" = TO_DATE('").append(val).append("', 'yyyy-mm-dd') or ");
						continue;
					}

					// 不等于(not equals)
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NUM_NOT_EQUALS)) && val.length() > 0)) {
						cndtn.append(fieldname).append(" <> ").append(val).append(" or ");
						continue;
					}
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_STRING_NOT_EQUALS)) && val.length() > 0)) {
						cndtn.append(fieldname).append(" <> '").append(val).append("' or ");
						continue;
					}
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_DATE_NOT_EQUALS)) && val.length() > 0)) {
						cndtn.append(fieldname).append(" <> TO_DATE('").append(val).append("', 'yyyy-mm-dd') or ");
						continue;
					}

					// 大于
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NUM_GT))) && val.length() > 0) {
						cndtn.append(fieldname).append(" > ").append(val).append(" or ");
						continue;
					}
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_STRING_GT))) && val.length() > 0) {
						cndtn.append(fieldname).append(" > '").append(val).append("' or ");
						continue;
					}
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_DATE_GT)) && val.length() > 0)) {
						cndtn.append(fieldname).append(" > TO_DATE('").append(val).append("', 'yyyy-mm-dd') or ");
						continue;
					}
					// 小于
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NUM_LT))) && val.length() > 0) {
						cndtn.append(fieldname).append(" < ").append(val).append(" or ");
						continue;
					}
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NUM_LT))) && val.length() > 0) {
						cndtn.append(fieldname).append(" < '").append(val).append("' or ");
						continue;
					}
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_DATE_LT)) && val.length() > 0)) {
						cndtn.append(fieldname).append(" < TO_DATE('").append(val).append("', 'yyyy-mm-dd') or ");
						continue;
					}
					// 大于等于
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NUM_GTE))) && val.length() > 0) {
						cndtn.append(fieldname).append(" >= ").append(val).append(" or ");
						continue;
					}
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_STRING_GTE))) && val.length() > 0) {
						cndtn.append(fieldname).append(" >= '").append(val).append("' or ");
						continue;
					}
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_DATE_GTE)) && val.length() > 0)) {
						cndtn.append(fieldname).append(" >= TO_DATE('").append(val).append("', 'yyyy-mm-dd') or ");
						continue;
					}

					// 小于等于
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_NUM_LTE))) && val.length() > 0) {
						cndtn.append(fieldname).append(" <= ").append(val).append(" or ");
						continue;
					}
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_STRING_LTE))) && val.length() > 0) {
						cndtn.append(fieldname).append(" <= '").append(val).append("' or ");
						continue;
					}
					if ((prmn.toLowerCase().startsWith(OPERATIONS.get(OP_DATE_LTE)) && val.length() > 0)) {
						cndtn.append(fieldname).append(" <= TO_DATE('").append(val).append("', 'yyyy-mm-dd') or ");
						continue;
					}

					// 模糊
					if (prmn.toLowerCase().startsWith(OPERATIONS.get(OP_LIKE)) && val.length() > 0) {
						cndtn.append(fieldname).append(" like '%").append(val).append("%' or ");
						continue;
					}
				}
				cndtn = cndtn.toString().endsWith("or ") ? cndtn.delete(cndtn.length() - 3, cndtn.length()).append(
						" ) and ") : cndtn;
				cndtn = cndtn.toString().trim().endsWith("(") ? cndtn.delete(cndtn.lastIndexOf("("), cndtn.length())
						: cndtn;
			}
		}

		cndtn = cndtn.toString().trim().endsWith("and") ? cndtn.delete(cndtn.lastIndexOf("and"), cndtn.length())
				: cndtn;
		log.debug(cndtn);
		return cndtn.toString();
	}

	/**
	 * Create the order by statement.
	 * 
	 * @param params the parameter table
	 */
	public void createOrderBy(ParamsTable params) {
		orderByClauses = createOrderByClauses(params);
	}

	/**
	 * Create the order by statement.
	 * 
	 * @param params the parameter table
	 * @return order by strings
	 */
	public OrderByClause[] createOrderByClauses(ParamsTable params) {
		// If the paramter is null, return the "";
		if (params == null) return new OrderByClause[0];

		String orderby = getOrderField(params);
		String desc = getOrderDirection(params);
		String[] orderlist = StringUtil.split(orderby, ';');
		List<OrderByClause> rtn = new ArrayList<OrderByClause>();

		// loop the orderlist & create the statement.;
		if (orderlist != null && orderlist.length > 0) {
			for (int i = 0; i < orderlist.length; i++) {
				// Ingore if the field name is not validity.
				SortOrder sortOrder = SortOrder.ASC;
				if (!StringUtil.isBlank(desc) && "desc".equalsIgnoreCase(desc)) {
					sortOrder = SortOrder.DESC;
				}
				rtn.add(new OrderByClause(sortOrder, orderlist[i]));
			}
		}
		return rtn.toArray(new OrderByClause[rtn.size()]);
	}

	public String createOrderByWithString(ParamsTable params) {
		return createOrderByWithString(createOrderByClauses(params));
	}

	public String createOrderByWithString(OrderByClause[] orderByClauses) {
		StringBuilder cndtn = new StringBuilder(250);
		for (int i = 0; i < orderByClauses.length; i++) {
			if (i > 0) cndtn.append(", ");
			cndtn.append(orderByClauses[i].toString());
		}
		return cndtn.toString();
	}

	/**
	 * @param classname String
	 * @param params Object
	 * @return the Order By statement
	 * @see com.whollyframework.base.dao.support.AbstractSQLUtils#createOrderBy(java.lang.String, java.lang.Object)
	 */
	public String createOrderByWithString(String classname, Object params) {
		// If the paramter is null, return the "";
		if (params == null) return "";

		Class<?> cls = null;
		try {
			cls = Class.forName(classname);
		} catch (Exception ex) {
			return "";
		}

		String orderby = getOrderField(params);
		String desc = getOrderDirection(params);
		String[] orderlist = StringUtil.split(orderby, ';');

		ParamsTable orderbyParams = new ParamsTable();

		Collection<String> values = new ArrayList<String>();
		if (orderlist != null) {
			outer: for (int i = 0; i < orderlist.length; i++) {
				String orderbyFiled = orderlist[i].trim();
				String fieldName = orderbyFiled;
				if (fieldName.indexOf(" ") != -1) {
					fieldName = fieldName.substring(0, fieldName.indexOf(" "));
				}
				Class<?> currentClass = cls;
				while (currentClass != null) {
					try {
						currentClass.getDeclaredField(fieldName);
						values.add(orderbyFiled);
						continue outer;
					} catch (NoSuchFieldException e) {
					}
					currentClass = currentClass.getSuperclass();
				}
			}
		}
		String[] vals = (String[]) values.toArray(new String[values.size()]);
		orderbyParams.setParameter("_orderby", StringUtil.unite(vals, ";"));
		if (!StringUtil.isBlank(desc)) {
			orderbyParams.setParameter("_desc", desc);
		}

		return createOrderByWithString(orderbyParams);
	}

	/**
	 * @param params
	 * @return the Order By Direction statement
	 */
	private String getOrderDirection(Object params) {
		String desc = "";
		try {
			if (params instanceof ParamsTable) {
				desc = ((ParamsTable) params).getParameterAsString("_desc");
			} else {
				desc = (String) PropertyUtils.getProperty(params, "_desc");
			}

			desc = (desc == null || desc.trim().length() == 0 ? "" : "desc");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return desc;
	}

	/**
	 * @param params
	 * @return the Order By Field statement
	 */
	private String getOrderField(Object params) {
		String orderby = null;
		try {
			if (params instanceof ParamsTable) {
				orderby = (String) ((ParamsTable) params).getParameterAsText("_orderby", ";");
			}
			if (orderby == null) orderby = "";
		} catch (Exception ex) {
			ex.printStackTrace();
			orderby = "";
		}
		return orderby.endsWith(";") ? orderby.substring(0, orderby.length() - 1) : orderby;
	}

	/**
	 * 添加条件到SQL语句字串（不支持包含子查询的SQL语句）
	 * 
	 * @param sql SQL语句字串
	 * @param condition 条件字串
	 * @return 拼装后的SQL语句字串
	 */
	public String appendCondition(String sql, String condition) {
		String newSQL = sql.toLowerCase();

		int index;
		if ((index = newSQL.indexOf(" where ")) >= 0) {
			// Append after the "where" direct if it has "where" statement
			// already.
			newSQL = sql.substring(0, index + 7) + " (" + condition + ") AND " + sql.substring(index + 7);
		} else if ((index = newSQL.indexOf(" order by ")) >= 0) {
			// Append before the "order" if it has no "where" but "order"
			// statement.
			newSQL = sql.substring(0, index) + " WHERE (" + condition + ") " + sql.substring(index);
		} else {
			// Append in the end if it has no "where" or "order".
			newSQL = sql + " WHERE (" + condition + ") ";
		}
		return newSQL;
	}

	/**
	 * 添加条件到SQL语句字串（支持子查询SQL语句）
	 * 
	 * @param sql SQL语句字串
	 * @param condition 条件字串
	 * @return 拼装后的SQL语句字串
	 */
	public String appendConditionToLast(String sql, String condition) {
		String newSQL = sql.toLowerCase();
		int endIndex = getSubSelectEndIndex(newSQL);
		String start = newSQL.substring(0, endIndex);
		String end = endIndex < newSQL.length() ? newSQL.substring(endIndex) : "";
		int index;
		if ((index = end.indexOf(" where ")) >= 0) {
			// 存在where
			newSQL = start + end.substring(0, index + 7) + " (" + condition + ") AND " + end.substring(index + 7);
		} else if ((index = end.indexOf(" order by ")) >= 0) {
			// 没有where但有order by
			newSQL = start + end.substring(0, index) + " WHERE (" + condition + ") " + end.substring(index);
		} else {
			// 不存在where
			newSQL = start + end + " WHERE (" + condition + ") ";
		}

		return newSQL;
	}

	/**
	 * 
	 * @param sql 查询语句
	 * @return 如果主查询语句中存在WHERE，则返回WHERE子句的开始位置，否则返回最后位置
	 */
	public int getSubSelectEndIndex(String sql) {
		String newSQL = sql.toLowerCase();
		int fromIndex = getFromIndex(newSQL);
		int where = newSQL.indexOf(" where ");
		// int orderby = newSQL.indexOf(" order by ");
		char[] chars = sql.toCharArray();
		boolean flag = false;
		Stack<Character> stack = new Stack<Character>();
		int index = -1;
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			if (flag == true && chars[i] != ' ') {
				buffer.append(chars[i]);
			}
			// 从主查询语句的FORM位置之后开始判断
			if (i < fromIndex) continue;
			// 如果左括号
			if (chars[i] == '(') {
				stack.push('(');
				if (stack.size() == 1) {
					index = i;
					if (where > 0 && where < index) {// 如果主查询语句中存在WHERE
						return where;
					}
					flag = true;
				} else if (stack.size() == 0) {
					index = i;
					if (where > 0 && where < index) {// 发现取到的是子查询的WHERE，继续查找WHERE
						where = newSQL.indexOf(" where ", where + 6);
					}
					flag = false;
				} else {
					flag = false;
				}
			} else if (chars[i] == ')') {// 如果为右括号
				stack.pop();
				if (stack.size() == 1) {
					index = i;
					if (where > 0 && where < index) {// 如果主查询语句中存在WHERE
						return where;
					}
					flag = true;
				} else if (stack.size() == 0) {
					index = i;
					if (where > 0 && where < index) {// 发现取到的是子查询的WHERE，继续查找WHERE
						where = newSQL.indexOf(" where ", where + 6);
					}
					flag = false;
				} else {
					flag = false;
				}
			}
		}
		return index + 1;
	}

	/**
	 * 
	 * @param sql 查询语句
	 * @return 返回主查询语句的FROM位置
	 */
	public int getFromIndex(String sql) {
		String newSQL = sql.toLowerCase();
		StringBuffer rtnSQL = new StringBuffer();

		int fromIndex = newSQL.indexOf(" from");
		while (fromIndex > 0) {
			String start = newSQL.substring(0, fromIndex);
			String end = newSQL.substring(fromIndex);
			if (isWhenCase(start)) {
				int index = end.indexOf(" end") + 4;
				rtnSQL.append(start + end.substring(0, index));
				newSQL = end.substring(index);
			} else {
				rtnSQL.append(start);
				break;
			}
			fromIndex = newSQL.indexOf(" from");
		}

		return rtnSQL.length();
	}

	/**
	 * 是否存在WHEN...Case...END语句
	 * 
	 * @param sql 查询语句
	 * @return 如果是返回true,否则返回false
	 */
	public boolean isWhenCase(String sql) {
		int when = sql.indexOf("when ");
		if (when >= 0) { return sql.indexOf("case") > 0; }
		return false;
	}

	/**
	 * 是否子查询语句
	 * 
	 * @param sql 查询语句
	 * @return 如果是返回true,否则返回false
	 */
	public boolean isSubSelect(String sql) {
		int startIndex = sql.indexOf("(");
		StringBuffer buffer = new StringBuffer();
		if (startIndex > 0) {
			String str = sql.substring(startIndex + 1);
			boolean flag = false;
			for (int i = 0; i < str.toCharArray().length; i++) {
				char ch = str.toCharArray()[i];
				if (ch != ' ' || flag) { // 从第一个非空字符开始加载
					buffer.append(ch);
					flag = true;
				}

				if (buffer.length() == 6) {
					break;
				}
			}

			if ("SELECT".equalsIgnoreCase(buffer.toString())) { return true; }
		}

		return false;
	}

	/**
	 * 返回子查询语句开始位置
	 * 
	 * @param sql 查询语句
	 * @return 返回子查询语句开始位置
	 */
	public int getSubSelectStartIndex(String sql) {
		char[] chars = sql.toCharArray();
		boolean flag = false;
		Stack<Character> stack = new Stack<Character>();

		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			if (flag == true && chars[i] != ' ') {
				buffer.append(chars[i]);
			}

			if ("SELECT".equalsIgnoreCase(buffer.toString())) { return i - 6; }

			// 如果为最外层的括号
			if (chars[i] == '(') {
				stack.push('(');
			}

			if (chars[i] == ')') {
				stack.pop();
			}

			if (stack.size() == 1) {
				flag = true;
			} else {
				flag = false;
			}
		}

		return -1;
	}

}
