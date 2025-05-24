<?php

class Database {
    private $host = 'localhost';
    private $uname = 'root';
    private $pass = '';
    private $db = 'pak_saefi';
    public $conn;

    public function __construct() {
        $this->conn = new mysqli($this->host, $this->uname, $this->pass, $this->db);
        if ($this->conn->connect_error) {
            die("Connection failed: " . $this->conn->connect_error);
        } 
    }   
}

?>
