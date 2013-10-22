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
