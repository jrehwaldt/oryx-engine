


$().ready(function() {

    var idee1 = {workItem: JSON.stringify(DEMO_WORK_ITEM_1), resource: JSON.stringify(DEMO_PARTICIPANT_BUZY_WILLI)};
    var idee2 = JSON.stringify({workItem: DEMO_WORK_ITEM_1, resource: DEMO_PARTICIPANT_BUZY_WILLI});
    var idee3 = JSON.stringify({workItem: DEMO_WORK_ITEM_1, resource: DEMO_PARTICIPANT_BUZY_WILLI});

    $('#data').html(
        JSON.stringify(idee1) + "<br/>" + "<br/>" + "<br/>" + "<br/>" +
        idee2 + "<br/>" + "<br/>" + "<br/>" + "<br/>" +
        idee3 + "<br/>" + "<br/>"
    );

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
    /*
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
    */
    
    /*
    $.ajax({
        type: 'POST',
        url: 'api/worklist/items',
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

    /*
    $.post('api/worklist/item/claim', {workItem: DEMO_WORKITEM, resource: null},
        function(data) {
            $('#demo').html(data);
        },
        "json"
    );
    */
});

