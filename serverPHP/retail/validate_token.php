<?php

require 'vendor/autoload.php';
use \Firebase\JWT\JWT;

$key = "MIICWwIBAAKBgQDTTXlkmwqsjAScPanbzQmR+oArVJR2zgDzfbQzZ9KbQVcUXXVJ
9y1oP99mcrS/5MmUeoy/2NLJVQR2AgzJjI5f2hint/Iilrs+ukVEBczQk8kTG4KI
8s/wjnAtIvzxCDnYk1dznRq3sbKFoiR0oAUEHdx3UAJMtS/oLUaLwnqEqwIDAQAB
AoGABIh++DeTTQxb9PYltL97WxRit4tC/CGDR7YEra2miQon0hoHMCULZlcodcw0
MIG6CWNvrZX7P7KBO/7jQ1eunjnVLSPhbBziF2iU8sC7W5xnh9b5T14DwVeLf2Df
1PQDx7PCVXYsFHSx4XpuaqGYy9za/2QTEixta29A/VYwCEECQQDxKW04P5GxEmvn
lLTjWFxBFJGSXUOb1hsVuDXulDCXF0rX+T6X4OMCyLuzEBgYEycnneL6QcojpdOe
q5YCBlsbAkEA4E27N34hC9vAfiQd8Ojrzwmsafux+wE77RhMWVUea+c/hdSy7Ok9
DeVjL43LmG+YM0P/M2/8prv+lc+nm8oFsQJAeMzDhPVyyTvtCSeYJHlSXNcsxCAr
iZgKISsQ+N0KKeTk/KJBFFr126MpQlzBdO3DIzkXRTxHQOwDwOs6TPFaGQJAVp/u
c6n0XT4LAywzm/x4RUb5HR0lSUjaueAubDmhPA7cy3SvfSs+dwVl3tGmFULTxsi1
4EmZi1MDKRw/IL1OoQJAdvwXF00fXZgqhfH2vYroBwMZobqG3LvJoX4YlljYsCj3
bshMl8EsMFCQqg0dQ5CXTgorDNL+YHCKGOYlUeAuIA==";


function validate_key($jwt) {
    if($jwt){
        try {
            $decoded = JWT::decode($jwt, $GLOBALS["key"], array('HS256')); 
            $decoded_array = (array) $decoded;
            return $decoded_array['username']; 
        }catch (Exception $e){
            return null;
        }
    }

    return null;
}

function encode_user($user){
    $payload = array(
        "username" => $user
    );

    $jwt = JWT::encode($payload, $GLOBALS["key"]);   
    
    return $jwt;
}

?>