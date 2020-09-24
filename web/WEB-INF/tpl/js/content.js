const LOCAL_TIME = new Date().toLocaleString();
const DAYS = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"]

var appendTime = () => {
    $(".time").text(new Date().toLocaleString().substring(11, 25));

}

var appendDays = () => {
    var now = new Date();
    for (var i = 0; i < 5; i++) {
        $("." + i).text(DAYS[now.getDay() + i] + ", " +
                (((now.getDate() + i).toString().length > 1) ? (now.getDate() + i) : ("0" + (now.getDate() + i))) +
                " " + now.toUTCString().substring(7, 17))
    }
}

var setBackground = () => {
    const hours = parseInt(LOCAL_TIME.substring(12, 15).trim());
    const $content_block = $("#app");
    if (hours > 6 && hours < 18) {
        $content_block.addClass("day-gradient");
    }
    if (hours >= 18 && hours <= 19) {
        $content_block.addClass("sunset-gradient");
    }
    if (hours >= 21) {
        $content_block.addClass("night-gradient");
    }
}

var startScripts = () => {
    appendTime();
    setBackground();
    appendDays();
}

startScripts();
