<?php
/**
 * Created by Alessandro Riccardi.
 * User: Alericcardi
 * Date: 25/07/2018
 * Time: 12:04
 */
include_once 'DbManager.php';
include_once 'Control.php';
static $PARAMS_LOGIN = ['email_u', 'password_u'];
static $PARAMS_USER = ['id'];
static $PARAMS_INSERT = ['name_u', 'surname_u', 'phone_u', 'birth_u', 'email_u', 'password_u', 'date_create_u', 'date_update_u', 'delete_u'];
static $PARAMS_UPDATE = ['id', 'name_u', 'surname_u', 'phone_u', 'birth_u', 'email_u', 'password_u', 'date_create_u', 'date_update_u', 'delete_u'];
$table = "user";
if (isset($_GET['action'])) {
    $action = $_GET['action'];
    if ($action == 'select-login') {
        # action --> SELECT
        $paramNames = $PARAMS_LOGIN;
        $params = Control::getParams($paramNames);
        if (!empty($params)) {
            $db = new DbManager($table, $paramNames, $params);
            $db->select();
        }
    } else if ($action == 'select-user') {
        # action --> SELECT
        $paramNames = $PARAMS_USER;
        $params = Control::getParams($paramNames);
        if (!empty($params)) {
            $db = new DbManager($table, $paramNames, $params);
            $db->select();
        }
    } else if ($action == 'insert') {
        # action --> UPLOAD
        $paramNames = $PARAMS_INSERT;
        $params = Control::getParams($paramNames);
        if (!empty($params)) {
            $db = new DbManager($table, $paramNames, $params);
            $db->insert();
        }
    } else if ($action == 'update') {
        # action --> UPLOAD
        $paramNames = $PARAMS_UPDATE;
        $params = Control::getParams($paramNames);
        if (!empty($params)) {
            $db = new DbManager($table, $paramNames, $params);
            $db->update();
        }
    } else {
        echo 'No action.';
    }
} else {
    echo 'No action.';
}