<%@ page import="registros.Document" %>



<div class="fieldcontain ${hasErrors(bean: documentInstance, field: 'authorId', 'error')} ">
	<label for="authorId">
		<g:message code="document.authorId.label" default="Author Id" />
		
	</label>
	<g:textField name="authorId" value="${documentInstance?.authorId}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: documentInstance, field: 'bindData', 'error')} ">
	<label for="bindData">
		<g:message code="document.bindData.label" default="Bind Data" />
		
	</label>
	
</div>

<div class="fieldcontain ${hasErrors(bean: documentInstance, field: 'category', 'error')} ">
	<label for="category">
		<g:message code="document.category.label" default="Category" />
		
	</label>
	<g:textField name="category" value="${documentInstance?.category}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: documentInstance, field: 'compositionArchetypeId', 'error')} ">
	<label for="compositionArchetypeId">
		<g:message code="document.compositionArchetypeId.label" default="Composition Archetype Id" />
		
	</label>
	<g:textField name="compositionArchetypeId" value="${documentInstance?.compositionArchetypeId}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: documentInstance, field: 'content', 'error')} ">
	<label for="content">
		<g:message code="document.content.label" default="Content" />
		
	</label>
	<g:select name="content" from="${registros.Item.list()}" multiple="multiple" optionKey="id" size="5" value="${documentInstance?.content*.id}" class="many-to-many"/>
</div>

<div class="fieldcontain ${hasErrors(bean: documentInstance, field: 'end', 'error')} required">
	<label for="end">
		<g:message code="document.end.label" default="End" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="end" precision="day"  value="${documentInstance?.end}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: documentInstance, field: 'start', 'error')} required">
	<label for="start">
		<g:message code="document.start.label" default="Start" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="start" precision="day"  value="${documentInstance?.start}"  />
</div>

