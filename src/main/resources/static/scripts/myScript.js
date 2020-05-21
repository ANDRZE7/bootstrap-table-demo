function initializeTable(tableId, processing, serverSide, url, languageUrl) {

    // setting defaults
    let _tableId = tableId || "tableId";
    let _processing = processing || false;
    let _serverSide = serverSide || false;
    let _url = url || "./api/data";
    let _languageUrl = languageUrl || "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Polish.json";


    // initialization: language and columns
    $(_tableId).DataTable({
        language: {
            url: _languageUrl
        },
        columns: [
            { "data": "id" },
            { "data": "name" },
            { "data": "surname" },
            { "data": "email" },
            { "data": "dateOfBirth" },
            { "data": "personalNumber" }
        ]
    });

    // ajax initialization
    if (url) {
        $(_tableId).DataTable({
            "processing": _processing,
            "serverSide": _serverSide,
            "ajax": {
                url: _url,
                dataSrc: function (json) {
                    for (var i = 0, ien = json.data.length; i < ien; i++) {
                        json.data[i][0] = '<a href="/message/' + json.data[i][0] + '>View message</a>';
                    }
                    return json.data;
                }
            }
        });
    }

}

// bind to #rowsetSize HTML select; onchange event
function udpateRowsetSize(event) {
    $.post("/persons/api", {'size': event.target.value}, function () {
        window.location.reload();
    });

}

$(document).ready(function () {
    switch (example) {
        case 'example1': {
            $("#myDataTable").DataTable({
                language: {
                    url: "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Polish.json",
                },
            });
        }
            break;
        case 'example2': {
            $('#myDataTable').DataTable({
                "processing": true,
                "ajax": {
                    url: "../persons/api/data",
                    dataSrc: function (json) {
                        for (var i = 0, ien = json.data.length; i < ien; i++) {
                            json.data[i][0] = '<a href="/message/' + json.data[i][0] + '>View message</a>';
                        }
                        return json.data;
                    },
                    dataFilter: function (data) {
                        let json = {};
                        json.data = jQuery.parseJSON(data);
                        json.draw = 1;
                        json.recordsTotal = json.data.length;
                        json.recordsFiltered = json.data.length;
                        result = JSON.stringify(json); // return JSON string
                        return result;
                    }
                },
                language: {
                    url: "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Polish.json",
                },
                columns: [
                    { "data": "id" },
                    { "data": "name" },
                    { "data": "surname" },
                    { "data": "email" },
                    { "data": "dateOfBirth" },
                    { "data": "personalNumber" }
                ]
            });
        }
            break;
        case 'example3': {

            // initialize searh button and key press for example 3
            $(document).on("preInit.dt", function () {
                var $sb = $(".dataTables_filter input[type='search']");
                // remove current handler
                $sb.off();
                // Add key hander
                $sb.on("keypress", function (evtObj) {
                    if (evtObj.keyCode == 13) {
                        $('#myDataTable').DataTable().search($sb.val()).draw();
                    }
                });
                // add button and button handler
                var btn = $("<button type='button'>Go</button>");
                $sb.after(btn);
                btn.on("click", function (evtObj) {
                    $('#myDataTable').DataTable().search($sb.val()).draw();
                });
            });

            $('#myDataTable').DataTable({
                "processing": true,
                "serverSide": true,
                "ajax": {
                    url: "../persons/api/data/managed",
                    dataSrc: function (json) {
                        for (var i = 0, ien = json.data.length; i < ien; i++) {
                            json.data[i][0] = '<a href="/message/' + json.data[i][0] + '>View message</a>';
                        }
                        return json.data;
                    },
                    dataFilter: function (data) {
                        return data;
                    }
                },
                language: {
                    url: "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Polish.json",
                },
                columns: [
                    { "data": "id", "searchable": true },
                    { "data": "name" },
                    { "data": "surname" },
                    { "data": "email", "searchable": true, "orderable": true },
                    { "data": "dateOfBirth", "searchable": true },
                    { "data": "personalNumber" }
                ]
            });
        }
            break;
    }
});
