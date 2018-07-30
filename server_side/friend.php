<?php
/**
 * Created by Alessandro Riccardi.
 * User: Alericcardi
 * Date: 25/07/2018
 * Time: 17:11
 */

include_once 'DbManager.php';
include_once 'Control.php';

static $SF = '_d';
static $PARAMS_SELECT = ['id_user_f'];
static $PARAMS_INSERT = ['id', 'id_user_f', 'id_friend_f', 'date_create_f', 'date_update_f', 'delete_f'];
static $PARAMS_UPDATE = ['id', 'id_user_f', 'id_friend_f', 'date_create_f', 'date_update_f', 'delete_f'];
$table = "friend";

if (isset($_GET['action'])) {
    $action = $_GET['action'];
    if ($action == 'select') {
        # action --> SELECT
        $paramNames = $PARAMS_SELECT;
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
        # action --> UPDATE
        $paramNames = $PARAMS_UPDATE;
        $params = Control::getParams($paramNames);
        if (!empty($params)) {
            $db = new DbManager($table, $paramNames, $params);
            $db->update();
        }
    } else {
        echo 'No action . ';
    }
} else {
    echo 'No action . ';
}
