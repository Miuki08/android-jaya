<?php
header('Content-Type: application/json');
require_once 'db_connect.php';

if ($_SERVER['REQUEST_METHOD'] === 'GET' && isset($_GET['user_id'])) {
    $userId = intval($_GET['user_id']);

    $conn = (new Database())->conn;

    $stmt = $conn->prepare("
        SELECT 
            o.id,
            o.user_id,
            o.flower_id,
            o.quantity,
            o.order_date,
            f.name AS flowerName,
            CAST(f.price * o.quantity AS DECIMAL(10,2)) AS totalPrice
        FROM orders o
        JOIN flowers f ON o.flower_id = f.id
        WHERE o.user_id = ?
    ");
    $stmt->bind_param("i", $userId);
    $stmt->execute();
    $result = $stmt->get_result();

    $orders = [];
    while ($row = $result->fetch_assoc()) {
        $orders[] = $row;
    }

    echo json_encode(["orders" => $orders]);
    $conn->close();
} else {
    echo json_encode(["error" => "Invalid request"]);
}
?>
