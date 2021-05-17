function del(id) {
    if (confirm("Are you sure you wish to delete the url?"))
        $.ajax({
            url: `delete`,
            method: 'post',
            data: {
                id: id
            },
            success: function () {
                location.reload()
            }
        })
}

function add(userId) {
    const regexp = /(ftp|http|https):\/\/(\w+:?\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
    let urlText = $("#urlText").val()
    if (regexp.test(urlText)) {
        $("#errorMsg").hide()
        $.ajax({
            url: 'add',
            method: 'post',
            data: {
                userId: userId,
                url: urlText
            },
            success: function () {
                $("#urlText").val("")
                location.reload()
            }
        })
    } else {
        let errorMsg = $("#errorMsg")
        if (!errorMsg.hidden)
            errorMsg.hide()
        errorMsg.slideDown()
    }
}

function logout() {
    document.cookie = 'user=; expires=Thu, 01 Jan 1970 00:00:00 UTC;'
    $(location).attr("href", "logout");
}

function nav() {
    let val = $("#cntText").val()
    if (val !== '') {
        document.cookie = `howMany=${val}`
        $(location).attr("href", "popular");
    } else alert("Invalid number")
}