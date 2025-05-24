<?php

error_reporting(E_ALL);
ini_set('display_errors', 1);

require_once 'db_connect.php';

class User {
    private $conn;

    public function __construct() {
        $database = new Database();
        $this->conn = $database->conn;
    }

    // Method untuk registrasi user
    public function register($username, $email, $password) {
        $hashedPassword = password_hash($password, PASSWORD_BCRYPT);
        $stmt = $this->conn->prepare("INSERT INTO pak_saefi (username, email, password) VALUES (?, ?, ?)");
        $stmt->bind_param("sss", $username, $email, $hashedPassword);
    
        if ($stmt->execute()) {
            return ["message"=>"Registration successful"];
        } else {
            return ["error" => "Registration failed: " . $stmt->error];
        }
    }

    public function login($username, $password) {
        $sql = "SELECT * FROM pak_saefi WHERE username = ?";
        $stmt = $this->conn->prepare($sql);
        $stmt->bind_param("s", $username);
        $stmt->execute();
        $result = $stmt->get_result();
    
        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
    
            if (password_verify($password, $row['password'])) {
                // Kirim data user sebagai objek
                $userData = [
                    "id_users" => $row["id_users"],  // pastikan kolom ini sesuai di tabel kamu
                    "username" => $row["username"]
                ];
    
                return [
                    "message" => "Login successful",
                    "user" => $userData
                ];
            } else {
                return ["error" => "Invalid password"];
            }
        } else {
            return ["error" => "User not found"];
        }
    }
    

    // Method untuk menutup koneksi
    public function closeConnection() {
        $this->conn->close();
    }
}

?>
