// === Ventas Mensuales ===
fetch('/dashboard/ventas-mensuales')
    .then(response => response.json())
    .then(data => {
        const labels = data.map(item => {
            // convierte número de mes -> nombre
            const meses = ["Ene", "Feb", "Mar", "Abr", "May", "Jun",
                "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"];
            return meses[item.mes - 1];
        });
        const valores = data.map(item => item.total);

        const ctx = document.getElementById('monthlySalesChart').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Ventas ($)',
                    data: valores,
                    backgroundColor: 'rgba(75, 192, 192, 0.6)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: { beginAtZero: true }
                }
            }
        });
    });

// === Productos más vendidos ===
fetch('/dashboard/productos-mas-vendidos')
    .then(response => response.json())
    .then(data => {
        const labels = data.map(item => item.producto);
        const valores = data.map(item => item.cantidad);

        const ctx = document.getElementById('topProductsChart').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Kg Vendidos',
                    data: valores,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.6)',
                        'rgba(54, 162, 235, 0.6)',
                        'rgba(255, 206, 86, 0.6)',
                        'rgba(75, 192, 192, 0.6)',
                        'rgba(153, 102, 255, 0.6)'
                    ],
                    borderColor: 'rgba(0, 0, 0, 0.7)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                indexAxis: 'y',
                scales: {
                    x: { beginAtZero: true }
                }
            }
        });
    });

function exportarReporteCompleto() {
    const chart1 = document.getElementById('monthlySalesChart').toDataURL('image/png');
    const chart2 = document.getElementById('topProductsChart').toDataURL('image/png');

    const formData = new FormData();
    formData.append('chart1', chart1);
    formData.append('chart2', chart2);

    fetch('/private/export/reporte-completo', {
        method: 'POST',
        body: formData
    })
        .then(resp => {
            if (resp.ok) return resp.blob();
            else throw new Error('Error al generar el PDF');
        })
        .then(blob => {
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'Reporte_Completo_AgroSell.pdf';
            a.click();
        })
        .catch(err => console.error(err));
}

