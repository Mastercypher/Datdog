<?php
/**
 * Created by PhpStorm.
 * User: aleric
 * Date: 25/07/2018
 * Time: 17:12
 */

class Control {

    /**
     * Get POST parameters.
     *
     * @param $array
     *
     * @return array
     */
    public static function get_params($array) {
        $params = array();
        if (Control::verify_post($array) and Control::verify_values($array)) {
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
    public static function verify_post($array) {
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
    public static function verify_values($array) {
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