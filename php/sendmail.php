<?php

$headers = "MIME-Version: 1.0" . "\r\n";
$headers .= "Content-type:text/html;charset=UTF-8" . "\r\n";
$headers .= 'From: ' . $_POST["email"] . "\r\n"; 
date_default_timezone_set('America/New_York');
$main_message = "From website contact form at " . date('Y-m-d H:i:s') . ": " . '</br></br>' . $_POST['message'];
mail("n.sallis@hotmail.com", $_POST['subject'], $main_message, $headers);

echo "mail sent";
exit;
?>
