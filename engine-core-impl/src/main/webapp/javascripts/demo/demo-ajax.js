$().ready(function() {

    var idee1 = {workItem: JSON.stringify(DEMO_WORK_ITEM_1), resource: JSON.stringify(DEMO_PARTICIPANT_BUZY_WILLI)};
    var idee2 = JSON.stringify({workItem: DEMO_WORK_ITEM_1, resource: DEMO_PARTICIPANT_BUZY_WILLI});
    var idee3 = JSON.stringify({workItem: DEMO_WORK_ITEM_1, resource: DEMO_PARTICIPANT_BUZY_WILLI});
    var idee6 = JSON.stringify(DEMO_PARTICIPANT_BUZY_WILLI);
console.log(idee6);
  /*  $.ajax({
        type: 'POST',
        url: 'api/worklist/1/claim',
        data: idee6,
        success: function(data) {
            $('#result_1').html(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            $('#result_1').html(jqXHR.responseText).addClass('error');
        },
        contentType: 'application/json', // we send json
        dataType: "json" // we expect json
    });
*/
  /*  $('#data').html(
        JSON.stringify(idee1) + "<br/>" + "<br/>" + "<br/>" + "<br/>" +
        idee2 + "<br/>" + "<br/>" + "<br/>" + "<br/>" +
        idee3 + "<br/>" + "<br/>"
    );
*/
    $("#loginButton").click(function(){

        var uuid = $('#loginBox option:selected').attr('value');
        var name = $('#loginBox option:selected').html();
        $.Storage.set("participantUUID", uuid);
        $.Storage.set("participantName", name);
        $(location).attr('href', 'worklist/');
    });

    $.ajax({
        type: 'GET',
        url: 'api/identity/participants',
        success: function(data) {
            console.log(data);
            var participants = data;
            $.each(participants, function(i, participant) {
                $("#participantnames").append(participant.name);
                $("#loginBox").append("<option value=\"" + participant.id + "\">" + participant.name + "</option>");
            });

        },
        error: function(jqXHR, textStatus, errorThrown) {
            $('#participants').html(jqXHR.responseText).addClass('error');
        },
        dataType: "json" // we expect json
    });


    /*
    $.ajax({
        type: 'POST',
        url: 'api/worklist/item/claim',
        data: idee1,
        success: function(data) {
            $('#result_1').html(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            $('#result_1').html(jqXHR.responseText).addClass('error');
        },
        contentType: 'application/json', // we send json
        dataType: "json" // we expect json
    });

    $.ajax({
        type: 'GET',
        url: 'api/worklist/item/claim',
        data: idee1,
        success: function(data) {
            $('#result_1').html(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            $('#result_1').html(jqXHR.responseText).addClass('error');
        },
        contentType: 'application/json', // we send json
        dataType: "json" // we expect json
    });

    $.ajax({
        type: 'POST',
        url: 'api/worklist/items/post',
        data: JSON.stringify(DEMO_PARTICIPANT_BUZY_WILLI),
        success: function(data) {
            $('#result_2').html(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            $('#result_2').html(jqXHR.responseText).addClass('error');
        },
        contentType: 'application/json', // we send json
        dataType: "json" // we expect json
    });

    $.ajax({
        type: 'GET',
        url: 'api/worklist/items',
        data: {resource: JSON.stringify(DEMO_PARTICIPANT_BUZY_WILLI)},
        success: function(data) {
            $('#result_3').html(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            $('#result_3').html(jqXHR.responseText).addClass('error');
        },
        contentType: 'application/json', // we send json
        dataType: "json" // we expect json
    });
	*/
});

