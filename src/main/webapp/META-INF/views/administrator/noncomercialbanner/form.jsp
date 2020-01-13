<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<acme:form>
	<acme:form-url code="administrator.noncomercialbanner.form.label.urlPicture" path="urlPicture"/>
	<acme:form-textarea code="administrator.noncomercialbanner.form.label.slogan" path="slogan"/>
	<acme:form-url code="administrator.noncomercialbanner.form.label.urlTarget" path="urlTarget"/>
	
	<jstl:if test="${hasJingle == true}">
	<acme:form-url code="administrator.noncomercialbanner.form.label.jingle" path="jingle"/>
	</jstl:if>
	
	<acme:form-return code="administrator.noncomercialbanner.form.label.button.return"/>
</acme:form>