<?php
header('Content-Type: application/json');
require_once 'db_connect.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['order_id']) && isset($_POST['quantity'])) {
    $orderId = intval($_POST['order_id']);
    $quantity = intval($_POST['quantity']);

    $conn = (new Database())->conn;
    $stmt = $conn->prepare("UPDATE orders SET quantity = ? WHERE id = ?");
    $stmt->bind_param("ii", $quantity, $orderId);

    if ($stmt->execute()) {
        echo json_encode(["message" => "Pesanan berhasil diperbarui"]);
    } else {
        echo json_encode(["error" => "Gagal memperbarui pesanan"]);
    }

    $conn->close();
} else {
    echo json_encode(["error" => "Invalid request"]);
}
?>
