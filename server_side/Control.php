<?php
/**
 * Created by PhpStorm.
 * User: aleric
 * Date: 25/07/2018
 * Time: 17:12
 */

class Control {
    public static $PARAMS_ERROR = "Error with parameters, on or more params missed in GET request.";

    /**
     * Get POST parameters.
     *
     * @param $array
     *
     * @return array
     */
    public static function getParams($array) {
        $params = array();
        if (Control::verifyPost($array)) {
            foreach ($array as &$value) {
                $params[$value] = $_GET[$value];
            }
        } else {
            JsonMsg::print_response(false, Control::$PARAMS_ERROR);
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
    public static function verifyPost($array) {
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
    public static function verifyValues($array) {
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

}