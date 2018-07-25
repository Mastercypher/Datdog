<?php
/**
 * Created by PhpStorm.
 * User: aleric
 * Date: 25/07/2018
 * Time: 23:32
 */

include 'JsonMsg.php';
include 'Control.php';


class DbManager {
    private static $FETCH_SUCCESS = "Success";
    private static $FETCH_ERROR = "Fail";
    private static $INSERTED_SUCCESS = "It has been inserted";
    private static $INSERTED_ERROR = "It hasn\"t been inserted";
    private static $UPDATED_SUCCESS = "It has been updated";
    private static $UPDATED_ERROR = "It hasn\"t been updated";
    private static $SQL_ERROR = "Sql error";

    private static $TYPE_INSERT = "insert";
    private static $TYPE_UPDATE = "update";

    private $servername = "localhost";
    private $dbname = "my_datdog";
    private $username = "";
    private $password = "";

    private $table = "";
    private $paramNames = array();
    private $params = array();

    function __construct($table, $paramNames, $params) {
        $this->table = $table;
        $this->paramNames = $paramNames;
        $this->params = $params;
    }

    /**
     * Upload the data loaded in the constructor, make sure to have
     * uploaded all the data.
     *
     * @return bool
     */
    public function upload() {
        if (empty($this->table) or empty($this->paramNames) or empty($this->params) or empty($this->params["id"])) {
            return false;
        }

        $exist = $this->existRow($this->params["id"]);
        if (!$exist) {
            # Make an insert
            $success = $this->sqlAction(DbManager::$TYPE_INSERT);
        } else {
            $success = $this->sqlAction(DbManager::$TYPE_INSERT);
        }

        return $success;
    }

    /**
     * Return the data referred to the specific row by parameters
     */
    public function fetch() {
        try {
            $conn = new PDO("mysql:host=$this->servername;dbname=$this->dbname", $this->username, $this->password);
            $sql = $this->getSelectSql();

            $prep = $conn->prepare($sql);
            $prep->execute(array('id_user_f' => $this->params['id_user_f']));
            $res = $prep->fetchAll();

            # Check the respond
            if (empty($prep->errorInfo()[2])) {
                JsonMsg::print_response(true, DbManager::$FETCH_SUCCESS, $res);
                return true;
            } else {
                JsonMsg::print_response(false, DbManager::$FETCH_ERROR);
                return false;
            }

        } catch (PDOException $e) {
            //echo 'Connection failed: ' . $e->getMessage();
            JsonMsg::print_response(false, DbManager::$SQL_ERROR);
        }
    }

    /**
     * Make the action, The action consist in a INSERT or in a UPDATE.
     *
     * @param $type
     *
     * @return bool
     */
    private function sqlAction($type) {
        try {
            $conn = new PDO("mysql:host=$this->servername;dbname=$this->dbname", $this->username, $this->password);

            # SQL string preparation 
            $sql = "";
            if ($type == DbManager::$TYPE_INSERT) {
                $sql = $this->getInsertSql();
            } else if ($type == DbManager::$TYPE_UPDATE) {
                $sql = $this->getUpdateSql();
            } else {
                return false;
            }

            # SQL array preparation 
            $valuesToInject = $this->getArrayValues();

            # SQL injection
            $prep = $conn->prepare($sql);
            $prep->execute($valuesToInject);
            $prep->fetchAll();


            # RESPOND string preparation 
            $success = "";
            $error = "";
            if ($type == DbManager::$TYPE_INSERT) {
                $success = DbManager::$INSERTED_SUCCESS;
                $error = DbManager::$INSERTED_ERROR;
            } else if ($type == DbManager::$TYPE_UPDATE) {
                $success = DbManager::$UPDATED_SUCCESS;
                $error = DbManager::$UPDATED_ERROR;
            } else {
                return false;
            }

            # Check the respond
            if (empty($prep->errorInfo()[2])) {
                JsonMsg::print_response(true, $success);
                return true;
            } else {
                JsonMsg::print_response(false, $error);
                return false;
            }

        } catch (PDOException $e) {
            //echo "Connection failed: " . $e->getMessage();
            JsonMsg::print_response(false, DbManager::$SQL_ERROR);
            return false;
        }
    }

    /**
     * Check if the row already exist.
     *
     * @param $id
     *
     * @return bool
     */
    private function existRow($id) {
        try {
            $conn = new PDO("mysql:host=$this->servername;dbname=$this->dbname", $this->username, $this->password);
            $sql = "SELECT * FROM report WHERE id = ?";
            $prep = $conn->prepare($sql);
            $prep->execute(array($id));
            $res = $prep->fetchAll();

            return !empty($res);
        } catch (PDOException $e) {
            //echo "Connection failed: " . $e->getMessage();
            JsonMsg::print_response(false, DbManager::$SQL_ERROR);
            return false;
        }
    }


    /**
     * Creation of sql string for INSERT injection.
     *
     * @return string
     */
    private function getSelectSql() {
        $sql = "SELECT * FROM $this->table WHERE";
        foreach ($this->paramNames as $name) {
            if (array_search($name, $this->paramNames) != sizeof($this->paramNames)) {
                $sql .= "$name=:$name,";
            } else {
                $sql .= "$name=:$name,";
            }
        }

        return $sql;
    }

    /**
     * Creation of sql string for INSERT injection.
     *
     * @return string
     */
    private function getInsertSql() {
        $sql_start = "INSERT INTO $this->table(";
        $sql_end = " VALUES (";
        foreach ($this->paramNames as $name) {
            if (array_search($name, $this->paramNames) != sizeof($this->paramNames)) {
                $sql_start .= "$name,";
                $sql_end .= ":$name,";

            } else {
                $sql_start .= "$name)";
                $sql_end .= ":$name,)";
            }
        }

        return $sql_start . $sql_end;
    }

    /**
     * Creation of sql string for UPDATE injection.
     *
     * @return string
     */
    private function getUpdateSql() {
        $sql = "UPDATE $this->table SET ";
        foreach ($this->paramNames as $name) {
            if (array_search($name, $this->paramNames) != sizeof($this->paramNames)) {
                $sql .= "$name=:$name,";
            } else {
                $sql .= "$name=:$name";
            }
        }

        return $sql;
    }

    /**
     * Creation of array to inject in the sql.
     *
     * @return array
     */
    private function getArrayValues() {
        $array_execute = array();
        foreach ($this->paramNames as $name) {
            $array_execute[$name] = $this->params[$name];
        }

        return $array_execute;
    }
}