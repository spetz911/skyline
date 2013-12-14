/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 12/12/13
 * Time: 23:33
 * To change this template use File | Settings | File Templates.
 */

function getSelectedText() {
    var text = "";
    if (typeof window.getSelection != "undefined") {
        text = window.getSelection().toString();
    }
    return text;
}

function doSomethingWithSelectedText () {
    var selectedText = getSelectedText();
    if (selectedText) {
        alert(selectedText + "");
    }
}

document.onmouseup = doSomethingWithSelectedText;
document.onkeyup = doSomethingWithSelectedText;


function replaceSelection(replacedHtml) {
    var sel, range, node;

    if (typeof window.getSelection != "undefined") {
        // IE 9 and other non-IE browsers
        sel = window.getSelection();

        // Test that the Selection object contains at least one Range
        if (sel.getRangeAt && sel.rangeCount) {
            // Get the first Range (only Firefox supports more than one)
            range = window.getSelection().getRangeAt(0);
            range.deleteContents();

            // Create a DocumentFragment to insert and populate it with HTML
            // Need to test for the existence of range.createContextualFragment
            // because it's non-standard and IE 9 does not support it
            if (range.createContextualFragment) {
                node = range.createContextualFragment(replacedHtml);
            }
            range.insertNode(node);
        }
    }
}


function replaceSelectedText(replacementText) {
    var sel, range;
    if (window.getSelection) {
        sel = window.getSelection();
        if (sel.rangeCount) {
            range = sel.getRangeAt(0);
            range.deleteContents();
            range.insertNode(document.createTextNode(replacementText));
        }
    }
}


// @* onselect works only in textarea  *@

