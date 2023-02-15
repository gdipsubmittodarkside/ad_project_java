$(document).ready(function() {

    console.log(chartDatas)


    var chartData_x = chartDatas.map(x => x["x_lable"])
    var chartData_y = chartDatas.map(x => x["y_data"])


    var config = {
        type: 'radar',
        options: {
            legend: {
                display: false
            },
            cutoutPercentage: 0.1,
            animation: {
                animateScale: true
            },
            scale: {
                ticks: {
                    display: false,
                    beginAtZero: true,
                    max: 100,
                    min: 0

                }
            }
        },
        data: {
            labels: chartData_x,
            machineLabels: ["active", "progress", "expired", "never"],
            datasets: [{
                borderWidth: 0,
                backgroundColor: 'rgba(173, 46, 179, 0.2)',
                borderColor: 'rgb(173, 46, 179)',
                pointBackgroundColor: 'rgb(173, 46, 179)',
                pointBorderColor: '#fff',
                pointHoverBackgroundColor: '#fff',
                pointHoverBorderColor: 'rgb(173, 46, 179)',
                data: chartData_y
            }]
        }
    };

    // Instantiate the pie chart in the canvas element.
    var myPie = new Chart(document.getElementById('courseChart'), config);

});