<?php
/**
 * Created by Alessandro Riccardi.
 * User: Alericcardi
 * Date: 25/07/2018
 * Time: 17:11
 */

include 'JsonMsg.php';
include 'Control.php';

static $INSERTED_SUCCESS = 'It has been inserted';
static $INSERTED_ERROR = 'It hasn\'t been inserted';
static $UPDATED_SUCCESS = 'It has been updated';
static $UPDATED_ERROR = 'It hasn\'t been updated';
static $SQL_ERROR = 'Sql error';

static $SF = '_d';
static $PARAMS_CREATE = ['id', 'id_nfc_d', 'id_user_d', 'name_d', 'breed_d', 'colour_d', 'birth_d', 'size_d', 'sex_d', 'date_create_d', 'date_update_d', 'delete_d'];
static $PARAMS_LOGIN = ['id_user_d'];

$servername = 'localhost';
$dbname = 'my_datdog';
$username = "";
$password = "";


if (isset($_GET['action'])) {
    $action = $_GET['action'];


    if ($action == 'request') {
        # action --> REQUEST
        $params = Control::get_params($PARAMS_LOGIN);
        if ($params) {
            try {
                $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
                $sql = 'SELECT * FROM dog WHERE id_user_d = :id_user_d';

                $prep = $conn->prepare($sql);
                $prep->execute(array('id_user_d' => $params['id_user_d']));
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
                if (!exist_dog($conn, $params['id'])) {
                    $sql = 'INSERT INTO dog(id, id_nfc_d, id_user_d, name_d, breed_d, colour_d, birth_d, size_d, sex_d, date_create_d, date_update_d, delete_d) VALUES (:id,:id_nfc_d, :id_user_d, :name_d, :breed_d, :colour_d, :birth_d, :size_d, :sex_d, :date_create_d, :date_update_d, :delete_d)';

                    $prep = $conn->prepare($sql);
                    $prep->execute(['id' => $params['id'], 'id_nfc_d' => $params['id_nfc_d'], 'id_user_d' => $params['id_user_d'], 'name_d' => $params['name_d'], 'breed_d' => $params['breed_d'], 'colour_d' => $params['colour_d'], 'birth_d' => $params['birth_d'], 'size_d' => $params['size_d'], 'sex_d' => $params['sex_d'], 'delete_d' => $params['delete_d'], 'date_create_d' => $params['date_create_d'], 'date_update_d' => $params['date_update_d']]);
                    $res = $prep->fetchAll();
                    if (empty($prep->errorInfo()[2])) {
                        JsonMsg::print_response(true, $INSERTED_SUCCESS);
                    } else {
                        JsonMsg::print_response(false, $INSERTED_ERROR);
                    }
                } else {
                    $sql = 'UPDATE dog SET id=:id,id_nfc_d=:id_nfc_d,id_user_d=:id_user_d,name_d=:name_d,breed_d=:breed_d,colour_d=:colour_d,birth_d=:birth_d,size_d=:size_d,sex_d=:sex_d,date_create_d=:date_create_d,date_update_d=:date_update_d,delete_d=:delete_d';

                    $prep = $conn->prepare($sql);
                    $prep->execute(['id' => $params['id'], 'id_nfc_d' => $params['id_nfc_d'], 'id_user_d' => $params['id_user_d'], 'name_d' => $params['name_d'], 'breed_d' => $params['breed_d'], 'colour_d' => $params['colour_d'], 'birth_d' => $params['birth_d'], 'size_d' => $params['size_d'], 'sex_d' => $params['sex_d'], 'delete_d' => $params['delete_d'], 'date_create_d' => $params['date_create_d'], 'date_update_d' => $params['date_update_d']]);
                    $res = $prep->fetchAll();
                    if (empty($prep->errorInfo()[2])) {
                        JsonMsg::print_response(true, $UPDATED_SUCCESS);
                    } else {
                        JsonMsg::print_response(false, $UPDATED_ERROR);
                    }
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


function exist_dog($conn, $id) {
    $sql = 'SELECT * FROM dog WHERE id = ?';
    $prep = $conn->prepare($sql);
    $prep->execute(array($id));
    $res = $prep->fetchAll();

    return !empty($res);
}