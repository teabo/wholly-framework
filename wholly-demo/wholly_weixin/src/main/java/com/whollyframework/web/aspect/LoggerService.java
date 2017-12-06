package com.whollyframework.web.aspect;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.whollyframework.annotation.ClassLog;
import com.whollyframework.annotation.MethodLog;
import com.whollyframework.authentications.IWebUser;
import com.whollyframework.base.model.DataPackage;
import com.whollyframework.base.model.ValueObject;
import com.whollyframework.dbservice.operatelog.model.OperateLog;
import com.whollyframework.dbservice.operatelog.service.OperateLogService;
import com.whollyframework.util.IPUtil;
import com.whollyframework.util.StringUtil;
import com.whollyframework.util.property.DefaultProperty;

/**
 * 
 * @Aspect 实现spring aop 切面（Aspect）：
 *         一个关注点的模块化，这个关注点可能会横切多个对象。事务管理是J2EE应用中一个关于横切关注点的很好的例子。 在Spring
 *         AOP中，切面可以使用通用类（基于模式的风格） 或者在普通类中以 @Aspect 注解（@AspectJ风格）来实现。
 * 
 *         AOP代理（AOP Proxy）： AOP框架创建的对象，用来实现切面契约（aspect contract）（包括通知方法执行等功能）。
 *         在Spring中，AOP代理可以是JDK动态代理或者CGLIB代理。 注意：Spring
 *         2.0最新引入的基于模式（schema-based
 *         ）风格和@AspectJ注解风格的切面声明，对于使用这些风格的用户来说，代理的创建是透明的。
 * @author Chris Hsu
 * 
 */
@Component
@Aspect
public class LoggerService {
	@Autowired
	private OperateLogService operlogService;

	private OperateLog czrz = null;

	/**
	 * 在Spring2.0中，Pointcut的定义包括两个部分：Pointcut表示式(expression)和Pointcut签名(signature )。
	 * 让我们先看看execution表示式的格式： 括号中各个pattern分别表示修饰符匹配（modifier-pattern?）、
	 * 返回值匹配（ret-type-pattern）、 类路径匹配（declaring-type-pattern?）、 方法名匹配（name-pattern）、
	 * 参数匹配（(param-pattern)）、 异常类型匹配（throws-pattern?），其中后面跟着“?”的是可选项。
	 * 
	 * @param point
	 * @throws Throwable
	 */
	@Pointcut("@annotation(com.whollyframework.annotation.MethodLog)")
	public void methodCachePointcut() {

	}

