package org.xpd.app;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.xpd.models.XpdUser;
import org.xpd.util.SessionUtil;

@Aspect
@Component
@EnableAspectJAutoProxy
public class SecurityCheckAspect {

	@Around("execution(* org.xpd.supplier.controller.SupplierInvoiceController.*(..))")
	public Object aroundSupplier(ProceedingJoinPoint joinPoint) throws Throwable {
		Object obj = null;
		if(isUserAuthenticated(joinPoint))
			return joinPoint.proceed();
		throw new Exception("Unauthorized User");
	}
	
	@Around("execution(* org.xpd.buyer.controller.BuyerInvoiceController.*(..))")
	public Object aroundBuyer(ProceedingJoinPoint joinPoint) throws Throwable {
		Object obj = null;
		if(isUserAuthenticated(joinPoint))
			return joinPoint.proceed();
		throw new Exception("Unauthorized User");
	}
	
	private boolean isUserAuthenticated(ProceedingJoinPoint joinPoint) {
		HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
		String auth = request.getHeader("Authorization");
		XpdUser loggedInUser = SessionUtil.getInstance().userSessionMap.get(auth);
		if (loggedInUser != null && SessionUtil.getInstance().userSessionMap.get(loggedInUser.getUsername()) != null)
			return true;
		return false;
	}
}
