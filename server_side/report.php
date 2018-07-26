<?php
/**
 * Created by Alessandro Riccardi.
 * User: Alericcardi
 * Date: 25/07/2018
 * Time: 17:12
 */

include_once 'DbManager.php';
include_once 'Control.php';

static $PARAMS_INSERT = ['id', 'id_user_r', 'id_dog_r', 'location_r', 'date_create_r', 'date_update_r', 'date_found_r', 'delete_r'];
static $PARAMS_UPDATE= ['id', 'id_user_r', 'id_dog_r', 'location_r', 'date_create_r', 'date_update_r', 'date_found_r', 'delete_r'];
static $PARAMS_SELECT_USER = ['id_user_r'];
static $PARAMS_SELECT_DOG = ['id_dog_r'];

$table = "report";

if (isset($_GET['action'])) {
    $action = $_GET['action'];
    if ($action == 'select_user') {
        # action --> SELECT
        $paramNames = $PARAMS_SELECT_USER;
        $params = Control::getParams($paramNames);
        if (!empty($params)) {
            $db = new DbManager($table, $paramNames, $params);
            $db->select();
        }
    } else if ($action == 'select_dog') {
        # action --> SELECT
        $paramNames = $PARAMS_SELECT_DOG;
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
