<%--
- list.jsp
-
- Copyright (c) 2019 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:list>
	<acme:list-column code="employer.duty.list.label.title" path="title" width="30%"/>		
	<acme:list-column code="employer.duty.list.label.percentage" path="percentage" width="10%"/>	
	<acme:list-column code="employer.duty.list.label.description" path="description" width="60%"/>	
	
</acme:list>
<jstl:set var="paramIdDescriptor" value="${param.idDescriptor}"/>



<jstl:if test="${param.fm == false}">
<h4><acme:menu-suboption code="employer.duty.list.button.addDuty" action="/employer/duty/create?idDescriptor=${paramIdDescriptor}&fm=${param.fm}"/></h4>
</jstl:if>
