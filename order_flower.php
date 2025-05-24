<?php
include 'db_connect.php';
$conn = (new Database())->conn;

$user_id = $_POST['user_id'];
$flower_id = $_POST['flower_id'];
$quantity = $_POST['quantity'];
$order_date = date('Y-m-d H:i:s');

$query = "INSERT INTO orders (user_id, flower_id, quantity, order_date) VALUES (?, ?, ?, ?)";
$stmt = $conn->prepare($query);
$stmt->bind_param("iiis", $user_id, $flower_id, $quantity, $order_date);

$response = array();
if ($stmt->execute()) {
    $response['success'] = true;
    $response['message'] = "Pesanan berhasil dibuat";
} else {
    $response['success'] = false;
    $response['message'] = "Gagal memesan";
}

header('Content-Type: application/json');
echo json_encode($response);
?>
