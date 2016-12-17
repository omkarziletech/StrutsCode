/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function showOriginDestinationList(data) {
    document.getElementById('getRates').disabled = false;
    showPopUp();
    var docListDiv = createHTMLElement("div", "originAndDestinationDiv", document.body.offsetWidth - 100, document.body.offsetHeight - 90, document.body);
    jQuery("#originAndDestinationDiv").html(data);
    document.getElementById("originAndDestinations").style.height = docListDiv.offsetHeight - 10 + 'px';
    document.getElementById("originAndDestinations").style.width = docListDiv.offsetWidth - 10 + 'px';
}

function checkAndUnceckAll(check, checkboxNames) {
    var check = document.getElementById(check).checked;
    var checkBoxes = document.getElementsByName(checkboxNames);
    for (i = 0; i < checkBoxes.length; i++) {
        checkBoxes[i].checked = check;
    }
    var region = document.getElementsByName("region");
    for (i = 0; i < region.length; i++) {
        region[i].checked = check;
    }
    if (check) {
        document.getElementById('top10').checked = false;
    }
}
function checkAndUnceckRegions(check, checkboxNames) {
    var check = document.getElementById(check).checked;
    getElementByClass(checkboxNames, check);
}

var allHTMLTags = new Array();

function getElementByClass(theClass, check) {

//Create Array of All HTML Tags
    var allHTMLTags = document.getElementsByTagName("*");
    var htmlTags = new Array();
//Loop through all tags using a for loop
    for (i = 0; i < allHTMLTags.length; i++) {

//Get all tags with the specified class name.
        if (allHTMLTags[i].className == theClass) {
            allHTMLTags[i].checked = check;
        }
    }
}
sortedDistance = null;
var shortestDistance = null;
function setDistance(allCity, data) {
    var directionsService = new google.maps.DirectionsService();
    allCity.replace("\"", "");
    var city = allCity.split(',', 100);
    city[0] = city[0].substring(1, city[0].length);
    showOriginDestinationList(data);
    var doorOrigin = document.getElementById("doorOrigin");
    var zip = document.getElementById("zip");
    if (null != doorOrigin && trim(doorOrigin.value) != '') {
        sortedDistance = new Array(city.length);
        var count = 0;
        var total_Count = 0;
        var cy_count = 0;
        var cy_total_Count = 0;
        var currentCity = null;
        for (i = 0; i < city.length; i++) {
            if (city[i].indexOf('(') > 0) {
                currentCity = city[i].substring(0, city[i].indexOf('(') - 1);
            } else {
                currentCity = city[i];
            }
            var request = {
                origin: zip.value,
                destination: currentCity,
                travelMode: google.maps.DirectionsTravelMode.DRIVING,
                region: "UNITED STATES"
            };
            directionsService.route(request, function(response, status) {
                total_Count++;
                if (status == google.maps.DirectionsStatus.OK) {
                    var address = response.routes[0].legs[0].end_address;
                    var lastindex = address.split(",", 30);
                    address = trim(lastindex[lastindex.length - 3]);
                    var citySpan = document.getElementById(address);
                    var duration = response.routes[0].legs[0].distance.text;
                    var distance = duration.substring(0, duration.length - 3).replace(",", "");
                    if (null != citySpan) {
                        citySpan.innerHTML = citySpan.innerHTML + " <span id='distance' style='color:black;background:#CCEBFF;align:right'>" + duration + "</span>";
                        citySpan.id = trim(distance);
                        citySpan.parentNode.id = trim(distance) + '_TD';
                        sortedDistance[count] = distance;
                    } else {
                        //alert(address);
                    }
                    count++;
                }
                if (total_Count == city.length) {
                    sortedDistance.sort(compare);
                    var totalCount = sortedDistance.length > 5 ? 5 : sortedDistance.length;
                    var shortestDistanceSpan = null;
                    for (j = 0; j < totalCount; j++) {
                        shortestDistanceSpan = document.getElementById(sortedDistance[j]);
                        if (null != shortestDistanceSpan) {
                            if (j == 0) {
                                shortestDistance = sortedDistance[j];
                                shortestDistanceSpan.innerHTML = "<font color='#347C2C'>" + shortestDistanceSpan.innerHTML + "</font>";
//                                    calculateShortestDistance();
                            } else {
                                shortestDistanceSpan.innerHTML = "<font color='#C35617'>" + shortestDistanceSpan.innerHTML + "</font>";
                            }
                        }
                    }
                }
            });
        }
    }
}
function calculateShortestDistance() {
    var cy_count = 0;
    var cy_total_Count = 0;
    var actualDistance = 0;
    var shortCyCity = "";
    var currentCity = null;
    var zip = document.getElementById("zip");
    var doorOrigin = document.getElementById("doorOrigin");
    var directionsService = new google.maps.DirectionsService();
    jQuery.ajaxx({
        dataType : "json",
        data: {
            className: "com.gp.cong.logisoft.bc.fcl.QuoteDwrBC",
            methodName: "getCyYardCity",
            dataType : "json"
        },
        success: function(data) {
            if (null !== data) {
                var CyYardDistance = new Array(data.length);
                var closestCity = new Array(data.length);
                for (var i = 0; i < data.length; i++) {
                    if (data[i].indexOf('(') > 0) {
                        currentCity = data[i].substring(0, data[i].indexOf('('));
                    } else {
                        currentCity = data[i];
                    }
                    var request = {
                        origin: zip.value,
                        destination: currentCity,
                        travelMode: google.maps.DirectionsTravelMode.DRIVING,
                        region: "UNITED STATES"
                    };
                    directionsService.route(request, function(response, status) {
                        cy_total_Count++;
                        if (status == google.maps.DirectionsStatus.OK) {
                            var address = response.routes[0].legs[0].end_address;
                            var lastindex = address.split(",", 30);
                            address = trim(lastindex[lastindex.length - 3]);
                            var duration = response.routes[0].legs[0].distance.text;
                            var distance = duration.substring(0, duration.length - 3).replace(",", "");
                            CyYardDistance[cy_count] = distance;
                            if (cy_count == 0) {
                                actualDistance = parseInt(distance);
                                shortCyCity = address;
                            }
                            if (parseInt(actualDistance) > parseInt(distance)) {
                                actualDistance = parseInt(distance);
                                shortCyCity = address;
                            }
                            cy_count++;
                        }
                        if (cy_total_Count == data.length) {
                            CyYardDistance.sort(compare);
                            closestCity.sort(compare);
                            if (parseInt(CyYardDistance[0]) < parseInt(shortestDistance)) {
                                var originDestn = document.getElementById("originDestn");
                                originDestn.innerHTML = originDestn.innerHTML + "<tr><td colspan='8'><font color='red' style='font-weight: bold;font-family: Arial;font-size: 13px;'>***NO RAMP RATE AVAILABLE FROM THE CLOSEST CY IN <a style = 'background:yellow'> " + shortCyCity + " (" + CyYardDistance[0] + " miles) FROM " + doorOrigin.value + ".</a> YOU MUST CONTACT A STEAMSHIP LINE TO HANDLE THE <br> DOOR MOVE***</font><br></span><td></tr>";
                            }
                        }
                    });
                }
            }
        }
    });
}
function compare(a, b) {
    return a - b;
}
function selectTopTen(obj) {
    var check = obj.checked;
    if (check) {
        document.getElementById('checkAll').checked = false;
        var checkBoxes = document.getElementsByName('originDestination');
        for (i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i].checked = !check;
        }
        var region = document.getElementsByName("region");
        for (i = 0; i < region.length; i++) {
            region[i].checked = !check;
        }
    }

    getElementByClassForTopTen('top5', obj.checked);
}
function getElementByClassForTopTen(theClass, check) {

//Create Array of All HTML Tags
    var allHTMLTags = document.getElementsByTagName("*");
    var htmlTags = new Array();
//Loop through all tags using a for loop
    for (i = 0; i < allHTMLTags.length; i++) {

//Get all tags with the specified class name.
        if (allHTMLTags[i].className == theClass) {
            allHTMLTags[i].childNodes[0].checked = check;
        }
    }
}
function getDistancefromFDAndDestination(allCity, doorDest, country, origin, haz, cityIds, formName) {
    sortedDistance = null;
    var door = doorDest;
    if (doorDest.indexOf('/') > 0) {
        doorDest = doorDest.substring(0, doorDest.indexOf('/'));
    }
    if (country.indexOf('(') > 0) {
        country = country.substring(0, country.indexOf('('));
    }
    var directionsService = new google.maps.DirectionsService();
    var total_Count = 0;
    for (i = 0; i < allCity.length; i++) {
        var request = {
            origin: doorDest + ',' + country,
            destination: allCity[i] + ',' + country,
            travelMode: google.maps.DirectionsTravelMode.DRIVING
        };
        directionsService.route(request, function(response, status) {
            total_Count++;
            if (status == google.maps.DirectionsStatus.OK) {
                var address = response.routes[0].legs[0].end_address;
                var lastindex = address.split(",");
                if (lastindex.length == 2) {
                    address = trim(lastindex[lastindex.length - 2]);
                } else {
                    address = trim(lastindex[lastindex.length - 3]);
                }
                var duration = response.routes[0].legs[0].distance.text;
                var distance = duration.substring(0, duration.length - 3).replace(",", "");
                if (null == sortedDistance) {
                    sortedDistance = address + "=" + distance;
                }
                else {
                    sortedDistance = sortedDistance + "," + address + "=" + distance;
                }
            }
            if (total_Count == allCity.length) {
                window.parent.hideProgressBar();
                GB_show('FCL Rates Comparison Grid', '/logisoft/rateGrid.do?action=Origin&origin=' + formName.isTerminal.value +
                        "&destination=" + origin + "&commodity=" + formName.commcode.value + '&hazardous=' + haz + "&zip=" + doorDest + "," + country +
                        "&doorOrigin=" + door + "&distances=" + sortedDistance + "&cityIds=" + cityIds, document.body.offsetHeight - 20, document.body.offsetWidth - 100);
            }
        });
    }
}
