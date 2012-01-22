Simple Web Forms creation and auto saving submitted data.

1. Form Definition node is created in /usr/local/etc/forms

	* On Admin Part there is UI for this: 
		List of existing Forms: /admin/com.choncms.webpage.forms.list.do
		Edit/Create Form: /admin/com.choncms.webpage.forms.edit.do

	Example:
		simple.form
			- data: HTML data without <form> tag, but with all input fields and submit button
			- successData: data to be shown on success, (enabled velocity and variables from form data can be used)
			- errorData: TBD

2. Render form on HTML using extensions

	$ext.forms.render('simple.form')
	

After submit, form data is posted on current node, page is reloaded and in place
of $ext.forms.render will be rendered form.successData


On server side, form data is converted to java Map, with field names as keys.
Special system input hidden values are named with double underscore, eg. __formName, and those can be found in "ctx" key of map.

There are 3 limitation rules for naming form fields:

	1. names of your input fields must NOT start with "__"
	2. name of your input fields must NOT be "ctx"
	3. name of your input fields must NOT be "type" 
 
 
	Example for submit data processing to server:
		If the form "simple.form" property "data" is:
			<input type="text" name="txtProperty" />
			<input type="submit"/>
			
		the Map on server on submit will look like:
			txtProperty: 'Property Value',
			ctx: {
				formName: 'simple.form', 
				submit_form: true, 
				... (all other fields starting with __ ) .. 
			}
		
		Node for data submit under simple.form will be created with name System.currentTimeMillis()
			simple.form
				12340323420 <- System.currentTimeMillis()
					type: 'form.submit',
					txtProperty: 'Property Value'
 
TODO:
	- advanced form fields, file upload 
 	- form validation  
 	- run a workflow for each submitted form


