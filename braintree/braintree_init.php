<?php
session_start();
require_once("lib/autoload.php");

if(file_exists(__DIR__ . "/../.env")) {
    $dotenv = new Dotenv\Dotenv(__DIR__ . "/../");
    $dotenv->load();
}
Braintree_Configuration::environment('sandbox');
Braintree_Configuration::merchantId('cmxscthzw4ybmnwb');
Braintree_Configuration::publicKey('pvzvyd83vz97t92f');
Braintree_Configuration::privateKey('0a3aac4a5634de61d6e411297e302377');
?>
