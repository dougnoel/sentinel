 function customEvent(typeOfEvent) {
    var event = document.createEvent("CustomEvent");
    event.initCustomEvent(typeOfEvent, true, true, null);
    event.dataTransfer = {
        data: {},
        setData: function (key, value) {
            this.data[key] = value;
        },
        getData: function (key) {
            return this.data[key];
        }
    };
    return event;
}
function dispatchEvent(element, event, transferData) {
    if (transferData !== undefined) {
        event.dataTransfer = transferData;
    }
    if (element.dispatchEvent) {
        element.dispatchEvent(event);
    } else if (element.fireEvent) {
        element.fireEvent("on" + event.type, event);
    }
}
function executeDrageAndDrop(element, target) {
    var dragStartEvent = customEvent('dragstart');
    dispatchEvent(element, dragStartEvent);
    var dropEvent = customEvent('drop');
    dispatchEvent(target, dropEvent, dragStartEvent.dataTransfer);
    var dragEndEvent = customEvent('dragend');
    dispatchEvent(element, dragEndEvent, dropEvent.dataTransfer);
}
executeDrageAndDrop(arguments[0], arguments[1])