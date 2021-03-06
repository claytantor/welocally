<%@ page 
	contentType="text/html; charset=iso-8859-1" 
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:url value='/publisher/place/querytest.json' var="searchPlacesAction"/>
<c:url value='/publisher/place/feature.json' var="savePlaceByFeatureAction"/>
<c:url value='/publisher/place/chooser-add.json' var="addPlaceAction"/>
<script>
    var publisherKey = '${param['publisherKey']}';
    var welocallyBaseurl = 'http://www.welocally.com/';
var jsonObjFeatures = []; //declare features array
var selectedFeatureIndex = 0;

function getLocationsByAddress(address, options) {
	
	$('#selectable').empty();
	jsonObjFeatures = [];
	$.ajax('${searchPlacesAction}', {
        data : {
            'publisher-key' : publisherKey,
            'welocally-baseurl' : welocallyBaseurl,
            'address' : address,
            'query' : options.q
        },
        error : function(jqXHR, textStatus, errorThrown) {
            console.error(textStatus);
        },
        success : function(data, textStatus, jqXHR) {
            $.each(data.places, function(i,item){
                //console.log(JSON.stringify(item));
                jsonObjFeatures.push(item);
                $('#selectable').html($('#selectable').html() + buildListItemForPlace(item,i));
            });

            $( "#dialog-modal" ).dialog({
                height: 500,
                width: 500,
              close: function(event, ui) {
                  $('#results').hide();
                  $('#place-search').val('')
                  $("#add-span").hide();
              }
            });

            $("#dialog-modal").dialog("option", "position", "center");

            $('#results').show();
          $("#add-span").show();
          $("#add-form-div").hide();
        }
    });
}


function buildListItemForPlace(place,i) {
        var itemLabel = '<b>'+place.name+'</b>';
        if (place.address) {
            itemLabel += "<br>" + place.address;
        }
		return '<li class=\"ui-widget-content\" id="f'+i+'" title="select place">'+itemLabel+'</li>';
}

function searchForPlaces() {
		console.log("click");
		var options = {};
		options.q = $('#place-search').val();

		getLocationsByAddress($('#place-address').val(), options);
		return false;
}

$(function() {
	$( "#search-places-action" ).click(searchForPlaces);

	$( "#choose-place-action" ).click(function() {

	    var featureSelected = jsonObjFeatures[selectedFeatureIndex];
	    console.log(JSON.stringify(featureSelected));


	    $.ajax({
			type : 'POST',
			url : '${savePlaceByFeatureAction}',
			contentType: 'application/json',
			dataType : 'json',
			data: JSON.stringify(featureSelected),
			success : function(data){
				<%--$('#waiting').hide(500);
				$('#message').removeClass().addClass((data.error === true) ? 'error' : 'success')
					.text(data.msg).show(500);
				if (data.error === true)
					$('#demoForm').show(500);--%>

				console.log(data.place.name);
				$( "#place-id" ).val( data.place.id );
				$( "#place-name" ).val( data.place.name );
				$( "#dialog-modal" ).dialog( 'close' );

			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				 console.error('error');
				<%--$('#waiting').hide(500);
				$('#message').removeClass().addClass('error')
					.text('There was an error.').show(500);
				$('#demoForm').show(500);--%>
			}
		});


		return false;
	});

    $( "#add-place-action" ).click(function() {
        $("#results").hide();
        $("#add-form-div").show();

        $('#add-place-city').val($('#place-address').val());
        $('#add-place-name').val($('#place-search').val());
        $('#add-place-name').focus();
        return false;
    });
    $( "#cancel-add-link" ).click(function() {
        $("#add-form-div").hide();
        $("#results").show();
    });
    $( "#save-place-action" ).click(function() {
        if (!$("#add-place-name").val().match(/\S/)) {
            alert("Please enter the new place's name");
            return false;
        }
        if (!$("#add-place-street").val().match(/\S/)) {
            alert("Please enter the new place's street address");
            return false;
        }
        if (!$("#add-place-city").val().match(/\S/)) {
            alert("Please enter the new place's city");
            return false;
        }
        if (!$("#add-place-state").val().match(/\S/)) {
            alert("Please enter the new place's state");
            return false;
        }
        if (!$("#add-place-zip").val().match(/\S/)) {
            alert("Please enter the new place's zip code");
            return false;
        }
        $.ajax({
            type : 'POST',
            url : '${addPlaceAction}',
//            contentType: 'application/json', // don't do this or request params won't get through
            dataType : 'json',
            data: $('#add-form').serialize(),
            success : function(data){
                console.log(data.place.name);
                $( "#place-id" ).val( data.place.id );
                $( "#place-name" ).val( data.place.name );
                $( "#dialog-modal" ).dialog( 'close' );

            },
            error : function(XMLHttpRequest, textStatus, errorThrown) {
                console.error('error');
                alert("An error occurred saving your changes, please try again");
            }
        });
        return false;
    });

	$("#results").hide();
	$("#selection").hide();
    $("#add-span").hide();
    $("#add-form-div").hide();


	$( "#selectable" ).selectable({
		   selected: function(event, ui) {
				selectedFeatureIndex = $('#scroller-places li').index(ui.selected);
				$("#selection").show();
		   }
	});

	$( "#dialog-modal" ).dialog({
		height: 200,
		width: 500,
		modal: true,
		resizable:true,
		autoOpen: false
	});

    $(window).resize(function() {
    	$("#dialog-modal").dialog("option", "position", "center");
    });

	$('#place-name').focus(function() {
		$( "#dialog-modal" ).dialog( 'open' );
	});

});




