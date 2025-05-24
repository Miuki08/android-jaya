<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

header('Content-Type: application/json');
require_once 'User.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST['username'];
    $email = $_POST['email'];
    $password = $_POST['password'];

    $user = new User();
    $response = $user->register($username, $email, $password);
    echo json_encode($response);

    $user->closeConnection();
} else {
    echo json_encode(["error" => "Invalid request method"]);
}
?>