<?php
/**
 * Created by Alessandro Riccardi.
 * User: Alericcardi
 * Date: 25/07/2018
 * Time: 12:04
 */

include 'DbManager.php';
include 'Control.php';

static $PARAMS_CREATE = ['name_u', 'surname_u', 'phone_u', 'birth_u', 'email_u', 'password_u', 'date_create_u', 'date_update_u', 'delete_u'];
static $PARAMS_LOGIN = ['email_u', 'password_u'];
$table = "user";

if (isset($_GET['action'])) {
    $action = $_GET['action'];
    if ($action == 'fetch') {
        # action --> REQUEST
        $paramNames = $PARAMS_LOGIN;
        $params = Control::get_params($paramNames);
        $db = new DbManager($table, $paramNames, $params);

        $db->fetch();
    } else if ($action == 'upload') {
        # action --> UPLOAD
        $paramNames = $PARAMS_LOGIN;
        $params = Control::get_params($paramNames);
        $db = new DbManager($table, $paramNames, $params);

        $db->upload();
    } else {
        echo 'No action.';
    }
} else {
    echo 'No action.';
}

