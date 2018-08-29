//INICIALIZATIONS
$(".dropdown-trigger").dropdown();
$('.alert').alert();

function submitAjax(form, url, idModalClose, idContainnerReturnHtml, clearContainnerReturnHtml){
	  var json = {};
	  $(form).each(function(){
		    $(this).find(':input').each(function(){
		    	var name = $(this)[0].name;
		    	var value = $(this)[0].type == 'checkbox' ? $(this)[0].checked : $(this)[0].value;
		    	if(name != ''){
		    		if(name.includes('collection')){
		    			var nameCollection = name.split(':')[1];
		    			var namefield = name.split(':')[2];
		    			if(json[nameCollection] == undefined){
		    				json[nameCollection] = [{}];
		    				json[nameCollection][json[nameCollection].length -1][namefield] = value;
		    			}else if(json[nameCollection][json[nameCollection].length -1][namefield] == undefined){
		    				json[nameCollection][json[nameCollection].length -1][namefield] = value;
		    			}else{
		    				json[nameCollection][json[nameCollection].length] = {};
		    				json[nameCollection][json[nameCollection].length -1][namefield] = value;
		    			}
		    		}else if(name.includes('.')){
		    			if(json[name.split('.')[0]] == undefined){
		    				json[name.split('.')[0]] = {};
		    			}
		    		}else{
		    			json[name] = value;
		    		}
		    	}
			});
		});
	  console.log(json);
	  console.log(JSON.stringify(json));
	return submitAjaxJson(JSON.stringify(json), url, idModalClose, idContainnerReturnHtml, clearContainnerReturnHtml);
}

function submitAjaxJson(json, url, idModalClose, idContainnerReturnHtml, clearContainnerReturnHtml){
	loading(true);
	  $.post({
		  url: url,
	      data : json,
          dataType: "json",
          contentType: "application/json",
	      success : function(result) {
	    	  console.log(result);
        }
	}).done(function( data ) {
		if(idContainnerReturnHtml){
			$(idContainnerReturnHtml).html(data.responseText);
		}
		if(data.info != undefined){
			alert(data.info);
		}
		if(idModalClose != undefined){
			$(idModalClose).modal('hide');
		}
	}).fail(function(data) {
		//TODO  verificar por que o spring retorna parse error
		if(data.status == 200){
			if(idContainnerReturnHtml != undefined){
				if(clearContainnerReturnHtml){
					$(idContainnerReturnHtml).html(data.responseText);
				}else{
					$(idContainnerReturnHtml).html($(idContainnerReturnHtml).html() + data.responseText);
				}
			}
			if(data.info != undefined){
				alert(data.info);
			}
			if(idModalClose != undefined){
				$(idModalClose).modal('hide');
			}
		}else{
			if(data.responseJSON){
				alert("error: " + data.responseJSON.message);
			}else{
				alert("error");
			}
		}
	}).always(function() {
		console.log( "finished" );
		loading(false);
	});
	return false;
}

function loadHtml(url, container, clearContainer){
	loading(true);
	if(!clearContainer){
		$(container).html('');
	}
	$.post({
		  url: url,
	}).done(function( data ) {
		if(!clearContainer){
			$(container).html(data);
		}else{
			$(container).html($(container).html() + data);			
		}
	}).fail(function(data) {
		alert("error: " + data.responseText.message);
	}).always(function() {
		console.log( "finished" );
		loading(false);
	});
	return false;
}


function split( val ) {
    return val.split( /,\s*/ );
}

function extractLast( term ) {
    return split( term ).pop();
}

function autocompleteRemote(input, urlControllerSource){
    $(input).on( "keydown", function( event ) {
        if ( event.keyCode === $.ui.keyCode.TAB &&
            $( this ).autocomplete( "instance" ).menu.active ) {
          event.preventDefault();
        }
      })
      .autocomplete({
        source: function( request, response ) {
          $.getJSON(urlControllerSource, {
            term: extractLast( request.term )
          }, response );
        },
        search: function() {
          // custom minLength
          var term = extractLast( this.value );
          if ( term.length < 1 ) {
            return false;
          }
        },
        focus: function() {
          // prevent value inserted on focus
          return false;
        },
        select: function( event, ui ) {
	    	if(this.value.includes(ui.item.value)){
	    		this.value = this.value.replace(ui.item.value + ", ", '').trim();
	    	}
          var terms = split( this.value );
          
          
          // remove the current input
          terms.pop();
          // add the selected item
          terms.push( ui.item.value );
          // add placeholder to get the comma-and-space at the end
          terms.push( "" );
          this.value = terms.join( ", " );
          

          return false;
        },
        appendTo: "form"
      });
}

function loading(open){
	if(open){
		$("#loader").modal().show();
	}else{
		setTimeout(function() {
			$("#loader").modal('hide');
		}, 500);
	}
}