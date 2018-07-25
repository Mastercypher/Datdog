<?php
/**
 * Created by Alessandro Riccardi.
 * User: Alericcardi
 * Date: 25/07/2018
 * Time: 12:04
 */

include 'JsonMsg.php';

static $USER_LOGIN_SUCCESS = "Login success";
static $USER_LOGIN_ERROR = "Wrong email or password";
static $USER_REGISTER_SUCCESS = "User register successfully";
static $USER_REGISTER_ERROR = "User register error";
static $SQL_ERROR = "Sql error";

static $SF = "_u";
static $PARAMS_CREATE = ["name", "surname", "phone", "birth", "email", "password", "date_create", "date_update"];
static $PARAMS_LOGIN = ["email", "password"];

$servername = "localhost";
$dbname = "my_datdog";
$username = "";
$password = "";


if (isset($_GET["action"])) {
    $action = $_GET["action"];


    if ($action == "login") {
        # action --> LOGIN
        $params = get_params($PARAMS_LOGIN);
        if ($params) {
            try {
                $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
                $sql = "SELECT * FROM user WHERE email_u = :email";

                $prep = $conn->prepare($sql);
                $prep->execute(array("email" => $params['email']));
                $res = $prep->fetchAll();
                $user = $res[0];

                if ($user['password_u'] === $params['password']) {
                    JsonMsg::print_response(true, $USER_LOGIN_SUCCESS, $user);
                    return true;
                } else {
                    JsonMsg::print_response(false, $USER_LOGIN_ERROR);
                    return false;
                }
            } catch (PDOException $e) {
                //echo "Connection failed: " . $e->getMessage();
                JsonMsg::print_response(false, $SQL_ERROR);
            }
        } else {

            JsonMsg::print_response(false, JsonMsg::$NO_PARAM);
        }
    } else if ($action == "register") {
        # action --> REGISTER
        $params = get_params($PARAMS_CREATE);
        if ($params) {
            print_r($params);
            # Of course we dont want to insert a delete user.
            $delete = 0;

            try {
                $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
                $sql = "INSERT INTO `user` (name_u, surname_u, phone_u, birth_u, email_u, password_u, delete_u, date_create_u, date_update_u) VALUES (:name, :surname, :phone, :birth, :email, :password, :delete, :date_create, :date_update)";

                $prep = $conn->prepare($sql);
                $prep->execute(["name" => $params['name'], "surname" => $params['surname'], "phone" => $params['phone'], "birth" => $params['birth'], "email" => $params['email'], "password" => $params['password'], "delete" => $delete, "date_create" => $params['date_create'], "date_update" => $params['date_update']]);
                $res = $prep->fetchAll();
                if (empty($prep->errorInfo()[2])) {
                    JsonMsg::print_response(true, $USER_REGISTER_SUCCESS);
                } else {
                    JsonMsg::print_response(false, $USER_REGISTER_ERROR);
                }
            } catch (PDOException $e) {
                //echo "Connection failed: " . $e->getMessage();
                JsonMsg::print_response(false, $SQL_ERROR);
            }
        } else {
            JsonMsg::print_response(false, JsonMsg::$NO_PARAM);
        }
    }
} else {
    echo "No action.";
}


/**
 * Get POST parameters.
 *
 * @param $array
 *
 * @return array
 */
function get_params($array) {
    $params = array();
    if (verify_post($array) and verify_values($array)) {
        foreach ($array as &$value) {
            $params[$value] = $_GET[$value];
        }
    }
    return $params;
}

/**
 * Loop them to see if there is exist
 * the param.
 *
 * @param $array
 *
 * @return bool
 */
function verify_post($array) {
    # If it's empty
    if (!$array) {
        return false;
    }
    foreach ($array as &$value) {
        if (!isset($_GET[$value])) {
            return false;
        }
    }
    return true;
}

/**
 * Loop them to see if there is
 * value in the string.
 *
 * @param $array
 *
 * @return bool
 */
function verify_values($array) {
    # If it's empty
    if (!$array) {
        return false;
    }
    foreach ($array as &$value) {
        $get = $_GET[$value];
        if (count($get) < 0) {
            return false;
        }
    }
    return true;
}

