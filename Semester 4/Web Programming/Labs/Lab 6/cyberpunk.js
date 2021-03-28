let sorted = [];

function compareValues(a, b, col) {
    return ((a < b) ? -1 : (a > b) ? 1 : 0) * sorted[col];
}

function sort(col) {
    const table = document.querySelector("#imagesTable");
    let rows = Array.from(table.querySelectorAll("tr"));
    rows = rows.slice(1);
    if (sorted.length === 0) {
        sorted = new Array(rows.length)
        for (let i = 0; i < sorted.length; i++)
            sorted[i] = 1
    }
    let qs = `td:nth-child(${col+1})`;
    rows.sort((r1, r2) => {
        let t1 = r1.querySelector(qs);
        let t2 = r2.querySelector(qs);
        return compareValues(t1.textContent.toLowerCase(), t2.textContent.toLowerCase(), col);
        // text content seems to render the text as we see and inner html takes it however it's written inside the tag
    });
    rows.forEach(row => table.appendChild(row));
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
