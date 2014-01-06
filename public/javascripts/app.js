/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 05/01/14
 * Time: 14:17
 * To change this template use File | Settings | File Templates.
 */

jQuery(function($) {

    var $table = $('.container table');

    var productListUrl = $table.data('list');

    $.get(productListUrl, function (products) {
        $.each(products, function(index, eanCode) {
            $table.append(
                $('<tr/>').append(
                    $('<td/>').text(eanCode)));
        });
    });

});
