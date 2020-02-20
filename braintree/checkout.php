<?php
require_once ("braintree_init.php");
require_once 'lib/Braintree.php';



if(isset($_POST["nonce"])) {
    $amount = $_POST['amount'];
    $nonce = $_POST['nonce'];
    $result = Braintree_Transaction::sale([
        'amount' => $amount,
        'paymentMethodNonce' => $nonce,
        'options' => ['submitForSeattlement' => true]
    ]);
    if($result->success){
        echo("success!:" . $result->transaction->id);
    } else if ($result->transaction){
        echo "Error processing transaction: \n" . "code: " . $result->transaction->processorResponseCode . " \n";
    } else{
        echo "Validation errors: \n";
    }
} else {
    echo($clientToken = Braintree_ClientToken::generate());
}

?>