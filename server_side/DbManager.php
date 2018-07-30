<?php
/**
 * Created by PhpStorm.
 * User: aleric
 * Date: 25/07/2018
 * Time: 23:32
 */
include_once 'JsonMsg.php';
include_once 'Control.php';
class DbManager {
    private static $FETCH_SUCCESS = "";
    private static $FETCH_ERROR = "";
    private static $INSERTED_SUCCESS = "It has been inserted";
    private static $INSERTED_ERROR = "It hasn't been inserted";
    private static $UPDATED_SUCCESS = "It has been updated";
    private static $UPDATED_ERROR = "It hasn't been updated";
    private static $SQL_ERROR = "Sql error";
    private static $EMPTY_PARAMS = "Empty parameters";
    private static $NO_ENOUGH_PARAMS = "No enough parameters";
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
     * Insert.
     *
     * @return bool
     */
    public function insert() {
        if ($this->controlInjection()) {
            $success = $this->sqlAction(DbManager::$TYPE_INSERT);
            return $success;
        }
        return false;
    }
    /**
     * Update by id.
     *
     * @return bool
     */
    public function update() {
        if ($this->controlInjection()) {
            $success = $this->sqlAction(DbManager::$TYPE_UPDATE);
            return $success;
        }
        return false;
    }
    /**
     * Select row/rows.
     *
     * @return bool
     */
    public function select($usrPsw = false) {
        if ($this->controlInjection()) {
            if ($usrPsw) {
                $success = $this->fetch($usrPsw);
                return $success;
            } else {
                $success = $this->fetch();
                return $success;
            }
        }
        return false;
    }
    /**
     * Return the data referred to the specific row by parameters.
     *
     * @param bool $usrPsw
     *
     * @return bool
     */
    private function fetch($usrPsw = false) {
        try {
            $conn = new PDO("mysql:host=$this->servername;dbname=$this->dbname", $this->username, $this->password);
            $sql = $this->getSelectSql($this->paramNames);
            $valuesToInject = $this->getArrayValuesInsert($this->paramNames, $this->params);
            $prep = $conn->prepare($sql);
            $prep->execute($valuesToInject);
            $res = $prep->fetchAll();
            if ($usrPsw) {
                $res["password_u"] = "";
            }
            # Check the respond
            if (empty($prep->errorInfo()[2])) {
                JsonMsg::print_response(true, DbManager::$FETCH_SUCCESS, $res);
                return true;
            } else {
                JsonMsg::print_response(false, DbManager::$FETCH_ERROR, $prep->errorInfo()[2]);
                return false;
            }
        } catch (PDOException $e) {
            //echo 'Connection failed: ' . $e->getMessage();
            JsonMsg::print_response(false, DbManager::$SQL_ERROR);
        }
    }
    /**
     * Control before injection.
     *
     * @return bool
     */
    private function controlInjection() {
        if (empty($this->table) or empty($this->paramNames) or empty($this->params)) {
            JsonMsg::print_response(false, DbManager::$EMPTY_PARAMS);
            return false;
        }
        if (!$this->areEnough()) {
            JsonMsg::print_response(false, DbManager::$NO_ENOUGH_PARAMS);
            return false;
        }
        return true;
    }
    /**
     * Control if the parameter names match the size of the parameter values.
     *
     * @return bool
     */
    private function areEnough() {
        if (sizeof($this->paramNames) == sizeof($this->params)) {
            return true;
        }
        return false;
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
                $sql = $this->getInsertSql($this->paramNames);
            } else if ($type == DbManager::$TYPE_UPDATE) {
                $sql = $this->getUpdateSql($this->paramNames);
            } else {
                return false;
            }
            # SQL array preparation
            if ($type == DbManager::$TYPE_INSERT) {
                $valuesToInject = $this->getArrayValuesInsert($this->paramNames, $this->params);
            } else if ($type == DbManager::$TYPE_UPDATE) {
                $valuesToInject = $this->getArrayValuesUpdate($this->paramNames, $this->params);
            } else {
                return false;
            }
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
                JsonMsg::print_response(true, $success, $prep->errorInfo()[2]);
                return true;
            } else {
                JsonMsg::print_response(false, $error, $prep->errorInfo()[2]);
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
     * @param $keyName
     * @param $key
     *
     * @return bool
     */
    public function existRow($keyName, $key) {
        try {
            $conn = new PDO("mysql:host=$this->servername;dbname=$this->dbname", $this->username, $this->password);
            $sql = $this->getSelectSql($keyName);
            $valuesToInject = $this->getArrayValues($keyName, $key);
            $prep = $conn->prepare($sql);
            $prep->execute($valuesToInject);
            $res = $prep->fetchAll();
            print_r($valuesToInject);
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
     * @param $paramNames
     *
     * @return string
     */
    private function getSelectSql($paramNames) {
        $sql = "SELECT * FROM $this->table WHERE ";
        foreach ($paramNames as $name) {
            if (array_search($name, $paramNames) != sizeof($paramNames) - 1) {
                $sql .= "$name=:$name and ";
            } else {
                $sql .= "$name=:$name";
            }
        }
        return $sql;
    }
    /**
     * Creation of sql string for INSERT injection.
     *
     * @param $paramNames
     *
     * @return string
     */
    private function getInsertSql($paramNames) {
        $sql_start = "INSERT INTO $this->table(";
        $sql_end = " VALUES (";
        foreach ($paramNames as $name) {
            if (array_search($name, $paramNames) != sizeof($paramNames) - 1) {
                $sql_start .= "$name,";
                $sql_end .= ":$name,";
            } else {
                $sql_start .= "$name)";
                $sql_end .= ":$name)";
            }
        }
        return $sql_start . $sql_end;
    }
    /**
     * Creation of sql string for UPDATE injection.
     *
     * @param $paramNames
     *
     * @return string
     */
    private function getUpdateSql($paramNames) {
        $sql = "UPDATE $this->table SET ";
        foreach ($paramNames as $name) {
            if (array_search($name, $paramNames) != sizeof($paramNames) - 1) {
                $sql .= "$name=:$name,";
            } else {
                $sql .= "$name=:$name";
            }
        }
        $sql .= " WHERE id=:id";
        return $sql;
    }
    /**
     * Creation of array to inject in the sql.
     *
     * @param $paramNames
     * @param $param
     *
     * @return array
     */
    private function getArrayValuesInsert($paramNames, $param) {
        $arrayExecute = array();
        foreach ($paramNames as $name) {
            $arrayExecute[$name] = $param[$name];
        }
        return $arrayExecute;
    }
    /**
     * Creation of array to inject in the sql.
     *
     * @param $paramNames
     * @param $param
     *
     * @return array
     */
    private function getArrayValuesUpdate($paramNames, $param) {
        $arrayExecute = array();
        foreach ($paramNames as $name) {
            $arrayExecute[$name] = $param[$name];
        }
        unset($arrayExecute['id']);
        $arrayExecute['id'] = $param['id'];
        return $arrayExecute;
    }
}