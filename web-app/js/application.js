if (typeof jQuery !== 'undefined') {
    (function ($) {
        $('#spinner').ajaxStart(function () {
            $(this).fadeIn();
        }).ajaxStop(function () {
                $(this).fadeOut();
            });
    })(jQuery);
}

// local function
var orsp = {
    radioChild: function (parent, child, value) {
        var pSelect = "input[name='" + parent + "-id']";
        var pChecked = pSelect + ":checked";
        var cSelect = "#" + child + "-item";
        var fcn = function () {
            var val = $(pChecked).val();
            if (val == value) {
                $(cSelect).show();
            } else {
                $(cSelect).hide();
            }
        };
        $(pSelect).change(fcn);
        fcn();
    },
    selectChild: function (parent, child, value) {
        var pSelect = "select[name='" + parent + "-id']";
        var cSelect = "#" + child + "-item";
        var fcn = function () {
            var vals = $(pSelect).val();
            var show = false;
            if (vals != null) {
                for (var i = 0; i < vals.length; i++) {
                    if (vals[i] == value) {
                        show = true;
                        break;
                    }
                }
            }
            if (show) {
                $(cSelect).show();
            } else {
                $(cSelect).hide();
            }
        };
        $(pSelect).change(fcn);
        fcn();
    },
};

$(document).ready(function () {
    $("#tabs").tabs();
    //$("#attachments .drag-target").each(function (x) {
    //    alert("droppable");
    //});
    //$("#attachments .drag-target").droppable({
    //   hoverClass: "active",
    //   drop: function (e, ui) {
    //       alert("something dropped");
    //   }
    //});
    var atCount = 0;
    $("#attachments .drag-target").on({
       "dragenter": function (e) {
           e.preventDefault();
           e.stopPropagation();
           $(this).addClass("active");
       },
        "dragleave": function (e) {
            e.preventDefault();
            e.stopPropagation();
            $(this).removeClass("active");
        },
        "dragover": function (e) {
            e.preventDefault();
            e.stopPropagation();
        },
        "drop": function (evt) {
            evt.preventDefault();
            evt.stopPropagation();
            $(this).removeClass("active");
            var files = evt.originalEvent.dataTransfer.files;
            var form = $("#att-form").get(0);
            var data = new FormData(form);
            var rows = [];
            for (var i=0; i < files.length; i++) {
                data.append("files", files[i], files[i].name);
                var row = $("<tr></tr>").append($("<td>" + files[i].name + "</td>"));
                rows.push(row);
            }
            $.ajax({"url": "/orsp/irb/attach",
                "type": "POST",
                "data": data,
                "processData": false,
                "contentType": false,
                success: function () {
                    $("#attachments table").append(rows);
                }
            });
        }
    });
});
