<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:form>
	<jstl:if test="${hasRequest != true}">
	
	<acme:form-textbox code="authenticated.auditorrequest.form.label.firm" path="firm"/>
	<acme:form-textarea code="authenticated.auditorrequest.form.label.respStatement" path="respStatement"/>
	<acme:form-textarea code="authenticated.auditorrequest.form.label.description" path="description"/>
	
	
	<acme:form-submit test ="${command == 'create'}" code="authenticated.auditorrequest.form.button.create" action="/authenticated/auditorrequest/create"/>
	<acme:form-return code="authenticated.auditorrequest.form.label.button.return"/>
	</jstl:if>
	<jstl:if test="${hasRequest == true}">
	<h4><acme:message code="authenticated.auditorrequest.hasrequest"/></h4>
	<acme:form-return code="authenticated.auditorrequest.form.label.button.return"/>
	</jstl:if>
	
</acme:form>