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
static $PARAMS_CREATE = ['id', 'id_user_r', 'id_dog_r', 'location_r', 'date_create_r', 'date_update_r', 'date_found_r', 'delete_r'];
static $PARAMS_LOGIN = ['id_user_r'];

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
                $sql = 'SELECT * FROM report WHERE id_user_d = :id_user';

                $prep = $conn->prepare($sql);
                $prep->execute(array('id_user_r' => $params['id_user_r']));
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
                    $sql = 'INSERT INTO report(id, id_user_r, id_dog_r, location_r, date_create_r, date_update_r, date_found_r, delete_r) VALUES (:id, :id_user_r, :id_dog_r, :location_r, :date_create_r, :date_update_r, :date_found_r, :delete_r)';

                    $prep = $conn->prepare($sql);
                    $prep->execute(['id' => $params['id'], 'id_nfc_d' => $params['id_nfc_d'], 'id_user_d' => $params['id_user_d'], 'name_d' => $params['name_d'], 'breed_d' => $params['breed_d'], 'colour_d' => $params['colour_d'], 'birth_d' => $params['birth_d'], 'size_d' => $params['size_d'], 'sex_d' => $params['sex_d'], 'date_create_d' => $params['date_create_d'], 'date_update_d' => $params['date_update_d'], 'date_found_r' => $params['date_found_r'], 'delete_d' => $params['delete_d']]);
                    $res = $prep->fetchAll();
                    if (empty($prep->errorInfo()[2])) {
                        JsonMsg::print_response(true, $INSERTED_SUCCESS);
                    } else {
                        JsonMsg::print_response(false, $INSERTED_ERROR);
                    }
                } else {
                    $sql = 'UPDATE report SET id=:id,id_user_r=:id_user_r,id_dog_r=:id_dog_r,location_r=:location_r,date_create_r=:date_create_r,date_update_r=:date_update_r,date_found_r=:date_found_r,delete_r=:delete_r';

                    $prep = $conn->prepare($sql);
                    $prep->execute(['id' => $params['id'], 'id_nfc_d' => $params['id_nfc_d'], 'id_user_d' => $params['id_user_d'], 'name_d' => $params['name_d'], 'breed_d' => $params['breed_d'], 'colour_d' => $params['colour_d'], 'birth_d' => $params['birth_d'], 'size_d' => $params['size_d'], 'sex_d' => $params['sex_d'], 'date_create_d' => $params['date_create_d'], 'date_update_d' => $params['date_update_d'], 'date_found_r' => $params['date_found_r'], 'delete_d' => $params['delete_d']]);
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


function exist_report($conn, $id) {
    $sql = 'SELECT * FROM report WHERE id = ?';
    $prep = $conn->prepare($sql);
    $prep->execute(array($id));
    $res = $prep->fetchAll();

    return !empty($res);
}