</script>
<style>
		.search-field { width: 100%; }
        #results { margin-top: 5px; }
		#selectable .ui-selecting { background: #BFED8E; }
		#selectable .ui-selected { background: #7D8C6C; color: white; }
		#selectable { list-style-type: none; margin: 0; padding: 0; }
		#selectable li { margin: 3px 3px 3px 0; padding: 0.2em; cursor: pointer; }
		#scroller-places { height: 240px; width: 100%; overflow-y: scroll;}
        /*#add-span { float:right; }*/
        #add-span { }
        /*#add-place-action { float:right; }*/
        #selection { margin-top: 5px; }
        #add-form-div { margin-top: 5px; }
</style>
<div id="dialog-modal" title="Choose Place">
    <form onsubmit="return searchForPlaces()">
	<div>Find where the event will occur.</div>
	<div>	
		place address:</br>
		<input type="text" id="place-address" class="search-field" value="Oakland, CA"></br>	
		search term: (optional)</br>
		<input type="text" id="place-search" class="search-field"></br>
		<%--<div id="search-places-action"><a href="#">find places</a></div> --%>
		<button id="search-places-action">find places</button>
        <span id="add-span">don't see a match?&nbsp;&nbsp;<button id="add-place-action">add new place</button></span>
	</div>
	<div id="results">
	<div id="scroller-places">
		<ol id="selectable">
		</ol>	
	</div>
	<%--<div><img src="<c:url value='/images/spacer.gif' />" height="5"/></div>--%>
	<div id="selection">
		<button id="choose-place-action">choose place</button>
	</div>
	</div>
    </form>
    <form onsubmit="return false" id="add-form">
    <div id="add-form-div">
        place name:</br>
        <input type="text" id="add-place-name" name="add-place-name" class="search-field"></br>
        street address:</br>
        <input type="text" id="add-place-street" name="add-place-street" class="search-field"></br>
        city:</br>
        <input type="text" id="add-place-city" name="add-place-city" class="search-field"></br>
        state:</br>
        <input type="text" id="add-place-state" name="add-place-state" class="search-field"></br>
        zip code:</br>
        <input type="text" id="add-place-zip" name="add-place-zip" class="search-field"></br>
        <a id="cancel-add-link" href="#">cancel</a>
        <button id="save-place-action">add</button>
    </div>
    </form>
</div>
	