	// 方法执行的前后调用
	@Around("methodCachePointcut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		if (!(point.getTarget() instanceof OperateLogService)) {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			String ip = IPUtil.getIpAddr(request);
			HttpSession session = request.getSession();
			IWebUser user = getUser(session);
			String loginName;
			if (user != null) {
				loginName = user.getLoginName();
			} else {
				loginName = "匿名用户";
			}
			
			ClassLog classLog = getClassLog(point);
			
			MethodLog methodCache = getMethodLog(point);
			String monthRemark = "";
			String operType = "1"; // 查询
			if (methodCache != null) {
				monthRemark = methodCache.remark();
				operType = methodCache.operType();
			}

			String monthName = point.getSignature().getName();
			String packages = point.getThis().getClass().getName();
			if (packages.indexOf("$$EnhancerBySpringCGLIB$$") > -1) { // 如果是CGLIB动态生成的类
				try {
					packages = packages.substring(0, packages.indexOf("$$"));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			Object object;
			czrz = new OperateLog();
			czrz.setModelPath(packages + "." + monthName);
			try {
				if ("2".equals(operType) || "3".equals(operType)) { // 3：更新； 2：新增
					// save.action
					StringBuffer buf = new StringBuffer();
					String id = request.getParameter("id");
					if (!StringUtil.isBlank(id)) {
						buf.append("id = ").append(id).append("");
					} else {
						// 新增
					}
					czrz.setOperateCondition(buf.toString());
				} else if ("4".equals(operType)) {
					String id = request.getParameter("_selects");
					StringBuffer buf = new StringBuffer();
					if (StringUtil.isBlank(id)) {
						id = request.getParameter("id");
						if (!StringUtil.isBlank(id)) {
							buf.append("id = ").append(id).append(" ");
						}
					} else {
						buf.append("id = ").append(id).append(" ");
					}
					czrz.setOperateCondition(buf.toString());

				} else if ("1".equals(operType)) {
					// 记录datagrid的查询信息
					StringBuilder sb = new StringBuilder();

					Enumeration<?> pNames = request.getParameterNames();
					while (pNames.hasMoreElements()) {
						String name = (String) pNames.nextElement();
						String value = request.getParameter(name);

						sb.append("[").append(name.substring(name.indexOf("_") + 1) + " ")
								.append(" " + LoggerUtil.getOperator(name) + " ").append(" " + value)
								.append("]");
					}
					czrz.setOperateCondition(sb.toString());
				}

				object = point.proceed();
			} catch (Exception e) {
				// 异常处理记录日志..log.error(e);
				throw e;
			}
			// num_id与主键同值;
			// 用于唯一标识应用系统/资源库产生的日志数据中的一条记录，在日志记录产生时生成，其格式和产生方式由应用系统/资源库自行决定
			czrz.setRegId(DefaultProperty.getProperty("whollyframework.operate.log.regId"));// 12个字符串;
																							// 应用系统/资源库标识由12位字符组成，结构如下：
			czrz.setUserId(loginName);
			czrz.setOrganization("");// 待定;
			czrz.setOrganizationId("");// 待定;
			czrz.setUserName(user.getName() == null ? "" : user.getName());
			czrz.setTerminalId(ip);
			czrz.setOperateTime(toDateStr(new Date()));

			czrz.setOperateNumber(0);
			czrz.setErrorCode("");
			czrz.setOperateName(classLog!=null?"["+classLog.remark()+"]:"+monthRemark:monthRemark);
			czrz.setInsertTime(new Date());
			czrz.setCollectType("");// 待定;
			czrz.setSendid("");// 待定;
			czrz.setOperateType(operType);
			// 这里有点纠结 就是不好判断第一个object元素的类型 只好通过 方法描述来 做一一 转型感觉 这里 有点麻烦 可能是我对 aop不太了解
			// 希望懂的高手在回复评论里给予我指点
			// 有没有更好的办法来记录操作参数 因为参数会有 实体类 或者javabean这种参数怎么把它里面的数据都解析出来?
			Boolean hasFieldErrors = (Boolean) request.getAttribute("hasFieldErrors");
			if (hasFieldErrors != null && hasFieldErrors) {
				// 出现异常
				czrz.setOperateResult("0");// 用户操作的结果，包括成功/失败。1:成功； 0：失败
				czrz.setErrorCode("2000");// 应用系统方面的错误
			} else {
				// 正常
				czrz.setOperateResult("1");// 成功
				if (object instanceof DataPackage) {
					DataPackage<?> datas = (DataPackage<?>)object;
					czrz.setOperateNumber(datas.rowCount);
				} else if (object instanceof List) {
					List<?> datas = (List<?>)object;
					czrz.setOperateNumber(datas.size());
				} else if (object instanceof ValueObject) {
					czrz.setOperateNumber(1);
				} else if (object instanceof Integer) {
					czrz.setOperateNumber((Integer)object);
				}
			}

			if (!StringUtils.isEmpty(czrz.getOperateName())) {
				// 排除login.action
				operlogService.doCreate(czrz);
			}
			return object;
		}
		return point.proceed();
	}

	// 方法运行出现异常时调用
	@AfterThrowing(pointcut = "methodCachePointcut()", throwing = "ex")
	public void afterThrowing(Exception ex) {
		System.out.println("afterThrowing");
		System.out.println(ex);
	}
	
	public static ClassLog getClassLog(ProceedingJoinPoint joinPoint) throws Exception {
		return joinPoint.getTarget().getClass().getAnnotation(ClassLog.class);
	}

	// 获取方法的中文备注____用于记录用户的操作日志描述
	public static MethodLog getMethodLog(ProceedingJoinPoint joinPoint) throws Exception {
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();

		Method[] method = joinPoint.getTarget().getClass().getMethods();
		MethodLog methodCache = null;
		for (Method m : method) {
			if (m.getName().equals(methodName)) {
				Class<?>[] tmpCs = m.getParameterTypes();
				if (tmpCs.length == arguments.length) {
					methodCache = m.getAnnotation(MethodLog.class);
					break;
				}
			}
		}
		return methodCache;
	}

	public IWebUser getUser(HttpSession session) {
		Object obj = session.getAttribute("USER");
		if (obj == null) {
			return null;
		}
		return (IWebUser) obj;
	}

	public String toDateStr(Date d) {
		String str = null;
		try {
			str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return str;
	}

}
