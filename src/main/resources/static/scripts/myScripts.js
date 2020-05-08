function initializeTable(tableId, processing, serverSide, url, languageUrl) {

    // setting defauls
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
