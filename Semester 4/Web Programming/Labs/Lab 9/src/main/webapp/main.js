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
    $.ajax({
        url: 'add',
        method: 'post',
        data: {
            userId: userId,
            url: $("#urlText").val()
        },
        success: function () {
            $("#urlText").val("")
            location.reload()
        }
    })
}

function nav() {
    let val = $("#cntText").val()
    if (val !== '') {
        document.cookie = `howMany=${val}`
        $(location).attr("href", "popular");
    } else alert("Invalid number")
}