<?php
// Koneksi ke database
include 'db_connect.php';
$database = new Database();
$conn = $database->conn;

// Query ambil data bunga
$query = "SELECT * FROM flowers";
$result = mysqli_query($conn, $query);

$flowers = array();

while ($row = mysqli_fetch_assoc($result)) {
    $flowers[] = array(
        'id' => (int)$row['id'],
        'name' => $row['name'],
        'price' => (double)$row['price'],
        'image_url' => $row['image_url']
    );
}

// Set header untuk JSON
header('Content-Type: application/json');
echo json_encode($flowers);
?>
