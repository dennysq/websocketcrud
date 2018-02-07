var $TABLE = $('#table');
var $BTN = $('#export-btn');
var $EXPORT = $('#export');

$('.table-add').click(function () {
    var $clone = $TABLE.find('tr.hide').clone(true).removeClass('hide table-line');
    $TABLE.find('table').append($clone);
});

$('.table-remove').click(function () {
    $(this).parents('tr').detach();
});

$('.table-up').click(function () {
    var $row = $(this).parents('tr');
    if ($row.index() === 1)
        return; // Don't go above the header
    $row.prev().before($row.get(0));
});

$('.table-down').click(function () {
    var $row = $(this).parents('tr');
    $row.next().after($row.get(0));
});

// A few jQuery helpers for exporting only
jQuery.fn.pop = [].pop;
jQuery.fn.shift = [].shift;

$BTN.click(function () {
    var $rows = $TABLE.find('tr:not(:hidden)');
    var headers = [];
    var data = [];

    // Get the headers (add special header logic here)
    $($rows.shift()).find('th:not(:empty)').each(function () {
        headers.push($(this).text().toLowerCase());
    });

    // Turn all existing rows into a loopable array
    $rows.each(function () {
        var $td = $(this).find('td');
        var h = {};

        // Use the headers from earlier to name our hash keys
        headers.forEach(function (header, i) {
            h[header] = $td.eq(i).text();
        });

        data.push(h);
    });

    // Output the result
    $EXPORT.text(JSON.stringify(data));
});
var personas = [];
var wsocket;
function connect() {
    wsocket = new WebSocket("ws://localhost:8080/WebSocketCRUD-1.0/directorio");
    wsocket.onmessage = onMessage;
    // wsocket.onopen = () => wsocket.send('getAll');
}
function onMessage(evt) {
    //     var arraypv = evt.data.split("/");
    console.log(evt.data);
    personas = JSON.parse(evt.data);
    //       this.countries =evt.data;

    console.log(JSON.stringify(personas));

    $TABLE.find('table tr:not(:first)').remove();
    var data = '';
    if (personas.length > 0) {
        for (i = 0; i < personas.length; i++) {
            data += '<tr>';
            data += '<td contenteditable="true">' + personas[i].nombre + '</td>';
            data += '<td contenteditable="true">' + personas[i].email + '</td>';
            data += '</tr>';
        }
    }
    $TABLE.find('table').append(data);
    // app.FetchAll();
    //   document.getElementById("price").innerHTML = arraypv[0];
    // document.getElementById("volume").innerHTML = arraypv[1];
}
window.addEventListener("load", connect, false);
recuperar = function () {
    wsocket.send('getAll');
};
guardar = function () {

    var $rows = $TABLE.find('tr:not(:hidden)');
    var headers = [];
    var data = [];

    // Get the headers (add special header logic here)
    $($rows.shift()).find('th:not(:empty)').each(function () {
        headers.push($(this).text().toLowerCase());
    });

    // Turn all existing rows into a loopable array
    $rows.each(function () {
        var $td = $(this).find('td');
        var h = {};

        // Use the headers from earlier to name our hash keys
        headers.forEach(function (header, i) {
            h[header] = $td.eq(i).text();
        });

        data.push(h);
    });

    // Output the result
    // $EXPORT.text(JSON.stringify(data));
    wsocket.send("addAll;" + JSON.stringify(data));
};
var app = new function () {



    this.el = document.getElementById('countries');
    //  this.countries = ['France', 'Germany', 'England', 'Spain', 'Belgium', 'Italy', 'Portugal', 'Irland', 'Luxembourg'];
    this.Count = function (data) {
        var el = document.getElementById('counter');
        var name = 'country';
        if (data) {
            if (data > 1) {
                name = 'Personas';
            }
            el.innerHTML = data + ' ' + name;
        } else {
            el.innerHTML = 'No ' + name;
        }
    };
    this.Recuperar = function () {
        wsocket.send('getAll');
    }

    this.FetchAll = function () {
        var data = '';
        if (countries.length > 0) {
            for (i = 0; i < countries.length; i++) {
                data += '<tr>';
                data += '<td>' + countries[i] + '</td>';
                data += '<td><button onclick="app.Edit(' + i + ')">Edit</button></td>';
                data += '<td><button onclick="app.Delete(' + i + ')">Delete</button></td>';
                data += '</tr>';
            }
        }
        this.Count(countries.length);
        return this.el.innerHTML = data;
    };
    this.Add = function () {
        el = document.getElementById('add-name');
        // Get the value
        wsocket.send("add;" + el.value);
        var country = el.value;
        if (country) {
            // Add the new value
            countries.push(country.trim());
            // Reset input value
            el.value = '';
            // Dislay the new list
            this.FetchAll();
        }
    };
    this.Edit = function (item) {
        var el = document.getElementById('edit-name');
        // Display value in the field
        el.value = countries[item];
        // Display fields
        document.getElementById('spoiler').style.display = 'block';
        self = this;
        document.getElementById('saveEdit').onsubmit = function () {
            // Get value
            var country = el.value;
            if (country) {
                // Edit value
                countries.splice(item, 1, country.trim());
                wsocket.send("edit;" + item + ";" + el.value);
                // Display the new list
                self.FetchAll();
                // Hide fields
                CloseInput();
            }
        };
    };
    this.Delete = function (item) {
        // Delete the current row
        countries.splice(item, 1);
        wsocket.send("delete;" + item);
        // Display the new list
        this.FetchAll();
    };

};
//  app.FetchAll();
function CloseInput() {
    document.getElementById('spoiler').style.display = 'none';
}
;