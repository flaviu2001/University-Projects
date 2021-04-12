let sorted = [];

function compareValues(a, b, col) {
    return ((a < b) ? -1 : (a > b) ? 1 : 0) * sorted[col];
}

function sort(col) {
    const table = document.querySelector("#imagesTable");
    let rows = Array.from(table.querySelectorAll("tr")).slice(1).map(function (elem) {
        return elem.querySelectorAll("td")[col].textContent
        // text content seems to render the text as we see and inner html takes it however it's written inside the tag
    });
    if (sorted.length === 0) {
        sorted = new Array(rows.length)
        for (let i = 0; i < sorted.length; i++)
            sorted[i] = 1
    }
    rows.sort((r1, r2) => {
        return compareValues(r1.toLowerCase(), r2.toLowerCase(), col)
    });
    for (let i = 0; i < rows.length; ++i)
        table.querySelectorAll("tr")[i + 1].querySelectorAll("td")[col].textContent = rows[i]
    if (sorted[col] === 1) {
        for (let i in sorted)
            if (sorted.hasOwnProperty(i)) // cures a warning
                sorted[i] = 1
        sorted[col] = -1;
    } else
        for (let i in sorted)
            if (sorted.hasOwnProperty(i))
                sorted[i] = 1;
}
