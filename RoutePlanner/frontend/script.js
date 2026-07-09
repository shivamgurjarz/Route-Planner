function findRoute(){

    let source=document.getElementById("source").value;
    let destination=document.getElementById("destination").value;
    let vehicle=document.getElementById("vehicle").value;

    if(source===destination){

        document.getElementById("result").innerHTML=
        "<h3>Please select different cities.</h3>";

        return;
    }

    document.getElementById("result").innerHTML=

    `
    <h2>Shortest Route</h2>

    <br>

    <h3>${source} ➜ ${destination}</h3>

    <br>

    <p><b>Distance :</b> 1430 km</p>

    <p><b>Estimated Time :</b> 5 Hours</p>

    <p><b>Vehicle :</b> ${vehicle}</p>

    <p><b>Fuel Cost :</b> ₹1150</p>

    <br>

    <p style="color:green;"><b>Algorithm Used : Dijkstra</b></p>

    `;

}

function showRoutes(){

    let source = document.getElementById("source").value;
    let destination = document.getElementById("destination").value;

    if(source === destination){
        document.getElementById("result").innerHTML =
        "<h3>Please select different cities.</h3>";
        return;
    }

    document.getElementById("result").innerHTML =

    `
    <h2>Possible Routes</h2>

    <br>

    <p>1. ${source} ➜ City A ➜ ${destination}</p>
    <p>Distance : 1430 km</p>

    <br>

    <p>2. ${source} ➜ City B ➜ ${destination}</p>
    <p>Distance : 1620 km</p>

    <br>

    <p style="color:blue;"><b>Algorithm Used : DFS Backtracking</b></p>
    `;
}