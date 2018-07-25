<?php
/**
 * Created by Alessandro Riccardi.
 * User: Alericcardi
 * Date: 25/07/2018
 * Time: 17:12
 */

include 'JsonMsg.php';
include 'Control.php';

static $INSERTED_SUCCESS = 'It has been inserted';
static $INSERTED_ERROR = 'It hasn\'t been inserted';
static $UPDATED_SUCCESS = 'It has been updated';
static $UPDATED_ERROR = 'It hasn\'t been updated';
static $SQL_ERROR = 'Sql error';

static $SF = '_d';
static $PARAMS_CREATE = ['id', 'id_user_f', 'id_friend_f', 'date_create_f', 'date_update_f', 'delete_f'];
static $PARAMS_LOGIN = ['id_user_f'];

$servername = 'localhost';
$dbname = 'my_datdog';
$username = "";
$password = "";

$table = "friend";


if (isset($_GET['action'])) {
    $action = $_GET['action'];


    if ($action == 'request') {
        # action --> REQUEST
        $params = Control::get_params($PARAMS_LOGIN);
        if ($params) {
            try {
                $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
                $sql = 'SELECT * FROM friend WHERE id_user_f = :id_user_f';

                $prep = $conn->prepare($sql);
                $prep->execute(array('id_user_f' => $params['id_user_f']));
                $res = $prep->fetchAll();
                $user = $res[0];


            } catch (PDOException $e) {
                //echo 'Connection failed: ' . $e->getMessage();
                JsonMsg::print_response(false, $SQL_ERROR);
            }
        } else {

            JsonMsg::print_response(false, JsonMsg::$NO_PARAM);
        }
    } else if ($action == 'upload') {
        # action --> REGISTER
        $params = Control::get_params($PARAMS_CREATE);
        if ($params) {
            # Of course we dont want to insert a delete user.
            $delete = 0;

            try {
                $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
                if (!exist_report($conn, $params['id'])) {
                    sql_insert($table, $params);
                } else {
                    sql_update($table, $params);
                }
            } catch (PDOException $e) {
                //echo 'Connection failed: ' . $e->getMessage();
                JsonMsg::print_response(false, $SQL_ERROR);
            }
        } else {
            JsonMsg::print_response(false, JsonMsg::$NO_PARAM);
        }
    }
} else {
    echo 'No action.';
}


function exist_report($conn, $id) {
    $sql = 'SELECT * FROM report WHERE id = ?';
    $prep = $conn->prepare($sql);
    $prep->execute(array($id));
    $res = $prep->fetchAll();

    return !empty($res);
}

function sql_insert($table, $params) {
    $servername = 'localhost';
    $dbname = 'my_datdog';
    $username = "";
    $password = "";
    try {
        $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
        $sql = sql_insert_string_creation($table, $params);

        $prep = $conn->prepare($sql);
        $prep->execute(sql_insert_array_creation($params));
        $res = $prep->fetchAll();
        if (empty($prep->errorInfo()[2])) {
            JsonMsg::print_response(true, "success");
            return true;
        } else {
            JsonMsg::print_response(false, "fail");
            return false;
        }

    } catch (PDOException $e) {
        //echo 'Connection failed: ' . $e->getMessage();
        JsonMsg::print_response(false, "sql_error");
        return false;
    }
}



function sql_update($table, $params) {
    $servername = 'localhost';
    $dbname = 'my_datdog';
    $username = "";
    $password = "";
    try {
        $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
        $sql = sql_insert_string_creation($table, $params);

        $prep = $conn->prepare($sql);
        $prep->execute(sql_update_array_creation($params));
        $res = $prep->fetchAll();
        if (empty($prep->errorInfo()[2])) {
            JsonMsg::print_response(true, "success");
            return true;
        } else {
            JsonMsg::print_response(false, "fail");
            return false;
        }

    } catch (PDOException $e) {
        //echo 'Connection failed: ' . $e->getMessage();
        JsonMsg::print_response(false, "sql_error");
        return false;
    }
}

function sql_insert_string_creation($table, $params) {
    $sql_start = "INSERT INTO $table(";
    $sql_end = " VALUES (";
    foreach ($params as $param) {
        if (array_search($param, $params) != sizeof($params)) {
            $sql_start .= "$param,";
            $sql_end .= ":$param,";

        } else {
            $sql_start .= "$param)";
            $sql_end .= ":$param,)";
        }
    }

    return $sql_start . $sql_end;
}


function sql_update_string_creation($table, $params) {
    $sql = "UPDATE $table SET ";
    foreach ($params as $param) {
        if (array_search($param, $params) != sizeof($params)) {
            $sql .= "$param=:$param,";
        } else {
            $sql .= "$param=:$param";
        }
    }

    return $sql;
}

function sql_insert_array_creation($params) {
    $array_execute = array();
    foreach ($params as $param) {
        $array_execute['$param'] = $param;
    }

    return $array_execute;
}
