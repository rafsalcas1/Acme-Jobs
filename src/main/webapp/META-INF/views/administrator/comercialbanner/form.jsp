<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<acme:form>
	<acme:form-url code="administrator.comercialbanner.form.label.urlPicture" path="urlPicture"/>
	<acme:form-textbox code="administrator.comercialbanner.form.label.slogan" path="slogan"/>
	<acme:form-url code="administrator.comercialbanner.form.label.urlTarget" path="urlTarget"/>
	<jstl:if test="${command == 'show'}">
	<acme:form-textbox code="administrator.comercialbanner.form.label.creditCard" path="creditNumber" readonly="true"/>
	</jstl:if>
	
	
	
	
	<acme:form-return code="administrator.comercialbanner.form.label.button.return"/>
</acme:form>